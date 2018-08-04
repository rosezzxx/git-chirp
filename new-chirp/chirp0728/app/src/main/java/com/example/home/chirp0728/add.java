package com.example.home.chirp0728;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("新增活動");
        TextView dtime = (TextView)findViewById(R.id.dtime);
        dtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(add.this,add_time.class);
                startActivity(intent);
            }
        });
    }
}