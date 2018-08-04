package com.example.home.chirp0728;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class deatial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatial);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("活動詳細內容");
    }
}
