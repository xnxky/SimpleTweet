package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;

import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class FolloweesFragment extends FollowFragment {

  public static FolloweesFragment newInstance(User user) {
    FolloweesFragment followeesFragment = new FolloweesFragment();
    Bundle args = new Bundle();
    args.putSerializable("user", user);
    followeesFragment.setArguments(args);
    return followeesFragment;
  }

  protected void fetchData(AsyncHttpResponseHandler handler) {
    User targetUser = (User) getArguments().getSerializable("user");
    targetUser.resetCursor();
    client.getFollowees(
        targetUser,
        handler
    );
  }

}
