package com.example.home.chirp0728;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class leavemessage extends AppCompatActivity {

    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leavemessage);

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";



        Bundle bundle =getIntent().getExtras(); //抓前一頁變數
        final String  doing_id=bundle.getString("doing_id"); //活動id
        final String  message_id=bundle.getString("message_id"); //留言id


        //----基本資料-------
        TextView message_time=(TextView)findViewById(R.id.textView4); //留言時間
        TextView message_content=(TextView)findViewById(R.id.message_content); //留言內容
        TextView reply_time=(TextView)findViewById(R.id.textView6); //回復時間
        TextView reply_content=(TextView)findViewById(R.id.reply_content); //回覆內容



        String query = "select * from doing_view a " +
                " inner join message b " +
                " on a.doing_id=b.doing_id " +
                " where b.message_id='"+message_id+"'";


        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {


                message_time.setText((rs.getString("message_time")).substring(0,16)); //留言時間
                message_content.setText(rs.getString("message_content")); //留言內容





               reply_time.setText((rs.getString("message_reply_time")+"                     ").substring(0,16)); //回復時間
               reply_content.setText(rs.getString("message_reply")); //回覆內容





            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        Button reply_button=(Button)findViewById(R.id.reply_button);


        reply_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

                EditText content = (EditText)findViewById(R.id.reply_edit);

                //Bundle bundle =getIntent().getExtras(); //抓前一頁變數
                // String  doing_id=bundle.getString("doing_id"); //活動id




                String query = "update message set message_reply='"+content.getText()+"' , message_reply_time=GETDATE() where message_id='"+message_id+"'";

                //Toast.makeText(leavemessage.this,"update message set message_reply='"+content.getText()+"' , message_reply_time=GETDATE() where message_id='"+message_id+"'",Toast.LENGTH_LONG).show();
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();

                }catch (SQLException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent();
                intent.setClass(leavemessage.this,leavemessage.class);
                Bundle bundle=new Bundle();
                bundle.putString("doing_id",doing_id);
                bundle.putString("message_id",message_id);
                intent.putExtras(bundle);
                startActivity(intent);

//                AlertDialog.Builder dialog = new AlertDialog.Builder(leavemessage.this);
//                dialog.setTitle("參加成功"); //設定dialog 的title顯示內容
//                dialog.setMessage("請在指定時間參加活動！"); //設定dialog 的內容
//                //dialog.setIcon(android.R.drawable.ic_dialog_alert);//設定dialog 的ICON
//                dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
//                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent();
//                        intent.setClass(leavemessage.this,leavemessage.class);
//                        Bundle bundle=new Bundle();
//                        bundle.putString("doing_id",doing_id);
//                        bundle.putString("message_id",message_id);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
//                });
//                dialog.show();


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
