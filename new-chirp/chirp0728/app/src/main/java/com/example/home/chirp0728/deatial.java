package com.example.home.chirp0728;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class deatial extends AppCompatActivity implements OnMapReadyCallback {
    private static String LOG_TAG = "deatial";
    private Button btn_query;
    private GoogleMap map;
    private LocationManager mgr;
    private LocationListener mLocationListenerGPS;
    private LocationListener mLocationListenerNETWORK;
    private final int TIMEOUT_SEC = 20;
    private int currentTime = 0;
    private Handler handler = new Handler();
    private boolean getLocationFlag = false;
    private LatLng START = null; // 啟始座標
    private TextView et_address2;
    String et_address;;
    String PostParam;

    //------連線--------------
    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;

    String type_id=""; //類別id
    String view_type_id="";  //是否訂閱類別id

    String doing_id2=""; //活動id
    String account_id=""; // 主辦人id

    String money2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatial);




        et_address2 = (TextView)findViewById(R.id.addresss);
        et_address = et_address2.getText().toString();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mgr = (LocationManager) getSystemService(LOCATION_SERVICE );
        //getLatLngByAddr();

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";


        Bundle bundle =getIntent().getExtras(); //抓前一頁變數
        final String  doing_id=bundle.getString("doing_id"); //活動id



        //----基本資料-------
        final TextView doing_name=(TextView)findViewById(R.id.doing_name); //活動名稱
        TextView doing_type=(TextView)findViewById(R.id.doing_type); //活動類別
        TextView address=(TextView)findViewById(R.id.addresss); //活動地址
        TextView time=(TextView)findViewById(R.id.time); //活動時間
        TextView up=(TextView)findViewById(R.id.up); //活動人數
        TextView money=(TextView)findViewById(R.id.money); //活動費用
        TextView textView9=(TextView)findViewById(R.id.textView9); //活動詳細內容
        TextView name=(TextView)findViewById(R.id.name); //主辦人暱稱
        TextView look=(TextView)findViewById(R.id.look); //查看主辦人過去活動紀錄


        String query="";
        if (doing_id.equals("sensor")){  //搖一搖
            query = " select TOP 1 * from doing_view  " +
                    " ORDER BY NEWID() ";
        }
        else{
            query = "select * from doing_view  " +
                    "where doing_id='"+doing_id+"' ";
        }



        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {

                doing_id2=rs.getString("doing_id");
                doing_name.setText(rs.getString("doing_name")); //活動名稱
                doing_type.setText(rs.getString("type_name")); //活動類別
                address.setText(rs.getString("doing_place")); //活動地址
                time.setText("活動時間："+(rs.getString("doing_start")).substring(0,16)+"~"+(rs.getString("doing_end")).substring(0,16)); //活動時間

                String peo=rs.getString("totalpeople");
                String pee=peo.substring(peo.indexOf('-')+1);
                up.setText("人數上限："+pee+"人"); //活動人數

                String money2="0";
                if((rs.getString("pay_money").equals("0"))==false){
                    money2=rs.getString("pay_money"); //活動費用
                }





                money.setText(money2); //活動費用
                textView9.setText(rs.getString("doing_content")); //活動詳細內容
                name.setText(rs.getString("nickname")); //主辦人暱稱
                account_id=rs.getString("account_id"); //主辦人id
                type_id=rs.getString("type_id"); //類別id


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        //-------是否訂閱-----------------------------------

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

        String query2 = "select * from subscription  " +
                " where subscription_type='2' and subscription_content='"+type_id+"'  "+
                " and account_id='"+userid+"' ";

        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query2);
            rs = stmt.executeQuery();

            while (rs.next()) {

                view_type_id=rs.getString("subscription_content");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ImageView img;
        img=(ImageView)findViewById(R.id.love);
        String uri = "@drawable/like";
        String uri2 = "@drawable/like_red";
        int imageResource ;
        if(view_type_id.equals("")){
            imageResource = getResources().getIdentifier(uri, null, getPackageName());
        }else{
            imageResource = getResources().getIdentifier(uri2, null, getPackageName());
        }
        Drawable image = getResources().getDrawable(imageResource);
        img.setImageDrawable(image);



        //-------------按下愛心訂閱-------------------------------------------
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

                String query;
                if(type_id.equals(view_type_id)){
                    query = "delete from subscription  " +
                            "where subscription_type='2' and subscription_content='"+type_id+"'";
                }else{
                    query = "insert into subscription ( account_id,subscription_type,subscription_content) values('"+userid+"','2','"+type_id+"')";
                }

                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();

                }catch (SQLException e) {
                    e.printStackTrace();
                }

                Bundle bundle =getIntent().getExtras(); //抓前一頁變數
                //final String  doing_id=bundle.getString("doing_id"); //活動id

                Intent intent = new Intent();
                intent.setClass(deatial.this,deatial.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("doing_id",doing_id2);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });



        //----我要參加---------------------------------------


        Button btnchooseimage=(Button)findViewById(R.id.btnchooseimage);
        btnchooseimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

                //Bundle bundle =getIntent().getExtras(); //抓前一頁變數
                // String  doing_id=bundle.getString("doing_id"); //活動id


                String query = "insert into doing_detail (doing_id, account_id) values('"+doing_id+"','"+userid+"')";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();

                }catch (SQLException e) {
                    e.printStackTrace();
                }


                //判斷需不需要收費
                if (money2.equals("0")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(deatial.this);
                    dialog.setTitle("參加成功"); //設定dialog 的title顯示內容
                    dialog.setMessage("請在指定時間參加活動！"); //設定dialog 的內容
                    //dialog.setIcon(android.R.drawable.ic_dialog_alert);//設定dialog 的ICON
                    dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                    dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(deatial.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }else{

                    String query3 = "insert into money (money_doing_id, money_account_id,money_sum) values('"+doing_id+"','"+userid+"','"+money2+"')";
                    try {
                        connect = CONN(un, passwords, db, ip);
                        stmt = connect.prepareStatement(query3);
                        rs = stmt.executeQuery();

                    }catch (SQLException e) {
                        e.printStackTrace();
                    }

                    AlertDialog.Builder dialog = new AlertDialog.Builder(deatial.this);
                    dialog.setTitle("付款通知"); //設定dialog 的title顯示內容
                    dialog.setMessage("需付款活動，將導向付款畫面"); //設定dialog 的內容
                    //dialog.setIcon(android.R.drawable.ic_dialog_alert);//設定dialog 的ICON
                    dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String name = doing_name.getText().toString(); //活動名稱
                            String value = money2; //金額
                            //跳頁
                            Intent intent = new Intent();
                            intent.setClass(deatial.this,Main2Activity.class);
                            final Bundle bundle = new Bundle();
                            bundle.putString("name",name);
                            bundle.putString("value",value);
                            intent.putExtras(bundle);
                            startActivity(intent);


                            new MakeNetworkCall().execute("http://fatfat-ting.ics-expo.org/index/Android/index" +
                                    "?get=4", "Get");
                        }
                    });
                    dialog.show();

                }

            }
        });

        //----我要留言---------------------------------------

        Button btnmessage=(Button)findViewById(R.id.btnmessage);
        btnmessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(deatial.this,message.class);
                Bundle bundle=new Bundle();
                bundle.putString("doing_id",doing_id2);
                intent.putExtras(bundle);
                startActivity(intent);



            }
        });



        //---------line分享---------------------------------------

        ImageButton imageButton5=(ImageButton)findViewById(R.id.imageButton5);
        imageButton5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



            }
        });



        //---------加好友---------------------------------------

        TextView addfriend=(TextView)findViewById(R.id.addfriend);
        addfriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
                String userid = sharedPreferences.getString("id" , "0"); //抓SharedPreferences內Name值

                String query = " insert into friend (friend_account, account_id) values('"+account_id+"','"+userid+"')";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();

                }catch (SQLException e) {
                    e.printStackTrace();
                }



                AlertDialog.Builder dialog = new AlertDialog.Builder(deatial.this);
                dialog.setTitle("成功加好友"); //設定dialog 的title顯示內容
                dialog.setMessage("已成為好友！"); //設定dialog 的內容
                //dialog.setIcon(android.R.drawable.ic_dialog_alert);//設定dialog 的ICON
                dialog.setCancelable(false); //關閉 Android 系統的主要功能鍵(menu,home等...)
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent();
//                        intent.setClass(deatial.this,MainActivity.class);
//                        startActivity(intent);
                    }
                });
                dialog.show();






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


    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getLatLngByAddr();
    }

    private void getLatLngByAddr() {
        try {
            Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE); // 地區:台灣
            List<Address> addresses = gc.getFromLocationName(et_address.trim(), 1);

            if (addresses != null && !addresses.isEmpty()) {
                double latitude = ((Address) addresses.get(0)).getLatitude();
                double longitude = ((Address) addresses.get(0)).getLongitude();
                String addrline = ((Address) addresses.get(0))
                        .getAddressLine(0);

                if (addrline != null) {
                    START = new LatLng(latitude, longitude);
                    setMapMarker(et_address.trim());
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private void setMapMarker(String addrStr) {
        if ( map != null) {
            map.clear();
            Marker note1 = map.addMarker( new MarkerOptions().position(START).title(addrStr));
        }

        // 設定中心點
        CameraUpdate center = CameraUpdateFactory.newLatLng(START);
        map.moveCamera(center);
        map.animateCamera(CameraUpdateFactory. zoomTo(15), 1000, null );

        // 也可以用這個方法取代上面設定中心點
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(START, 15));
        //

    }


    //http
    InputStream ByGetMethod(String ServerURL) {

        InputStream DataInputStream = null;
        try {

            URL url = new URL(ServerURL);
            HttpURLConnection cc = (HttpURLConnection)
                    url.openConnection();
            //set timeout for reading InputStream
            cc.setReadTimeout(5000);
            // set timeout for connection
            cc.setConnectTimeout(5000);
            //set HTTP method to GET
            cc.setRequestMethod("GET");
            //set it to true as we are connecting for input
            cc.setDoInput(true);

            //reading HTTP response code
            int response = cc.getResponseCode();

            //if response code is 200 / OK then read Inputstream
            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in GetData", e);
        }
        return DataInputStream;

    }

    InputStream ByPostMethod(String ServerURL) {

        InputStream DataInputStream = null;
        try {

            //Post parameters
            PostParam = "first_name=android&amp;last_name=pala";

            //Preparing
            URL url = new URL(ServerURL);

            HttpURLConnection cc = (HttpURLConnection)
                    url.openConnection();
            //set timeout for reading InputStream
            cc.setReadTimeout(5000);
            // set timeout for connection
            cc.setConnectTimeout(5000);
            //set HTTP method to POST
            cc.setRequestMethod("POST");
            //set it to true as we are connecting for input
            cc.setDoInput(true);
            //opens the communication link
            cc.connect();

            //Writing data (bytes) to the data output stream
            DataOutputStream dos = new DataOutputStream(cc.getOutputStream());
            dos.writeBytes(PostParam);
            //flushes data output stream.
            dos.flush();
            dos.close();

            //Getting HTTP response code
            int response = cc.getResponseCode();

            //if response code is 200 / OK then read Inputstream
            //HttpURLConnection.HTTP_OK is equal to 200
            if(response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in GetData", e);
        }
        return DataInputStream;

    }

    String ConvertStreamToString(InputStream stream) {

        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder response = new StringBuilder();

        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
        } finally {

            try {
                stream.close();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error in ConvertStreamToString", e);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
            }
        }
        return response.toString();


    }

    public void DisplayMessage(String a) {

        //TxtResult = (TextView) findViewById(R.id.response);
        //TxtResult.setText(a);
    }

    private class MakeNetworkCall extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DisplayMessage("Please Wait ...");
        }

        @Override
        protected String doInBackground(String... arg) {

            InputStream is = null;
            String URL = arg[0];
            Log.d(LOG_TAG, "URL: " + URL);
            String res = "";


            if (arg[1].equals("Post")) {

                is = ByPostMethod(URL);

            } else {

                is = ByGetMethod(URL);
            }
            if (is != null) {
                res = ConvertStreamToString(is);
            } else {
                res = "Something went wrong";
            }
            return res;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            DisplayMessage(result);
            Log.d(LOG_TAG, "Result: " + result);
        }
    }


}
