package com.codepath.apps.simpletweets.viewHolder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.listener.OnItemClickListener;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.util.CustomStyle;
import com.codepath.apps.simpletweets.util.LinkifiedTextView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiangyang_xiao on 2/14/16.
 */
public class ImageTextViewHolder extends BaseViewHolder {

  @Bind(R.id.ivProfileImage)
  ImageView ivProfileImage;
  @Bind(R.id.tvUserName)
  TextView tvUserName;
  @Bind(R.id.tvScreenName)
  TextView tvScreenName;
  @Bind(R.id.tvBody)
  LinkifiedTextView tvBody;
  @Bind(R.id.tvTime)
  TextView tvTime;

  Context mContext;

  public ImageTextViewHolder(View itemView, OnItemClickListener listener, Context context) {
    super(itemView, listener);
    mContext = context;
    ButterKnife.bind(this, itemView);
  }

  @Override
  public void bindView(Tweet tweet) {
    int black = ContextCompat.getColor(mContext, R.color.black);
    SpannableStringBuilder nameText = CustomStyle.stylizeFirstPart(
        tweet.getUser().getName(),
        "",
        black
    );
    tvUserName.setText(nameText);
    String screenName = "@" + tweet.getUser().getScreenName();
    tvScreenName.setText(screenName);
    tvTime.setText(tweet.getCreatedAt());
    tvBody.setText(tweet.getBody());
    ivProfileImage.setImageResource(android.R.color.transparent);
    Picasso.with(mContext)
        .load(tweet.getUser().getProfileImageUrl())
        .fit()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder_error)
        .into(ivProfileImage);
  }
}
