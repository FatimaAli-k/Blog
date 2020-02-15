package com.example.blog.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.blog.MainActivity;
import com.example.blog.R;
import com.example.blog.ui.comments.CommentBarFragment;
import com.example.blog.ui.comments.CommentsDialogFragment;
import com.example.blog.ui.comments.CommentsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExpandedPostFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.expanded_post, container, false);
//

        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();

        fab.hide();

        FullPostFragment postFragment=new FullPostFragment();
        CommentsFragment commentsFragment = new CommentsFragment();
        CommentBarFragment commentBar=new CommentBarFragment();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fragment1, postFragment, "fragmentone").commit();
        fm.beginTransaction().replace(R.id.fragment2, commentsFragment, "fragmenttwo").commit();
        getChildFragmentManager().beginTransaction().replace(R.id.commentBarFrame, commentBar, "commentBarFrame").commit();



        return root;
    }
}