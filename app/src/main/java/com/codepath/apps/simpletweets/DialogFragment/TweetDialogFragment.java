package com.codepath.apps.simpletweets.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.listener.TweetSavedListener;

/**
 * Created by xiangyang_xiao on 2/13/16.
 */
public class TweetDialogFragment extends DialogFragment {

  private TweetSavedListener listener;

  public TweetDialogFragment() {
  }

  public static TweetDialogFragment newInstance(
      TweetSavedListener listener
  ) {
    TweetDialogFragment dialogFragment =
        new TweetDialogFragment();
    dialogFragment.listener = listener;
    return dialogFragment;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final View view = LayoutInflater.from(getActivity())
        .inflate(R.layout.content_tweet, null);
    Dialog dialog = new AlertDialog
        .Builder(getActivity())
        .setView(view)
        .create();
    listener.setView(view);
    listener.setDialog(dialog);
    return dialog;
  }
}
