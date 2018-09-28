package com.example.home.chirp0728;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class lookmessage extends AppCompatActivity {


    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;


    ArrayList<String> data_name = new ArrayList<String>();//活動名稱
    String[] array_name =new String[data_name.size()];
    ArrayList<String> data_id = new ArrayList<String>();  //活動id
    String[] array_id =new String[data_id.size()];
    ArrayList<String> data_message_id = new ArrayList<String>();  //留言id
    String[] array_message_id =new String[data_message_id.size()];
    ArrayList<String> data_message_content = new ArrayList<String>();  //留言內容
    String[] array_message_content =new String[data_message_content.size()];
    ArrayList<String> data_reply = new ArrayList<String>();  //回覆內容
    String[] array_reply =new String[data_reply.size()];


    List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookmessage);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("查看留言");


        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";



        //--------留言列表-----------
        ListView listview = (ListView)findViewById(R.id.listview_view);
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));

        data_id.clear();
        data_name.clear();
        data_message_content.clear();
        data_message_id.clear();
        data_reply.clear();
        items.clear();
        array_id=new String[data_id.size()];
        array_name=new String[data_name.size()];
        array_message_content=new String[data_message_content.size()];
        array_message_id=new String[data_message_id.size()];
        array_reply=new String[data_reply.size()];

        String query = "select * from doing_view a  " +
                " inner join message b  " +
                " on a.doing_id=b.doing_id  " +
                " where a.account_id='rosezzxx'  "+
                " order by b.message_time ";

        try{
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String id =rs.getString("doing_id"); //活動id
                String name =rs.getString("doing_name"); //活動名稱
                String message_id =rs.getString("message_id"); //留言id
                String message_content =rs.getString("message_content"); //留言內容
                String reply =rs.getString("message_reply"); //留言內容
                data_id.add(id);
                data_name.add(name);
                data_message_id.add(message_id);
                data_message_content.add(message_content);
                data_reply.add(reply);

            }


            array_name=data_name.toArray(array_name);
            array_id=data_id.toArray(array_id);
            array_message_content=data_message_content.toArray(array_message_content);
            array_message_id=data_message_id.toArray(array_message_id);
            array_reply=data_reply.toArray(array_reply);
            SimpleAdapter adapter;


            for(int i=0;i<array_id.length;i++){
                Map item=new HashMap();
                item.put("name",array_name[i]);
                item.put("message_id",array_message_id[i]);
                item.put("message_content",array_message_content[i]);
                item.put("reply",array_reply[i]);
                items.add(item);
            }

            adapter = new SimpleAdapter(this,items,R.layout.lookmessage_layout, new String[]{"name","message_content","reply"},new int[]{R.id.item_title_tv,R.id.message_content,R.id.message_reply});
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);




            listview.setOnItemClickListener(onClickListView);

        } catch (SQLException e) {
            e.printStackTrace();
        }






    }



    //---------------------------------------------------
    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent();
            intent.setClass(lookmessage.this,leavemessage.class);
            Bundle bundle=new Bundle();
            bundle.putString("doing_id",array_id[position].toString());
            bundle.putString("message_id",array_message_id[position].toString());
            intent.putExtras(bundle);
            startActivity(intent);

        }
    };


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
