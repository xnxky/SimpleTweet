package com.codepath.apps.simpletweets.activity.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activity.TimelineActivity;
import com.codepath.apps.simpletweets.fragments.UserHeaderFragment;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.twitter.TwitterApp;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public abstract class ProfileActivity extends AppCompatActivity {

  protected User user;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    user = (User)getIntent().getSerializableExtra("user");
    if(user == null) {
      TimelineActivity.getCurrentUser();
      TwitterApp.getRestClient().getUserInfo(new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          user = User.fromJSON(response);
          setupUserProfile(savedInstanceState);
        }
      });
    } else {
      setupUserProfile(savedInstanceState);
    }
  }

  protected void setupUserProfile(Bundle savedInstanceState) {
    String titleName = user == null ? "User Info Not Available" : "@"+user.getScreenName();
    getSupportActionBar().setTitle(titleName);
    if(savedInstanceState == null) {
      UserHeaderFragment fragmentUserHeader = UserHeaderFragment.newInstance(user);
      String screenName = user == null ? null : user.getScreenName();
      Fragment fragmentUserTimeline = getTimelineFragment(screenName);
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.flProfileContainer, fragmentUserTimeline);
      ft.replace(R.id.flUserHeader, fragmentUserHeader);
      ft.commit();
    }
  }

  protected abstract Fragment getTimelineFragment(String screenName);

}
