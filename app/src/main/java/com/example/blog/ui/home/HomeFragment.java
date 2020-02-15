package com.example.blog.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.Posts;
import com.example.blog.ui.comments.CommentsFragment;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements PostRecyclerViewAdapter.ItemClickListener, PostRecyclerViewAdapter.CommentClickListener, PostRecyclerViewAdapter.PicClickListener{

//
   PostRecyclerViewAdapter adapter;
    RelativeLayout postsRelativeLayout;
    ArrayList<Posts> postsList=new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//
        Toast.makeText(getActivity(),"id"+getArguments().getInt("catId"),Toast.LENGTH_LONG).show();

        if(getArguments().getBoolean("loggedOut")){

            LoginManager.getInstance().logOut();

            Intent intent = getActivity().getIntent();

            startActivity(intent);
        }

       Posts post=new Posts();
        post.setImage("https://square.github.io/picasso/static/sample.png");

        post.setId(54);
        post.setTitle("title");
        post.setContent("content content content content content content content content content content content content content content" +
                " content content content content content content content content content content" );
        postsList.add(post);

        for(int i=0;i<5;i++){
           post=new Posts();
//           post.setImage("https://square.github.io/picasso/static/sample.png");


            post.setId(i);
            post.setTitle("title"+i);
            post.setContent("content content content content content content content content content content content content content content" +
                    " content content content content content content content content content content" +
                    " content content content content content content content content content content" +
                    " content content content content content content content content content content"+i);
            postsList.add(post);
        }


try {


    RecyclerView recyclerView = root.findViewById(R.id.posts_recycler_view);
    postsRelativeLayout = root.findViewById(R.id.postsRelativeLayout);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    adapter = new PostRecyclerViewAdapter(getContext(), postsList);
    adapter.setClickListener(this);
    adapter.setCommentClickListener(this);
    adapter.setPicClickListener(this);
    recyclerView.setAdapter(adapter);
}catch (Exception e){}





        return root;
    }

    @Override
    public void onItemClick(View view, final int position) {
//        Toast.makeText(getContext(), "pos: " + position+" id: "+ catList.get(position).getId(), Toast.LENGTH_SHORT).show();



// Toast.makeText(getContext(), "pos: " + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCommentClick(View view, final int position) {
        final int postid= postsList.get(position).getId();
       
        Bundle bundle = new Bundle();
        bundle.putInt("postId",postid);


        CommentsFragment cf=new CommentsFragment();
        cf.setArguments(bundle);
        FragmentTransaction ft =  getChildFragmentManager().beginTransaction();
        Fragment prev =  getChildFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        cf.show(ft, "dialog");
    }


    @Override
    public void onPicClick(View view, final int position) {


        PopUpClass popUpClass = new PopUpClass();
        popUpClass.showPopupWindow(view,postsList.get(position).getImage());

//        Toast.makeText(getContext(), "ooooooooooooo" , Toast.LENGTH_SHORT).show();

    }

}