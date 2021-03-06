package com.codepath.apps.simpletweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

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

  public static ArrayList<User> fromJSONArray(JSONArray response) {
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
