package com.example.home.chirp0728;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class add_time extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("設定時間");
    }
}
