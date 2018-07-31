package com.example.home.chirp0728;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;


public class add_condition extends AppCompatActivity {

    CheckBox ckb1,ckb2,ckb3,ckb4,ckb5,ckb6,ckb7,ckb8;
    Button btnadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_condition);

        ckb8 = (CheckBox)findViewById(R.id.checkBox);
        ckb1 = (CheckBox)findViewById(R.id.checkBox2);
        ckb2 = (CheckBox)findViewById(R.id.checkBox3);
        ckb3 = (CheckBox)findViewById(R.id.checkBox4);
        ckb4 = (CheckBox)findViewById(R.id.checkBox5);
        ckb5 = (CheckBox)findViewById(R.id.checkBox6);
        ckb6 = (CheckBox)findViewById(R.id.checkBox7);
        ckb7 = (CheckBox)findViewById(R.id.checkBox8);

        ckb8.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    ckb1.setChecked(false);
                    ckb2.setChecked(false);
                    ckb3.setChecked(false);
                    ckb4.setChecked(false);
                    ckb5.setChecked(false);
                    ckb6.setChecked(false);
                    ckb7.setChecked(false);
                }
            }
        });
        ckb1.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ckb8.setChecked(false);
                }
            }
        });
        ckb2.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ckb8.setChecked(false);
                }
            }
        });
        ckb3.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ckb8.setChecked(false);
                }
            }
        });
        ckb4.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ckb8.setChecked(false);
                }
            }
        });
        ckb5.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ckb8.setChecked(false);
                }
            }
        });
        ckb6.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ckb8.setChecked(false);
                }
            }
        });
        ckb7.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    ckb8.setChecked(false);
                }
            }
        });


        Intent intent = getIntent();
        String dtime = intent.getStringExtra("dtime_ok");


        btnadd = (Button)findViewById(R.id.btnadd);
        btnadd.setOnClickListener(btnaddonclick);

    }

    private Button.OnClickListener btnaddonclick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            String par="";
                if (ckb1.isChecked() == true) {
                    par = par + "1,";
                }
                if (ckb2.isChecked() == true) {
                    par = par + "2,";
                }
                if (ckb3.isChecked() == true) {
                    par = par + "3,";
                }
                if (ckb4.isChecked() == true) {
                    par = par + "4,";
                }
                if (ckb5.isChecked() == true) {
                    par = par + "5,";
                }
                if (ckb6.isChecked() == true) {
                    par = par + "6,";
                }
                if (ckb7.isChecked() == true) {
                    par = par + "7,";
                }
                if (ckb8.isChecked() == true) {
                    par = "8";
                }

                Intent Intent = new Intent();

                Intent.putExtra("condition", par);
                setResult(3, Intent);
                finish();


        }
    };
}
