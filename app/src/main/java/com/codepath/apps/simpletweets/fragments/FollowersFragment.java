package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class FollowersFragment extends FollowFragment {

  public static FollowersFragment newInstance(String screenName) {
    FollowersFragment followersFragment = new FollowersFragment();
    Bundle args = new Bundle();
    args.putString("screen_name", screenName);
    followersFragment.setArguments(args);
    return followersFragment;
  }

  protected void fetchData(AsyncHttpResponseHandler handler) {
    String screenName = getArguments().getString("screen_name");
    client.getFollowers(
        screenName,
        handler
    );
  }

}
