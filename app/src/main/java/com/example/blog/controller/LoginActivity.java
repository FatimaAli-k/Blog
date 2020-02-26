package com.example.blog.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.blog.MainActivity;
import com.example.blog.R;
import com.example.blog.URLs;
import com.example.blog.controller.tools.volley.FetchJson;
import com.example.blog.controller.tools.volley.IResult;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton fbLogin;
    EditText username,password;
    URLs baseUrl=new URLs();
    String TAG="login";
    IResult mResultCallback = null;
    FetchJson mVolleyService;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);

        username=findViewById(R.id.user_name);
        password=findViewById(R.id.password);


        Button login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //make api call
                progress.show();
                makeApiCall(username.getText().toString(), password.getText().toString());

            }
        });

        Button register=findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });


       fbLogin = findViewById(R.id.login_button);

       Button fbLoginCustomBtn=findViewById(R.id.fb_login);
       fbLoginCustomBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               fbLogin.performClick();
           }
       });

        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;



        fbLogin.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        fbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("facebook login", loggedIn + " ??");



                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });

    }

    private void makeApiCall(String email, String password) {
        String url=baseUrl.getUrl(baseUrl.getLogin());
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getApplicationContext());
        Map<String,String> params=new HashMap<>();
        params.put("email",email);
        params.put("password",password);
        JSONObject sendJson=new JSONObject(params);
        mVolleyService.postDataVolley("GETCALL",url,sendJson);
//
    }
    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);
//                Toast.makeText(getContext(),"//"+response,Toast.LENGTH_LONG).show();
                progress.dismiss();

                if( parsJson(response)){
                   Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
               }
                else {
                    Toast.makeText(getApplicationContext(),R.string.login_credentials_wrong,Toast.LENGTH_LONG).show();

                }


            }
            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);


//                Toast.makeText(getContext(),"//"+response,Toast.LENGTH_LONG).show();

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + error);
                Toast.makeText(getApplicationContext(),"noooooooo",Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        };
    }


    boolean parsJson(JSONObject response) {

        boolean success = false;

        try {


            String name = response.getString("name");
           String imgStr = response.getString("picture");
           String id = response.getString("id");
            if(imgStr == null || imgStr.equals("") || imgStr.equals("http://aqlam.turathalanbiaa.com/aqlam/image/000000.png")){
                imgStr="https://alkafeelblog.edu.turathalanbiaa.com/aqlam/image/000000.png";
            }

            SharedPreferences prefs = getSharedPreferences("profile", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_id",id);
            editor.putString("user_name",name);
            editor.putString("profile_pic",imgStr);
            editor.apply();
           success=true;


        } catch (JSONException e) {
            success=false;

        }
        return success;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


}

