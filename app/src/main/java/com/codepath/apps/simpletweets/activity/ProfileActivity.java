package com.codepath.apps.simpletweets.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.UserHeaderFragment;
import com.codepath.apps.simpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.twitter.TwitterApp;
import com.codepath.apps.simpletweets.twitter.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

  TwitterClient client;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    User user = (User)getIntent().getSerializableExtra("user");
    if(user == null) {
      client = TwitterApp.getRestClient();
      client.getUserInfo(new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          User user = User.fromJSON(response);
          setupUserProfile(user, savedInstanceState);
        }
      });
    } else {
      setupUserProfile(user, savedInstanceState);
    }
  }

  private void setupUserProfile(User user, Bundle savedInstanceState) {
    getSupportActionBar().setTitle("@" + user.getScreenName());
    if(savedInstanceState == null) {
      String screenName = getIntent().getStringExtra("screen_name");
      UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
      UserHeaderFragment fragmentUserHeader = UserHeaderFragment.newInstance(user);
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.flContainer, fragmentUserTimeline);
      ft.replace(R.id.flUserHeader, fragmentUserHeader);
      ft.commit();
    }
  }

}
