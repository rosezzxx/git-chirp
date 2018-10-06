package com.example.home.chirp0728;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_chat);

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";

        inputt = (EditText)findViewById(R.id.input);
        listView = (ListView)findViewById(R.id.list_of_messages);

        String query = "select chat.account_id,chat_friends_id,account.username,chat_content,chat_time from chat inner join account on chat.account_id=account.account_id where chat.account_id='test' or chat_friends_id='test' order by chat_time";

        ArrayList<String> friend_n = new ArrayList<String>();
        String[] array_friend_name =new String[friend_n.size()];
        ArrayList<String> chat_con = new ArrayList<String>();
        String[] array_chat_con =new String[chat_con.size()];
        ArrayList<String> chat_timee = new ArrayList<String>();
        String[] array_chat_timee =new String[chat_timee.size()];
        List<Map<String, String>> items = new ArrayList<Map<String, String>>();
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
                chat_timee.add(chat_time);
                //Toast.makeText(this,friend_n + "---" + chat_con + "++++" + chat_timee,Toast.LENGTH_LONG).show();
            }
            array_friend_name=friend_n.toArray(array_friend_name);
            array_chat_con=chat_con.toArray(array_chat_con);
            array_chat_timee=chat_timee.toArray(array_chat_timee);
            SimpleAdapter adapter;


            for(int i=0;i<array_friend_name.length;i++){
                Map item=new HashMap();
                item.put("name",array_friend_name[i]);
                item.put("content",array_chat_con[i]);
                item.put("time",array_chat_timee[i]);
                items.add(item);
            }

            adapter = new SimpleAdapter(this,items,R.layout.friends_layout_3, new String[]{"name","content","time"},new int[]{R.id.Itemname,R.id.Itemcon,R.id.Itemtime});
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query_insert="insert into chat(account_id,chat_friends_id,chat_content,chat_time) values('test','rosezzxx','"+inputt.getText()+"',getdate())";

                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query_insert);
                    rs = stmt.executeQuery();
//                    inputt.setText("11", TextView.BufferType.EDITABLE);
//                    Intent intent = new Intent(friends_chat.this, friends_chat.class);
//                    startActivity(intent);


                } catch (SQLException e) {
                    e.printStackTrace();
                }
                inputt.setText("");
                Intent intent = new Intent(friends_chat.this, friends_chat.class);
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
