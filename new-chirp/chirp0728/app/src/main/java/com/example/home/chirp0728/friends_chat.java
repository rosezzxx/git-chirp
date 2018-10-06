package com.example.home.chirp0728;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class friends_chat extends AppCompatActivity {

    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;

    private ListView listView;
    private EditText inputt;

    ArrayList<String> friend_n = new ArrayList<String>();
    String[] array_friend_name =new String[friend_n.size()];
    ArrayList<String> chat_con = new ArrayList<String>();
    String[] array_chat_con =new String[chat_con.size()];
    ArrayList<String> chat_timee = new ArrayList<String>();
    String[] array_chat_timee =new String[chat_timee.size()];
    List<Map<String, String>> items = new ArrayList<Map<String, String>>();

    SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_chat);

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";

        Bundle bundle =getIntent().getExtras(); //抓前一頁變數
        final String friend_idd=bundle.getString("friend_idd");

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("聊天室");

        inputt = (EditText)findViewById(R.id.input);
        listView = (ListView)findViewById(R.id.list_of_messages);

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

        String query = "select chat.account_id,chat_friends_id,account.username,chat_content,chat_time from chat " +
                "inner join account " +
                "on chat.account_id=account.account_id " +
                "where (chat.account_id='"+userid+"' or chat_friends_id='"+userid+"') and (chat.account_id='"+friend_idd+"' or chat_friends_id='"+friend_idd+"') " +
                "order by chat_time";

        //Toast.makeText(this,friend_idd,Toast.LENGTH_LONG).show();

        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String friend_name = rs.getString("username");
                String chat_content = rs.getString("chat_content");
                String chat_time = rs.getString("chat_time");
                friend_n.add(friend_name);
                chat_con.add(chat_content);
                chat_timee.add(chat_time.substring(0,19));
                //Toast.makeText(this,friend_n + "---" + chat_con + "++++" + chat_timee,Toast.LENGTH_LONG).show();
            }
            array_friend_name=friend_n.toArray(array_friend_name);
            array_chat_con=chat_con.toArray(array_chat_con);
            array_chat_timee=chat_timee.toArray(array_chat_timee);


            for(int i=0;i<array_friend_name.length;i++){
                Map item=new HashMap();
                item.put("name",array_friend_name[i]);
                item.put("content",array_chat_con[i]);
                item.put("time",array_chat_timee[i]);
                items.add(item);
            }

            adapter = new SimpleAdapter(friends_chat.this,items,R.layout.friends_layout_3, new String[]{"name","content","time"},new int[]{R.id.Itemname,R.id.Itemcon,R.id.Itemtime});
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //1代表已讀，2代表未讀
        String query_update="update chat set chat_check='1' where chat_friends_id='"+userid+"' and account_id='"+friend_idd+"' and chat_check='2'";
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query_update);
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值
                String query_insert="insert into chat(account_id,chat_friends_id,chat_content,chat_time,chat_check) values('"+userid+"','"+friend_idd+"','"+inputt.getText()+"',getdate(),'2')";

                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query_insert);
                    rs = stmt.executeQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                inputt.setText("");
                items.clear();
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                friend_n.clear();
                chat_con.clear();
                chat_timee.clear();
                array_chat_con.clone();
                array_chat_timee.clone();
                array_friend_name.clone();

                String query = "select chat.account_id,chat_friends_id,account.username,chat_content,chat_time from chat " +
                        "inner join account " +
                        "on chat.account_id=account.account_id " +
                        "where (chat.account_id='"+userid+"' or chat_friends_id='"+userid+"') and (chat.account_id='"+friend_idd+"' or chat_friends_id='"+friend_idd+"') " +
                        "order by chat_time";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String friend_name = rs.getString("username");
                        String chat_content = rs.getString("chat_content");
                        String chat_time = rs.getString("chat_time");
                        friend_n.add(friend_name);
                        chat_con.add(chat_content);
                        chat_timee.add(chat_time.substring(0,19));
                        //Toast.makeText(this,friend_n + "---" + chat_con + "++++" + chat_timee,Toast.LENGTH_LONG).show();
                    }
                    array_friend_name=friend_n.toArray(array_friend_name);
                    array_chat_con=chat_con.toArray(array_chat_con);
                    array_chat_timee=chat_timee.toArray(array_chat_timee);

                    for(int i=0;i<array_friend_name.length;i++){
                        Map item=new HashMap();
                        item.put("name",array_friend_name[i]);
                        item.put("content",array_chat_con[i]);
                        item.put("time",array_chat_timee[i]);
                        items.add(item);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }


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
