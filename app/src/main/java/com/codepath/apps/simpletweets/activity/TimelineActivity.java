package com.codepath.apps.simpletweets.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.simpletweets.DialogFragment.TweetDialogFragment;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.adapter.TweetRecylerViewAdapter;
import com.codepath.apps.simpletweets.listener.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.listener.TweetSavedListener;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.twitter.TwitterApp;
import com.codepath.apps.simpletweets.twitter.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class TimelineActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 20;
  @Bind(R.id.rvTweets)
  RecyclerView rvTweets;
  @Bind(R.id.srlHomelineContainer)
  SwipeRefreshLayout srlHomelineContainer;
  private TwitterClient client;
  private ArrayList<Tweet> tweets;
  private TweetRecylerViewAdapter rcAdapter;
  private User curUser;
  private MenuItem miActionProgressItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    ButterKnife.bind(this);
    rvTweets.setHasFixedSize(true);
    tweets = new ArrayList<>();
    rcAdapter = new TweetRecylerViewAdapter(
        tweets,
        new OnItemClickListener() {
          @Override
          public void onItemClick(View itemView, int position) {
            Intent intent = new Intent(getApplicationContext(), TweetDetailActivity.class);
            intent.putExtra("tweet", tweets.get(position));
            startActivityForResult(intent, REQUEST_CODE);
          }
        }
    );

    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(rcAdapter);
    alphaAdapter.setDuration(1000);
    alphaAdapter.setInterpolator(new OvershootInterpolator());
    alphaAdapter.setFirstOnly(false);
    rvTweets.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    rvTweets.setLayoutManager(layoutManager);

    rvTweets.addOnScrollListener(
        new EndlessRecyclerViewScrollListener(layoutManager) {
          @Override
          public void onLoadMore(int page, int totalItemsCount) {
            populateTimeline();
          }
        }
    );

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setLogo(R.drawable.action_logo);
    setSupportActionBar(toolbar);
    client = TwitterApp.getRestClient();
    client.getCurrentUser(new JsonHttpResponseHandler() {
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

    srlHomelineContainer.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            Tweet.setMaxId(-1);
            populateTimeline();
          }
        }
    );
    srlHomelineContainer.setColorSchemeResources(
        android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light
    );
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    miActionProgressItem = menu.findItem(R.id.miActionProgress);
    populateTimeline();
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.timeline, menu);
    return true;
  }


  /*
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_tweet:
        handleTweetActionClicked();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }
  */

  @OnClick(R.id.fabTweet)
  public void handleTweetActionClicked() {
    if (!isNetworkAvailable() || !isOnline()) {
      Toast.makeText(
          this,
          "Internet is not available; Tweeting is disabled",
          Toast.LENGTH_SHORT
      ).show();
      return;
    }
    if (curUser != null) {
      startTweet();
    } else {
      client.getCurrentUser(new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          curUser = User.fromJSON(response);
          startTweet();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
          Log.e("GetCurrentUserFail", throwable.getMessage());
          Toast.makeText(
              getApplicationContext(),
              "Current user not available; Tweeting is disabled!",
              Toast.LENGTH_SHORT
          ).show();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          Log.e("GetCurrentUserFail", throwable.getMessage());
          Toast.makeText(
              getApplicationContext(),
              "Current user not available; Tweeting is disabled!",
              Toast.LENGTH_SHORT
          ).show();
        }
      });
    }
  }

  private void startTweet() {
    /*
    Intent intent = new Intent(this, TweetActivity.class);
    intent.putExtra("user", curUser);
    startActivityForResult(intent, REQUEST_CODE);
    */

    TweetSavedListener listener =
        new TweetSavedListener(curUser, -1, null, getApplicationContext(), tweets, rcAdapter);
    TweetDialogFragment dialogFragment =
        TweetDialogFragment.newInstance(listener);
    FragmentManager fragmentManager = getSupportFragmentManager();
    dialogFragment.show(fragmentManager, "compose");
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      Tweet newTweet = (Tweet) data.getSerializableExtra("tweet");
      tweets.add(0, newTweet);
      rcAdapter.notifyItemInserted(0);
    }
  }

  private void populateTimeline() {
    if (!isNetworkAvailable()) {
      Toast.makeText(this, "network is not available; use local data instead", Toast.LENGTH_SHORT).show();
      handleNewTweets(getAllStoreTweets());
      return;
    }
    if (!isOnline()) {
      Toast.makeText(this, "internet is not connected; use local data instead", Toast.LENGTH_SHORT).show();
      handleNewTweets(getAllStoreTweets());
      return;
    }
    miActionProgressItem.setVisible(true);
    client.getHomeTimeline(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        miActionProgressItem.setVisible(false);
        handleNewTweets(Tweet.fromJSONArray(response));
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        miActionProgressItem.setVisible(false);
        Log.d("FailToFetch", errorResponse.toString());
        Toast.makeText(getApplicationContext(), "fail to fetch data; use local data instead", Toast.LENGTH_SHORT).show();
        handleNewTweets(getAllStoreTweets());
      }
    });
  }

  private void handleNewTweets(List<Tweet> newTweets) {
    if (srlHomelineContainer.isRefreshing()) {
      for(int i=tweets.size()-1; i>=0; i--) {
        tweets.remove(i);
        rcAdapter.notifyItemRemoved(i);
      }
      srlHomelineContainer.setRefreshing(false);
    }
    for(int i=0; i<newTweets.size(); i++) {
      tweets.add(i, newTweets.get(i));
      rcAdapter.notifyItemInserted(i);
    }
  }

  private List<Tweet> getAllStoreTweets() {
    return new Select()
        .from(Tweet.class)
        .orderBy("uid DESC")
        .execute();
  }

  private Boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
  }

  private boolean isOnline() {
    Runtime runtime = Runtime.getRuntime();
    try {
      Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
      int exitValue = ipProcess.waitFor();
      return (exitValue == 0);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return false;
  }

}
