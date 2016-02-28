package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.User;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class UserHeaderFragment extends Fragment {
  @Bind(R.id.tvRetweet)
  TextView tvRetweet;
  @Bind(R.id.ivProfileImage)
  ImageView ivProfileImage;
  @Bind(R.id.tvUserName)
  TextView tvUserName;
  @Bind(R.id.tvScreenName)
  TextView tvScreenName;
  @Bind(R.id.tvFollowee)
  TextView tvFollowee;
  @Bind(R.id.tvFollower)
  TextView tvFollower;

  public static UserHeaderFragment newInstance(User user) {
    UserHeaderFragment fragmentUserHeader = new UserHeaderFragment();
    Bundle args = new Bundle();
    args.putSerializable("user", user);
    fragmentUserHeader.setArguments(args);
    return fragmentUserHeader;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_user_header, container, false);
    ButterKnife.bind(this, view);
    populateUserHeader();
    return view;
  }

  private void populateUserHeader() {
    User user = (User)getArguments().getSerializable("user");
    Picasso.with(getActivity())
    .load(user.getProfileImageUrl())
    .fit()
    .error(R.drawable.placeholder_error)
    .placeholder(R.drawable.placeholder)
    .into(ivProfileImage);

    tvUserName.setText(user.getName());
    String screenName = "@" + user.getScreenName();
    tvScreenName.setText(screenName);

    tvFollowee.setText(user.getFriendsCount()+" "+"Followig");
    tvFollower.setText(user.getFollowersCount()+" "+"Followers");
  }

}