package com.example.asus.a0404;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class dategolist extends AppCompatActivity {

    Connection con;
    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;

    ArrayList<String> data_name = new ArrayList<String>();
    String[] array_name =new String[data_name.size()];
    ArrayList<String> data_id = new ArrayList<String>();  //活動id
    String[] array_id =new String[data_id.size()];

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
        setContentView(R.layout.activity_dategolist);

        ListView listview = (ListView) findViewById(R.id.listview);

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String time = bundle.getString("date");



        /*
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    TextView t1 = (TextView)findViewById(R.id.textView10);
                    try {
                        date = (Date)formatter.parse(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
        }*/



        //資料庫部分
        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";
        connect = CONN(un, passwords, db, ip);
        String query = "select * from doing left join doing_detail  on doing_detail.doing_id =  doing.doing_id where ((doing_start BETWEEN Convert(datetime, '"+time+" 00:00:00') and Convert(datetime, '"+time+" 23:59:59')) or (doing_start <=CONVERT(char(10), '"+time+"',126))) and ((doing_end BETWEEN Convert(datetime, '"+time+" 00:00:00') and Convert(datetime, '"+time+" 23:59:59')) or (doing_end >=CONVERT(char(10), '"+time+"',126))) and (doing_detail.account_id = '"+userid+"' or (doing_detail.account_id is null and doing.account_id = '"+userid+"'))";
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String id =rs.getString("doing_id");
                String name =rs.getString("doing_name");
                data_id.add(id);
                data_name.add(name);
            }

            array_name=data_name.toArray(array_name);
            array_id=data_id.toArray(array_id);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_name);
            listview.setAdapter(NoCoreAdapter);


            listview.setOnItemClickListener(onClickListView);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Toast 快顯功能 第三個參數 Toast.LENGTH_SHORT 2秒  LENGTH_LONG 5秒
            Toast.makeText(dategolist.this,"id="+array_id[position], Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(dategolist.this ,Main2Activity.class);
            Bundle bundle=new Bundle();
            bundle.putString("doing_id",array_id[position].toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
}
