package com.codepath.apps.simpletweets.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.twitter.TwitterReply;
import com.squareup.picasso.Picasso;
import com.yqritc.scalablevideoview.ScalableVideoView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetDetailActivity extends AppCompatActivity {

  @Bind(R.id.tvRetweet)
  TextView tvRetweet;
  @Bind(R.id.ivProfileImage)
  ImageView ivProfileImage;
  @Bind(R.id.tvUserName)
  TextView tvUserName;
  @Bind(R.id.tvScreenName)
  TextView tvScreenName;
  @Bind(R.id.tvBody)
  TextView tvBody;
  @Bind(R.id.ivBody)
  ImageView ivBody;
  @Bind(R.id.vvBody)
  ScalableVideoView vvBody;
  @Bind(R.id.etReply)
  EditText etReply;
  @Bind(R.id.tvLeftCharCount)
  TextView tvLeftCharCount;
  @Bind(R.id.btnTweet)
  Button btnTweet;
  @Bind(R.id.ivCancel)
  ImageView ivCancel;

  private Tweet tweet;
  private long replyId;
  private String author;
  private RelativeLayout.LayoutParams etBottomParams;
  private RelativeLayout.LayoutParams etAboveParams;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tweet_detail);
    ButterKnife.bind(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setLogo(R.drawable.action_logo);
    tweet = (Tweet)getIntent().getSerializableExtra("tweet");
    replyId = tweet.getUid();
    author = tweet.getUser().getName();
    etBottomParams = new RelativeLayout.LayoutParams(etReply.getLayoutParams());
    etBottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    etAboveParams = new RelativeLayout.LayoutParams(etReply.getLayoutParams());
    etAboveParams.addRule(RelativeLayout.ABOVE, R.id.btnTweet);
    setupView();
  }

  private void setupView() {
    Picasso.with(this)
        .load(tweet.getUser().getProfileImageUrl())
        .into(ivProfileImage);

    tvUserName.setText(tweet.getUser().getName());
    String screenName = "@"+tweet.getUser().getScreenName();
    tvScreenName.setText(screenName);
    tvBody.setText(tweet.getBody());
    btnTweet.setVisibility(View.GONE);
    ivCancel.setVisibility(View.GONE);
    tvLeftCharCount.setVisibility(View.GONE);
    etReply.setLayoutParams(etBottomParams);

    etReply.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            tvLeftCharCount.setVisibility(View.VISIBLE);
            btnTweet.setVisibility(View.VISIBLE);
            ivCancel.setVisibility(View.VISIBLE);
            etReply.setLayoutParams(etAboveParams);
          }
        }
    );
    ivCancel.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            etReply.setText("");
            etReply.clearFocus();
            tvLeftCharCount.setVisibility(View.GONE);
            btnTweet.setVisibility(View.GONE);
            ivCancel.setVisibility(View.GONE);
            etReply.setLayoutParams(etBottomParams);
          }
        }
    );
    TwitterReply.SetTwitterReply(
        etReply,
        tvLeftCharCount,
        btnTweet,
        replyId,
        author,
        this
    );
  }

}
