package com.codepath.apps.simpletweets.listener;

import java.util.List;

/**
 * Created by xiangyang_xiao on 2/28/16.
 */
public interface HandleNewObjectsListener<T> {
  void handleNewObjects(List<T> objects);
}
