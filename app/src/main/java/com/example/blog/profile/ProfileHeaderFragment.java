package com.example.blog.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.blog.R;
import com.squareup.picasso.Picasso;

public class ProfileHeaderFragment extends Fragment {
    ImageView profilePic;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
           View root = inflater.inflate(R.layout.profile_header, container, false);
//        profilePic=root.findViewById(R.id.profilePic);
//        Picasso.with( getContext()).
//                        load("http://aqlam.turathalanbiaa.com/aqlam/image/000000.png")
//                        .fit().centerCrop().into( profilePic);


        return root;
    }
}