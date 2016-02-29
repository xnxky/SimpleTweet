package com.codepath.apps.simpletweets.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.viewHolder.BaseViewHolder;
import com.codepath.apps.simpletweets.viewHolder.FollowViewHolder;

import java.util.List;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class FollowRecyclerViewAdapter
    extends RecyclerViewAdapter<User> {

  public FollowRecyclerViewAdapter(
      List<User> users, OnItemClickListener listener) {
    super(users, listener);
  }

  @Override
  public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View tweetView = inflater.inflate(R.layout.item_user_info, parent, false);
    return new FollowViewHolder(tweetView, mListener, parent.getContext());
  }
}
