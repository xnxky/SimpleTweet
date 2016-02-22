package com.codepath.apps.simpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.twitter.TwitterReply;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetActivity extends AppCompatActivity {

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

  private User user;
  private long replyId;
  private String author;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_tweet);
    ButterKnife.bind(this);

    Intent intent = getIntent();
    replyId = intent.getIntExtra("replyId", -1);
    author = intent.hasExtra("author") ?
        intent.getStringExtra("author") : "";
    user = (User) intent.getSerializableExtra("user");
    setUpView();
  }

  public void setUpView() {
    //cancel image
    ivCancel.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            finish();
          }
        }
    );

    //profile image
    Picasso.with(this)
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
        this,
        null
    );
  }

}
