package com.example.home.chirp0728;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class update_time extends AppCompatActivity {

    EditText startdate,starttime,enddate,endtime;
    Button btnadd;
    String stime,etime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);

        Intent intent = getIntent();
        String dtime = intent.getStringExtra("dtime_ok");
        Toast.makeText(update_time.this,dtime, Toast.LENGTH_SHORT).show();
        startdate = (EditText)findViewById(R.id.startdate);
        starttime = (EditText)findViewById(R.id.starttime);
        enddate = (EditText)findViewById(R.id.enddate);
        endtime = (EditText)findViewById(R.id.endtime);

        if(dtime.equals("")==false){
            String dtime_start = dtime.substring(0,dtime.indexOf("~"));
            String dtime_start_date = dtime_start.substring(0,dtime_start.indexOf(" "));
            String dtime_start_time = dtime_start.substring(dtime_start.indexOf(" ")+1,dtime_start.length());
            String dtime_end = dtime.substring(dtime.indexOf("~")+1,dtime.length());
            String dtime_end_date = dtime_end.substring(0,dtime_end.indexOf(" "));
            String dtime_end_time = dtime_end.substring(dtime_end.indexOf(" ")+1,dtime_end.length());

            startdate.setText(dtime_start_date.replaceAll("-",""));
            starttime.setText(dtime_start_time);
            stime = dtime_start_time.replaceAll(":","");
            enddate.setText(dtime_end_date.replaceAll("-",""));
            endtime.setText(dtime_end_time);
            etime = dtime_end_time.replaceAll(":","");
        }

       startdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    dpds();
                }
            }
        });
        startdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dpds();
            }
        });

        starttime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    tpds();
                }
            }
        });
        starttime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tpds();
            }
        });

        enddate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    dpde();
                }
            }
        });
        enddate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dpde();
            }
        });

        endtime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    tpde();
                }
            }
        });
        endtime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tpde();
            }
        });

        startdate.setInputType(InputType.TYPE_NULL);
        starttime.setInputType(InputType.TYPE_NULL);
        enddate.setInputType(InputType.TYPE_NULL);
        endtime.setInputType(InputType.TYPE_NULL);

        btnadd = (Button)findViewById(R.id.btnadd);
        btnadd.setOnClickListener(btnaddonclick);




    }

    private void dpds() {
        Calendar c = Calendar.getInstance();

        new DatePickerDialog(update_time.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                // TODO Auto-generated method stub
                if(monthOfYear<10 && dayOfMonth <10){
                    startdate.setText(year+"0"+(monthOfYear+1)+"0"+dayOfMonth);
                }
                else if(monthOfYear<10){
                    startdate.setText(year+"0"+(monthOfYear+1)+""+dayOfMonth);
                }
                else if(dayOfMonth<10){
                    startdate.setText(year+""+(monthOfYear+1)+"0"+dayOfMonth);
                }
                else{
                    startdate.setText(year+""+(monthOfYear+1)+""+dayOfMonth);
                }

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void tpds() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(update_time.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                if(hh<10 && mm<10){
                    starttime.setText("0"+hh+":0"+mm);
                    stime="0"+hh+"0"+mm;
                }
                else if(hh<10){
                    starttime.setText("0"+hh+":"+mm);
                    stime="0"+hh+""+mm;
                }
                else if(mm<10){
                    starttime.setText(hh+":0"+mm);
                    stime=hh+"0"+mm;
                }
                else{
                    starttime.setText(hh+":"+mm);
                    stime=hh+""+mm;
                }

            }
        },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
    }

    private void dpde() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(update_time.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub


                if(monthOfYear<10 && dayOfMonth <10){
                    enddate.setText(year+"0"+(monthOfYear+1)+"0"+dayOfMonth);
                }
                else if(monthOfYear<10){
                    enddate.setText(year+"0"+(monthOfYear+1)+""+dayOfMonth);
                }
                else if(dayOfMonth<10){
                    enddate.setText(year+""+(monthOfYear+1)+"0"+dayOfMonth);
                }
                else{
                    enddate.setText(year+""+(monthOfYear+1)+""+dayOfMonth);
                }
            }


        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();


    }

    private void tpde() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(update_time.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                if(hh<10 && mm<10){
                    endtime.setText("0"+hh+":0"+mm);
                    etime="0"+hh+"0"+mm;
                }
                else if(hh<10){
                    endtime.setText("0"+hh+":"+mm);
                    etime="0"+hh+""+mm;
                }
                else if(mm<10){
                    endtime.setText(hh+":0"+mm);
                    etime=hh+"0"+mm;
                }
                else{
                    endtime.setText(hh+":"+mm);
                    etime=hh+""+mm;
                }

            }
        },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
    }

    private Button.OnClickListener btnaddonclick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            int check=0;
            if(startdate.getText().toString().equals("")){
                Toast.makeText(update_time.this,"起始日期未填寫", Toast.LENGTH_SHORT).show();
                startdate.requestFocus();
                check=1;
            }
            else if(starttime.getText().toString().equals("")){
                Toast.makeText(update_time.this,"起始時間未填寫", Toast.LENGTH_SHORT).show();
                starttime.requestFocus();
                check=1;
            }
            else if(enddate.getText().toString().equals("")){
                Toast.makeText(update_time.this,"結束日期未填寫", Toast.LENGTH_SHORT).show();
                enddate.requestFocus();
                check=1;
            }
            else if(endtime.getText().toString().equals("")){
                Toast.makeText(update_time.this,"結束時間未填寫", Toast.LENGTH_SHORT).show();
                endtime.requestFocus();
                check=1;
            }
            else if(Integer.parseInt(enddate.getText().toString()) < Integer.parseInt(startdate.getText().toString())){
                Toast.makeText(update_time.this,"日期錯誤", Toast.LENGTH_SHORT).show();
                enddate.requestFocus();
                check=1;
            }
            else if((Integer.parseInt(enddate.getText().toString()) == Integer.parseInt(startdate.getText().toString())) && (Integer.parseInt(etime) < Integer.parseInt(stime))){
                Toast.makeText(update_time.this, "時間錯誤", Toast.LENGTH_SHORT).show();
                endtime.requestFocus();
                check=1;
            }
            else{
                Intent Intent = new Intent();
                String startt=startdate.getText() + " " + starttime.getText();
                String endt=enddate.getText() + " " + endtime.getText();
                Intent.putExtra("startt", startt);
                Intent.putExtra("endt", endt);
                setResult(1, Intent);
                finish();
            }

        }
    };
}
