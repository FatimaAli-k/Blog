package com.example.blog.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.android.volley.VolleyError;
import com.example.blog.MainActivity;
import com.example.blog.R;
import com.example.blog.model.Posts;
import com.example.blog.ui.comments.CommentsDialogFragment;
import com.example.blog.volley.FetchJson;
import com.example.blog.volley.IResult;
import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener, ClickListenerInterface{

    //
    PostRecyclerAdapter adapter;
    RelativeLayout postsRelativeLayout;
    //    ArrayList<Posts> postsList=new ArrayList<>();
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;

    private String TAG = "commentsFragment";
    IResult mResultCallback = null;
    FetchJson mVolleyService;


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
        adapter = new PostRecyclerAdapter(getContext());

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

        loadFirstPage();
//
//\/2CAL2433ZeIihfX1Hb2139CX0pW.jpg

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();

                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
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


//        String url="http://192.168.9.108:8000/api/post";
//        String url="https://api.themoviedb.org/3/movie/top_rated?api_key=ee462a4199c4e7ec8d93252494ba661b&language=en-US&page=1";
////        initVolleyCallback();
//        mVolleyService =new FetchJson(mResultCallback,getContext());
//        mVolleyService.getDataVolley("GETCALL",url);



        return root;
    }


    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
////        List<Posts> movies = Movie.createMovies(adapter.getItemCount());
////        progressBar.setVisibility(View.GONE);
//        Posts post;
////
//        ArrayList<Posts> postsList=new ArrayList<>();
//
//        for(int i=0;i<10;i++){
//
//            post=new Posts();
////                    post.setImage("https://square.github.io/picasso/static/sample.png");
//            post.setTitle(""+i);
//
//            postsList.add(post);
//        }
//        adapter.addAll(postsList);
//
//        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//        else isLastPage = true;

        String url="https://api.themoviedb.org/3/tv/popular?api_key=ee462a4199c4e7ec8d93252494ba661b&language=en-US&page=1";
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());
        mVolleyService.getDataVolley("GETCALL",url);

    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        String url="https://api.themoviedb.org/3/tv/popular?api_key=ee462a4199c4e7ec8d93252494ba661b&language=en-US&page="+currentPage;
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());
        mVolleyService.getDataVolley("GETCALL",url);
    }


    @Override
    public void onRefresh() {
        // itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        loadFirstPage();
    }

    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,final JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);
//                Toast.makeText(getContext(),"//"+response,Toast.LENGTH_LONG).show();

                adapter.removeLoadingFooter();
                isLoading = false;
                Posts post;
//
                ArrayList<Posts> postsList=new ArrayList<>();

//

                postsList= parsJsonObj(response);
                swipeRefresh.setRefreshing(false);
                adapter.addAll(postsList);


                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
                //
                Toast.makeText(getContext(),"//"+currentPage,Toast.LENGTH_LONG).show();


            }
            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);


//                Toast.makeText(getContext(),"//"+response,Toast.LENGTH_LONG).show();

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + error);
            }
        };
    }
    //
    ArrayList<Posts> parsJsonObj(JSONObject response){

        ArrayList<Posts> postsList=new ArrayList<>();
        try {

//            currentPage=response.getInt("current_page");
//
//            totalPage=response.getInt("last_page");
//
//           JSONArray data=response.getJSONArray("data");
//           Toast.makeText(getContext(),""+data.length(),Toast.LENGTH_LONG).show();
//           for(int i=0;i<data.length();i++){
//               itemCount++;
//               JSONObject obj=data.getJSONObject(i);
//               int id=obj.getInt("id");
//               String title=obj.getString("title");
//               String content=obj.getString("content");

//            currentPage=response.getInt("page");
//
            TOTAL_PAGES=response.getInt("total_pages");

            JSONArray data=response.getJSONArray("results");
            Toast.makeText(getContext(),""+data.length(),Toast.LENGTH_LONG).show();
            for(int i=0;i<data.length();i++){
//                itemCount++;
                JSONObject obj=data.getJSONObject(i);
                int id=obj.getInt("id");
                String title=obj.getString("name");
                String content=obj.getString("overview");
                String img=obj.getString("poster_path");

                img=img.replace("\\","");
              String imgUrl= "https://image.tmdb.org/t/p/original/"+img;

                Posts post=new Posts();
                post.setId(id);
                post.setImage(imgUrl);
                post.setTitle(title);
                post.setContent(content);
                postsList.add(post);
            }




//            adapter.notifyDataSetChanged();

        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return postsList;
    }

    @Override
    public void onItemClick(View view, int position) {

        Bundle bundle = new Bundle();
//        bundle.putInt("post",adapter.getItem(position).getId());
        bundle.putParcelable("post",adapter.getItem(position));

        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_post,bundle);
//
    }


    @Override
    public void onPicClick(View view, int position) {
        PopUpClass popUpClass = new PopUpClass();
        popUpClass.showPopupWindow(view,adapter.getItem(position).getImage());

    }

    @Override
    public void onCommentClick(View view, int position) {

        final int postid= adapter.getItem(position).getId();

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
}