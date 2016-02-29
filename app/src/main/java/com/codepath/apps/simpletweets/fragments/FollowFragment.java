package com.codepath.apps.simpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activity.FollowActivity;
import com.codepath.apps.simpletweets.adapter.FollowRecyclerViewAdapter;
import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public abstract class FollowFragment extends RecyclerViewFragment<User> {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void setRcAdapater() {
    rcAdapter = new FollowRecyclerViewAdapter(
        objects,
        new OnItemClickListener() {
          @Override
          public void onItemClick(View itemView, int position) {
            Intent intent = new Intent(getActivity(), FollowActivity.class);
            intent.putExtra("user", objects.get(position));
            startActivity(intent);
          }
        }
    );
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_follow;
  }

  @Override
  protected List<User> getAllStoredObjects() {
    Toast.makeText(
        getActivity(),
        "network is not available; use local data instead",
        Toast.LENGTH_SHORT).show();
    return Collections.emptyList();
  }

  @Override
  protected ArrayList<User> getObjectsFromJsonArray(JSONArray response) {
    return new ArrayList<>();
  };

  @Override
  protected ArrayList<User> getObjectsFromJsonObject(JSONObject response) {
    try {
      return User.fromJSONArray(response.getJSONArray("ids"));
    } catch (JSONException e) {
      Log.e("JSON Follow Error", e.getMessage());
    }
    return new ArrayList<>();
  }
}
