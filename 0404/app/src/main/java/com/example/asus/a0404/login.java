package com.example.asus.a0404;

import java.io.IOError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.SharedPreferences;

public class login extends AppCompatActivity {
    int sum = 0;
    String total;
    String accountid;


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
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                EditText user_1 = (EditText)findViewById(R.id.user);
                String userid = user_1.getText().toString();

                EditText psw_1 = (EditText)findViewById(R.id.psw);
                String psw_2 = psw_1.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences



                ip = "140.131.114.241";
                un = "chirp2018";
                passwords = "chirp+123";
                db = "107-chirp";
                connect = CONN(un, passwords, db, ip);
                String query = "SELECT account_id,count(*) as total FROM account Where account_id = '"+userid+"' and account_pwd = '"+psw_2+"' group by account_id";

                try {

                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();
                    ArrayList<String> data = new ArrayList<String>();
                    ArrayList<String> data2 = new ArrayList<String>();
                    while (rs.next()) {

                        total = rs.getString("total");
                        accountid = rs.getString("account_id");
                        data.add(total);
                        data2.add(accountid);
                        sum = Integer.parseInt(total);
                    }

                    if(sum == 1){

                        Toast.makeText(login.this, "登入成功", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setClass(login.this,MainActivity.class);
                        startActivity(intent);


                        sharedPreferences.edit().putString("Name", accountid).apply(); //存使用者id進sharedPreferences

                    }else{
                        Toast.makeText(login.this, "帳號密碼有誤，請重新輸入", Toast.LENGTH_SHORT).show();

                    }

                    //Toast.makeText(login.this, total, Toast.LENGTH_SHORT).show();



                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
