package com.example.blog;

import android.view.View;




public interface ClickListenerInterface {
    void onItemClick(View view, int position);
    void onCommentClick(View view, int position);
    void onPicClick(View view, int position);
    void onPostExpandClick(View view, int position);
    void onProfileClick(View view, int position);
}



