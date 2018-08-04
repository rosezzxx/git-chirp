package com.example.home.chirp0728;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class sign extends AppCompatActivity {
    private RadioButton radboy,radgril;
    private RadioGroup radsex;
    private EditText userid,pass,name,phone,nickname;
    private Spinner spnad,sex;
    String sexx,address;
    String user_1,name_1,sex_1;
    String accountid,total;
    int sum = 0;
    Connection con;
    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;

    @SuppressLint("NewApi")
    private Connection CONN(String _user, String _pass, String _DB, String _server) {
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        final EditText user = (EditText) findViewById(R.id.user);
        final String account_id = user.getText().toString();
        final EditText namet = (EditText) findViewById(R.id.name);


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("會員註冊");

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";
        //檢查帳號是否重複
        user.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(sign.this, "222", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                connect = CONN(un, passwords, db, ip);
                String query = "SELECT count(*) as total FROM account Where account_id = '"+s+"' ";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();
                    ArrayList<String> data = new ArrayList<String>();
                    while (rs.next()) {
                        total = rs.getString("total");
                        data.add(total);

                        sum = Integer.parseInt(total);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if(sum > 0){
                    Toast.makeText(sign.this, "此帳號以重複，請重新填寫", Toast.LENGTH_SHORT).show();
                }

            }
        });




        //當選擇性別
        sex = (Spinner)findViewById(R.id.radsex);
        sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sexx = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //當選取區域
        spnad = (Spinner)findViewById(R.id.spnad);
        spnad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                address = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //切換到上傳頭貼
        Button next = (Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //設定Intent切換頁面使用
                Intent intent = new Intent();
                intent.setClass(sign.this,imgupload.class);
                final Bundle bundle = new Bundle();

                //取值
                userid = (EditText) findViewById(R.id.user);
                final String userid_2 = userid.getText().toString();
                pass = (EditText) findViewById(R.id.pass);
                final String pass_2 = pass.getText().toString();
                name = (EditText) findViewById(R.id.name);
                final String name_2 = name.getText().toString();
                phone = (EditText) findViewById(R.id.phone);
                final String phone_2 = phone.getText().toString();
                nickname =(EditText)findViewById(R.id.nickname);
                final String nickname_2 = nickname.getText().toString();

                if (userid_2.matches("") || name_2.matches("") || nickname_2.matches("")|| phone_2.matches("") ||  sexx.matches("") || address.matches("")){
                    //Toast toast = Toast.makeText(sign.this, "欄位填寫未完成，請確實填寫", Toast.LENGTH_LONG);
                    Toast.makeText(sign.this, "欄位填寫未完成，請確實填寫", Toast.LENGTH_SHORT).show();

                }else{
                    //將值丟入bundle
                    bundle.putString("user",userid_2);
                    bundle.putString("pass",pass_2);
                    bundle.putString("name",name_2);
                    bundle.putString("nickname",nickname_2);
                    bundle.putString("phone",phone_2);
                    bundle.putString("sex",sexx);
                    bundle.putString("address",address);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

}