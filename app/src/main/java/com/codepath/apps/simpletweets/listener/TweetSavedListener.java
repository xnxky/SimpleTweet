package com.codepath.apps.simpletweets.listener;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.twitter.TwitterReply;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiangyang_xiao on 2/21/16.
 */
public class TweetSavedListener {
  @Bind(R.id.ivCancel)
  ImageView ivCancel;
  @Bind(R.id.ivProfileImage)
  ImageView ivProfileImage;
  @Bind(R.id.tvUserName)
  TextView tvUsername;
  @Bind(R.id.tvScreenName)
  TextView tvScreenName;
  @Bind(R.id.etTweet)
  EditText etTweet;
  @Bind(R.id.tvLeftCharCount)
  TextView tvLeftCharCount;
  @Bind(R.id.btnTweet)
  Button btnTweet;

  private Dialog dialog;
  private Context context;
  private TweetsArrayAdapter aTweets;

  private User user;
  private long replyId;
  private String author;

  public void setView(View view) {
    ButterKnife.bind(this, view);
    setUpView();
  }

  public void setDialog(Dialog dialog) {
    this.dialog = dialog;
  }

  public TweetSavedListener(
      User user, long replyId, String author,
      Context context,
      TweetsArrayAdapter aTweets

  ) {
    this.user = user;
    this.replyId = replyId;
    this.author = author;
    this.context = context;
    this.aTweets = aTweets;
  }

  public void setUpView() {
    //cancel image
    ivCancel.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.dismiss();
          }
        }
    );

    //profile image
    Picasso.with(context)
        .load(user.getProfileImageUrl())
        .into(ivProfileImage);
    tvUsername.setText(user.getName());
    String screenName = "@" + user.getScreenName();
    tvScreenName.setText(screenName);
    TwitterReply.SetTwitterReply(
        etTweet,
        tvLeftCharCount,
        btnTweet,
        replyId,
        author,
        context,
        this
    );
  }

  public void finish(Tweet newTweet) {
    aTweets.insert(newTweet, 0);
    dialog.dismiss();
  }
}
