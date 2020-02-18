package com.example.blog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    ImageView profilePic,background;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        background=findViewById(R.id.profile_background);
        background.getLayoutParams().height=350;


        Picasso.with(this).load(R.drawable.aqlamdefault).fit().into(background);




    }
}
