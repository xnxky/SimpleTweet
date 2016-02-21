package com.codepath.apps.simpletweets.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

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
@Table(name="tweets")
public class Tweet
    extends Model
    implements Serializable {

  private static long maxId = -1;
  @Column(name="body")
  private String body;
  @Column(name="uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
  private long uid;
  @Column(name="user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
  private User user;
  @Column(name="created_at")
  private String createdAt;
  @Column(name="retweet_count")
  private int retweet_count;
  @Column(name="retweeted")
  private boolean retweeted;
  @Column(name="favourite_count")
  private int favouriteCount;
  @Column(name="favorited")
  private boolean favorited;
  @Column(name="orig_tweet", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
  private Tweet origTweet;

  public Tweet() {
    super();
  }

  public static long getMaxId() {
    return maxId;
  }

  public static void setMaxId(long maxId) {
    Tweet.maxId = maxId;
  }

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

  public static Tweet fromJson(JSONObject jsonObject, boolean saveFlag) {
    try {
      Tweet existingTweet = new Select()
          .from(Tweet.class)
          .where("uid = ?", jsonObject.getLong("id"))
          .executeSingle();
      if(existingTweet != null) {
        return existingTweet;
      }

      Tweet tweet = new Tweet();
      tweet.body = jsonObject.getString("text");
      tweet.uid = jsonObject.getLong("id");
      tweet.createdAt = fromRawTweetDate(jsonObject.getString("created_at")).replaceAll("^in ", "");
      tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
      tweet.retweet_count = jsonObject.getInt("retweet_count");
      tweet.retweeted = jsonObject.getBoolean("retweeted");
      tweet.favouriteCount = jsonObject.getInt("favorite_count");
      tweet.favorited = jsonObject.getBoolean("favorited");

      try {
        tweet.origTweet = Tweet.fromJson(jsonObject.getJSONObject("retweeted_status"));
      } catch (JSONException e) {
        tweet.origTweet = null;
      }

      if(maxId < 0 || maxId > tweet.uid) {
        maxId = tweet.uid;
      }
      if(saveFlag) {
        tweet.save();
      }
      return tweet;
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Tweet fromJson(JSONObject jsonObject) {
    return fromJson(jsonObject, true);
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
