package com.codepath.apps.simpletweets.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.util.CustomStyle;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiangyang_xiao on 2/17/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

  public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
    super(context, 0, tweets);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    Tweet tweet = getItem(position);
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext())
          .inflate(R.layout.item_tweet, parent, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    //android.R.color.black will not work (not sure why)
    int black = ContextCompat.getColor(getContext(), R.color.black);
    SpannableStringBuilder nameText = CustomStyle.stylizeFirstPart(
        tweet.getUser().getName(),
        "",
        black
    );
    viewHolder.tvUserName.setText(nameText);
    String screenName = "@"+tweet.getUser().getScreenName();
    viewHolder.tvScreenName.setText(screenName);
    viewHolder.tvTime.setText(tweet.getCreatedAt());
    viewHolder.tvBody.setText(tweet.getBody());
    viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
    Picasso.with(getContext())
        .load(tweet.getUser().getProfileImageUrl())
        .fit()
        .into(viewHolder.ivProfileImage);
    return convertView;
  }

  static class ViewHolder {
    @Bind(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.tvScreenName)
    TextView tvScreenName;
    @Bind(R.id.tvBody)
    TextView tvBody;
    @Bind(R.id.tvTime)
    TextView tvTime;

    public ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }

  }

}
