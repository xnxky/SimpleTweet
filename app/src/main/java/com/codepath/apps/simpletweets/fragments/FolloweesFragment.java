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
    user.resetCursor();
    args.putSerializable("user", user);
    followeesFragment.setArguments(args);
    return followeesFragment;
  }

  protected void fetchData(AsyncHttpResponseHandler handler) {
    User targetUser = (User) getArguments().getSerializable("user");
    client.getFollowees(
        targetUser,
        handler
    );
  }

  @Override
  protected void setCursor(int nextCursor) {
    User targetUser = (User) getArguments().getSerializable("user");
    assert targetUser != null;
    targetUser.setFolloweesCursor(nextCursor);
  }

}
