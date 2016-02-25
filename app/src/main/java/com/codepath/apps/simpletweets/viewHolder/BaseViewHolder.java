package com.codepath.apps.simpletweets.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.models.Tweet;

/**
 * Created by xiangyang_xiao on 2/14/16.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

  OnItemClickListener mListener;

  public BaseViewHolder(View itemView, OnItemClickListener listener) {
    super(itemView);
    mListener = listener;
    itemView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (mListener != null) {
              mListener.onItemClick(v, getLayoutPosition());
            }
          }
        }
    );
  }

  public abstract void bindView(Tweet tweet);
}
