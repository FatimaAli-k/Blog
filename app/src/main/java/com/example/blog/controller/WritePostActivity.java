package com.example.blog.controller;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WritePostActivity extends AppCompatActivity implements CatDropDownFragment.OnDataPass {

    private static String TAG = WritePostActivity.class.getSimpleName();

    EditText title,content;
    int cat_Id=1;

    IResult mResultCallback = null;
    FetchJson mVolleyService;

    String sendPostUrl;
    URLs baseUrl=new URLs();
    String userID="0";
    Boolean checkTitle=false, checkContent=false;

    private static final int PERMISSION_REQUEST_CODE = 100;
    ImageView uploadImg;
    File imageFile=null;

    FrameLayout imgFrame;
    ImageButton cancelPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);
        Button sendPost=findViewById(R.id.sendPostBtn);

//        Button goBack=findViewById(R.id.goBack);
        title=findViewById(R.id.titleEditTxt);
        content=findViewById(R.id.detailsEditTxt);
        imgFrame=findViewById(R.id.imgFrame);
        cancelPic=findViewById(R.id.cancelPic);

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

        uploadImg=findViewById(R.id.uploadImg);
        Button openGallary=findViewById(R.id.getImgFromGallary);
        openGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
            }
        });



        sendPostUrl=baseUrl.getSendPostUrl();


       sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+imageFile);
                if(checkPostData()) {
                   if(imageFile != null) {
                        if (checkPermission())
                            uploadPostWithImg(userID, title.getText().toString(), content.getText().toString(), cat_Id);
                        else
                            requestPermission();
                    }
                   else if(imageFile == null)
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

        cancelPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFile=null;

                imgFrame.setVisibility(View.GONE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==1 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imgFrame.setVisibility(View.VISIBLE);
                Picasso.with(WritePostActivity.this).load(selectedImage).into(uploadImg);
                imageFile = new File(getImgPath(selectedImage));
                Log.d(TAG, "onActivityResult: "+imageFile);


            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    public String getImgPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        return filePath;
    }

    void uploadPostWithImg(String userId, String title,String content,int cat_Id){

        final ProgressDialog mProgressDialog = new ProgressDialog(WritePostActivity.this);
        mProgressDialog.setMessage("uploading...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // downloadTask.cancel(true);
            }
        });

        //"https://api.imgur.com/3/upload"
        Ion.with(getApplicationContext())
                .load(sendPostUrl)
//                .uploadProgressHandler(new ProgressCallback() {
//                    @Override
//                    public void onProgress(long uploaded, long total) {
//                        // Displays the progress bar for the first time.
////                        mNotifyManager.notify(notificationId, mBuilder.build());
////                        mBuilder.setProgress((int) total, (int) uploaded, false);
//                    }
//                })
                .uploadProgressDialog(mProgressDialog)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {

                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMax((int)total);
                        mProgressDialog.setProgress((int)uploaded);


                    }
                })

                .setTimeout(60 * 60 * 1000)
                .setMultipartFile("input_img", "file", imageFile)
                .setMultipartParameter("user_id",userId)
                .setMultipartParameter("title",title)
                .setMultipartParameter("content",content)
                .setMultipartParameter("category_id",""+cat_Id)
                .asJsonObject()
                // run a callback on completion
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        mProgressDialog.dismiss();
                        // When the loop is finished, updates the notification
//                        mBuilder.setContentText("Upload complete")
//                                // Removes the progress bar
//                                .setProgress(0, 0, false);
                        Log.d(TAG, "onCompleted: "+result);
//                        mNotifyManager.notify(notificationId, mBuilder.build());
                        if (e != null) {
                            Log.e(TAG, "error/ "+e );
                            Toast.makeText(getApplicationContext(), R.string.upload_failed, Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), R.string.upload_complete, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(WritePostActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(WritePostActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(WritePostActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;
        }
    }



}
