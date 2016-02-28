package com.codepath.apps.simpletweets.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweets.listener.ActionBarListener;

public class TimelineActivity extends AppCompatActivity {

  private MenuItem miActionProgressItem;
  private HomeTimelineFragment fragmentHomeTimeline;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setLogo(R.drawable.action_logo);
    setSupportActionBar(toolbar);
    fragmentHomeTimeline =
        (HomeTimelineFragment) getSupportFragmentManager()
        .findFragmentById(R.id.fragment_timeline);
  }

  @Override
  public boolean onPrepareOptionsMenu (Menu menu){
    miActionProgressItem = menu.findItem(R.id.miActionProgress);
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
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu){
    getMenuInflater().inflate(R.menu.timeline, menu);
    return true;
  }

}
