package com.codepath.apps.simpletweets.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.simpletweets.listener.HandleNewObjectsListener;
import com.codepath.apps.simpletweets.twitter.TwitterApp;
import com.google.common.base.Joiner;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangyang_xiao on 2/17/16.
 */

@Table(name = "users")
public class User
    extends Model
    implements Serializable {

  @Column(name = "name")
  private String name;
  @Column(name = "uid", unique = true)
  private long uid;
  @Column(name = "screen_name")
  private String screenName;
  @Column(name = "profile_url")
  private String profileImageUrl;

  @Column(name = "tagline")
  private String tagline;

  @Column(name = "followers_count")
  private int followersCount;

  public int getFollowersCursor() {
    return followersCursor;
  }

  public int getFolloweesCursor() {
    return followeesCursor;
  }

  public void setFollowersCursor(int followersCursor) {
    this.followersCursor = followersCursor;
  }

  public void setFolloweesCursor(int followeesCursor) {
    this.followeesCursor = followeesCursor;
  }

  private int followersCursor = -1;
  private int followeesCursor = -1;

  public int getFollowersCount() {
    return followersCount;
  }

  public String getTagline() {
    return tagline;
  }

  public int getFriendsCount() {
    return followingsCount;
  }

  @Column(name = "followings_count")
  private int followingsCount;

  public User() {
    super();
  }

  public static User fromJSON(JSONObject json) {
    try {
      User existingUser = new Select()
          .from(User.class)
          .where("uid = ?", json.getLong("id"))
          .executeSingle();
      if (existingUser != null) {
        return existingUser;
      }
      User user = new User();
      user.name = json.getString("name");
      user.uid = json.getLong("id");
      user.screenName = json.getString("screen_name");
      user.profileImageUrl = json.getString("profile_image_url");
      user.tagline = json.getString("description");
      user.followersCount = json.getInt("followers_count");
      user.followingsCount = json.getInt("friends_count");
      user.save();
      return user;
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void fromUserIds(
      JSONArray response,
      final HandleNewObjectsListener listener
  ) {
    List<String> ids = new ArrayList<>();
    for (int i = 0; i < response.length(); i++) {
      try {
        ids.add(response.getString(i));
      } catch (JSONException e) {
      }
    }

    TwitterApp.getRestClient().getUsers(
        Joiner.on(",").join(ids),
        new JsonHttpResponseHandler() {
          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            ArrayList<User> users = User.fromJsonArray(response);
            listener.handleNewObjects(users);
          }

          @Override
          public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.d("FailToFetch", errorResponse.toString());
          }
        }
    );
}

  private static ArrayList<User> fromJsonArray(JSONArray response) {
    ArrayList<User> users = new ArrayList<>();
    for(int i=0; i<response.length(); i++) {
      try {
        User user = User.fromJSON(response.getJSONObject(i));
        users.add(user);
      } catch(JSONException e) {
      }
    }
    return users;
  }

  public void resetCursor() {
    followeesCursor = -1;
    followeesCursor = -1;
  }

  public long getUid() {
    return uid;
  }

  public String getScreenName() {
    return screenName;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public String getName() {
    return name;
  }

  public String getPrefixName() {
    return "@" + screenName + "     ";
  }

}
