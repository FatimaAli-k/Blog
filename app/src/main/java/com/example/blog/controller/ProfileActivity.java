package com.example.blog.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blog.R;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    ImageView profilePic,background;
    TextView name;
    String userId,userName,imgStr,myUserId;
   String myFbId;
    String SHARD_PERFNAME="profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        background=findViewById(R.id.profile_background);
        profilePic=findViewById(R.id.ph_profilePic);
        background.getLayoutParams().height=350;


        Picasso.with(this).load(R.drawable.aqlamdefault).fit().into(background);

        Intent intent = getIntent();
        userId= intent.getStringExtra("user_id");

        final boolean fbLoggedOut = AccessToken.getCurrentAccessToken() == null;
//
        if(!fbLoggedOut)
            myFbId=Profile.getCurrentProfile().getId();

        SharedPreferences prefs = getSharedPreferences(SHARD_PERFNAME, Activity.MODE_PRIVATE);
       myUserId= prefs.getString("user_id",null);

        //make api call, send userId get user info

        //check if its my profile
        if(userId.equals(myUserId)|| userId.equals(myFbId)){
            //my profile
            //show additional options

        }




//        Intent intent = getIntent();
////        userId= intent.getStringExtra("user_id");
//        Bundle bundle=new Bundle();
//        bundle=intent.getBundleExtra("user");
//        userId=bundle.getString("user_id");
//        userName=bundle.getString("user_name");
//       imgStr=bundle.getString("profile_pic");
//
//       if(imgStr != null && !imgStr.equals(""))
//        Picasso.with(this).load(imgStr).fit().into(profilePic);



        //load profile info from db

        //add my posts fragment


        TextView username=findViewById(R.id.ph_name);
        username.setText("id"+userId);


        






    }

    //false if logged in, true if logged out
    public boolean isLoggedOut(){

        SharedPreferences prefs = getSharedPreferences(SHARD_PERFNAME, Activity.MODE_PRIVATE);
        if (prefs.getString("user_id",null)!=null){
            return false;
        }
        return true;
    }

}
