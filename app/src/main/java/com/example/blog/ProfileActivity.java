package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    ImageView profilePic,background;
    TextView name;
    String userId,userName,imgStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        background=findViewById(R.id.profile_background);
        profilePic=findViewById(R.id.ph_profilePic);
        background.getLayoutParams().height=350;


        Picasso.with(this).load(R.drawable.aqlamdefault).fit().into(background);


        Intent intent = getIntent();
//        userId= intent.getStringExtra("user_id");
        Bundle bundle=new Bundle();
        bundle=intent.getBundleExtra("user");
        userId=bundle.getString("user_id");
        userName=bundle.getString("user_name");
       imgStr=bundle.getString("profile_pic");

       if(imgStr != null && !imgStr.equals(""))
        Picasso.with(this).load(imgStr).fit().into(profilePic);



        //load profile info from db

        //add my posts fragment


        TextView username=findViewById(R.id.ph_name);
        username.setText(userName);


        






    }
}
