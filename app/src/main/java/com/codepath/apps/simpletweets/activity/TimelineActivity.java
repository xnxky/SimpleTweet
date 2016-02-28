package com.codepath.apps.simpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.simpletweets.fragments.TabsFragment;

public class TimelineActivity extends AppCompatActivity {

  private MenuItem miActionProgressItem;
  private HomeTimelineFragment fragmentHomeTimeline;

  final static private String[] TAB_TITILES = {"Home", "Mentions"};
  final static String[] fragmentClassNames = {
          HomeTimelineFragment.class.getName(),
          MentionsTimelineFragment.class.getName()
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    TabsFragment tabsFragment = TabsFragment.newInstance(
        TAB_TITILES,
        fragmentClassNames
    );
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.flContainer, tabsFragment);
    ft.commit();
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    ActionBar actionbar = getSupportActionBar();
    actionbar.setLogo(R.drawable.action_logo);
    actionbar.setDisplayUseLogoEnabled(true);
    actionbar.setDisplayShowHomeEnabled(true);
    actionbar.setHomeButtonEnabled(true);
    /*
    fragmentHomeTimeline =
        (HomeTimelineFragment) getSupportFragmentManager()
        .findFragmentById(R.id.fragment_timeline);
        */
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
    Intent intent = new Intent(this, ProfileActivity.class);
    startActivity(intent);
  }

}
