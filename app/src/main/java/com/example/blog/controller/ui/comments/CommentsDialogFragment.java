package com.example.blog.controller.ui.comments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.blog.R;
import com.example.blog.model.Comments;
import com.facebook.AccessToken;

import java.util.ArrayList;


public class CommentsDialogFragment extends DialogFragment  {

//    private CategoriesViewModel categoriesViewModel;

    SwipeRefreshLayout swipeRefresh;
  CommentsRecyclerViewAdapter adapter;
    LinearLayout commentsLinearLayout;
    ArrayList<Comments> commentsList =new ArrayList<>();

    public CommentsDialogFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.dialog_comments_fragment, container,false);
        SharedPreferences prefs = getActivity().getSharedPreferences("profile", Activity.MODE_PRIVATE);
        final boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
//

        CommentsFragment commentsFragment=new CommentsFragment();
        CommentBarFragment commentBar=new CommentBarFragment();
        int postId= getArguments().getInt("postId");

        Bundle bundle=new Bundle();
        bundle.putInt("postId",postId);
        commentBar.setArguments(bundle);
        commentsFragment.setArguments(bundle);

       getChildFragmentManager().beginTransaction().replace(R.id.dialogFrame,commentsFragment,"dialogFrame").commit();
        if(prefs.getString("user_id",null)!=null || !loggedOut)
            getChildFragmentManager().beginTransaction().replace(R.id.dialogCommentBarFrame, commentBar, "commentBarFrame").commit();

        getDialog().setTitle("CommentsDialogFragment");
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_round_corner);

//        int width = LinearLayout.LayoutParams.MATCH_PARENT;
//        int height = LinearLayout.LayoutParams.MATCH_PARENT;
//        boolean focusable = true;
////
//final PopupWindow popupWindow = new PopupWindow(view, width, height, focusable);
//        popupWindow.setOutsideTouchable(true);
//        //Set the location of the window on the screen
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

//        int postId=0;
//
//
//
//        if(getArguments()!=null) {
//            postId = getArguments().getInt("postId");
//
//        }
//
//        Comments com=new Comments();
//        com.setContent("comments"+postId);
//        commentsList.add(com);
//
//        RecyclerView recyclerView = view.findViewById(R.id.comments_recycler_view);
//        commentsLinearLayout = view.findViewById(R.id.commentsLinearLayout);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter= new CommentsRecyclerViewAdapter( getContext(), commentsList);
//        adapter.setClickListener(this);
//        recyclerView.setAdapter(adapter);
//
//       CommentBarFragment commentBar=new CommentBarFragment();
//        FragmentManager fm = getChildFragmentManager();
//        fm.beginTransaction().replace(R.id.commentBarFrame, commentBar, "commentBarFrame").commit();

        return view;
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