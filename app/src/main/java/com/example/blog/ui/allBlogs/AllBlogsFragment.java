package com.example.blog.ui.allBlogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.blog.R;

public class AllBlogsFragment extends Fragment {

//    private AllBlogsViewModel allBlogsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        allBlogsViewModel =
//                ViewModelProviders.of(this).get(AllBlogsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_all_blogs, container, false);
//        final TextView textView = root.findViewById(R.id.text_all_blogs);
//        allBlogsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        return root;
    }
}