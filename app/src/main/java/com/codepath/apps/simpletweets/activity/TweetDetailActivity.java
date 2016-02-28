package com.codepath.apps.simpletweets.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.UserHeaderFragment;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.twitter.TwitterReply;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetDetailActivity extends AppCompatActivity {


  @Bind(R.id.tvBody)
  TextView tvBody;
  @Bind(R.id.ivBody)
  ImageView ivBody;
  @Bind(R.id.vvBody)
  VideoView vvBody;

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
  private User author;
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
    tweet = (Tweet) getIntent().getSerializableExtra("tweet");
    replyId = tweet.getUid();
    author = tweet.getUser();
    etBottomParams = new RelativeLayout.LayoutParams(etReply.getLayoutParams());
    etBottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    etAboveParams = new RelativeLayout.LayoutParams(etReply.getLayoutParams());
    etAboveParams.addRule(RelativeLayout.ABOVE, R.id.btnTweet);
    setupView();
  }

  private void setupView() {
    UserHeaderFragment fragmentUserHeader = UserHeaderFragment.newInstance(author);
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.replace(R.id.flUserHeader, fragmentUserHeader);
    ft.commit();

    tvBody.setText(tweet.getBody());
    btnTweet.setVisibility(View.GONE);
    ivCancel.setVisibility(View.GONE);
    tvLeftCharCount.setVisibility(View.GONE);
    etReply.setLayoutParams(etBottomParams);

    final String initEditText = author.getPrefixName();
    etReply.setOnFocusChangeListener(
        new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) {
              if(etReply.getText().toString().equals("")) {
                etReply.setText(initEditText);
              }
              etReply.requestFocus();
              etReply.setSelection(etReply.getText().length());
              tvLeftCharCount.setVisibility(View.VISIBLE);
              btnTweet.setVisibility(View.VISIBLE);
              ivCancel.setVisibility(View.VISIBLE);
              etReply.setLayoutParams(etAboveParams);
            }
            else {
            etReply.setText("");
            etReply.clearFocus();
          }
          }
        }
    );
    /*
    etReply.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String prefix = replyId < 0 ? "" : author.getPrefixName();
            etReply.setText(prefix);
            etReply.setSelection(etReply.getText().length());
            tvLeftCharCount.setVisibility(View.VISIBLE);
            btnTweet.setVisibility(View.VISIBLE);
            ivCancel.setVisibility(View.VISIBLE);
            etReply.setLayoutParams(etAboveParams);
          }
        }
    );
    */
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
        this,
        null
    );

    if (tweet.getImageUrl() != null) {
      ivBody.setVisibility(View.VISIBLE);
      Picasso.with(this)
          .load(tweet.getImageUrl())
          .fit()
          .error(R.drawable.placeholder_error)
          .placeholder(R.drawable.placeholder)
          .into(ivBody);
    } else {
      ivBody.setVisibility(View.GONE);
      ivBody.setImageDrawable(null);
    }

    if (tweet.getVideoUrl() != null) {
      vvBody.setVideoPath(tweet.getVideoUrl());
      MediaController mediaController = new MediaController(this);
      mediaController.setAnchorView(vvBody);
      vvBody.setMediaController(mediaController);
      vvBody.requestFocus();
      vvBody.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        // Close the progress bar and play the video
        public void onPrepared(MediaPlayer mp) {
          vvBody.start();
        }
      });
      ivBody.setVisibility(View.GONE);
    } else {
      vvBody.setVisibility(View.GONE);
    }
  }

  /*
  TODO : to make the lib work
  private void setupVideoView() {
    vvBody.addMediaPlayerListener(
        new SimpleMainThreadMediaPlayerListener() {
          @Override
          public void onVideoPreparedMainThread() {
            // We hide the cover when video is prepared. Playback is about to start
            vvCover.setVisibility(View.INVISIBLE);
          }

          @Override
          public void onVideoStoppedMainThread() {
            // We show the cover when video is stopped
            vvCover.setVisibility(View.VISIBLE);
          }

          @Override
          public void onVideoCompletionMainThread() {
            // We show the cover when video is completed
            vvCover.setVisibility(View.VISIBLE);
          }
        }
    );
    vvCover.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            mVideoPlayerManager.playNewVideo(
                null, vvBody, "https://www.youtube.com/watch?feature=player_embedded&v=w9j3-ghRjBs"
                //null, vvBody, tweet.getVideoUrl()
            );
          }
        }
    );
  }
   */

}
