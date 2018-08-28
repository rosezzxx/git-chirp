package com.example.home.chirp0728;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class my_foundActivity extends Fragment {

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
    String type_id="%";
    List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();


    public my_foundActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_found,container,false);
        initListView(view);
        return  view;
        //return inflater.inflate(R.layout.fragment_my_found, container, false);
    }

    private void initListView(View view) {
        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";

        SharedPreferences preferences = this.getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        String userid = preferences.getString("Name" , "0"); //抓SharedPreferences內Name值

        //--------創辦列表-----------
        ListView listview = (ListView)view.findViewById(R.id.my_found_listview);
        String query = "select * from doing " +
                "where account_id='"+userid+"' ";


        data_id.clear();
        data_name.clear();
        data_address.clear();
        data_time.clear();
        items.clear();
        array_id=new String[data_id.size()];
        array_name=new String[data_name.size()];
        array_address=new String[data_address.size()];
        array_time=new String[data_time.size()];

        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String id =rs.getString("doing_id");
                String name =rs.getString("doing_name");
                String address =rs.getString("doing_place");
                String time=""+(rs.getString("doing_start")).substring(0,16)+"~"+(rs.getString("doing_end")).substring(0,16);
                data_id.add(id);
                data_name.add(name);
                data_address.add(address);
                data_time.add(time);
            }


            array_name=data_name.toArray(array_name);
            array_id=data_id.toArray(array_id);
            array_address=data_address.toArray(array_address);
            array_time=data_time.toArray(array_time);
            SimpleAdapter adapter;


            for(int i=0;i<array_id.length;i++){
                Map item=new HashMap();
                item.put("name",array_name[i]);
                item.put("address",array_address[i]);
                item.put("time",array_time[i]);
                items.add(item);
            }

            adapter = new SimpleAdapter(getContext(),items,R.layout.activity_dolist, new String[]{"name","address","time"},new int[]{R.id.item_title_tv,R.id.item_content_address,R.id.item_content_time});
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);

//            ArrayAdapter NoCoreAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, array_name);
//            listview.setAdapter(NoCoreAdapter);

            listview.setOnItemClickListener(onClickListView);

        } catch (SQLException e) {
            e.printStackTrace();
        }





    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



            Intent intent = new Intent();
            intent.setClass(getContext(),deatial.class);
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
