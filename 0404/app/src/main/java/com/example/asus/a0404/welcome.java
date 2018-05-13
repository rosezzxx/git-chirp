package com.example.asus.a0404;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;


//fb
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Arrays;
public class welcome extends AppCompatActivity {


    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private TextView info;
    String user,name,gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值

        if (userid.length() > 0){
            Toast.makeText(welcome.this, userid, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(welcome.this,MainActivity.class);
            startActivity(intent);

        }

        //切換到登入
        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(welcome.this,login.class);
                startActivity(intent);
            }
        });

        //切換到註冊
        Button register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(welcome.this,sign.class);
                startActivity(intent);
            }
        });


    }

}
