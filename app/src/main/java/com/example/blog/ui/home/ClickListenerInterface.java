package com.example.blog.ui.home;

import android.view.View;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;


public interface ClickListenerInterface {
    void onItemClick(View view, int position);
    void onCommentClick(View view, int position);
    void onPicClick(View view, int position);
    void onPostExpandClick(View view, int position);
}



