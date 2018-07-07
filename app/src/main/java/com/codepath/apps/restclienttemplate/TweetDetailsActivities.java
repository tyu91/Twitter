package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

public class TweetDetailsActivities extends AppCompatActivity {
    TwitterClient client;

    private TextView tvUserName;
    private TextView tvTime;
    private TextView tvBody;
    private ImageButton ibComment;
    private ImageButton ibLike;
    private ImageButton ibRT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_tweet_details);

        client = TwitterApp.getRestClient(this);

        tvUserName = findViewById(R.id.tvUserName);
        tvTime = findViewById(R.id.tvTime);
        tvBody = findViewById(R.id.tvBody);
        ibComment = findViewById(R.id.ibComment);
        ibLike = findViewById(R.id.ibLike);
        ibRT = findViewById(R.id.ibRT);
    }
}
