package com.example.home.chirp0728;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class message extends AppCompatActivity {






    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;


    ArrayList<String> data_id = new ArrayList<String>();  //留言人
    String[] array_id =new String[data_id.size()];
    ArrayList<String> data_content = new ArrayList<String>();  //留言內容
    String[] array_content =new String[data_content.size()];
    ArrayList<String> data_time = new ArrayList<String>();  //留言時間
    String[] array_time =new String[data_time.size()];


    List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);









        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";


        Bundle bundle =getIntent().getExtras(); //抓前一頁變數
        final String  doing_id=bundle.getString("doing_id"); //活動id

        Toast.makeText(this, doing_id, Toast.LENGTH_SHORT).show();

        //--------留言列表-----------
        ListView listview = (ListView)findViewById(R.id.listview_view);
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));


        data_id.clear();
        data_content.clear();
        data_time.clear();
        items.clear();
        array_id=new String[data_id.size()];
        array_content=new String[data_content.size()];
        array_time=new String[data_time.size()];


        String query = "select a.*,b.nickname from message a " +
                " inner join account b " +
                " on a.account_id=b.account_id " +
                " where doing_id='"+doing_id+"' "+
                "  order by message_time asc ";

        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String id =rs.getString("nickname");
                String content =rs.getString("message_content");
                String time=""+(rs.getString("message_time")).substring(0,16);
                data_id.add(id);
                data_content.add(content);
                data_time.add(time);
            }



            array_id=data_id.toArray(array_id);
            array_content=data_content.toArray(array_content);
            array_time=data_time.toArray(array_time);
            SimpleAdapter adapter;


            for(int i=0;i<array_id.length;i++){
                Map item=new HashMap();
                item.put("nickname",array_id[i]);
                item.put("content",array_content[i]);
                item.put("time",array_time[i]);
                items.add(item);
            }

            adapter = new SimpleAdapter(this,items,R.layout.message_layout, new String[]{"nickname","content","time"},new int[]{R.id.message_nickname,R.id.message_content,R.id.message_time});
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);

//            ArrayAdapter NoCoreAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, array_name);
//            listview.setAdapter(NoCoreAdapter);

            listview.setOnItemClickListener(onClickListView);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        //--------留言--------------------------------------------------------------

        Button btnadd=(Button)findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值

                EditText content = (EditText)findViewById(R.id.content);



                String query = " insert into message (doing_id, account_id,message_content) values('"+doing_id+"','"+userid+"','"+content.getText()+"') ";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();

                }catch (SQLException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent();
                intent.setClass(message.this,message.class);
                Bundle bundle=new Bundle();
                bundle.putString("doing_id",doing_id);
                intent.putExtras(bundle);
                startActivity(intent);





            }
        });







    }



    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            Intent intent = new Intent();
            intent.setClass(message.this,deatial.class);
            Bundle bundle=new Bundle();
            bundle.putString("doing_id",array_id[position].toString());
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
