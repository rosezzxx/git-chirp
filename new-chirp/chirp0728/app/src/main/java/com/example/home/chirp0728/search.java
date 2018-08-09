package com.example.home.chirp0728;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("搜尋活動");
    }
}
