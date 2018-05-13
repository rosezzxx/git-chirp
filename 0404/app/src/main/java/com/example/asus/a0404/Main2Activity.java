package com.example.asus.a0404;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;
    ImageButton btn_query;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值

        Toast.makeText(Main2Activity.this,userid,Toast.LENGTH_SHORT).show();

        Bundle bundle =getIntent().getExtras(); //抓前一頁變數
        String  doing_id=bundle.getString("doing_id"); //活動id

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";

        TextView textview1=(TextView)findViewById(R.id.textView1); //活動名稱
        TextView textview2=(TextView)findViewById(R.id.textView2); //內容
        TextView textview1_1=(TextView)findViewById(R.id.textView1_1); //暱稱
        TextView textview1_2=(TextView)findViewById(R.id.textView1_2); //人數
        TextView textview1_3=(TextView)findViewById(R.id.textView1_3); //活動日期
        final TextView textview1_4=(TextView)findViewById(R.id.textView1_4); //活動地址


        String query = "select * from doing a inner join account b on a.account_id=b.account_id where doing_id='"+doing_id+"'";
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                textview1.setText(rs.getString("doing_name"));
                textview2.setText(rs.getString("doing_content"));
                textview1_1.setText("發起人："+rs.getString("username"));
                textview1_2.setText("人數上限："+rs.getString("totalpeople")+"人");
                textview1_3.setText("活動時間："+(rs.getString("doing_start")).substring(0,16)+"~"+(rs.getString("doing_end")).substring(0,16));
                textview1_4.setText("活動地址："+rs.getString("doing_place"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Button b1=(Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值

                Bundle bundle =getIntent().getExtras(); //抓前一頁變數
                String  doing_id=bundle.getString("doing_id"); //活動id


                String query = "insert into doing_detail (doing_id, account_id) values('"+doing_id+"','"+userid+"')";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();

                }catch (SQLException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.this);
                dialog.setTitle("參加成功"); //設定dialog 的title顯示內容
                dialog.setMessage("請在指定時間參加活動！"); //設定dialog 的內容
                //dialog.setIcon(android.R.drawable.ic_dialog_alert);//設定dialog 的ICON
                dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 按下"是"以後要做的事情
                    }
                });
                dialog.show();


            }
        });


        btn_query = (ImageButton) this.findViewById(R.id. imageButton);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String address_1 =  textview1_4.getText().toString();
               String address = address_1.substring(5,address_1.length());

               Toast.makeText(Main2Activity.this, address, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                bundle.putString("address",address);
                intent.putExtras(bundle);
                intent.setClass(Main2Activity.this,map.class);

                startActivity(intent);

            }
        });


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
