package com.example.asus.a0404;


import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.ListView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener  {

    //------連線--------------
    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;

   //----------全部活動-----------
//    ArrayList<String> data_name = new ArrayList<String>();
//    String[] array_name =new String[data_name.size()];
//    ArrayList<String> data_id = new ArrayList<String>();  //活動id
//    String[] array_id =new String[data_id.size()];

    //-----------全部類別----------
    ArrayList<String> type_data_name = new ArrayList<String>();
    String[] type_array_name =new String[type_data_name.size()];
    ArrayList<String> type_data_id = new ArrayList<String>();  //活動id
    String[] type_array_id =new String[type_data_id.size()];
    String type_id="%";

    //-------page--------
    private android.support.design.widget.TabLayout mTabs;
    private ViewPager mViewPager;
    private Fragment_view fragment1 = new Fragment_view();
    private Fragment_friend fragment2 = new Fragment_friend();
    private Fragment_insert fragment3 = new Fragment_insert();
    private Fragment_notice fragment4 = new Fragment_notice();
    private Fragment_my fragment5 = new Fragment_my();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";

        //--------活動列表-----------
//        ListView listview = (ListView) findViewById(R.id.listview);
//        String query = "select * from doing";
//        try {
//            connect = CONN(un, passwords, db, ip);
//            stmt = connect.prepareStatement(query);
//            rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                String id =rs.getString("doing_id");
//                String name =rs.getString("doing_name");
//                data_id.add(id);
//                data_name.add(name);
//            }
//
//            array_name=data_name.toArray(array_name);
//            array_id=data_id.toArray(array_id);
//            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_name);
//            //listview.setAdapter(NoCoreAdapter);
//            //listview.setOnItemClickListener(onClickListView);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


        //登出
        Button logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(MainActivity.this,welcome.class);
                startActivity(intent);
                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                sharedPreferences.edit().putString("Name", "").apply(); //存使用者id進sharedPreferences
            }
        });






        //--------全部分類列表-----------
        ListView listview1 = (ListView) findViewById(R.id.listview1);
        String query1 = "select * from doingtype";
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query1);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String type_id =rs.getString("type_id");
                String type_name =rs.getString("type_name");
                type_data_id.add(type_id);
                type_data_name.add(type_name);
            }

            type_array_name=type_data_name.toArray(type_array_name);
            type_array_id=type_data_id.toArray(type_array_id);

            List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
            for(int i=0;i<type_array_id.length;i++){
                Map item=new HashMap();
                item.put("name",type_array_name[i]);
                items.add(item);
            }

            SimpleAdapter adapter = new SimpleAdapter(this,items,R.layout.typelistview, new String[]{"name"},new int[]{R.id.type_txt});
            listview1.setAdapter(adapter);

//            ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,type_array_name);
//            listview1.setAdapter(NoCoreAdapter);
            listview1.setOnItemClickListener(onClickListView1);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TextView t1;

        t1=(TextView)findViewById(R.id.textView1);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this ,Main2Activity.class);
                startActivity(intent);
            }
        });

        Button b1;

        b1=(Button)findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"測試111",Toast.LENGTH_SHORT).show();

                String query = "select * from T1";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();
                    ArrayList<String> data = new ArrayList<String>();
                    while (rs.next()) {
                        String id = rs.getString("a2");
                        data.add(id);
                    }

                }catch (SQLException e) {
                    e.printStackTrace();
                }



                onBackPressed();
            }
        });

        //大頭照
        ImageView img = (ImageView) findViewById(R.id.imageView);

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值

        String msg;
        String image="";
        try {
            connect = CONN(un, passwords, db, ip);
            String commands = "SELECT  img FROM account WHERE (account_id = '"+userid+"')";
            Statement stmt = connect.createStatement();
            rs = stmt.executeQuery(commands);
            if (rs.next()) {
                image = rs.getString("img");

                String base64String = image;
                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img.setImageBitmap(decodedByte);
            }
            else {
            }

        } catch (SQLException ex) {
            msg = ex.getMessage().toString();
            Log.d("hitesh", msg);
        }



        //------page-----------------
        mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);


        mViewPager.addOnPageChangeListener(this);
        mTabs.addOnTabSelectedListener(this);


        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragment1;
                    case 1:
                        return fragment2;
                    case 2:
                        return fragment3;
                    case 3:
                        return fragment4;
                    case 4:
                        return fragment5;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 5;
            }
        });


    }
    //傳type_id給fragment
    public String get_type_id()
    {
        return this.type_id;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //mTabs.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }





//----------全部活動------------
//    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            // Toast 快顯功能 第三個參數 Toast.LENGTH_SHORT 2秒  LENGTH_LONG 5秒
//            Toast.makeText(MainActivity.this,"id="+array_id[position], Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this ,Main2Activity.class);
//            Bundle bundle=new Bundle();
//            bundle.putString("doing_id",array_id[position].toString());
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
//    };

    //--------------全部分類-------------
    private AdapterView.OnItemClickListener onClickListView1 = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            Toast.makeText(MainActivity.this,"分類="+type_array_id[position], Toast.LENGTH_SHORT).show();

            type_id=type_array_id[position];
            mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position) {
                        case 0:
                            return fragment1;
                        case 1:
                            return fragment2;
                        case 2:
                            return fragment3;
                        case 3:
                            return fragment4;
                        case 4:
                            return fragment5;
                    }
                    return null;
                }

                @Override
                public int getCount() {
                    return 5;
                }
            });


            onBackPressed();
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //返回鍵
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
