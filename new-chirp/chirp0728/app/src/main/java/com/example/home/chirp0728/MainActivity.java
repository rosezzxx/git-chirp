package com.example.home.chirp0728;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


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

    String type_id="%";
    List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

    //private LinearLayout L1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Toast.makeText(MainActivity.this,"測試111",Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";




            //--------活動列表-----------
            ListView listview = (ListView)findViewById(R.id.listview_view);
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

            //String query = "select * from doing where type_id like '"+type_id+"' ";


            String query = "select * from doing_view " +
                    " where doing_name like '%%' or doing_content like '%%' or type_name like '%%' " +
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



        //----搜尋---------------------------------------


        Button button9=(Button)findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this,search.class);
                startActivity(intent);


            }
        });







        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,add.class);
                startActivity(intent);

            }
        });
    }


    //---------查看活動詳細內容-----------------------------------------
    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent();
            intent.setClass(MainActivity.this,deatial.class);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_slideshow) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {  //首頁

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {  //訂閱
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,like_type.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {  //活動管理
            Intent i2 = new Intent();
            i2.setClass(MainActivity.this, mydoing.class);
            startActivity(i2);
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {  //好友管理

        } else if (id == R.id.nav_schedule) {  //查看行事曆
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,calendar.class);
            startActivity(intent);


        } else if (id == R.id.nav_lead) {  //留言管理

        }else if (id == R.id.nav_access) {  //帳號管理

        }else if (id == R.id.nav_out) {  //登出

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
