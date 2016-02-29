package com.codepath.apps.simpletweets.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.viewHolder.BaseViewHolder;

import java.util.List;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public abstract class RecyclerViewAdapter<T>
    extends RecyclerView.Adapter<BaseViewHolder> {

  private List<T> mObjects;
  protected OnItemClickListener mListener;

  public RecyclerViewAdapter(
      List<T> objects, OnItemClickListener listener) {
    mObjects = objects;
    mListener = listener;
  }

  @Override
  public abstract  BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

  @Override
  public void onBindViewHolder(BaseViewHolder holder, int position) {
    Object object = mObjects.get(position);
    holder.bindView(object);
  }

  @Override
  public int getItemCount() {
    return mObjects.size();
  }
}
