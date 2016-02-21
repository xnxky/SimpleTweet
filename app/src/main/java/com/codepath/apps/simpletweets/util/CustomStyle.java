package com.codepath.apps.simpletweets.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

/**
 * Created by xiangyang_xiao on 2/7/16.
 */
public class CustomStyle {
  public static SpannableStringBuilder stylizeFirstPart
      (String str1, String str2, int targetColor) {
    final SpannableStringBuilder sb = new SpannableStringBuilder(
        str1 + " " + str2
    );

    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
    final ForegroundColorSpan fcs = new ForegroundColorSpan(targetColor);

    sb.setSpan(bss, 0, str1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    sb.setSpan(fcs, 0, str1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

    return sb;
  }
}
