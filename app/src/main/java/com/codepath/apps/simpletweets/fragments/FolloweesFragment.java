package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class FolloweesFragment extends FollowFragment {

  public static FolloweesFragment newInstance(String screenName) {
    FolloweesFragment followeesFragment = new FolloweesFragment();
    Bundle args = new Bundle();
    args.putString("screen_name", screenName);
    followeesFragment.setArguments(args);
    return followeesFragment;
  }

  protected void fetchData(AsyncHttpResponseHandler handler) {
    String screenName = getArguments().getString("screen_name");
    client.getFollowees(
        screenName,
        handler
    );
  }

}
