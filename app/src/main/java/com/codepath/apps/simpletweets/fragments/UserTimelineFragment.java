package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;

import com.activeandroid.query.Select;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

/**
 * Created by xiangyang_xiao on 2/27/16.
 */

public class UserTimelineFragment extends TweetsListFragment {

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
  }

  @Override
  protected void fetchData(AsyncHttpResponseHandler handler) {
    String screenName = getArguments().getString("screen_name");
    client.getUserTimeline(screenName, handler);
  }

  //Update accordingly
  protected List<Tweet> getAllStoredObjects() {
    return new Select()
        .from(Tweet.class)
        .orderBy("uid DESC")
        .execute();
  }

}
