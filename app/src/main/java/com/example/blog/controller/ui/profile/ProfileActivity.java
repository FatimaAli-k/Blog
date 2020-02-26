package com.example.blog.controller.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.blog.R;
import com.example.blog.URLs;
import com.example.blog.controller.tools.volley.FetchJson;
import com.example.blog.controller.tools.volley.IResult;
import com.example.blog.controller.ui.home.HomeFragment;
import com.example.blog.model.Categories;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    ImageView profilePic,background;
    TextView name;
    String userId,userName,imgStr,myUserId;
   String myFbId;
    String SHARD_PERFNAME="profile";
    URLs baseUrl=new URLs();
    String TAG="profile";
    IResult mResultCallback = null;
    FetchJson mVolleyService;
    int points;
//
//    LinearLayoutManager layoutManager;
//   ProfilePostsRecyclerAdapter adapter;
//   CoordinatorLayout coordinatorLayout;
//
//    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        final NestedScrollView scrollView=findViewById(R.id.scrollView);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

//        background=findViewById(R.id.profile_background);
        profilePic=findViewById(R.id.ph_profilePic);
//        background.getLayoutParams().height=350;


//        Picasso.with(this).load(R.drawable.aqlamdefault).fit().into(background);

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


        ProfilePostsFragment postsFragment=new ProfilePostsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.posts_frame, postsFragment, "postFragment").commit();

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


        name=findViewById(R.id.ph_name);


        //load profile info from db
        String url=baseUrl.getUrl(baseUrl.getProfileInfo());
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getApplicationContext());
        Map<String,String> params=new HashMap<>();
        params.put("id",userId);
        JSONObject sendJson=new JSONObject(params);
        mVolleyService.postDataVolley("GETCALL",url,sendJson);
//

        //add my posts fragment




    }

    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);
//                Toast.makeText(getContext(),"//"+response,Toast.LENGTH_LONG).show();
               if(parsJson(response)){
                   name.setText(userName);
                   if(imgStr == null || imgStr.equals("") || imgStr.equals("http://aqlam.turathalanbiaa.com/aqlam/image/000000.png")){
                       imgStr="https://alkafeelblog.edu.turathalanbiaa.com/aqlam/image/000000.png";
                   }
                     Picasso.with(getApplicationContext()).load(imgStr).fit().into(profilePic);


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
            }
        };
    }

    boolean parsJson(JSONObject response){

        boolean success=false;

        try {




           userName = response.getString("name");
           imgStr = response.getString("picture");
           points=response.getInt("points");
           success=true;


        }catch (JSONException e) {

            success=false;
        }

        return success;
    }

}
