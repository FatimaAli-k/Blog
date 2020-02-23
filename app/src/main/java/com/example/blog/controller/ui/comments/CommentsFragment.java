package com.example.blog.controller.ui.comments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.blog.R;
import com.example.blog.controller.tools.URLs;
import com.example.blog.model.Comments;
import com.example.blog.controller.tools.PaginationListener;
import com.example.blog.controller.tools.volley.FetchJson;
import com.example.blog.controller.tools.volley.IResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CommentsFragment extends Fragment{


    RecyclerView recyclerView;
//    SwipeRefreshLayout swipeRefresh;
    CommentsRecyclerViewAdapter adapter;
    LinearLayout commentsLinearLayout;
    LinearLayoutManager layoutManager;

    private String TAG = "commentsFragment";
    IResult mResultCallback = null;
    FetchJson mVolleyService;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;

    //
    URLs baseUrl=new URLs();
    String firstPageUrl;
    String route="commentpagination";

    Map<String,String> params=new HashMap<>();
    JSONObject sendJson;


    public CommentsFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_comments, container,false);


//

        int postId;


        if(getArguments()!=null) {
            postId = getArguments().getInt("postId");
            params.put("post_id",""+postId);
            sendJson=new JSONObject(params);

        }


        firstPageUrl=baseUrl.getUrl(route);


//

//        swipeRefresh=root.findViewById(R.id.swipeRefresh);
        recyclerView = root.findViewById(R.id.comments_recycler_view);
        commentsLinearLayout = root.findViewById(R.id.commentsLinearLayout);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter= new CommentsRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);

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





        loadFirstPage();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void loadFirstPage() {



        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());

        mVolleyService.postDataVolley("GETCALL",firstPageUrl,sendJson);
//        mVolleyService.getDataVolley("GETCALL",firstPageUrl);


    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

      String nextPageUrl=baseUrl.getNextPageUrl(route,currentPage);

//        String url="https://api.themoviedb.org/3/tv/popular?api_key=ee462a4199c4e7ec8d93252494ba661b&language=en-US&page="+currentPage;
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());
        mVolleyService.postDataVolley("GETCALL",nextPageUrl,sendJson);
//
    }


//    @Override
//    public void onRefresh() {
//        // itemCount = 0;
//        currentPage = PAGE_START;
//        isLastPage = false;
//        adapter.clear();
//        loadFirstPage();
//    }

    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,final JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);
//                Toast.makeText(getContext(),"//"+response,Toast.LENGTH_LONG).show();

                adapter.removeLoadingFooter();
                isLoading = false;

                ArrayList<Comments> commentsList;

                commentsList= parsJsonObj(response);
//                swipeRefresh.setRefreshing(false);
                adapter.addAll(commentsList);


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
//                swipeRefresh.setRefreshing(false);


            }
        };
    }
    //
    ArrayList<Comments> parsJsonObj(JSONObject response){

        ArrayList<Comments> commentsList=new ArrayList<>();
        try {

//

            TOTAL_PAGES=response.getInt("last_page");
            Log.d(TAG, "parsJsonObj: "+TOTAL_PAGES);

            JSONArray data=response.getJSONArray("data");
            for(int i=0;i<data.length();i++){

                JSONObject obj=data.getJSONObject(i);
                int id=obj.getInt("id");
                String content=obj.getString("content");
                String user_id=obj.getString("user_id");
                String created_at=obj.getString("created_at");
                JSONObject user=obj.getJSONObject("user");
                String username=user.getString("name");


                Comments comments=new Comments();
                comments.setContent(content);
                comments.setId(id);
                comments.setUser_id(user_id);
                comments.setCreated_at(created_at);
//                comments.setUsername(name);

                commentsList.add(comments);
            }




//            adapter.notifyDataSetChanged();

        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return commentsList;
    }




}