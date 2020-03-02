package com.example.blog.controller.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.blog.R;
import com.example.blog.URLs;
import com.example.blog.controller.tools.ClickListenerInterface;
import com.example.blog.controller.tools.PaginationListener;
import com.example.blog.controller.tools.PopUpClass;
import com.example.blog.controller.tools.volley.AppController;
import com.example.blog.controller.tools.volley.FetchJson;
import com.example.blog.controller.tools.volley.IResult;
import com.example.blog.controller.ui.comments.CommentsDialogFragment;
import com.example.blog.controller.ui.comments.ExpandedPostFragment;
import com.example.blog.controller.ui.home.FullPostFragment;
import com.example.blog.model.Posts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfilePostsFragment extends Fragment implements ClickListenerInterface {

    LinearLayoutManager layoutManager;
    ProfilePostsRecyclerAdapter adapter;
    RelativeLayout relativeLayout;

    RecyclerView recyclerView;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;
    URLs baseUrl=new URLs();
    private String TAG = "profilePostsFragment";
    IResult mResultCallback = null;
    FetchJson mVolleyService;
    JSONObject sendJson;
    boolean myProfile=false;
    String firstPageUrl,incViewUrl,deletePostUrl;
    int deletedItemPosition;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_profile_posts, container, false);
//
        firstPageUrl=baseUrl.getUrl(baseUrl.getPostByUserId());
        incViewUrl=baseUrl.getIncViewsUrl();
        deletePostUrl=baseUrl.getUrl(baseUrl.getDeletePost());

        String userId=getArguments().getString("user_id");
        myProfile=getArguments().getBoolean("my_profile");

        Map<String,String> params=new HashMap<>();
        params.put("user_id",userId);
        sendJson=new JSONObject(params);

        recyclerView = root.findViewById(R.id.posts_recycler_view);
        relativeLayout = root.findViewById(R.id.profileRelativeLayout);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProfilePostsRecyclerAdapter(getContext(),myProfile);



        //click listeners
        adapter.setClickListener(this);
//        adapter.setCommentClickListener(this);
        adapter.setPicClickListener(this);
        adapter.setPostExpandClickListener(this);
        //delete
        adapter.setProfileClickListener(this);

        recyclerView.setAdapter(adapter);

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



        return root;
    }
    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());
        mVolleyService.postDataVolley("GETCALL",firstPageUrl,sendJson);
//
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        String nextPageUrl=baseUrl.getNextPageUrl(baseUrl.getPostByUserId(),currentPage);
//        String url="https://api.themoviedb.org/3/tv/popular?api_key=ee462a4199c4e7ec8d93252494ba661b&language=en-US&page="+currentPage;
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());
        mVolleyService.postDataVolley("GETCALL",nextPageUrl,sendJson);
    }




    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,final JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);

                if(requestType.equals("delete")){
                    adapter.remove(adapter.getItem(deletedItemPosition));
                    Toast.makeText(getContext(),R.string.delete_complete,Toast.LENGTH_LONG).show();
                }
                else {
                    currentPage += 1;
                    adapter.removeLoadingFooter();
                    isLoading = false;


                    ArrayList<Posts> postsList;

                    postsList = parsJsonObj(response);
                    adapter.addAll(postsList);


                    if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;
                    //
//                Toast.makeText(getContext(),"//"+currentPage,Toast.LENGTH_LONG).show();
                }

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

                Toast.makeText(getContext(),""+error,Toast.LENGTH_LONG).show();
                if(requestType.equals("delete")){
                    Toast.makeText(getContext(),R.string.delete_failed,Toast.LENGTH_LONG).show();
                }
                else {
                    adapter.removeLoadingFooter();
                    isLoading = false;
                }

            }
        };
    }
    //
    ArrayList<Posts> parsJsonObj(JSONObject response){

        ArrayList<Posts> postsList=new ArrayList<>();
        try {


            if(response.getInt("last_page")>11) {
                if (TOTAL_PAGES != response.getInt("last_page")) {
                    TOTAL_PAGES++;
                }
            }
            else {TOTAL_PAGES=response.getInt("last_page");}
            if(TOTAL_PAGES==0){
                Toast.makeText(getContext(),R.string.no_posts,Toast.LENGTH_SHORT).show();
            }

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
                    image=baseUrl.getImagePath()+image;

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

                JSONObject cat=obj.getJSONObject("cat");
                String catName=cat.getString("name");


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
    public void onPostExpandClick(View view, int position) {
        TextView postDetails=view.findViewById(R.id.postDetails);
        TextView seeMore = view.findViewById(R.id.seeMore);
        Log.d("line count", ""+postDetails.getLineCount());
        if (postDetails.getMaxLines() == 3) {
            postDetails.setMaxLines(1000);
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

    }

    @Override
    public void onProfileClick(View view, final int position) {

        //delete listener
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        deletedItemPosition=deletePost(adapter.getItem(position).getId(),position);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.delete).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();


    }

    public int deletePost(int id,int position) {
        Log.d(TAG, "deletePost: ");

        Map<String,String> params=new HashMap<>();
        params.put("id",""+id);
       JSONObject deleteJson=new JSONObject(params);

        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getContext());
        mVolleyService.postDataVolley("delete",deletePostUrl,deleteJson);

        return position;
    }

    @Override
    public void onCatClick(View view, int position) {

//        Bundle bundle = new Bundle();
//        bundle.putInt("catId",adapter.getItem(position).getCategory_id());
//
    }
}