package com.codepath.apps.simpletweets.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class TweetsPagerAdapter extends FragmentPagerAdapter {
  private String[] tabTitles;
  private Fragment[] fragments;

  public TweetsPagerAdapter(
      String[] tabTitles,
      String[] fragmentClassNames,
      Object[] fragmentArguments,
      FragmentManager fragmentManager) {
    super(fragmentManager);
    this.tabTitles = tabTitles;

    fragments = new Fragment[tabTitles.length];
    for (int i = 0; i < fragmentClassNames.length; i++) {
      String fragmentClassName = fragmentClassNames[i];
      Object argument = fragmentArguments[i];
      try {
        if (argument == null) {
          fragments[i] = (Fragment) Class.forName(fragmentClassName)
              .getDeclaredConstructor()
              .newInstance();
        } else {
          Class argumentClass = argument.getClass();
          Method newInstanceMethod =
              Class.forName(fragmentClassName)
              .getMethod("newInstance", argumentClass);
          fragments[i] = (Fragment)newInstanceMethod.invoke(null, argumentClass.cast(argument));
        }

      } catch (Exception e) {
        Log.e("FragmentAdapterError", "could not create instance for class " + fragmentClassName);
      }
    }
  }

  @Override
  public Fragment getItem(int position) {
    return fragments[position];
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return tabTitles[position];
  }

  @Override
  public int getCount() {
    return fragments.length;
  }
}
