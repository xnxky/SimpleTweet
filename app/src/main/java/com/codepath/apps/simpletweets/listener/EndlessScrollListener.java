package com.codepath.apps.simpletweets.listener;

import android.widget.AbsListView;

/**
 * Created by xiangyang_xiao on 2/13/16.
 */
public abstract class EndlessScrollListener
    implements AbsListView.OnScrollListener {

  private int visibleThreshold = 8;
  private int currentPage = -1;
  private int previousTotalItemCount = 0;
  private boolean loading = true;
  private int startingPageIndex = 0;

  public EndlessScrollListener() {
  }

  public EndlessScrollListener(int visibleThreshold) {
    this.visibleThreshold = visibleThreshold;
  }

  public EndlessScrollListener(int visibleThreshold, int startPage) {
    this.visibleThreshold = visibleThreshold;
    this.startingPageIndex = startPage;
    this.currentPage = startPage;
  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    // If the total item count is zero and the previous isn't, assume the
    // list is invalidated and should be reset back to initial state
    if (totalItemCount < previousTotalItemCount) {
      this.currentPage = this.startingPageIndex;
      this.previousTotalItemCount = totalItemCount;
      if (totalItemCount == 0) {
        this.loading = true;
      }
    }
    // If it's still loading, we check to see if the dataset count has
    // changed, if so we conclude it has finished loading and update the current page
    // number and total item count.
    if (loading && (totalItemCount > previousTotalItemCount)) {
      loading = false;
      previousTotalItemCount = totalItemCount;
      currentPage++;
    }

    // If it isn't currently loading, we check to see if we have breached
    // the visibleThreshold and need to reload more data.
    // If we do need to reload some more data, we execute onLoadMore to fetch the data.
    if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount) {
      loading = onLoadMore(currentPage + 1, totalItemCount);
    }
  }

  // Defines the process for actually loading more data based on page
  // Returns true if more data is being loaded; returns false if there is no more data to load.
  public abstract boolean onLoadMore(int page, int totalItemsCount);

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    // Don't take any action on changed
  }
}
