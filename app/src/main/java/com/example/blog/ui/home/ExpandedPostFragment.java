package com.example.blog.ui.home;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.blog.MainActivity;
import com.example.blog.R;
import com.example.blog.model.Posts;
import com.example.blog.ui.comments.CommentBarFragment;
import com.example.blog.ui.comments.CommentsDialogFragment;
import com.example.blog.ui.comments.CommentsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExpandedPostFragment extends Fragment {

    int postId;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.expanded_post, container, false);
//

        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.hide();

        Posts post=new Posts();
        if(getArguments()!=null)
            post=getArguments().getParcelable("post");

        FullPostFragment postFragment=new FullPostFragment();
        CommentsFragment commentsFragment = new CommentsFragment();
        CommentBarFragment commentBar=new CommentBarFragment();

        //
        Bundle bundle=new Bundle();
        bundle.putInt("postId",post.getId());
        bundle.putParcelable("post",post);
        postFragment.setArguments(bundle);
        commentsFragment.setArguments(bundle);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.fragment1, postFragment, "fragmentone").commit();
        fm.beginTransaction().replace(R.id.fragment2, commentsFragment, "fragmenttwo").commit();
        getChildFragmentManager().beginTransaction().replace(R.id.commentBarFrame, commentBar, "commentBarFrame").commit();


        final ScrollView scrollView=root.findViewById(R.id.scrollView_post);
//        scrollView.fullScroll(ScrollView.FOCUS_UP);
        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               scrollView.fullScroll(ScrollView.FOCUS_UP);

            }
        });

        return root;
    }
}