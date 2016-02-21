package com.codepath.apps.simpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by xiangyang_xiao on 2/17/16.
 */
public class User implements Serializable {
  private String name;
  private long uid;
  private String screenName;
  private String profileImageUrl;

  public static User fromJSON(JSONObject json) {
    User user = new User();
    try {
      user.name = json.getString("name");
      user.uid = json.getLong("id");
      user.screenName = json.getString("screen_name");
      user.profileImageUrl = json.getString("profile_image_url");

    } catch (JSONException e) {
      e.printStackTrace();
    }
    return user;
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
}
