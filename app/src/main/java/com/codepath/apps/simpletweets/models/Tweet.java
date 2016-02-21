package com.codepath.apps.simpletweets.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by xiangyang_xiao on 2/17/16.
 */
public class Tweet implements Serializable {

  private String body;
  private long uid;
  private User user;
  private String createdAt;

  public static long getMaxId() {
    return maxId;
  }

  private static long maxId = -1;

  public static String fromRawTweetDate(String tweetDate) {
    String tweetDateFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(tweetDateFormat, Locale.ENGLISH);
    simpleDateFormat.setLenient(true);
    String relativeDate = "";
    try {
      long dateMills = simpleDateFormat.parse(tweetDate).getTime();
      relativeDate = DateUtils.getRelativeTimeSpanString(
          dateMills,
          System.currentTimeMillis(),
          DateUtils.SECOND_IN_MILLIS
      ).toString();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return relativeDate;
  }

  public static Tweet fromJson(JSONObject jsonObject) {
    Tweet tweet = new Tweet();
    try {
      tweet.body = jsonObject.getString("text");
      tweet.uid = jsonObject.getLong("id");
      tweet.createdAt = fromRawTweetDate(jsonObject.getString("created_at")).replaceAll("^in ", "");
      tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
      if(maxId < 0 || maxId > tweet.uid) {
        maxId = tweet.uid;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return tweet;
  }

  public static ArrayList<Tweet> fromJSONArray(JSONArray response) {
    ArrayList<Tweet> tweets = new ArrayList<>();
    for (int i = 0; i < response.length(); i++) {
      try {
        JSONObject tweetJson = response.getJSONObject(i);
        Tweet tweet = Tweet.fromJson(tweetJson);
        if (tweet != null) {
          tweets.add(tweet);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return tweets;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public long getUid() {
    return uid;
  }

  public String getBody() {
    return body;
  }

  public User getUser() {
    return user;
  }
}
