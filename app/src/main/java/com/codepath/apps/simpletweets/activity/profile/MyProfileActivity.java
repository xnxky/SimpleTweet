package com.codepath.apps.simpletweets.activity.profile;

import android.support.v4.app.Fragment;

import com.codepath.apps.simpletweets.fragments.UserTimelineFragment;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class MyProfileActivity extends ProfileActivity {

  @Override
  protected Fragment getTimelineFragment(String screenName) {
    UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
    return fragmentUserTimeline;
  }

}
