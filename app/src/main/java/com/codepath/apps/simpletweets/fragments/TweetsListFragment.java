package com.codepath.apps.simpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activity.TweetDetailActivity;
import com.codepath.apps.simpletweets.adapter.TweetRecylerViewAdapter;
import com.codepath.apps.simpletweets.listener.ActionBarListener;
import com.codepath.apps.simpletweets.listener.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.listener.OnCompseListener;
import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by xiangyang_xiao on 2/27/16.
 */
public class TweetsListFragment extends Fragment {

  private static final int REQUEST_CODE = 20;
  @Bind(R.id.rvTweets)
  RecyclerView rvTweets;
  @Bind(R.id.srlHomelineContainer)
  SwipeRefreshLayout srlHomelineContainer;
  @Bind(R.id.fabTweet)
  FloatingActionButton fabTweet;

  public ArrayList<Tweet> getTweets() {
    return tweets;
  }

  public TweetRecylerViewAdapter getRcAdapter() {
    return rcAdapter;
  }

  protected ArrayList<Tweet> tweets;
  protected TweetRecylerViewAdapter rcAdapter;
  protected OnCompseListener onCompseListener;
  protected ActionBarListener actionBarListener;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    tweets = new ArrayList<>();
    rcAdapter = new TweetRecylerViewAdapter(
        tweets,
        new OnItemClickListener() {
          @Override
          public void onItemClick(View itemView, int position) {
            Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
            intent.putExtra("tweet", tweets.get(position));
            startActivityForResult(intent, REQUEST_CODE);
          }
        }
    );
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
    ButterKnife.bind(this, view);
    rvTweets.setHasFixedSize(true);
    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(rcAdapter);
    alphaAdapter.setDuration(1000);
    alphaAdapter.setInterpolator(new OvershootInterpolator());
    alphaAdapter.setFirstOnly(false);
    rvTweets.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
    srlHomelineContainer.setColorSchemeResources(
        android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light
    );
    return view;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
      Tweet newTweet = (Tweet) data.getSerializableExtra("tweet");
      tweets.add(0, newTweet);
      rcAdapter.notifyItemInserted(0);
    }
  }

  public void setOnScrollListener(EndlessRecyclerViewScrollListener listener) {
    rvTweets.setLayoutManager(listener.getmLayoutManager());
    rvTweets.addOnScrollListener(listener);
  }

  public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
    srlHomelineContainer.setOnRefreshListener(listener);
  }

  public void setOnComposeLisener(OnCompseListener lisener) {
    this.onCompseListener = lisener;
  }

  public void setActionBarListener(ActionBarListener listener) {
    this.actionBarListener = listener;
  }

  public void handleNewTweets(List<Tweet> newTweets) {
    if (srlHomelineContainer.isRefreshing()) {
      for (int i = tweets.size() - 1; i >= 0; i--) {
        tweets.remove(i);
        rcAdapter.notifyItemRemoved(i);
      }
      srlHomelineContainer.setRefreshing(false);
    }
    for (int i = 0; i < newTweets.size(); i++) {
      tweets.add(i, newTweets.get(i));
      rcAdapter.notifyItemInserted(i);
    }
  }

  @OnClick(R.id.fabTweet)
  public void handleTweetActionClicked() {
    onCompseListener.compose();
  }
}
