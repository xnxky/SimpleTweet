package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.adapter.TweetsPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class TabsFragment extends Fragment {

  @Bind(R.id.viewpager)
  ViewPager vpViewPager;
  @Bind(R.id.tabs)
  PagerSlidingTabStrip tabStrip;

  public static TabsFragment newInstance(
      String[] titles,
      String[] fragmentClassNames,
      //The restriction is that only one serializable argument for each
      //fragment newInstance, so there are multiple, they have to be
      //put in a new serializable class
      Object[] fragmentArguments
  ) {
    TabsFragment tabsFragment = new TabsFragment();
    Bundle args = new Bundle();
    args.putStringArray("titles", titles);
    args.putStringArray("fragmentClassNames", fragmentClassNames);
    args.putSerializable("fragmentArguments", fragmentArguments);
    tabsFragment.setArguments(args);
    return tabsFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tabs, container, false);
    ButterKnife.bind(this, view);
    String[] titles = getArguments().getStringArray("titles");
    String[] fragmentClassNames = getArguments().getStringArray("fragmentClassNames");
    Object[] fragmentArguments = (Object[])getArguments().getSerializable("fragmentArguments");

    vpViewPager.setAdapter(new TweetsPagerAdapter(
        titles,
        fragmentClassNames,
        fragmentArguments,
        getActivity().getSupportFragmentManager())
    );
    tabStrip.setViewPager(vpViewPager);
    return view;
  }
}
