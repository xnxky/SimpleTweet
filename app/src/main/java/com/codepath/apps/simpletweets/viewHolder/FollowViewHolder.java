package com.codepath.apps.simpletweets.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activity.profile.OtherProfileActivity;
import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.models.User;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public class FollowViewHolder extends BaseViewHolder {

  @Bind(R.id.ivProfileImage)
  ImageView ivProfileImage;
  @Bind(R.id.tvUserName)
  TextView tvUserName;
  @Bind(R.id.tvScreenName)
  TextView tvScreenName;

  Context mContext;

  public FollowViewHolder(View itemView, OnItemClickListener listener, Context context) {
    super(itemView, listener);
    mContext = context;
    ButterKnife.bind(this, itemView);
  }

  @Override
  public void bindView(Object userObj) {
    final User user = User.class.cast(userObj);
    Picasso.with(mContext)
        .load(user.getProfileImageUrl())
        .fit()
        .error(R.drawable.placeholder_error)
        .placeholder(R.drawable.placeholder)
        .into(ivProfileImage);

    tvUserName.setText(user.getName());
    String screenName = user.getTagline();
    tvScreenName.setText(screenName);

    ivProfileImage.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(mContext, OtherProfileActivity.class);
            intent.putExtra("user", user);
            mContext.startActivity(intent);
          }
        }
    );
  }
}



