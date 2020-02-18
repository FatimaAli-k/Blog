package com.example.blog.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.blog.MainActivity;
import com.example.blog.R;
import com.example.blog.model.Posts;
import com.example.blog.ui.comments.CommentsDialogFragment;
import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.blog.ui.home.PaginationScrollListener.PAGE_START;

public class HomeFragment extends Fragment implements PostRecyclerViewAdapter.ItemClickListener, PostRecyclerViewAdapter.CommentClickListener,
        PostRecyclerViewAdapter.PicClickListener, SwipeRefreshLayout.OnRefreshListener{

//
   PostRecyclerViewAdapter adapter;
    RelativeLayout postsRelativeLayout;
    ArrayList<Posts> postsList=new ArrayList<>();
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;


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


//
        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.show();


        swipeRefresh=root.findViewById(R.id.swipeRefresh);
    swipeRefresh.setOnRefreshListener(this);
    recyclerView = root.findViewById(R.id.posts_recycler_view);
    postsRelativeLayout = root.findViewById(R.id.postsRelativeLayout);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    recyclerView.setLayoutManager(layoutManager);
    adapter = new PostRecyclerViewAdapter(getContext(), postsList);
    adapter.setClickListener(this);
    adapter.setCommentClickListener(this);
    adapter.setPicClickListener(this);
    recyclerView.setAdapter(adapter);


        //scroll to top
    Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
    toolbar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            recyclerView.smoothScrollToPosition(0);
        }
    });

    //get posts from api
        try {
            doApiCall();
        }catch (Exception e){
            Toast.makeText(getContext(),"/"+e,Toast.LENGTH_LONG).show();
        }

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                doApiCall();
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });





        return root;
    }

    //on post click open expanded post fragment
    @Override
    public void onItemClick(View view, final int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("postId",postsList.get(position).getId());
        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_post,bundle);
//
 Toast.makeText(getContext(), "pos: " + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCommentClick(View view, final int position) {
        final int postid= postsList.get(position).getId();

        Bundle bundle = new Bundle();
        bundle.putInt("postId",postid);


        CommentsDialogFragment cf=new CommentsDialogFragment();
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



    /**
     * do api call here to fetch data from server
     */
    private void doApiCall() {

        new Handler().postDelayed(new Runnable() {
            //put all run() method code on Success of APIs
            @Override
            public void run() {
                //get json data from api

                Posts post;

                for(int i=0;i<10;i++){
                    itemCount++;


                    post=new Posts();
                    post.setImage("https://square.github.io/picasso/static/sample.png");
                    post.setId(i);
                    post.setTitle("title"+i);
                    post.setContent("vv"+itemCount);
                    postsList.add(post);
                }

                /**
                 * manage progress view
                 */
                if (currentPage != PAGE_START) adapter.removeLoading();
                adapter.addItems(postsList);
                swipeRefresh.setRefreshing(false);
                // check weather is last page or not
                if (currentPage < totalPage) {
                    adapter.addLoading();
                } else {
                    isLastPage = true;
                }
                isLoading = false;
            }
        }, 1500);
    }
    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        postsList.clear();
        adapter.notifyDataSetChanged();
        doApiCall();

    }


}