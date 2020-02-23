package com.example.blog.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blog.MainActivity;
import com.example.blog.R;
import com.example.blog.controller.tools.TextValidator;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText username,fullname,password,repeatPassword;
    Button register;
    boolean checkUsername=false,checkFullname=false,checkPassword=false,passwordMatch=false;
    Pattern special;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        termsAndConditions();
        username=findViewById(R.id.user_name);
        fullname=findViewById(R.id.public_name);
        password=findViewById(R.id.password);
        repeatPassword=findViewById(R.id.password_repeat);
//        special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");


        username.addTextChangedListener(new TextValidator(username) {
            @Override public void validate(TextView textView, String text) {

               if(text.isEmpty()){
                   username.setError(getString(R.string.empty_field_error));
                   checkUsername=false;
               }
//                else if(!Patterns.EMAIL_ADDRESS.matcher(text).matches()){
//                   username.setError(getString(R.string.email_format_error));
//                   checkUsername=false;
//               }
               else
                   checkUsername=true;
            }
        });
        fullname.addTextChangedListener(new TextValidator(fullname) {
            @Override public void validate(TextView textView, String text) {

                if(text.isEmpty()){
                    fullname.setError(getString(R.string.empty_field_error));
                    checkFullname=false;
                }

                else checkFullname=true;

            }
        });



        password.addTextChangedListener(new TextValidator(password) {
            @Override public void validate(TextView textView, String text) {

//                if(special.matcher(text).find()){
//                    password.setError("field required");
//                    checkPassword=false;
//                }
                if(text.length()<6){
                    password.setError(getString(R.string.min_char_error));
                    passwordMatch=false;
                }
                else if(text.isEmpty()){
                    password.setError(getString(R.string.empty_field_error));
                    passwordMatch=false;
                }
                else passwordMatch=true;

            }
        });

        repeatPassword.addTextChangedListener(new TextValidator(repeatPassword) {
            @Override public void validate(TextView textView, String text) {

                if(! text.equals(password.getText().toString())){
                    Log.d("psw", "validate: "+text+"//"+password.getText());
                    repeatPassword.setError(getString(R.string.password_mismatch_error));
                    checkPassword=false;
                }
                else if(text.isEmpty()){
                    repeatPassword.setError(getString(R.string.empty_field_error));
                    checkPassword=false;
                }
                else checkPassword=true;

            }
        });



        register=findViewById(R.id.registerBtn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkData()) {
                    //send reg request
                    //on success log in and go to main page
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(),"please check fields for errors",Toast.LENGTH_LONG).show();

            }

        });


    }


    private Boolean checkData() {
        boolean dataCorrect=false;

        if(checkUsername && checkFullname && checkPassword && passwordMatch)
            dataCorrect=true;


        return dataCorrect;
    }

    void termsAndConditions(){
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        dialog.setContentView(R.layout.terms_and_conditions);
        dialog.setCancelable(true);
        dialog.show();

        Button agree= dialog.findViewById(R.id.agree);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }



}
