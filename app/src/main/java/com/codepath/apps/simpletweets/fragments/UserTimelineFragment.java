package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.simpletweets.DialogFragment.TweetDialogFragment;
import com.codepath.apps.simpletweets.listener.ActionBarListener;
import com.codepath.apps.simpletweets.listener.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.listener.OnCompseListener;
import com.codepath.apps.simpletweets.listener.TweetSavedListener;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.twitter.TwitterApp;
import com.codepath.apps.simpletweets.twitter.TwitterClient;
import com.codepath.apps.simpletweets.util.NetworkCheck;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xiangyang_xiao on 2/27/16.
 */
public class UserTimelineFragment extends TweetsListFragment {

  private TwitterClient client;
  private User curUser;
  private ActionBarListener actionBarListener;

  public static UserTimelineFragment newInstance(String screen_name) {
    UserTimelineFragment userFragment =  new UserTimelineFragment();
    Bundle args = new Bundle();
    args.putString("screen_name", screen_name);
    userFragment.setArguments(args);
    return userFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    client = TwitterApp.getRestClient();
    client.getUserInfo(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        curUser = User.fromJSON(response);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.e("GetCurrentUserFail", throwable.getMessage());
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.e("GetCurrentUserFail", throwable.getMessage());
      }
    });
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    EndlessRecyclerViewScrollListener onScrollListener =
        new EndlessRecyclerViewScrollListener(layoutManager) {
          @Override
          public void onLoadMore(int page, int totalItemsCount) {
            populateTimeline();
          }
        };

    setOnScrollListener(onScrollListener);

    SwipeRefreshLayout.OnRefreshListener onRefreshListener =
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            Tweet.setMaxId(-1);
            populateTimeline();
          }
        };
    setOnRefreshListener(onRefreshListener);

    OnCompseListener onCompseListener =
        new OnCompseListener() {
          @Override
          public void compose() {
            if (!NetworkCheck.isNetworkAvailable(getActivity()) || !NetworkCheck.isOnline()) {
              Toast.makeText(
                  getActivity(),
                  "Internet is not available; Tweeting is disabled",
                  Toast.LENGTH_SHORT
              ).show();
              return;
            }
            if (curUser != null) {
              startTweet();
            } else {
              client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                  curUser = User.fromJSON(response);
                  startTweet();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                  Log.e("GetCurrentUserFail", throwable.getMessage());
                  Toast.makeText(
                      getActivity(),
                      "Current user not available; Tweeting is disabled!",
                      Toast.LENGTH_SHORT
                  ).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                  Log.e("GetCurrentUserFail", throwable.getMessage());
                  Toast.makeText(
                      getActivity(),
                      "Current user not available; Tweeting is disabled!",
                      Toast.LENGTH_SHORT
                  ).show();
                }
              });
            }
          }
        };
    setOnComposeLisener(onCompseListener);
    populateTimeline();
    return view;
  }

  private void populateTimeline() {
    if (!NetworkCheck.isNetworkAvailable(getActivity()) || !NetworkCheck.isOnline()) {
      Toast.makeText(getActivity(), "network is not available; use local data instead", Toast.LENGTH_SHORT).show();
      handleNewTweets(getAllStoreTweets());
      return;
    }
    if(actionBarListener != null) {
      actionBarListener.onDefault();
    }
    String screen_name = getArguments().getString("screen_name");
    client.getUserTimeline(screen_name, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        if (actionBarListener != null) {
          actionBarListener.onSuccess();
        }
        handleNewTweets(Tweet.fromJSONArray(response));
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        if (actionBarListener != null) {
          actionBarListener.onFailure();
        }
        Log.d("FailToFetch", errorResponse.toString());
        Toast.makeText(getActivity(), "fail to fetch data; use local data instead", Toast.LENGTH_SHORT).show();
        handleNewTweets(getAllStoreTweets());
      }
    });
  }

  private List<Tweet> getAllStoreTweets() {
    return new Select()
        .from(Tweet.class)
        .orderBy("uid DESC")
        .execute();
  }

  private void startTweet() {
    TweetSavedListener listener =
        new TweetSavedListener(curUser, -1, null, getActivity(), this);
    TweetDialogFragment dialogFragment =
        TweetDialogFragment.newInstance(listener);
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    dialogFragment.show(fragmentManager, "compose");
  }
}
