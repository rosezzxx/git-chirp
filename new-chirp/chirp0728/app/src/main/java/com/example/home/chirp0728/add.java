package com.example.home.chirp0728;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

public class add extends AppCompatActivity {

    EditText dname,dplace,dup,ddtime,dcondition,dmoney,ddetails;
    Spinner dtype,dtype2;
    Button btnadd;
    String str,str2,dtime_ok,ddtime_ok, dtime_startt,dtime_endt,ddtime_startt,ddtime_endt,dcondition_c,dcondition_n,d_condition;
    TextView dtime;

    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt,stmt2;
    ResultSet rs,rs2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("新增活動");

        dname = (EditText)findViewById(R.id.dname);
        dplace = (EditText)findViewById(R.id.dplace);
        dtype = (Spinner)findViewById(R.id.dtype);
        dtype2 = (Spinner)findViewById(R.id.dtype2);
        dup = (EditText)findViewById(R.id.dup);
        //dtime = (EditText)findViewById(R.id.dtime);
        ddtime = (EditText)findViewById(R.id.ddtime);
        dcondition = (EditText)findViewById(R.id.dcondition);
        dmoney = (EditText)findViewById(R.id.dmoney);
        ddetails = (EditText)findViewById(R.id.ddetails);


        ddtime.setInputType(InputType.TYPE_NULL);
        dcondition.setInputType(InputType.TYPE_NULL);

        dtime = (TextView)findViewById(R.id.dtime);
        dtime.setInputType(InputType.TYPE_NULL);
        dtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(add.this,add_time.class);
                Bundle bundle = new Bundle();
                bundle.putString("dtime_ok",dtime_ok);

                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });

        ddtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(add.this,add_time_sign.class);

                Bundle bundle = new Bundle();
                bundle.putString("ddtime_ok",ddtime_ok);

                intent.putExtras(bundle);
                startActivityForResult(intent,2);
            }
        });

        dcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(add.this,add_condition.class);

                Bundle bundle = new Bundle();
                bundle.putString("dcondition_ok",dcondition.getText().toString());

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
            ArrayAdapter<CItem> myaAdapter = new ArrayAdapter<CItem>(add.this,  android.R.layout.simple_list_item_1, lst);
            dtype.setAdapter(myaAdapter);
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
            dtime.setText(dtime_startt+ " ~ " + dtime_endt);
        }
        if (2 == resultCode && 2 == requestCode){
            ddtime_startt = data.getStringExtra("startt");
            ddtime_endt = data.getStringExtra("endt");
            ddtime.setText(ddtime_startt+ " ~ " + ddtime_endt);
        }
        if (3 == resultCode && 3 == requestCode){
            d_condition = data.getStringExtra("condition");
//            dcondition_c = data.getStringExtra("condition_chinese");
//            dcondition_n = data.getStringExtra("condition_number");
            dcondition.setText("設定完成");
        }
    }
    private Button.OnClickListener btnaddonclick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int check=0;
            if(dname.getText().toString().equals("")){
                Toast.makeText(add.this,"主題未填寫", Toast.LENGTH_SHORT).show();
                dname.requestFocus();
                check=1;
            }
            else if(dplace.getText().toString().equals("")){
                Toast.makeText(add.this,"地點未填寫", Toast.LENGTH_SHORT).show();
                dplace.requestFocus();
                check=1;
            }
            else if(str2.equals("--選擇上下限--")){
                Toast.makeText(add.this,"上下限未選", Toast.LENGTH_SHORT).show();
                dtype2.requestFocus();
                check=1;
            }
            else if(dup.getText().toString().equals("")){
                Toast.makeText(add.this,"人數未填寫", Toast.LENGTH_SHORT).show();
                dup.requestFocus();
                check=1;
            }
            else if(dtime.getText().toString().equals("")){
                Toast.makeText(add.this,"活動時間未設定", Toast.LENGTH_SHORT).show();
                check=1;
            }
            else if(ddtime.getText().toString().equals("")){
                Toast.makeText(add.this,"報名時間未設定", Toast.LENGTH_SHORT).show();
                check=1;
            }
            else if(dcondition.getText().toString().equals("")){
                Toast.makeText(add.this,"條件未設定", Toast.LENGTH_SHORT).show();
                check=1;
            }
            else if(dmoney.getText().toString().equals("")){
                Toast.makeText(add.this,"收費未填寫", Toast.LENGTH_SHORT).show();
                dmoney.requestFocus();
                check=1;
            }
            else if(ddetails.getText().toString().equals("")){
                Toast.makeText(add.this,"詳細介紹未填寫", Toast.LENGTH_SHORT).show();
                ddetails.requestFocus();
                check=1;
            }
            else if (check==0){
                String dpeople;
                if(str2.equals("上限")){
                    dpeople = "up-"+dup.getText().toString();
                }else{
                    dpeople = "down-"+dup.getText().toString();
                }

                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

                String query_insert="insert into doing(doing_name,account_id, type_id, doing_start, doing_end,parner_id, totalpeople, doing_place, doing_content, doing_date, sign_start, sign_end, pay_money) " +
                        "values('" + dname.getText().toString() + "','" + userid + "','" + str+ "','" + dtime_startt + "','"  + dtime_endt + "','" + d_condition + "','" + dpeople + "','" + dplace.getText().toString() + "','" + ddetails.getText().toString() + "',getdate(),'" + ddtime_startt + "','" + ddtime_endt + "','" + dmoney.getText().toString() + "');" +
                        "insert into doing_detail(doing_id,account_id,detail_date) values((select scope_identity() as a),'" + userid + "',getdate());";

                int insert_ok=0;
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query_insert);
                    rs = stmt.executeQuery();
                    Toast.makeText(add.this,"創辦成功", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent();
                intent.setClass(add.this,MainActivity.class);
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