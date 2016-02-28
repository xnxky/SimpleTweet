package com.codepath.apps.simpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activity.profile.MyProfileActivity;
import com.codepath.apps.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.simpletweets.fragments.TabsFragment;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.twitter.TwitterApp;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends AppCompatActivity {

  private MenuItem miActionProgressItem;
  private HomeTimelineFragment fragmentHomeTimeline;

  final static private String[] TAB_TITILES = {"Home", "Mentions"};
  final static String[] FRAGMENT_CLASS_NAMES = {
          HomeTimelineFragment.class.getName(),
          MentionsTimelineFragment.class.getName()
      };
  final static private String[] FRAGMENT_ARGUMENTS = {null, null};

  private static User curUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    setFragment();
    setHomePageActionBar();
    getCurrentUser();

    /*
    fragmentHomeTimeline =
        (HomeTimelineFragment) getSupportFragmentManager()
        .findFragmentById(R.id.fragment_timeline);
        */
  }

  private void setFragment() {
    TabsFragment tabsFragment = TabsFragment.newInstance(
        TAB_TITILES,
        FRAGMENT_CLASS_NAMES,
        FRAGMENT_ARGUMENTS
    );
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.flContainer, tabsFragment);
    ft.commit();
  }

  private void setHomePageActionBar() {
    ActionBar actionbar = getSupportActionBar();
    actionbar.setLogo(R.drawable.action_logo);
    actionbar.setDisplayUseLogoEnabled(true);
    actionbar.setDisplayShowHomeEnabled(true);
    actionbar.setHomeButtonEnabled(true);
  }

  public static User getCurrentUser() {
    if(curUser == null) {
      TwitterApp.getRestClient().getUserInfo(new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          curUser = User.fromJSON(response);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
          Log.e("GetCurrentUserFail", throwable.getMessage());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          Log.e("GetCurrentUserFail", throwable.getMessage());
        }
      });
    }
    return curUser;
  }


  @Override
  public boolean onPrepareOptionsMenu (Menu menu){
    miActionProgressItem = menu.findItem(R.id.miActionProgress);
    /*
    ActionBarListener listener = new ActionBarListener() {
      @Override
      public void onDefault() {
        miActionProgressItem.setVisible(true);
      }

      @Override
      public void onSuccess() {
        miActionProgressItem.setVisible(false);
      }

      @Override
      public void onFailure() {
        miActionProgressItem.setVisible(false);
      }
    };
    fragmentHomeTimeline.setActionBarListener(listener);
    */
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu){
    getMenuInflater().inflate(R.menu.menu_timeline, menu);
    return true;
  }

  public void onProfileView(MenuItem mi) {
    Intent intent = new Intent(this, MyProfileActivity.class);
    if(getCurrentUser() != null) {
      intent.putExtra("user", curUser);
    }
    startActivity(intent);
  }

}
