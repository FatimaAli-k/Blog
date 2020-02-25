package com.example.blog.controller.ui.bloggers;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.example.blog.R;
import com.example.blog.model.Users;
import com.example.blog.URLs;
import com.example.blog.controller.tools.ClickListenerInterface;
import com.example.blog.controller.tools.PaginationListener;
import com.example.blog.controller.tools.volley.FetchJson;
import com.example.blog.controller.tools.volley.IResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BloggersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ClickListenerInterface {

    LinearLayoutManager layoutManager;
    BloggersRecyclerAdapter adapter;
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
    final String route ="posttpagination";
    private String TAG = "BloggerseFragment";
    IResult mResultCallback = null;
    FetchJson mVolleyService;
    TextView errortxt;

    String firstPageUrl;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_bloggers, container, false);

        firstPageUrl=baseUrl.getUrl(route);

        swipeRefresh=root.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        recyclerView = root.findViewById(R.id.bloggers_recycler_view);
        postsRelativeLayout = root.findViewById(R.id.bloggersRelativeLayout);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BloggersRecyclerAdapter(getContext());

        //click listeners
        adapter.setClickListener(this);
        

        recyclerView.setAdapter(adapter);




        //scroll to top
        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        
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



        errortxt=root.findViewById(R.id.bloggersErrorTextView);


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
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());
        mVolleyService.getDataVolley("GETCALL",firstPageUrl);
//
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        String nextPageUrl=baseUrl.getNextPageUrl(route,currentPage);
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());
        mVolleyService.getDataVolley("GETCALL",nextPageUrl);
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


                ArrayList<Users> postsList;

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
    ArrayList<Users> parsJsonObj(JSONObject response){

        ArrayList<Users> list=new ArrayList<>();
        try {

            TOTAL_PAGES=response.getInt("last_page");

            JSONArray data=response.getJSONArray("data");
//            Toast.makeText(getContext(),""+data.length(),Toast.LENGTH_LONG).show();
            for(int i=0;i<data.length();i++){

                JSONObject obj=data.getJSONObject(i);

                String userId=obj.getString("user_id");


                Users users=new Users();

                //get username and profile pic
                users.setId(userId);
               users.setName("اسم المستخدم ");
               users.setPicture("https://alkafeelblog.edu.turathalanbiaa.com/aqlam/image/000000.png");
                //get cat name
                list.add(users);

            }




//            adapter.notifyDataSetChanged();

        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return list;
    }

    @Override
    public void onItemClick(View view, int position) {

//        updateViews(incViewUrl,adapter.getItem(position).getId());
//        Bundle bundle = new Bundle();

        Log.d(TAG, "onItemClick: ");
////        bundle.putInt("post",adapter.getItem(position).getId());
//        bundle.putParcelable("post",adapter.getItem(position));
//
//        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_post,bundle);
////
    }

    @Override
    public void onCommentClick(View view, int position) {
        
    }

    @Override
    public void onPicClick(View view, int position) {

    }

    @Override
    public void onPostExpandClick(View view, int position) {

    }

    @Override
    public void onProfileClick(View view, int position) {

    }

}