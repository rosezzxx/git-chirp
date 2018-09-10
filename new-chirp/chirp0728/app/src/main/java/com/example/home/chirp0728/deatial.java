package com.example.home.chirp0728;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class deatial extends AppCompatActivity {


        //------連線--------------
        String ip, db, un, passwords;
        Connection connect;
        PreparedStatement stmt;
        ResultSet rs;

        String type_id=""; //類別id
        String view_type_id="";  //是否訂閱類別id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatial);

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";


        Bundle bundle =getIntent().getExtras(); //抓前一頁變數
        final String  doing_id=bundle.getString("doing_id"); //活動id




        //----基本資料-------
        TextView doing_name=(TextView)findViewById(R.id.doing_name); //活動名稱
        final TextView doing_type=(TextView)findViewById(R.id.doing_type); //活動類別
        TextView address=(TextView)findViewById(R.id.address); //活動地址
        TextView time=(TextView)findViewById(R.id.time); //活動時間
        TextView up=(TextView)findViewById(R.id.up); //活動人數
        TextView money=(TextView)findViewById(R.id.money); //活動費用
        TextView textView9=(TextView)findViewById(R.id.textView9); //活動詳細內容
        TextView name=(TextView)findViewById(R.id.name); //主辦人暱稱
        TextView look=(TextView)findViewById(R.id.look); //查看主辦人過去活動紀錄

        String query = "select * from doing_view  " +
                "where doing_id='"+doing_id+"' ";

        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {

                doing_name.setText(rs.getString("doing_name")); //活動名稱
                doing_type.setText(rs.getString("type_name")); //活動類別
                address.setText(rs.getString("doing_place")); //活動地址
                time.setText("活動時間："+(rs.getString("doing_start")).substring(0,16)+"~"+(rs.getString("doing_end")).substring(0,16)); //活動時間

                String peo=rs.getString("totalpeople");
                String pee=peo.substring(peo.indexOf('-')+1);
                up.setText("人數上限："+pee+"人"); //活動人數

                String money2="0";
                if((rs.getString("pay_money").equals("0"))==false){
                    money2=rs.getString("pay_money"); //活動費用
                }
                money.setText(money2); //活動費用
                textView9.setText(rs.getString("doing_content")); //活動詳細內容
                name.setText(rs.getString("nickname")); //主辦人暱稱
                type_id=rs.getString("type_id"); //類別id


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        //-------是否訂閱-----------------------------------

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值

        String query2 = "select * from subscription  " +
                " where subscription_type='2' and subscription_content='"+type_id+"'  "+
                " and account_id='"+userid+"' ";


        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query2);
            rs = stmt.executeQuery();

            while (rs.next()) {

                view_type_id=rs.getString("subscription_content");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ImageView img;
        img=(ImageView)findViewById(R.id.love);
        String uri = "@drawable/like";
        String uri2 = "@drawable/like_red";
        int imageResource ;
        if(view_type_id.equals("")){
            imageResource = getResources().getIdentifier(uri, null, getPackageName());
        }else{
            imageResource = getResources().getIdentifier(uri2, null, getPackageName());
        }
        Drawable image = getResources().getDrawable(imageResource);
        img.setImageDrawable(image);



        //-------------按下愛心訂閱-------------------------------------------
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值

                String query;
                if(type_id.equals(view_type_id)){
                    query = "delete from subscription  " +
                            "where subscription_type='2' and subscription_content='"+type_id+"'";
                }else{
                    query = "insert into subscription ( account_id,subscription_type,subscription_content) values('"+userid+"','2','"+type_id+"')";
                }

                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();

                }catch (SQLException e) {
                    e.printStackTrace();
                }

                Bundle bundle =getIntent().getExtras(); //抓前一頁變數
                final String  doing_id=bundle.getString("doing_id"); //活動id

                Intent intent = new Intent();
                intent.setClass(deatial.this,deatial.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("doing_id",doing_id);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });






        //----我要參加---------------------------------------


        Button btnchooseimage=(Button)findViewById(R.id.btnchooseimage);
        btnchooseimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值



                //Bundle bundle =getIntent().getExtras(); //抓前一頁變數
                // String  doing_id=bundle.getString("doing_id"); //活動id



                String query = "insert into doing_detail (doing_id, account_id) values('"+doing_id+"','"+userid+"')";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();

                }catch (SQLException e) {
                    e.printStackTrace();
                }



                AlertDialog.Builder dialog = new AlertDialog.Builder(deatial.this);
                dialog.setTitle("參加成功"); //設定dialog 的title顯示內容
                dialog.setMessage("請在指定時間參加活動！"); //設定dialog 的內容
                //dialog.setIcon(android.R.drawable.ic_dialog_alert);//設定dialog 的ICON
                dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(deatial.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.show();


            }
        });


        //----我要留言---------------------------------------

        Button btnmessage=(Button)findViewById(R.id.btnmessage);
        btnmessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(deatial.this,message.class);
                Bundle bundle=new Bundle();
                bundle.putString("doing_id",doing_id);
                intent.putExtras(bundle);
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
