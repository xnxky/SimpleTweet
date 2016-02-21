package com.codepath.apps.simpletweets.twitter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by xiangyang_xiao on 2/21/16.
 */
public class TwitterReply {

  final static private TwitterClient client = TwitterApp.getRestClient();

  public static void SetTwitterReply (
      final EditText etTweet,
      final TextView tvLeftCharCount,
      final Button btnTweet,
      final long replyId,
      final String author,
      final Activity activity
  ) {

    final Resources resources = activity.getResources();
    final int initTotalCount = resources.getInteger(R.integer.init_char_count);
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
            tvLeftCharCount.setTextColor(resources.getColor(countColorId));
            btnTweet.setBackgroundColor(resources.getColor(btnColorId));
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
                  activity,
                  "Tweet is over 140 characters limit",
                  Toast.LENGTH_SHORT
              ).show();
            } else {
              String prefix = replyId < 0 ? "" : "@"+author;
              client.postTweet(
                  prefix+etTweet.getText().toString(),
                  replyId,
                  new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                      Toast.makeText(
                          activity,
                          "post tweet succeeded",
                          Toast.LENGTH_SHORT
                      ).show();
                      Tweet newTweet = Tweet.fromJson(response);
                      Intent intent = new Intent();
                      intent.putExtra("tweet", newTweet);
                      activity.setResult(Activity.RESULT_OK, intent);
                      activity.finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                      Toast.makeText(
                          activity,
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
  }
}
