package com.example.blog.ui.bloggers;

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

public class BloggersFragment extends Fragment {

    private BloggersViewModel bloggersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bloggersViewModel =
                ViewModelProviders.of(this).get(BloggersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bloggers, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        bloggersViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}