package com.codepath.apps.simpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.simpletweets.DialogFragment.TweetDialogFragment;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activity.TimelineActivity;
import com.codepath.apps.simpletweets.activity.TweetDetailActivity;
import com.codepath.apps.simpletweets.adapter.TweetRecylerViewAdapter;
import com.codepath.apps.simpletweets.listener.OnCompseListener;
import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.listener.TweetSavedListener;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.util.NetworkCheck;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by xiangyang_xiao on 2/27/16.
 */
public abstract class TweetsListFragment extends RecyclerViewFragment<Tweet> {

  private static final int REQUEST_CODE = 20;
  protected OnCompseListener onCompseListener;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setOnComposeListener();
  }

  @Override
  protected void setRcAdapater() {
    rcAdapter = new TweetRecylerViewAdapter(
        objects,
        new OnItemClickListener() {
          @Override
          public void onItemClick(View itemView, int position) {
            Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
            intent.putExtra("tweet", objects.get(position));
            startActivityForResult(intent, REQUEST_CODE);
          }
        }
    );
  }

  protected void setOnComposeListener() {
    onCompseListener = new OnCompseListener() {
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
        if (TimelineActivity.getCurrentUser() != null) {
          startTweet();
        } else {
          Toast.makeText(
              getActivity(),
              "Current user not available; Tweeting is disabled!",
              Toast.LENGTH_SHORT
          ).show();
        }
      }
    };
  }

  private void startTweet() {
    TweetSavedListener listener =
        new TweetSavedListener(TimelineActivity.getCurrentUser(), -1, null, getActivity(), this);
    TweetDialogFragment dialogFragment =
        TweetDialogFragment.newInstance(listener);
    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    dialogFragment.show(fragmentManager, "compose");
  }

  @OnClick(R.id.fabTweet)
  public void handleTweetActionClicked() {
    onCompseListener.compose();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_tweets_list;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
      Tweet newTweet = (Tweet) data.getSerializableExtra("tweet");
      objects.add(0, newTweet);
      rcAdapter.notifyItemInserted(0);
    }
  }

  @Override
  protected ArrayList<Tweet> getObjectsFromJsonArray(JSONArray response) {
    return Tweet.fromJSONArray(response);
  }

  @Override
  protected ArrayList<Tweet> getObjectsFromJsonObject(JSONObject response) {
    return new ArrayList<>();
  }

  @Override
  protected abstract void fetchData(AsyncHttpResponseHandler handler);

  @Override
  protected abstract List<Tweet> getAllStoredObjects();
}


