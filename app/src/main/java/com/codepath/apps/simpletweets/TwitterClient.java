package com.codepath.apps.simpletweets;

import android.content.Context;

import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
  public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this

  public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL

  public static final String REST_CONSUMER_KEY = "nR7mWcoVjfeBIzPunvEw1DQHq";       // Change this
  public static final String REST_CONSUMER_SECRET = "MROXlLBFw1c93NkTD2ILBrUcDh4vn5BW41LbPdoY69O5Si3fWx"; // Change this
  public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

  private static int COUNT_PER_FETCH = 25;

  public TwitterClient(Context context) {
    super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
  }

  /*
  GET https://api.twitter.com/1.1/
  count
	  since_id
	  */

  public void getHomeTimeline(AsyncHttpResponseHandler handler) {
    String url = getApiUrl("statuses/home_timeline.json");
    RequestParams params = new RequestParams();
    params.put("count", COUNT_PER_FETCH);
    if(Tweet.getMaxId() > 1) {
      params.put("max_id", Tweet.getMaxId()-1);
    }
    getClient().get(url, params, handler);
  }

  public void getCurrentUser(AsyncHttpResponseHandler handler) {
    String url = getApiUrl("account/verify_credentials.json");
    RequestParams params = new RequestParams();
    params.put("include_entities", false);
    params.put("skip_status", false);
    params.put("include_email", false);
    getClient().get(url, params, handler);
  }

  public void postTweet(
      String tweetBody,
      int in_reply_to_status_id,
      AsyncHttpResponseHandler handler
  ) {
    String url = getApiUrl("statuses/update.json");
    RequestParams params = new RequestParams();
    params.put("status", tweetBody);
    if(in_reply_to_status_id > 0) {
      params.put("in_reply_to_status_id", in_reply_to_status_id);
    }
    getClient().post(url, params, handler);
  }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}