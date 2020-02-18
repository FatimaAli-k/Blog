package com.example.blog.ui.comments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.blog.R;
import com.example.blog.model.Comments;
import com.example.blog.volley.FetchJson;
import com.example.blog.volley.IResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CommentsFragment extends Fragment implements CommentsRecyclerViewAdapter.ItemClickListener {


    RecyclerView recyclerView;
    private String TAG = "commentsFragment";
    IResult mResultCallback = null;
    FetchJson mVolleyService;
    Comments com;
    private ProgressDialog pDialog;

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




        initVolleyCallback();
        mVolleyService = new FetchJson(mResultCallback,getContext());
//        mVolleyService.getDataVolley("GETCALL","https://api.androidhive.info/volley/person_object.json");
  String url="https://jsonblob.com/api/b3efca9c-398d-11ea-a91b-41682e589a1a";
//        String url="http://192.168.9.108:8000/api/cat";


  mVolleyService.getArrayDataVolley("GETCALL",url);

        //post request
//        JSONObject sendObj = null;
//        Map<String, Integer> params = new HashMap<>();
//        params.put("post_id", postId);
////        params.put("id", sellMenuId);
////        params.put("user_sell_it_id", session.getshared("id"));
//        sendObj = new JSONObject(params);
//        mVolleyService.postDataVolley("POSTCALL", "http://192.168.1.150/datatest/post/data", sendObj);

//

//

        recyclerView = view.findViewById(R.id.comments_recycler_view);
        commentsLinearLayout = view.findViewById(R.id.commentsLinearLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter= new CommentsRecyclerViewAdapter( getContext(), commentsList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

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


    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);
//                Toast.makeText(getContext(),"//"+response,Toast.LENGTH_LONG).show();

            }
            @Override
            public void notifySuccessJsonArray(String requestType,JSONArray response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);
                parsJsonArray(response);

//                Toast.makeText(getContext(),"//"+response,Toast.LENGTH_LONG).show();

            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + error);
            }
        };
    }

    void parsJsonArray(JSONArray response){


        try {

            for (int i = 0; i < response.length(); i++) {

                JSONObject obj = (JSONObject) response
                        .get(i);

                String name = obj.getString("name");

                com=new Comments();
                com.setContent(name);
                commentsList.add(com);

            }

            adapter.notifyDataSetChanged();

        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }


}