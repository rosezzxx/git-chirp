package com.example.home.chirp0728;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class update_doing extends AppCompatActivity {

    EditText dname,dplace,dup,ddtime,dcondition,dmoney,ddetails;
    Spinner dtype,dtype2;
    Button btnadd;
    String str,str2,dtime_startt,dtime_endt,ddtime_startt,ddtime_endt,dcondition_c,dcondition_n,d_condition,dtype_id;
    TextView dtime;

    String doid="";

    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doing);

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("修改活動");

        dname = (EditText)findViewById(R.id.dname);
        dplace = (EditText)findViewById(R.id.dplace);
        dtype = (Spinner)findViewById(R.id.dtype);
        dtype2 = (Spinner)findViewById(R.id.dtype2);
        dup = (EditText)findViewById(R.id.dup);
        //dtime = (EditText)findViewById(R.id.dtime);
        dtime = (TextView)findViewById(R.id.dtime);
        ddtime = (EditText)findViewById(R.id.ddtime);
        dcondition = (EditText)findViewById(R.id.dcondition);
        ddetails = (EditText)findViewById(R.id.ddetails);
        dmoney = (EditText)findViewById(R.id.dmoney);


        Bundle bundle =getIntent().getExtras(); //抓前一頁變數
        final String  doing_id=bundle.getString("doing_id"); //活動id
        doid=doing_id;
        String select_doing="select * from doing where doing_id='"+doing_id+"'";
//        String select_doing="select * from doing where doing_id='100'";
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(select_doing);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dname.setText(rs.getString("doing_name"));
                dplace.setText(rs.getString("doing_place"));
                dtype_id = rs.getString("type_id");
                String totalpeople = rs.getString("totalpeople");
                dup.setText(totalpeople.substring(totalpeople.indexOf("-")+1,totalpeople.length()));
                if(totalpeople.substring(0,totalpeople.indexOf("-")) == "up"){
                    str2="最多";
                    dtype2.setSelection(0);
                }
                else{
                    str2="最少";
                    dtype2.setSelection(1);
                }
                dtime_startt = rs.getString("doing_start");
                dtime_endt = rs.getString("doing_end");
                dtime.setText( dtime_startt.substring(0,16).replaceAll("-","") + "~" + dtime_endt.substring(0,16).replaceAll("-",""));
                ddtime_startt = rs.getString("sign_start");
                ddtime_endt =rs.getString("sign_end");
                ddtime.setText( ddtime_startt.substring(0,16).replaceAll("-","") + "~" + ddtime_endt.substring(0,16).replaceAll("-",""));
                dcondition_n = rs.getString("parner_id");
                dcondition.setText("設定完成");
                dmoney.setText(rs.getString("pay_money"));
                ddetails.setText(rs.getString("doing_content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dcondition_c="";
        String dcon_sp[] = dcondition_n.split(",");
        for(int ii=0;ii<dcon_sp.length;ii++){
            String select_dcondition = "select * from parner where parner_id='" + dcon_sp[ii] + "'";
            try {
                connect = CONN(un, passwords, db, ip);
                stmt = connect.prepareStatement(select_dcondition);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    dcondition_c=dcondition_c + rs.getString("parner_name") + ",";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        dcondition.setText(dcondition_c);

        ddtime.setInputType(InputType.TYPE_NULL);
        dcondition.setInputType(InputType.TYPE_NULL);


        dtime.setInputType(InputType.TYPE_NULL);
        dtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(update_doing.this,update_time.class);
                Bundle bundle = new Bundle();
                bundle.putString("dtime_ok",dtime.getText().toString());

                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });

        ddtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(update_doing.this,update_time_sign.class);

                Bundle bundle = new Bundle();
                bundle.putString("ddtime_ok",ddtime.getText().toString());

                intent.putExtras(bundle);
                startActivityForResult(intent,2);
            }
        });

        dcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(update_doing.this,update_condition.class);

                Bundle bundle = new Bundle();
                bundle.putString("dcondition_ok",dcondition_n);

                intent.putExtras(bundle);
                startActivityForResult(intent,3);
            }
        });

        //connect = CONN(un, passwords, db, ip);
        String query = "select * from doingtype";

        List<CItem> lst = new ArrayList<CItem>();
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String type_id = rs.getString("type_id");
                String type_name = rs.getString("type_name");
                CItem item = new CItem(type_id, type_name);
                lst.add(item);
            }
            ArrayAdapter<CItem> myaAdapter = new ArrayAdapter<CItem>(update_doing.this,  android.R.layout.simple_list_item_1, lst);
            dtype.setAdapter(myaAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String select_type = "select * from doingtype where type_id='" + dtype_id + "'";
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(select_type);
            rs = stmt.executeQuery();
            while (rs.next()) {
                dtype.setSelection(Integer.parseInt(rs.getString("type_id"))-1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                str = ((CItem) dtype.getSelectedItem()).GetID().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        dtype2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                str2 = dtype2.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        btnadd = (Button)findViewById(R.id.btnadd);
        btnadd.setOnClickListener(btnaddonclick);
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (1 == resultCode && 1 == requestCode){
            dtime_startt = data.getStringExtra("startt");
            dtime_endt = data.getStringExtra("endt");
            dtime.setText(dtime_startt+ "~" + dtime_endt);
        }
        if (2 == resultCode && 2 == requestCode){
            ddtime_startt = data.getStringExtra("startt");
            ddtime_endt = data.getStringExtra("endt");
            ddtime.setText(ddtime_startt+ "~" + ddtime_endt);
        }
        if (3 == resultCode && 3 == requestCode){
            dcondition_c = data.getStringExtra("condition_chinese");
            dcondition_n = data.getStringExtra("condition_number");
            dcondition.setText(dcondition_c);
        }
    }
    private Button.OnClickListener btnaddonclick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int check=0;
            if(dname.getText().toString().equals("")){
                Toast.makeText(update_doing.this,"主題未填寫", Toast.LENGTH_SHORT).show();
                dname.requestFocus();
                check=1;
            }
            else if(dplace.getText().toString().equals("")){
                Toast.makeText(update_doing.this,"地點未填寫", Toast.LENGTH_SHORT).show();
                dplace.requestFocus();
                check=1;
            }
            else if(str2.equals("--選擇上下限--")){
                Toast.makeText(update_doing.this,"上下限未選", Toast.LENGTH_SHORT).show();
                dtype2.requestFocus();
                check=1;
            }
            else if(dup.getText().toString().equals("")){
                Toast.makeText(update_doing.this,"人數未填寫", Toast.LENGTH_SHORT).show();
                dup.requestFocus();
                check=1;
            }
            else if(dtime.getText().toString().equals("")){
                Toast.makeText(update_doing.this,"活動時間未設定", Toast.LENGTH_SHORT).show();
                check=1;
            }
            else if(ddtime.getText().toString().equals("")){
                Toast.makeText(update_doing.this,"報名時間未設定", Toast.LENGTH_SHORT).show();
                check=1;
            }
            else if(dcondition.getText().toString().equals("")){
                Toast.makeText(update_doing.this,"條件未設定", Toast.LENGTH_SHORT).show();
                check=1;
            }
            else if(dmoney.getText().toString().equals("")){
                Toast.makeText(update_doing.this,"收費未填寫", Toast.LENGTH_SHORT).show();
                dmoney.requestFocus();
                check=1;
            }
            else if(ddetails.getText().toString().equals("")){
                Toast.makeText(update_doing.this,"詳細介紹未填寫", Toast.LENGTH_SHORT).show();
                ddetails.requestFocus();
                check=1;
            }
            else if (check==0){
                String dpeople;
                Toast.makeText(update_doing.this,"123333", Toast.LENGTH_SHORT).show();
                if(str2.equals("上限")){
                    dpeople = "up-"+dup.getText().toString();
                }else{
                    dpeople = "down-"+dup.getText().toString();
                }
                Toast.makeText(update_doing.this,dpeople, Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

                String query_update="update doing set doing_name='" + dname.getText().toString() + "',"+
                        "account_id='" + userid + "',"+
                        "type_id='"+ str + "',"+
                        "doing_start='"+ dtime_startt + "',"+
                        "doing_end='"+ dtime_endt + "',"+
                        "parner_id='"+ dcondition_n + "',"+
                        "totalpeople='"+ dpeople + "',"+
                        "doing_place='"+ dplace.getText().toString() + "',"+
                        "doing_content='"+ ddetails.getText().toString() + "',"+
                        "sign_start='"+ ddtime_startt + "',"+
                        "sign_end='"+ ddtime_endt + "',"+
                        "pay_money='"+ dmoney.getText().toString()+ "'"+
                        " where doing_id='"+doid+"'";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query_update);
                    rs = stmt.executeQuery();
                    Toast.makeText(update_doing.this,"修改成功", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent();
                intent.setClass(update_doing.this,MainActivity.class);
                startActivity(intent);
            }
        }
    };

    public class CItem {
        private String ID;
        private String Value = "";

        public CItem() {
            ID = "";
            Value = "";
        }

        public CItem(String _ID, String _Value) {
            ID = _ID;
            Value = _Value;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return Value;
        }

        public String GetID() {
            return ID;
        }

        public String GetValue() {
            return Value;
        }
    }

    private Connection CONN(String _user, String _pass, String _DB,
                            String _server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnURL = "jdbc:jtds:sqlserver://" + _server + ";"
                    + "databaseName=" + _DB + ";user=" + _user + ";password="
                    + _pass + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}