package com.codepath.apps.simpletweets.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.viewHolder.BaseViewHolder;
import com.codepath.apps.simpletweets.viewHolder.TweetViewHolder;

import java.util.List;

/**
 * Created by xiangyang_xiao on 2/14/16.
 */
public class TweetRecylerViewAdapter
    extends RecyclerViewAdapter<Tweet> {

  public TweetRecylerViewAdapter(
      List<Tweet> objects, OnItemClickListener listener) {
    super(objects, listener);
  }

  @Override
  public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
    return new TweetViewHolder(tweetView, mListener, parent.getContext());
  }

}

