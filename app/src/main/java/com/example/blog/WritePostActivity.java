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
    LinearLayout mLayout;
    ArrayList<Categories> catList=new ArrayList<>();
    Button addCat;
    TextView catId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
//        setContentView(R.layout.terms_and_conditions);




//        mLayout = (LinearLayout) findViewById(R.id.catLinearLayout);
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

        addCat=findViewById(R.id.addCatBtn);
        catId=findViewById(R.id.writePost_catId);


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
                        catId.setText(""+index);

////
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


}
