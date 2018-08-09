package com.example.home.chirp0728;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class lookmessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookmessage);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("查看留言");
    }
}
