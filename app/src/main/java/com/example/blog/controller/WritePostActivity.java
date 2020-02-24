package com.example.blog.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.blog.MainActivity;
import com.example.blog.R;
import com.example.blog.model.Categories;
import com.example.blog.controller.tools.TextValidator;
import com.example.blog.URLs;
import com.example.blog.controller.tools.volley.FetchJson;
import com.example.blog.controller.tools.volley.IResult;
import com.facebook.AccessToken;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WritePostActivity extends AppCompatActivity {

    private static String TAG = WritePostActivity.class.getSimpleName();
    LinearLayout mLayout;
    ArrayList<Categories> catList=new ArrayList<>();
    Button addCat;
    TextView catIdTextView;
    EditText title,content;
    int cat_Id=1;

    IResult mResultCallback = null;
    FetchJson mVolleyService;

    String sendPostUrl;
    String getCatUrl;
    URLs baseUrl=new URLs();
//    final String route ="getpost";
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
            Toast.makeText(getApplicationContext(),"need to log in first",Toast.LENGTH_SHORT).show();
            finish();
        }



        sendPostUrl=baseUrl.getSendPostUrl();
        getCatUrl=baseUrl.getCategoriesUrl();


//        TextView textView = new TextView(this);
//        textView.setText("New text");

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
//       goBack.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               finish();
//           }
//       });

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

        addCat=findViewById(R.id.addCatBtn);
//        catIdTextView =findViewById(R.id.writePost_catId);


        initVolleyCallback();
        mVolleyService =new FetchJson(mResultCallback,getApplicationContext());
        mVolleyService.getArrayDataVolley("getArray",getCatUrl);
//        Categories categories=new Categories(0,"others");
//        catList.add(categories);
//        categories=new Categories(2,"smthin");
//        catList.add(categories);
//        categories=new Categories(46,"hmmm");
//        catList.add(categories);




            addCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getCat();
                PopupMenu menu = new PopupMenu(getApplicationContext(), view);

                for(int i=0;i<catList.size();i++) {

                    menu.getMenu().add(catList.get(i).getName());

                }


                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                    @Override
                    public boolean onMenuItemClick(MenuItem popupItem) {

                        int index=0;
                        String name=popupItem.getTitle().toString();
                        addCat.setText(name);

                        for(int i=0;i<catList.size();i++) {

                           if(name.equals(catList.get(i).getName())) {
                                index = catList.get(i).getId();
                                break;

                            }

                        }
                        cat_Id=index;
                        Toast.makeText(getApplicationContext(),"//"+index,Toast.LENGTH_LONG).show();


                        //more than one tag
//                        final LinearLayout layout=(LinearLayout)getLayoutInflater().inflate(R.layout.tag,null);
//
//                        Button tagName= layout.findViewById(R.id.tag);
//                        tagName.setText(name);
//                        tagName.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                mLayout.removeView(layout);
//
//                            }
//                        });
//
//                        mLayout.addView(layout);

                        return true;
                    }
                });

                menu.show();
            }
        });


    }

    private ArrayList<Categories> getCatListFromDb(JSONArray response) {
        ArrayList<Categories> list=new ArrayList<>();

        try {

            for (int i = 0; i < response.length(); i++) {

                JSONObject obj = (JSONObject) response
                        .get(i);

                int id= obj.getInt("id");
                String name = obj.getString("name");
                name=name.replaceAll("\n","");
//
                Categories cat=new Categories(id,name);
               list.add(cat);

            }


        }catch (JSONException e) {

        }
        return list;
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
               catList=getCatListFromDb(response);
//                Toast.makeText(getContext(),"//"+response,Toast.LENGTH_LONG).show();

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + error);

                Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_LONG).show();



            }
        };
    }

    Boolean checkPostData(){

        if(checkTitle && checkContent)
            return true;
        else  return false;

    }


}
