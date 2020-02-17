package com.example.blog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.blog.model.Categories;
import com.example.blog.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class WritePostActivity extends AppCompatActivity {

    private static String TAG = WritePostActivity.class.getSimpleName();
    ProgressDialog pDialog;
    LinearLayout mLayout;
    ArrayList<Categories> catList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
//        setContentView(R.layout.terms_and_conditions);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        mLayout = (LinearLayout) findViewById(R.id.catLinearLayout);
        Button sendPost=findViewById(R.id.sendPostBtn);

//        TextView textView = new TextView(this);
//        textView.setText("New text");

       sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        });

        final Button addCat=findViewById(R.id.addCatBtn);
        final TextView catId=findViewById(R.id.writePost_catId);


        Categories categories=new Categories(0,"others");
        catList.add(categories);
        categories=new Categories(2,"smthin");
        catList.add(categories);
        categories=new Categories(46,"hmmm");
        catList.add(categories);




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
                        catId.setText(index);

////                        catId.setText(catList.get(popupItem.getItemId()).getId());

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
//
//    private TextView createNewTextView(String text) {
//
//        final TextView textView =(TextView)getLayoutInflater().inflate(R.layout.tag, null);
//
//        textView.setText(text);
//        return textView;
//    }





    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

//    private void makeJsonObjectRequest() {
//        showpDialog();
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                urlJsonObj, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    // Parsing json object response
//                    // response will be a json object
//                    String name = response.getString("name");
//                    String email = response.getString("email");
//                    JSONObject phone = response.getJSONObject("phone");
//                    String home = phone.getString("home");
//                    String mobile = phone.getString("mobile");
//
//                    jsonResponse = "";
//                    jsonResponse += "Name: " + name + "\n\n";
//                    jsonResponse += "Email: " + email + "\n\n";
//                    jsonResponse += "Home: " + home + "\n\n";
//                    jsonResponse += "Mobile: " + mobile + "\n\n";
//
//                    txtResponse.setText(jsonResponse);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//                hidepDialog();
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                // hide the progress dialog
//                hidepDialog();
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
//    }

    /**
     * Method to make json array request where response starts with [
     * */





}
