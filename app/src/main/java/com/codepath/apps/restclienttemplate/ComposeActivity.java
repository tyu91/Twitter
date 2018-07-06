package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    TwitterClient client;

    private TextView tvCount;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient(this);
        tvCount = findViewById(R.id.tvCount);
        editText = findViewById(R.id.etTweet);

        editText.addTextChangedListener(mTextEditorWatcher);
    }

    //called in xml file; when hit post, do this
    public void onSubmit(View view){
        Log.i("onSubmit", "****************** in onSubmit ******************");

        Intent intent = getIntent();
        String mytweet = editText.getText().toString();

        if (intent.getBooleanExtra("", false)) {

            client.sendTweet(mytweet, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Log.i("onSubmit", "****************** in try block of onSuccess after sentTweet ******************");
                        Tweet tweet = Tweet.fromJSON(response);

                        // Prepare data intent
                        Intent data = new Intent();
                        // Pass relevant data back as a result
                        data.putExtra("etTweet", Parcels.wrap(tweet));
                        // Activity finished ok, return the data
                        setResult(RESULT_OK, data); // set result code and bundle data for response
                        finish(); // closes the activity, pass data to parent
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });

        } else {
            client.sendTweet(mytweet, intent.getLongExtra("uid", 0), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Log.i("onSubmit", "****************** in try block of onSuccess after sentTweet ******************");
                        Tweet tweet = Tweet.fromJSON(response);

                        // Prepare data intent
                        Intent data = new Intent();
                        // Pass relevant data back as a result
                        data.putExtra("etTweet", Parcels.wrap(tweet));
                        // Activity finished ok, return the data
                        setResult(RESULT_OK, data); // set result code and bundle data for response
                        finish(); // closes the activity, pass data to parent
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCount.setText(String.valueOf(140-s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };



}


