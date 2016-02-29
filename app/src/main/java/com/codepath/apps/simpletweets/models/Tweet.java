package com.codepath.apps.simpletweets.models;

import android.text.format.DateUtils;
import android.util.Log;

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
@Table(name = "tweets")
public class Tweet
    extends Model
    implements Serializable {

  private static long maxHomelineId = -1;
  @Column(name = "body")
  private String body;
  @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
  private long uid;
  @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
  private User user;
  @Column(name = "created_at")
  private String createdAt;
  @Column(name = "retweet_count")
  private int retweetCount;
  @Column(name = "retweeted")
  private boolean retweeted;
  @Column(name = "favourite_count")
  private int favouriteCount;
  @Column(name = "favorited")
  private boolean favorited;
  @Column(name = "orig_tweet", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
  private Tweet origTweet;
  @Column(name = "image_url")
  private String imageUrl;
  @Column(name = "video_url")
  private String videoUrl;

  public Tweet() {
    super();
  }

  public static long getMaxHomelineId() {
    return maxHomelineId;
  }

  public static void setMaxHomelineId(long maxHomelineId) {
    Tweet.maxHomelineId = maxHomelineId;
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
    if(jsonObject.toString().contains("mp4")) {
      Log.i("XXY", "get right mp4");
    }
    try {
      Tweet existingTweet = new Select()
          .from(Tweet.class)
          .where("uid = ?", jsonObject.getLong("id"))
          .executeSingle();
      if (existingTweet != null) {
        if (maxHomelineId < 0 || maxHomelineId > existingTweet.uid) {
          maxHomelineId = existingTweet.uid;
        }
        return existingTweet;
      }

      Tweet tweet = new Tweet();
      tweet.body = jsonObject.getString("text");
      tweet.uid = jsonObject.getLong("id");
      tweet.createdAt = jsonObject.getString("created_at");
      tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
      tweet.retweetCount = jsonObject.getInt("retweet_count");
      tweet.retweeted = jsonObject.getBoolean("retweeted");
      tweet.favouriteCount = jsonObject.getInt("favorite_count");
      tweet.favorited = jsonObject.getBoolean("favorited");

      try {
        tweet.origTweet = Tweet.fromJson(jsonObject.getJSONObject("retweeted_status"));
      } catch (JSONException e) {
        tweet.origTweet = null;
      }

      try {
        JSONObject entities = jsonObject.getJSONObject("entities");
        JSONArray media = entities.getJSONArray("media");
        if (media.length() > 0) {
          tweet.imageUrl = ((JSONObject) media.get(0)).getString("media_url");
        }
      } catch (JSONException e) {
        tweet.imageUrl = null;
      }

      try {
        JSONObject entities = jsonObject.getJSONObject("extended_entities");
        JSONArray media = entities.getJSONArray("media");
        if(media.length() > 0) {
          JSONObject mediaJsonObject = (JSONObject) media.get(0);
          JSONObject videoInfo = mediaJsonObject.getJSONObject("video_info");
          JSONArray variants =  videoInfo.getJSONArray("variants");
          for(int i=0; i<variants.length(); i++) {
            JSONObject content = (JSONObject)variants.get(i);
            if(content.getString("content_type").equals("video/mp4")) {
              tweet.videoUrl = content.getString("url");
              break;
            }
          }
        }
      } catch (JSONException e) {
        tweet.videoUrl = null;
      }

      if (maxHomelineId < 0 || maxHomelineId > tweet.uid) {
        maxHomelineId = tweet.uid;
      }
      if (saveFlag) {
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

  public String getImageUrl() {
    return imageUrl;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public String getCreatedAt() {
    return fromRawTweetDate(createdAt).replaceAll("^in ", "");
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
