package com.example.home.chirp0728;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class nextBank extends AppCompatActivity {

    //db連線變數
    Connection con;
    String ip, db, un, passwords,ip2,db2,un2,passwords2;
    Connection connect,connect2;
    PreparedStatement stmt,stmt2;
    ResultSet rs,rs2;

    String sex,location,phonenumber,nickname,encodedImage,namevalue;
    Button btnOK;

    EditText bank_code,bank_account;
    String bcode,baccount;

    //db連線
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
        setContentView(R.layout.activity_next_bank);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("修改帳戶資料");

        //取得前一頁值
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        sex = bundle.getString("sex");
        location = bundle.getString("location");
        phonenumber = bundle.getString("phonenumber");
        nickname = bundle.getString("nickname");
        encodedImage = bundle.getString("encodedImage");
        namevalue = bundle.getString("namevalue");

        btnOK = (Button)findViewById(R.id.btnOK);

        //按下完成更動資料資料
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bank_code = (EditText)findViewById(R.id.tBank);
                bank_account = (EditText)findViewById(R.id.tBankaccount);
                bcode = bank_code.getText().toString();
                baccount = bank_account.getText().toString();

                //修改基本資料
                ip = "140.131.114.241";
                un = "chirp2018";
                passwords = "chirp+123";
                db = "107-chirp";
                String query = "UPDATE account SET sex = '"+sex+"',location = '"+location+"',phonenumber = '"+phonenumber+"',nickname = '"+nickname+"',img  = '"+encodedImage+"' WHERE account_id = '"+namevalue+"'";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                //修改帳戶
                ip2 = "140.131.114.241";
                un2 = "chirp2018";
                passwords2 = "chirp+123";
                db2 = "107-chirp";
                String query2 = "UPDATE bank SET bank_code = '"+bcode+"',bank_account = '"+baccount+"' WHERE bank_user = '"+namevalue+"'";
                try {
                    connect2 = CONN(un2, passwords2, db2, ip2);
                    stmt2 = connect2.prepareStatement(query2);
                    rs2 = stmt2.executeQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Toast.makeText(nextBank.this, "修改成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(nextBank.this,MainActivity.class);
                startActivity(intent);

            }
        });

    }
}
