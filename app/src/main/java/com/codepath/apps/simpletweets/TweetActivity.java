package com.codepath.apps.simpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

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

  private int initTotalCount;
  private User user;
  private TwitterClient client;
  private int replyId;
  private Intent intent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initTotalCount = getResources().getInteger(R.integer.init_char_count);

    setContentView(R.layout.activity_tweet);
    ButterKnife.bind(this);

    intent = getIntent();
    client = TwitterApp.getRestClient();
    replyId = intent.getIntExtra("replyId", -1);
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
    String screenName = "@"+user.getScreenName();
    tvScreenName.setText(screenName);

    //tweet text
    etTweet.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
          }

          @Override
          public void afterTextChanged(Editable s) {
            int leftCharCount = initTotalCount - s.length();
            tvLeftCharCount.setText(String.valueOf(leftCharCount));
            int countColorId;
            int btnColorId;
            if (leftCharCount < 0) {
              countColorId = R.color.red;
              btnColorId = android.R.color.darker_gray;
            } else {
              countColorId = R.color.black;
              btnColorId = R.color.colorPrimary;
            }
            tvLeftCharCount.setTextColor(getResources().getColor(countColorId));
            btnTweet.setBackgroundColor(getResources().getColor(btnColorId));
          }
        }
    );

    //button
    btnTweet.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (etTweet.getText().length() > initTotalCount) {
              Toast.makeText(
                  getApplicationContext(),
                  "Tweet is over 140 characters limit",
                  Toast.LENGTH_SHORT
              ).show();
            } else {
              client.postTweet(
                  etTweet.getText().toString(),
                  replyId,
                          new JsonHttpResponseHandler() {
          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Toast.makeText(
                getApplicationContext(),
                "post tweet succeeded",
                Toast.LENGTH_SHORT
            ).show();
            Tweet newTweet = Tweet.fromJson(response);
            intent.putExtra("tweet", newTweet);
            setResult(RESULT_OK, intent);
            finish();
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Toast.makeText(
                getApplicationContext(),
                String.format(
                    "Post tweet failed due to %s. Please try again",
                    throwable.getMessage()
                ),
                Toast.LENGTH_SHORT
            ).show();
          }
        }
              );

            }
          }
        }
    );

        /*
    client.postTweet("this is a test from Xiangyang", -1,

        */
  }

}
