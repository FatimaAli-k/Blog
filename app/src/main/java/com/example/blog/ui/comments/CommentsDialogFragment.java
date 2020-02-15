package com.example.blog.ui.comments;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.Categories;
import com.example.blog.model.Comments;
import com.example.blog.ui.category.CategoriesRecyclerViewAdapter;

import java.util.ArrayList;


public class CommentsDialogFragment extends DialogFragment implements CommentsRecyclerViewAdapter.ItemClickListener {

//    private CategoriesViewModel categoriesViewModel;

  CommentsRecyclerViewAdapter adapter;
    LinearLayout commentsLinearLayout;
    ArrayList<Comments> commentsList =new ArrayList<>();

    public CommentsDialogFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_comments, container,false);

        getDialog().setTitle("CommentsDialogFragment");

//        int width = LinearLayout.LayoutParams.MATCH_PARENT;
//        int height = LinearLayout.LayoutParams.MATCH_PARENT;
//        boolean focusable = true;
////
//final PopupWindow popupWindow = new PopupWindow(view, width, height, focusable);
//        popupWindow.setOutsideTouchable(true);
//        //Set the location of the window on the screen
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        int postId=0;



        if(getArguments()!=null) {
            postId = getArguments().getInt("postId");

        }

        Comments com=new Comments();
        com.setContent("comments"+postId);
        commentsList.add(com);

        RecyclerView recyclerView = view.findViewById(R.id.comments_recycler_view);
        commentsLinearLayout = view.findViewById(R.id.commentsLinearLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter= new CommentsRecyclerViewAdapter( getContext(), commentsList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

       CommentBarFragment commentBar=new CommentBarFragment();
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().replace(R.id.commentBarFrame, commentBar, "commentBarFrame").commit();

        return view;
    }
    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(getContext(), "pos: " + position+" id: "+ commentsList.get(position).getId(), Toast.LENGTH_SHORT).show();


    }
    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();

        if(window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int)(getResources().getDisplayMetrics().widthPixels*0.80);
        params.height = (int)(getResources().getDisplayMetrics().heightPixels*0.80);
        window.setAttributes(params);
    }
}