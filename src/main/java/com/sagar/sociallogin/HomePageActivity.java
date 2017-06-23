package com.sagar.sociallogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sm185435 on 3/1/2017.
 */

public class HomePageActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView name;
    private TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profilePic = (ImageView) findViewById(R.id.imgProfilePic);
        name = (TextView) findViewById(R.id.txtName);
        email = (TextView) findViewById(R.id.txtEmail);

        Intent intent = getIntent();

        if (intent.getStringExtra("from").compareTo("google") == 0) {

            Picasso.with(this).load(intent.getStringExtra("image")).into(profilePic);
            name.setText(intent.getStringExtra("name"));
            email.setText(intent.getStringExtra("email"));
        } else if (intent.getStringExtra("from").compareTo("facebook") == 0) {

            Picasso.with(this).load(intent.getStringExtra("image")).into(profilePic);
            name.setText(intent.getStringExtra("name"));
            email.setText(intent.getStringExtra("email"));
        } else if (intent.getStringExtra("from").compareTo("twitter") == 0) {

            Picasso.with(this).load(intent.getStringExtra("image")).into(profilePic);
            name.setText(intent.getStringExtra("name"));

            TwitterSession session = Twitter.getSessionManager().getActiveSession();

            TwitterAuthClient authClient = new TwitterAuthClient();
            authClient.requestEmail(session, new Callback<String>() {
                @Override
                public void success(Result<String> result) {
                    Log.e("Test", result.data);

                    email.setText(result.data);
                }

                @Override
                public void failure(TwitterException exception) {

                }
            });
        } else if (intent.getStringExtra("from").compareTo("linkedin") == 0) {


            String url = "https://api.linkedin.com/v1/people/~";

            APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
            apiHelper.getRequest(this, url, new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse apiResponse) {

                    JSONObject jObject = apiResponse.getResponseDataAsJson();
                    try {
                        name.setText(jObject.getString("firstName")+" "+jObject.getString("lastName"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onApiError(LIApiError liApiError) {
                    // Error making GET request!
                }
            });

        }
        /*Log.e("Test", "Name: " + intent.getStringExtra("name") + ", email: " + intent.getStringExtra("email")
                + ", Image: " + intent.getStringExtra("image"));*/
    }

    public void logout(View view) {

        Intent intent1 = getIntent();

        if (intent1.getStringExtra("from").compareTo("facebook") == 0) {


            LoginManager.getInstance().logOut();

        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

}

