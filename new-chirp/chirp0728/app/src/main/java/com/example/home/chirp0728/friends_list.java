package com.example.home.chirp0728;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

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

    ArrayList<String> friend_n = new ArrayList<String>();
    String[] array_friend_name =new String[friend_n.size()];
    ArrayList<String> friend_i = new ArrayList<String>();
    String[] array_friend_id =new String[friend_i.size()];
    ArrayList<String> friend_mess = new ArrayList<String>();
    String[] array_friend_mess =new String[friend_mess.size()];
    ArrayList<Bitmap> friend_img = new ArrayList<Bitmap>();
    Bitmap[] array_friend_img =new Bitmap[friend_img.size()];
    List<Map<String, String>> items = new ArrayList<Map<String, String>>();


    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("好友列表");

        listView = (ListView)findViewById(R.id.listview_friend);
        ImageView img = (ImageView) findViewById(R.id.icon);
        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

        String query="select friend.friend_account,account.username,a.messnum,account.img from friend " +
                "inner join account " +
                "on friend.friend_account=account.account_id " +
                "left join (select account_id,count(*) as messnum from chat " +
                "where chat_check='2' and chat_friends_id='"+userid+"' " +
                "group by account_id) as a " +
                "on a.account_id = friend.friend_account " +
                "where friend.account_id='"+userid+"'";

        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String friend_name = rs.getString("username");
                String friend_id = rs.getString("friend_account");
                String friend_messnum = rs.getString("messnum");
                String image = rs.getString("img");
                String base64String = image;
                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                friend_n.add(friend_name);
                friend_i.add(friend_id);
                friend_mess.add(friend_messnum);
                friend_img.add(decodedByte);
            }
            array_friend_name=friend_n.toArray(array_friend_name);
            array_friend_id=friend_i.toArray(array_friend_id);
            array_friend_mess=friend_mess.toArray(array_friend_mess);
            array_friend_img=friend_img.toArray(array_friend_img);
            SimpleAdapter adapter;


            for(int i=0;i<array_friend_id.length;i++){
                Map item=new HashMap();
                item.put("id",array_friend_id[i]);
                item.put("name",array_friend_name[i]);
                item.put("messnum",array_friend_mess[i]);
                item.put("img",array_friend_img[i]);
                items.add(item);
            }

            adapter = new SimpleAdapter(this,items,R.layout.friends_layout_2, new String[]{"name","id","messnum","img"},new int[]{R.id.Itemname,R.id.ItemID,R.id.Itemmessagenumber,R.id.icon});
            // 重寫 ViewBinder 讓 Bitmap可以設定在ImageView上
            adapter.setViewBinder(new ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // 檢查是否是ImageView和Bitamp
                    if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                        ImageView iv = (ImageView) view;
                        Bitmap bm = (Bitmap) data;
                        iv.setImageBitmap(bm);
                        return true;
                    }
                    return false;
                }
            });

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
                Bundle bundle=new Bundle();
                bundle.putString("friend_idd",array_friend_id[position].toString());
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
