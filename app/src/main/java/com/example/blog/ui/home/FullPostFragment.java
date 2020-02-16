package com.example.blog.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.blog.R;

public class FullPostFragment extends Fragment {

    TextView postTitle,postDetails,seeMore,postId,viewCount,comments;
    ImageView postPic;
    int post_id;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
          View root = inflater.inflate(R.layout.full_post, container, false);
//






            postPic=root.findViewById(R.id.post_pic);
            postTitle=root.findViewById(R.id.postTitle);
            postDetails=root.findViewById(R.id.postDetails);
            postId=root.findViewById(R.id.psi_postId);
            viewCount=root.findViewById(R.id.viewsCount);
            comments=root.findViewById(R.id.commentsCount);


        post_id=getArguments().getInt("postId");

//        postId.setText(post_id);
        postTitle.setText("test"+post_id);


        return root;
    }
}