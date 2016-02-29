package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;

import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class FollowersFragment extends FollowFragment {

  public static FollowersFragment newInstance(User user) {
    FollowersFragment followersFragment = new FollowersFragment();
    Bundle args = new Bundle();
    args.putSerializable("user", user);
    followersFragment.setArguments(args);
    return followersFragment;
  }

  protected void fetchData(AsyncHttpResponseHandler handler) {
    User targetUser = (User) getArguments().getSerializable("user");
    targetUser.resetCursor();
    client.getFollowers(
        targetUser,
        handler
    );
  }

}
