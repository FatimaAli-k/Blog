package com.example.blog.ui.comments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.blog.R;

public class CommentBarFragment extends Fragment {

//    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.comment_bar, container, false);

        Button sendComment=root.findViewById(R.id.sendComment);
        EditText comment=root.findViewById(R.id.comment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"send cmt",Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}