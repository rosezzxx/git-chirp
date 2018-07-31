package com.example.asus.chirp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class imgupload extends AppCompatActivity {


    private Button btnupload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgupload);

        btnupload = (Button) findViewById(R.id.btnupload);

        //登入
        btnupload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(imgupload.this ,MainActivity.class);
                startActivity(intent);
            }

        });


    }
}
