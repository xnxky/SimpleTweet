package com.codepath.apps.simpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activity.profile.MyProfileActivity;
import com.codepath.apps.simpletweets.fragments.FolloweesFragment;
import com.codepath.apps.simpletweets.fragments.FollowersFragment;
import com.codepath.apps.simpletweets.fragments.RecyclerViewFragment;
import com.codepath.apps.simpletweets.fragments.TabsFragment;
import com.codepath.apps.simpletweets.listener.ActionBarListener;
import com.codepath.apps.simpletweets.models.User;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class FollowActivity extends AppCompatActivity {

  final static private String[] TAB_TITILES = {"FOLLOWERS", "FOLLOWEES"};
  final static String[] FRAGMENT_CLASS_NAMES = {
          FollowersFragment.class.getName(),
          FolloweesFragment.class.getName()
      };

  private ActionBarListener actionBarListener;
  private MenuItem miActionProgressItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_follow);
    setHomePageActionBar();
    setFragments();
  }

  private void setFragments() {
    User user = (User)getIntent().getSerializableExtra("user");
    String[] arguments = {user.getScreenName(), user.getScreenName()};
    TabsFragment tabsFragment = TabsFragment.newInstance(
        TAB_TITILES,
        FRAGMENT_CLASS_NAMES,
        arguments
    );

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.flFollowContainer, tabsFragment);
    ft.commit();
  }

  private void setHomePageActionBar() {
    ActionBar actionbar = getSupportActionBar();
    actionbar.setLogo(R.drawable.action_logo);
    actionbar.setDisplayUseLogoEnabled(true);
    actionbar.setDisplayShowHomeEnabled(true);
    actionbar.setHomeButtonEnabled(true);
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu){
    getMenuInflater().inflate(R.menu.menu_timeline, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu (Menu menu){
    miActionProgressItem = menu.findItem(R.id.miActionProgress);
    actionBarListener = new ActionBarListener() {
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
    RecyclerViewFragment.setActionBarListener(actionBarListener);
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if(actionBarListener != null) {
      RecyclerViewFragment.setActionBarListener(actionBarListener);
    }
  }

  public void onProfileView(MenuItem mi) {
    Intent intent = new Intent(this, MyProfileActivity.class);
    User curUser = TimelineActivity.getCurrentUser();
    if(curUser != null) {
      intent.putExtra("user", curUser);
    }
    startActivity(intent);
  }

}
