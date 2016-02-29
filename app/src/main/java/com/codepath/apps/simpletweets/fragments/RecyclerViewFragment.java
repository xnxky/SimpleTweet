package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.adapter.RecyclerViewAdapter;
import com.codepath.apps.simpletweets.listener.ActionBarListener;
import com.codepath.apps.simpletweets.listener.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.listener.HandleNewObjectsListener;
import com.codepath.apps.simpletweets.twitter.TwitterApp;
import com.codepath.apps.simpletweets.twitter.TwitterClient;
import com.codepath.apps.simpletweets.util.NetworkCheck;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public abstract class RecyclerViewFragment<T> extends Fragment {

  @Bind(R.id.recyclerView)
  RecyclerView recyclerView;
  @Bind(R.id.srlContainer)
  SwipeRefreshLayout srlContainer;

  protected TwitterClient client;

  public ArrayList<T> getObjects() {
    return objects;
  }

  public RecyclerViewAdapter<T> getRcAdapter() {
    return rcAdapter;
  }

  protected ArrayList<T> objects;
  protected RecyclerViewAdapter rcAdapter;
  protected AsyncHttpResponseHandler responseHandler;
  protected ActionBarListener actionBarListener;
  protected HandleNewObjectsListener<T> handleNewObjectsListener;
  protected boolean useHandleNewListener;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    client = TwitterApp.getRestClient();
    objects = new ArrayList<>();
    useHandleNewListener = false;
  }

  protected void setResponseHandler() {
    responseHandler = new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        if (actionBarListener != null) {
          actionBarListener.onSuccess();
        }
        List<T> newObjects = getObjectsFromJsonArray(response);
        if(! useHandleNewListener) {
          handleNewObjects(newObjects);
        }
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        if (actionBarListener != null) {
          actionBarListener.onSuccess();
        }
        List<T> newObjects = getObjectsFromJsonObject(response);
        if(! useHandleNewListener) {
          handleNewObjects(newObjects);
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        if (actionBarListener != null) {
          actionBarListener.onFailure();
        }
        Log.d("FailToFetch", errorResponse.toString());
        Toast.makeText(getActivity(), "fail to fetch data; use local data instead", Toast.LENGTH_SHORT).show();
        handleNewObjects(getAllStoredObjects());
      }
    };

    handleNewObjectsListener = new HandleNewObjectsListener<T>() {
      @Override
      public void handleNewObjects(List<T> objects) {
        handleNewObjects(objects);
      }
    };
  }

  protected void setOnScrollListener() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    EndlessRecyclerViewScrollListener onScrollListener =
        new EndlessRecyclerViewScrollListener(layoutManager) {
          @Override
          public void onLoadMore(int page, int totalItemsCount) {
            populateTimeline();
          }
        };
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addOnScrollListener(onScrollListener);
  }

  protected void setOnRefreshListener() {
    SwipeRefreshLayout.OnRefreshListener onRefreshListener =
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            preRefreshAction();
            populateTimeline();
          }
        };
    srlContainer.setOnRefreshListener(onRefreshListener);
  }

  protected void preRefreshAction() {
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutId(), container, false);
    ButterKnife.bind(this, view);
    setResponseHandler();
    setRcAdapater();
    setOnScrollListener();
    setOnRefreshListener();
    recyclerView.setHasFixedSize(true);
    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(rcAdapter);
    alphaAdapter.setDuration(1000);
    alphaAdapter.setInterpolator(new OvershootInterpolator());
    alphaAdapter.setFirstOnly(false);
    recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
    srlContainer.setColorSchemeResources(
        android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light
    );
    populateTimeline();
    return view;
  }

  public void setActionBarListener(ActionBarListener listener) {
    this.actionBarListener = listener;
  }

  public void handleNewObjects(List<T> newObjects) {
    if (srlContainer.isRefreshing()) {
      for (int i = objects.size() - 1; i >= 0; i--) {
        objects.remove(i);
        rcAdapter.notifyItemRemoved(i);
      }
      srlContainer.setRefreshing(false);
    }
    for (int i = 0; i < newObjects.size(); i++) {
      objects.add(i, newObjects.get(i));
      rcAdapter.notifyItemInserted(i);
    }
  }

  private void populateTimeline() {
    if (!NetworkCheck.isNetworkAvailable(getActivity()) || !NetworkCheck.isOnline()) {
      Toast.makeText(getActivity(), "network is not available; use local data instead", Toast.LENGTH_SHORT).show();
      handleNewObjects(getAllStoredObjects());
      return;
    }
    if (actionBarListener != null) {
      actionBarListener.onDefault();
    }
    fetchData(responseHandler);
  }

  protected abstract void fetchData(AsyncHttpResponseHandler handler);
  protected abstract List<T> getAllStoredObjects();
  protected abstract void setRcAdapater();
  protected abstract ArrayList<T> getObjectsFromJsonArray(JSONArray response);
  protected abstract ArrayList<T> getObjectsFromJsonObject(JSONObject response);
  protected abstract int getLayoutId();

}
