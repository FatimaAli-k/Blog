package com.example.blog.ui.comments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.Comments;

import java.util.ArrayList;


public class CommentsFragment extends Fragment implements CommentsRecyclerViewAdapter.ItemClickListener {

//    private CategoriesViewModel categoriesViewModel;

  CommentsRecyclerViewAdapter adapter;
    LinearLayout commentsLinearLayout;
    ArrayList<Comments> commentsList =new ArrayList<>();

    public CommentsFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_comments, container,false);


//

        int postId=0;



        if(getArguments()!=null) {
            postId = getArguments().getInt("postId");

        }

        Comments com=new Comments();
        com.setContent("comments"+postId);
        commentsList.add(com);

        com=new Comments();
        com.setContent("comments"+2);
        commentsList.add(com);

       com=new Comments();
        com.setContent("comments"+4);
        commentsList.add(com);

        RecyclerView recyclerView = view.findViewById(R.id.comments_recycler_view);
        commentsLinearLayout = view.findViewById(R.id.commentsLinearLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter= new CommentsRecyclerViewAdapter( getContext(), commentsList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);



        return view;
    }
    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(getContext(), "pos: " + position+" id: "+ commentsList.get(position).getId(), Toast.LENGTH_SHORT).show();


    }
    @Override
    public void onResume() {
        super.onResume();

    }
}