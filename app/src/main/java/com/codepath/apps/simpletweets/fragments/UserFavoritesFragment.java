package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;

import com.activeandroid.query.Select;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class UserFavoritesFragment extends TweetsListFragment {

  public static UserFavoritesFragment newInstance(String screen_name) {
    UserFavoritesFragment userFavoritesFragment =  new UserFavoritesFragment();
    Bundle args = new Bundle();
    args.putString("screen_name", screen_name);
    userFavoritesFragment.setArguments(args);
    return userFavoritesFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void fetchData(AsyncHttpResponseHandler handler) {
    String screenName = getArguments().getString("screen_name");
    client.getUserFavorites(screenName, handler);
  }

  //Update accordingly
  protected List<Tweet> getAllStoredObjects() {
    return new Select()
        .from(Tweet.class)
        .orderBy("uid DESC")
        .execute();
  }

}
