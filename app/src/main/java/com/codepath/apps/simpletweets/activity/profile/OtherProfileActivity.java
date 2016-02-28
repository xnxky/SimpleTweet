package com.codepath.apps.simpletweets.activity.profile;

import android.support.v4.app.Fragment;

import com.codepath.apps.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweets.fragments.TabsFragment;
import com.codepath.apps.simpletweets.fragments.UserTimelineFragment;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class OtherProfileActivity extends ProfileActivity {

  private static final String[] TITLES = {"HOME", "TWEETS"};
  private static final String[] FRAGMENT_CLASS_NAMES = {
      HomeTimelineFragment.class.getName(),
      UserTimelineFragment.class.getName()
  };

  @Override
  protected Fragment getTimelineFragment(String screenName) {
    Object[] arguments = {null, screenName};
    return TabsFragment.newInstance(
        TITLES,
        FRAGMENT_CLASS_NAMES,
        arguments
    );
  }
}
