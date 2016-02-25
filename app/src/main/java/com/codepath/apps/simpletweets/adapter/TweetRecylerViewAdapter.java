package com.codepath.apps.simpletweets.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.viewHolder.BaseViewHolder;
import com.codepath.apps.simpletweets.viewHolder.ImageTextViewHolder;

import java.util.List;

/**
 * Created by xiangyang_xiao on 2/14/16.
 */
public class TweetRecylerViewAdapter
    extends RecyclerView.Adapter<BaseViewHolder> {

  private List<Tweet> mTweets;
  private OnItemClickListener mListener;

  public TweetRecylerViewAdapter(
      List<Tweet> tweets, OnItemClickListener listener) {
    mTweets = tweets;
    mListener = listener;
  }

  @Override
  public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
    return new ImageTextViewHolder(tweetView, mListener, parent.getContext());
  }

  @Override
  public void onBindViewHolder(BaseViewHolder holder, int position) {
    Tweet tweet = mTweets.get(position);
    holder.bindView(tweet);
  }

  @Override
  public int getItemCount() {
    return mTweets.size();
  }
}

