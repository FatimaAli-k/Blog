package com.example.blog.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.blog.controller.ui.category.CatDropDownFragment;
import com.example.blog.MainActivity;
import com.example.blog.R;
import com.example.blog.controller.tools.TextValidator;
import com.example.blog.URLs;
import com.example.blog.controller.tools.volley.FetchJson;
import com.example.blog.controller.tools.volley.IResult;
import com.facebook.AccessToken;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WritePostActivity extends AppCompatActivity implements CatDropDownFragment.OnDataPass {

    private static String TAG = WritePostActivity.class.getSimpleName();

    EditText title,content;
    int cat_Id=1;

    IResult mResultCallback = null;
    FetchJson mVolleyService;

    String sendPostUrl;
    String getCatUrl;
    URLs baseUrl=new URLs();

    String userID="0";

    Boolean checkTitle=false, checkContent=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);
        Button sendPost=findViewById(R.id.sendPostBtn);

//        Button goBack=findViewById(R.id.goBack);
        title=findViewById(R.id.titleEditTxt);
        content=findViewById(R.id.detailsEditTxt);

        final boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        SharedPreferences prefs = getSharedPreferences("profile", Activity.MODE_PRIVATE);

        if(!loggedOut){
           userID=Profile.getCurrentProfile().getId();
        }
        else if(prefs.getString("user_id",null)!=null){
            userID=prefs.getString("user_id",null);
        }
        else{
            Toast.makeText(getApplicationContext(),R.string.must_login_to_post,Toast.LENGTH_SHORT).show();
            finish();
        }



        sendPostUrl=baseUrl.getSendPostUrl();
        getCatUrl=baseUrl.getCategoriesUrl();


       sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPostData()) {
                    sendPostToDb(userID, title.getText().toString(), content.getText().toString(), cat_Id);

                }
                else {
                    if(title.getText().toString().isEmpty())
                        title.setError(getString(R.string.empty_field_error));
                    else content.setError(getString(R.string.empty_field_error));
                }
            }
        });
//


        title.addTextChangedListener(new TextValidator(title) {
            @Override public void validate(TextView textView, String text) {

               if(text.isEmpty()){
                    title.setError("field required");
                    checkTitle=false;
                }
                else checkTitle=true;

            }
        });
        content.addTextChangedListener(new TextValidator(content) {
            @Override public void validate(TextView textView, String text) {

                if(text.isEmpty()){
                    content.setError("field required");
                    checkContent=false;
                }
                else checkContent=true;

            }
        });

    }


    public void sendPostToDb(String userId, String title,String content,int cat_Id){
        Log.d(TAG, "sendPostToDb: "+userId+"/"+title+"/"+content+"/"+cat_Id);

        Map<String,String> params=new HashMap<String, String>();
        params.put("user_id",userId);
        params.put("title",title);
        params.put("content",content);
        params.put("category_id",""+cat_Id);
        JSONObject sendObj =new JSONObject(params);
        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getApplicationContext());
        mVolleyService.postDataVolley("send",sendPostUrl,sendObj);
//

    }
    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,final JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


//                Toast.makeText(getApplicationContext(),"//"+response,Toast.LENGTH_LONG).show();


            }
            @Override
            public void notifySuccessJsonArray(String requestType, JSONArray response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + error);

                Toast.makeText(getApplicationContext(),"hm"+error,Toast.LENGTH_LONG).show();



            }
        };
    }

    Boolean checkPostData(){

        if(checkTitle && checkContent)
            return true;
        else  return false;

    }
    //from cat dropdown fragment
    @Override
    public void onDataPass(int data) {
        Log.d("LOG","cat id " + data);
        cat_Id=data;
    }


}
