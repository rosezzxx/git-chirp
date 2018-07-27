package com.example.asus.chirp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class login extends AppCompatActivity {

    private Button login,sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);






        //跳註冊頁
        sign = (Button) findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(login.this,"sign",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(login.this ,sign.class);
                startActivity(intent);
            }

        });


        //登入
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(login.this,"login",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(login.this,MainActivity.class);
                startActivity(intent);
            }

        });



    }
}
