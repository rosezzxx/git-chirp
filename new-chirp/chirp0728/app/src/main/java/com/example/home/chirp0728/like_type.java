package com.example.home.chirp0728;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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

public class like_type extends AppCompatActivity {



    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;

    ArrayList<String> data_name = new ArrayList<String>();//活動名稱
    String[] array_name =new String[data_name.size()];
    ArrayList<String> data_id = new ArrayList<String>();  //活動id
    String[] array_id =new String[data_id.size()];
    ArrayList<String> data_address = new ArrayList<String>();  //活動地址
    String[] array_address =new String[data_address.size()];
    ArrayList<String> data_time = new ArrayList<String>();  //活動時間
    String[] array_time =new String[data_time.size()];
    ArrayList<String> data_type = new ArrayList<String>();  //活動類別
    String[] array_type =new String[data_type.size()];
    String str;

    String type_id="%";
    List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_type);


        Toast.makeText(like_type.this,"1",Toast.LENGTH_SHORT).show();
        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";


        //--------活動列表-----------
        ListView listview = (ListView)findViewById(R.id.like_type_listview_view);
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));


        //type_id=((MainActivity)getActivity()).get_type_id();    //分類id


        data_id.clear();
        data_name.clear();
        data_address.clear();
        data_time.clear();
        data_type.clear();
        items.clear();
        array_id=new String[data_id.size()];
        array_name=new String[data_name.size()];
        array_address=new String[data_address.size()];
        array_time=new String[data_time.size()];
        array_type=new String[data_type.size()];

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

        String query = "select * from doing_view  " +
                " where type_id in  " +
                " (select subscription_content from subscription  " +
                " where subscription_type='2'  and account_id='"+userid+"') " +
                "  or " +
                "   account_id in " +
                "  (select subscription_content from subscription " +
                "   where " +
                "   subscription_type='1'  and account_id='"+userid+"') " +
                " order by doing_start";

        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String id =rs.getString("doing_id");
                String name =rs.getString("doing_name");
                String address =rs.getString("doing_place");
                String time=""+(rs.getString("doing_start")).substring(0,16)+"~"+(rs.getString("doing_end")).substring(0,16);
                String type =rs.getString("type_name")+"版";
                data_id.add(id);
                data_name.add(name);
                data_address.add(address);
                data_time.add(time);
                data_type.add(type);
            }


            array_name=data_name.toArray(array_name);
            array_id=data_id.toArray(array_id);
            array_address=data_address.toArray(array_address);
            array_time=data_time.toArray(array_time);
            array_type=data_type.toArray(array_type);
            SimpleAdapter adapter;


            for(int i=0;i<array_id.length;i++){
                Map item=new HashMap();
                item.put("name",array_name[i]);
                item.put("address",array_address[i]);
                item.put("time",array_time[i]);
                item.put("type",array_type[i]);
                items.add(item);
            }

            adapter = new SimpleAdapter(this,items,R.layout.activity_dolist, new String[]{"name","address","time","type"},new int[]{R.id.item_title_tv,R.id.item_content_address,R.id.item_content_time,R.id.item_title_type});
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(onClickListView);

        } catch (SQLException e) {
            e.printStackTrace();
        }




        //-----------------訂閱下拉式選單-------------------------------------


        final Spinner like_type = (Spinner)findViewById(R.id.like_type);

        String query2 = " select * from subscription a  " +
                " inner join doingtype b " +
                " on a.subscription_content=b.type_id " +
                " where a.subscription_type='2' and account_id='"+userid+"' ";

        String query3 = " select a.account_id ,b.account_id as account , nickname from subscription a  " +
                " inner join account b " +
                " on a.subscription_content=b.account_id " +
                " where a.subscription_type='1' and a.account_id='"+userid+"' ";


        List<CItem> lst = new ArrayList<CItem>();

        CItem item;
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query2);
            rs = stmt.executeQuery();

            item = new CItem("", "全部");
            lst.add(item);

            while (rs.next()) {
                String type2_id =rs.getString("type_id");
                String type2_name =rs.getString("type_name");
                item = new CItem(type2_id, type2_name);
                lst.add(item);
                ArrayAdapter<CItem> myaAdapter = new ArrayAdapter<CItem>(like_type.this,  android.R.layout.simple_list_item_1, lst);
                like_type.setAdapter(myaAdapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query3);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String type2_id =rs.getString("account");
                String type2_name =rs.getString("nickname");
                item = new CItem(type2_id, type2_name);
                lst.add(item);
                ArrayAdapter<CItem> myaAdapter = new ArrayAdapter<CItem>(like_type.this,  android.R.layout.simple_list_item_1, lst);
                like_type.setAdapter(myaAdapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }




        like_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                str = ((CItem) like_type.getSelectedItem()).GetID().toString();
//                Toast.makeText(like_type.this,
//                        "键:" + like_type.getSelectedItem().toString() + "、" + ((CItem) like_type.getSelectedItem()).GetID() +
//                                "，值:" + ((CItem) like_type.getSelectedItem()).GetValue(),
//                        Toast.LENGTH_LONG).show();
                like_type_list(((CItem) like_type.getSelectedItem()).GetID().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });








    }


    //----訂閱活動列表-------------------
    public void like_type_list(String voidtype_id){

        ListView listview = (ListView)findViewById(R.id.like_type_listview_view);
        listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));

        data_id.clear();
        data_name.clear();
        data_address.clear();
        data_time.clear();
        data_type.clear();
        items.clear();
        array_id=new String[data_id.size()];
        array_name=new String[data_name.size()];
        array_address=new String[data_address.size()];
        array_time=new String[data_time.size()];
        array_type=new String[data_type.size()];

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

        String query = "select * from doing_view  " +
                " where (type_id in  " +
                " (select subscription_content from subscription  " +
                " where subscription_type='2'  and account_id='"+userid+"') " +
                "  or " +
                "   account_id in " +
                "  (select subscription_content from subscription " +
                "   where " +
                "   subscription_type='1'  and account_id='"+userid+"') ) " +
                "  and (type_id like '%"+voidtype_id+"%'   or  account_id='"+voidtype_id+"' )  " +
                " order by doing_start";

        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String id =rs.getString("doing_id");
                String name =rs.getString("doing_name");
                String address =rs.getString("doing_place");
                String time=""+(rs.getString("doing_start")).substring(0,16)+"~"+(rs.getString("doing_end")).substring(0,16);
                String type =rs.getString("type_name")+"版";
                data_id.add(id);
                data_name.add(name);
                data_address.add(address);
                data_time.add(time);
                data_type.add(type);
            }


            array_name=data_name.toArray(array_name);
            array_id=data_id.toArray(array_id);
            array_address=data_address.toArray(array_address);
            array_time=data_time.toArray(array_time);
            array_type=data_type.toArray(array_type);
            SimpleAdapter adapter;


            for(int i=0;i<array_id.length;i++){
                Map item=new HashMap();
                item.put("name",array_name[i]);
                item.put("address",array_address[i]);
                item.put("time",array_time[i]);
                item.put("type",array_type[i]);
                items.add(item);
            }

            adapter = new SimpleAdapter(this,items,R.layout.activity_dolist, new String[]{"name","address","time","type"},new int[]{R.id.item_title_tv,R.id.item_content_address,R.id.item_content_time,R.id.item_title_type});
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(onClickListView);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class CItem {
        private String ID;
        private String Value = "";

        public CItem() {
            ID = "";
            Value = "";
        }

        public CItem(String _ID, String _Value) {
            ID = _ID;
            Value = _Value;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return Value;
        }

        public String GetID() {
            return ID;
        }

        public String GetValue() {
            return Value;
        }
    }








    //---------查看活動詳細內容-----------------------------------------
    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent();
            intent.setClass(like_type.this,deatial.class);
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
