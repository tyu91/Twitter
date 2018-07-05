package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimeLineActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;

    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    //Context context;

    public void onComposeAction(MenuItem mi) {
        //create intent for new activity
        Intent intent = new Intent(this, ComposeActivity.class);
        //show the activity
        this.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Tweet tweet = Parcels.unwrap(intent.getParcelableExtra("etTweet"));
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.composemenu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("onCreate", "******** WITHIN ON CREATE METHOD ********");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        client = TwitterApp.getRestClient(this);
        //find recycler view
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        //init arraylist
        tweets = new ArrayList<>();
        //construct adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);
        //recycler view set up (layout manager incl. adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //set adapter
        rvTweets.setAdapter(tweetAdapter);


        Log.i("onCreate", "******** BEFORE POPULATE TIMELINE ********");

        populateTimeline();

        Log.i("onCreate", "******** AFTER POPULATE TIMELINE ********");
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i("onSuccess", "****************** in JSONArray onSuccess ******************");
                //iterate through the JSON array
                //for each entry, deserialize JSON object
                for (int i = 0; i < response.length(); i++) {
                    Tweet tweet = null;
                    try {
                        //convert each obj to a Tweet model
                        //add that Tweet model to our data source
                        //notify the adapter we've added an item
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.i("onSuccess", "****************** exiting JSONArray onSuccess ******************");
            }

                @Override
                public void onSuccess ( int statusCode, Header[] headers, JSONObject response){
                    Log.d("TwitterClient", response.toString());
                }

                @Override
                public void onFailure ( int statusCode, Header[] headers, String
                responseString, Throwable throwable){
                    Log.d("TwitterClient", responseString);
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure ( int statusCode, Header[] headers, Throwable
                throwable, JSONObject errorResponse){
                    Log.d("TwitterClient", errorResponse.toString());
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure ( int statusCode, Header[] headers, Throwable
                throwable, JSONArray errorResponse){
                    Log.d("TwitterClient", errorResponse.toString());
                    throwable.printStackTrace();
                }

            });

        }
    }



