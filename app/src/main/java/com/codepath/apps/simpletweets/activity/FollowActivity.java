package com.codepath.apps.simpletweets.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.FollowFragment;
import com.codepath.apps.simpletweets.fragments.FolloweesFragment;
import com.codepath.apps.simpletweets.fragments.FollowersFragment;
import com.codepath.apps.simpletweets.models.User;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class FollowActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_follow);
    setFragments();
  }

  private void setFragments() {
    User user = (User)getIntent().getSerializableExtra("user");
    String followType = getIntent().getStringExtra("follow_type");
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    FollowFragment followFragment =
        followType.equals("followee") ?
            FolloweesFragment.newInstance(user) :
            FollowersFragment.newInstance(user);
    ft.replace(R.id.flFollowContainer, followFragment);
    ft.commit();
  }
}
