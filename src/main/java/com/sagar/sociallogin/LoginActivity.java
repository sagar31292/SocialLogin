package com.sagar.sociallogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "VoyQUAiZmvajbrd7KT6s8oKpB";
    private static final String TWITTER_SECRET = "qOyb7X6oTE5qxcoB36YjdHNqUyaL4mk8YwtdkxKUPYWzXXRtFl";


    //private static final String TWITTER_KEY = "nI5lUqK67OEsXwACNhDmXfzRl";
    //private static final String TWITTER_SECRET = "WY0RlEz8RBDDaO4y8IvFuFBou2RCL0ex5CJ0broMh81g4FxrC8";


    private static final int RC_SIGN_IN = 007;
    private SignInButton btnSignIn;
    private GoogleApiClient mGoogleApiClient;

    private LoginButton loginButtonFB;
    private TwitterLoginButton loginButtonTwitter;

    CallbackManager callbackManager;


    private static final String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));


        setContentView(R.layout.activity_login);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        loginButtonFB = (LoginButton) findViewById(R.id.btn_fb_login);
        loginButtonTwitter = (TwitterLoginButton) findViewById(R.id.login_button_twitter);


        facebookLoginInit();
        twitterLoginInit();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleLogin();
            }
        });

    }

    private void twitterLoginInit() {

        loginButtonTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                final TwitterSession session = Twitter.getSessionManager().getActiveSession();

                Call<User> userResult = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);
                userResult.enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {

                        final Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                        intent.putExtra("from", "twitter");
                        intent.putExtra("name", result.data.name);
                        intent.putExtra("image", result.data.profileImageUrl);

                        startActivity(intent);
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e("Test", exception.getMessage());
            }
        });
    }

    public void googleLogin() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void facebookLoginInit() {

        callbackManager = CallbackManager.Factory.create();

        loginButtonFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("Test", "Success");

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("Test", response.toString());
                        // Get facebook data from login
                        progressDialog.dismiss();
                        Bundle bFacebookData = getFacebookData(object);

                        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                        intent.putExtra("from", "facebook");
                        intent.putExtra("name", bFacebookData.getString("first_name"));
                        intent.putExtra("email", bFacebookData.getString("email"));
                        intent.putExtra("image", bFacebookData.getString("profile_pic"));
                        startActivity(intent);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("Test", "Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("Test", "Error");
            }
        });


    }


    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
            return null;
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.e(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra("from", "google");
            intent.putExtra("name", acct.getDisplayName());
            intent.putExtra("email", acct.getEmail());

            if (acct.getPhotoUrl() != null) {
                intent.putExtra("image", acct.getPhotoUrl().toString());
            } else {
                intent.putExtra("image", "no_profile_pic");

            }
            startActivity(intent);


        } else {

            Log.e(TAG, "Fail");
        }
    }


    public void requestUserProfile(LoginResult loginResult) {
        GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject me, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                        } else {
                            try {
                                String email = response.getJSONObject().get("email").toString();
                                Log.e("Test", email);
                            } catch (JSONException e) {
                                Log.e("Test", e.getMessage());
                            }
                            String id = me.optString("id");
                            // send email and id to your web server
                            Log.e("Test", response.getRawResponse());
                            Log.e("Test", me.toString());
                        }
                    }
                }).executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

            return;
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

        loginButtonTwitter.onActivityResult(requestCode, resultCode, data);


        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        requestCode, resultCode, data);
    }


    public void pagination(View view) {

        Intent intent = new Intent(this, PaginationActivity.class);
        startActivity(intent);
    }

    public void dataTest(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

    public void loginLinkedIn(View view) {

        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        /*Toast.makeText(getApplicationContext(), "success" +
                                        LISessionManager
                                                .getInstance(getApplicationContext())
                                                .getSession().getAccessToken().toString(),
                                Toast.LENGTH_LONG).show();*/


                        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                        intent.putExtra("from", "linkedin");

                        startActivity(intent);

                    }

                    @Override
                    public void onAuthError(LIAuthError error) {

                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);


    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
}
