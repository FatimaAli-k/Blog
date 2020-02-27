package com.example.blog.controller.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.blog.controller.tools.ClickListenerInterface;
import com.example.blog.MainActivity;
import com.example.blog.controller.ui.profile.ProfileActivity;
import com.example.blog.R;
import com.example.blog.controller.tools.PaginationListener;
import com.example.blog.controller.tools.PopUpClass;
import com.example.blog.URLs;
import com.example.blog.model.Posts;
import com.example.blog.controller.ui.comments.CommentsDialogFragment;
import com.example.blog.controller.tools.volley.AppController;
import com.example.blog.controller.tools.volley.FetchJson;
import com.example.blog.controller.tools.volley.IResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener, ClickListenerInterface {

    LinearLayoutManager layoutManager;
    PostRecyclerAdapter adapter;
    RelativeLayout postsRelativeLayout;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
//
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;



    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;
    URLs baseUrl=new URLs();
//    final String postRoute ="posttpagination";
//    final String viewsRoute="updateviews";
    private String TAG = "commentsFragment";
    IResult mResultCallback = null;
    FetchJson mVolleyService;
    TextView errortxt;

    String firstPageUrl,incViewUrl;
    JSONObject sendJson;

    final String SORT_BY_OLDEST="0";
    final String SORT_BY_LATEST="2";
    final String SORT_BY_VIEWS="1";
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//
//        Toast.makeText(getActivity(),"id"+getArguments().getInt("catId"),Toast.LENGTH_LONG).show();


        //send parameters
        Map<String,String> params=new HashMap<>();
        params.put("sortby",SORT_BY_LATEST);
        sendJson=new JSONObject(params);

       firstPageUrl=baseUrl.getPostsFeedUrl();
       incViewUrl=baseUrl.getIncViewsUrl();


        FloatingActionButton fab = ((MainActivity) getActivity()).getFloatingActionButton();
        fab.show();


        swipeRefresh=root.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);

        recyclerView = root.findViewById(R.id.posts_recycler_view);
        postsRelativeLayout = root.findViewById(R.id.postsRelativeLayout);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PostRecyclerAdapter(getContext());

        //click listeners
        adapter.setClickListener(this);
        adapter.setCommentClickListener(this);
        adapter.setPicClickListener(this);
        adapter.setPostExpandClickListener(this);
        adapter.setProfileClickListener(this);

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

//        Parcelable state = layoutManager.onSaveInstanceState();
//        layoutManager.onRestoreInstanceState(state);

        loadFirstPage();

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        recyclerView.addOnScrollListener(new PaginationListener(layoutManager) {
            protected void loadMoreItems() {
                 isLoading = true;
//                currentPage += 1;

//                // mocking network delay for API call
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                        loadNextPage();

//                    }
//                }, 1000);
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



        errortxt=root.findViewById(R.id.homeErrorTextView);


        return root;
    }



    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "save: ");
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = layoutManager.onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "restore: ");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mBundleRecyclerViewState != null){
                Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
               layoutManager.onRestoreInstanceState(listState);}

            }
        }, 500);
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


//        String url="https://api.themoviedb.org/3/tv/popular?api_key=ee462a4199c4e7ec8d93252494ba661b&language=en-US&page=1";
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());
        mVolleyService.postDataVolley("GETCALL",firstPageUrl,sendJson);
//
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        String nextPageUrl=baseUrl.getNextPageUrl(baseUrl.getPostsFeed(),currentPage);
//        String url="https://api.themoviedb.org/3/tv/popular?api_key=ee462a4199c4e7ec8d93252494ba661b&language=en-US&page="+currentPage;
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());
        mVolleyService.postDataVolley("GETCALL",nextPageUrl,sendJson);
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
                errortxt.setVisibility(View.GONE);
//                Toast.makeText(getContext(),"//"+response,Toast.LENGTH_LONG).show();
                currentPage += 1;
                adapter.removeLoadingFooter();
                isLoading = false;


                ArrayList<Posts> postsList;

                postsList= parsJsonObj(response);
                swipeRefresh.setRefreshing(false);
                adapter.addAll(postsList);


                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
                //
//                Toast.makeText(getContext(),"//"+currentPage,Toast.LENGTH_LONG).show();


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
                errortxt.setText("no connection... ");
                Toast.makeText(getContext(),""+error,Toast.LENGTH_LONG).show();
                errortxt.setVisibility(View.VISIBLE);
                swipeRefresh.setRefreshing(false);

                adapter.removeLoadingFooter();
                isLoading = false;


            }
        };
    }
    //
    ArrayList<Posts> parsJsonObj(JSONObject response){

        ArrayList<Posts> postsList=new ArrayList<>();
        try {

//  //movieDB api test
//            TOTAL_PAGES=response.getInt("total_pages");
//
//            JSONArray data=response.getJSONArray("results");
//            Toast.makeText(getContext(),""+data.length(),Toast.LENGTH_LONG).show();
//            for(int i=0;i<data.length();i++){
////                itemCount++;
//                JSONObject obj=data.getJSONObject(i);
//                int id=obj.getInt("id");
//                String title=obj.getString("name");
//                String content=obj.getString("overview");
//                String img=obj.getString("poster_path");
//
//                img=img.replace("\\","");
//              String image= "https://image.tmdb.org/t/p/w185/"+img;
//


            TOTAL_PAGES=response.getInt("last_page");

           JSONArray data=response.getJSONArray("data");
//           Toast.makeText(getContext(),""+data.length(),Toast.LENGTH_LONG).show();
           for(int i=0;i<data.length();i++){

               JSONObject obj=data.getJSONObject(i);
               int id=obj.getInt("id");
               String title=obj.getString("title");
               String content=obj.getString("content");
               String created_at=obj.getString("created_at");
               String image=obj.getString("image");
               int views=obj.getInt("views");
               int rate=obj.getInt("rate");
               int status=obj.getInt("status");
               int cat_id=obj.getInt("category_id");
               String userId=obj.getString("user_id");

               if(image != null && !image.equals(""))
                   image="https://alkafeelblog.edu.turathalanbiaa.com/aqlam/image/"+image;

               Posts post=new Posts();
                post.setId(id);
                post.setImage(image);
                post.setTitle(title);
                post.setCreated_at(created_at);
                post.setViews(views);
                post.setRate(rate);
                post.setCategory_id(cat_id);
                post.setStatus(status);
                post.setContent(content);
                post.setUser_id(userId);

                JSONObject user=obj.getJSONObject("user");
                String userName=user.getString("name");
                String profilePic=user.getString("picture");

               if(profilePic == null || profilePic.equals("") ||profilePic.equals("http://aqlam.turathalanbiaa.com/aqlam/image/000000.png")){
                   profilePic="https://alkafeelblog.edu.turathalanbiaa.com/aqlam/image/000000.png";
               }


                JSONObject cat=obj.getJSONObject("cat");
                String catName=cat.getString("name");

                //get username and profile pic
               post.setUsername(userName);
//               profilePic=baseUrl.getImagePath(profilePic);

               //database image path is outdated
               post.setProfilePic(profilePic);
               //get cat name
               post.setCategory_name(catName);

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

        updateViews(incViewUrl,adapter.getItem(position).getId());
        Bundle bundle = new Bundle();

//        bundle.putInt("post",adapter.getItem(position).getId());
        bundle.putParcelable("post",adapter.getItem(position));

        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_post,bundle);
//
    }

    @Override
    public void onPostExpandClick(View view, int position) {
        TextView postDetails=view.findViewById(R.id.postDetails);
        TextView seeMore = view.findViewById(R.id.seeMore);
        Log.d("line count", ""+postDetails.getLineCount());
                    if (postDetails.getMaxLines() == 3) {
                        postDetails.setMaxLines(40);
                        seeMore.setVisibility(View.GONE);
                        //incViews
                        updateViews(incViewUrl,adapter.getItem(position).getId());
                    } else {
                        postDetails.setMaxLines(3);
                        seeMore.setVisibility(View.VISIBLE);

                    }
    }

    private void updateViews(String url, int postId) {

        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(postId));



        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
               url, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {


//                    Toast.makeText(getApplicationContext(),
//                            " "+  response.getString("message"), Toast.LENGTH_LONG).show();
//
////

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onErrorResponse: "+e.getMessage());

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }

        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

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

    @Override
    public void onProfileClick(View view, int position) {
        Intent intent =new Intent(getContext(), ProfileActivity.class);
        Log.d(TAG, "onProfileClick: "+adapter.getItem(position).getUser_id());

        //it should only send user id and from there make a network call
//        Bundle bundle=new Bundle();
//        bundle.putString("user_name",adapter.getItem(position).getUsername());
//        bundle.putString("user_id",adapter.getItem(position).getUser_id());
//        bundle.putString("profile_pic",adapter.getItem(position).getProfilePic());
//        intent.putExtra("user",bundle);

        intent.putExtra("user_id",adapter.getItem(position).getUser_id());
        startActivity(intent);
    }
}