package com.codepath.apps.simpletweets.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class TweetsPagerAdapter extends FragmentPagerAdapter {
  private String[] tabTitles;
  private String[] fragmentClassNames;

  public TweetsPagerAdapter(
      String[] tabTitles,
      String[] fragmentClassNames,
      FragmentManager fragmentManager) {
    super(fragmentManager);
    this.tabTitles = tabTitles;
    this.fragmentClassNames = fragmentClassNames;
  }

  @Override
  public Fragment getItem(int position) {
    if(position < getCount()) {
      String fragmentClassName = fragmentClassNames[position];
      try {
        return (Fragment)Class.forName(fragmentClassName).getDeclaredConstructor().newInstance();
      } catch (Exception e) {
        Log.e("FragmentAdapterError", "could not create instance for class " + fragmentClassName);
      }
    }
    return null;
    /*
    switch (position) {
      case 0:
        return new HomeTimelineFragment();
      case 1:
        return new MentionsTimelineFragment();
      default:
        return null;
    }
    */
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return tabTitles[position];
  }

  @Override
  public int getCount() {
    return tabTitles.length;
  }
}
