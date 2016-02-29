package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;

import com.activeandroid.query.Select;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

/**
 * Created by xiangyang_xiao on 2/27/16.
 */
public class HomeTimelineFragment extends TweetsListFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void fetchData(AsyncHttpResponseHandler handler) {
    client.getHomeTimeline(handler);
  }

  //Update accordingly
  protected List<Tweet> getAllStoredObjects() {
    return new Select()
        .from(Tweet.class)
        .orderBy("uid DESC")
        .execute();
  }

}

