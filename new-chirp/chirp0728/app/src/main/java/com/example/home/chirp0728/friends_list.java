package com.example.home.chirp0728;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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


public class friends_list extends AppCompatActivity {

    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;

    private ListView listView;
    //private String[] list = {"曾意淳","吳昱萱","劉玉婷","好友1","好友2","好友3","好友4","好友5"};
    //private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";

        listView = (ListView)findViewById(R.id.listview_friend);

        //String query = "select friend.friend_account,account.username from friend inner join account on friend.friend_account=account.account_id where friend.account_id='test'";
        String query="select friend.friend_account,account.username,a.messnum from friend " +
                "inner join account " +
                "on friend.friend_account=account.account_id " +
                "left join (select account_id,count(*) as messnum from chat " +
                "where chat_check='2' and chat_friends_id='test' " +
                "group by account_id) as a " +
                "on a.account_id = friend.friend_account " +
                "where friend.account_id='test'";
        ArrayList<String> friend_n = new ArrayList<String>();
        String[] array_friend_name =new String[friend_n.size()];
        ArrayList<String> friend_i = new ArrayList<String>();
        String[] array_friend_id =new String[friend_i.size()];
        ArrayList<String> friend_mess = new ArrayList<String>();
        String[] array_friend_mess =new String[friend_mess.size()];
        List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String friend_name = rs.getString("username");
                String friend_id = rs.getString("friend_account");
                String friend_messnum = rs.getString("messnum");
                friend_n.add(friend_name);
                friend_i.add(friend_id);
                friend_mess.add(friend_messnum);
//                Toast.makeText(this,friend_n + "---" + friend_i,Toast.LENGTH_LONG).show();
            }
            array_friend_name=friend_n.toArray(array_friend_name);
            array_friend_id=friend_i.toArray(array_friend_id);
            array_friend_mess=friend_mess.toArray(array_friend_mess);
            SimpleAdapter adapter;


            for(int i=0;i<array_friend_id.length;i++){
                Map item=new HashMap();
                item.put("id",array_friend_id[i]);
                item.put("name",array_friend_name[i]);
                item.put("messnum",array_friend_mess[i]);
                items.add(item);
            }

            adapter = new SimpleAdapter(this,items,R.layout.friends_layout_2, new String[]{"name","id","messnum"},new int[]{R.id.Itemname,R.id.ItemID,R.id.Itemmessagenumber});
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(listView.this,"你单击的是第"+(position+1)+"条数据",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(friends_list.this,friends_chat.class);
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
