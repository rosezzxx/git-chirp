package com.example.home.chirp0728;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;


public class update_condition extends AppCompatActivity {

    CheckBox ckb1,ckb2,ckb3,ckb4,ckb5,ckb6,ckb7,ckb8;
    Button btnadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_condition);

        Intent intent = getIntent();
        String dcondition = intent.getStringExtra("dcondition_ok");

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("活動篩選");

        ckb8 = (CheckBox)findViewById(R.id.checkBox);
        ckb1 = (CheckBox)findViewById(R.id.checkBox2);
        ckb2 = (CheckBox)findViewById(R.id.checkBox3);
        ckb3 = (CheckBox)findViewById(R.id.checkBox4);
        ckb4 = (CheckBox)findViewById(R.id.checkBox5);
        ckb5 = (CheckBox)findViewById(R.id.checkBox6);
        ckb6 = (CheckBox)findViewById(R.id.checkBox7);
        ckb7 = (CheckBox)findViewById(R.id.checkBox8);

        String dcon_sp[] = dcondition.split(",");
        for(int ii=0;ii<dcon_sp.length;ii++){
            switch(dcon_sp[ii]){
                case "1" :
                    ckb1.setChecked(true);
                    break;
                case "2":
                    ckb2.setChecked(true);
                    break;
                case "3":
                    ckb3.setChecked(true);
                    break;
                case "4":
                    ckb4.setChecked(true);
                    break;
                case "5":
                    ckb5.setChecked(true);
                    break;
                case "6":
                    ckb6.setChecked(true);
                    break;
                case "7":
                    ckb7.setChecked(true);
                    break;
                case "8":
                    ckb8.setChecked(true);
                    ckb1.setChecked(false);
                    ckb2.setChecked(false);
                    ckb3.setChecked(false);
                    ckb4.setChecked(false);
                    ckb5.setChecked(false);
                    ckb6.setChecked(false);
                    ckb7.setChecked(false);
                    break;
            }

        }

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



        btnadd = (Button)findViewById(R.id.btnadd);
        btnadd.setOnClickListener(btnaddonclick);

    }

    private Button.OnClickListener btnaddonclick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            String par="";
            String par_name = "";
            if (ckb1.isChecked() == true) {
                par = par + "1,";
                par_name = par_name + "15-20歲,";
            }
            if (ckb2.isChecked() == true) {
                par = par + "2,";
                par_name = par_name + "20-30歲,";
            }
            if (ckb3.isChecked() == true) {
                par = par + "3,";
                par_name = par_name + "30-40歲,";
            }
            if (ckb4.isChecked() == true) {
                par = par + "4,";
                par_name = par_name + "40-50歲,";
            }
            if (ckb5.isChecked() == true) {
                par = par + "5,";
                par_name = par_name + "50歲以上,";
            }
            if (ckb6.isChecked() == true) {
                par = par + "6,";
                par_name = par_name + "男,";
            }
            if (ckb7.isChecked() == true) {
                par = par + "7,";
                par_name = par_name + "女,";
            }
            if (ckb8.isChecked() == true) {
                par = "8,";
                par_name = par_name + "無條件";
            }

            Intent Intent = new Intent();

            Intent.putExtra("condition_number", par);
            Intent.putExtra("condition_chinese", par_name);
            setResult(3, Intent);
            finish();


        }
    };
}
