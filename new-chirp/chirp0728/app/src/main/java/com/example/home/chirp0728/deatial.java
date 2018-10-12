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
    private LatLng START = null; // �ҩl�y��
    private TextView et_address2;
    String et_address;;
    String PostParam;

    //------�s�u--------------
    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;

    String type_id=""; //���Oid
    String view_type_id="";  //�O�_�q�\���Oid

    String doing_id2=""; //����id
    String account_id=""; // �D��Hid

    String money2;

    String updoing="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatial);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mgr = (LocationManager) getSystemService(LOCATION_SERVICE );
        //getLatLngByAddr();

        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";


        Bundle bundle =getIntent().getExtras(); //��e�@���ܼ�
        final String  doing_id=bundle.getString("doing_id"); //����id



        //----�򥻸��-------
        final TextView doing_name=(TextView)findViewById(R.id.doing_name); //���ʦW��
        TextView doing_type=(TextView)findViewById(R.id.doing_type); //�������O
        TextView address=(TextView)findViewById(R.id.addresss); //���ʦa�}


        TextView time=(TextView)findViewById(R.id.time); //���ʮɶ�
        TextView up=(TextView)findViewById(R.id.up); //���ʤH��
        TextView money=(TextView)findViewById(R.id.money); //���ʶO��
        TextView textView9=(TextView)findViewById(R.id.textView9); //���ʸԲӤ��e
        TextView name=(TextView)findViewById(R.id.name); //�D��H�ʺ�
        TextView look=(TextView)findViewById(R.id.look); //�d�ݥD��H�L�h���ʬ���


        String query="";
        if (doing_id.equals("sensor")){  //�n�@�n
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
                doing_name.setText(rs.getString("doing_name")); //���ʦW��
                doing_type.setText(rs.getString("type_name")); //�������O
                address.setText(rs.getString("doing_place")); //���ʦa�}
                time.setText("���ʮɶ��G"+(rs.getString("doing_start")).substring(0,16)+"~"+(rs.getString("doing_end")).substring(0,16)); //���ʮɶ�

                String peo=rs.getString("totalpeople");
                String doup=peo.substring(0,1);
                String pee=peo.substring(peo.indexOf('-')+1);

                if(doup.equals("d")){
                    up.setText("�H�ƤU���G"+pee+"�H"); //���ʤH��
                }
                else{
                    up.setText("�H�ƤW���G"+pee+"�H"); //���ʤH��
                }


                money2="0";
                if((rs.getString("pay_money").equals("0"))==false){
                    money2=rs.getString("pay_money"); //���ʶO��
                }





                money.setText(money2); //���ʶO��
                textView9.setText(rs.getString("doing_content")); //���ʸԲӤ��e
                name.setText(rs.getString("nickname")); //�D��H�ʺ�
                account_id=rs.getString("account_id"); //�D��Hid
                type_id=rs.getString("type_id"); //���Oid


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        et_address = address.getText().toString();
        //-------�O�_�q�\-----------------------------------

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //�إ�SharedPreferences
        String userid = sharedPreferences.getString("id" , "0"); //��SharedPreferences��Name��

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



        //-------------���U�R�߭q�\-------------------------------------------
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //�إ�SharedPreferences
                String userid = sharedPreferences.getString("id" , "0"); //��SharedPreferences��Name��

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

                Bundle bundle =getIntent().getExtras(); //��e�@���ܼ�
                //final String  doing_id=bundle.getString("doing_id"); //����id

                Intent intent = new Intent();
                intent.setClass(deatial.this,deatial.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("doing_id",doing_id2);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        final Button btnchooseimage=(Button)findViewById(R.id.btnchooseimage);




        String query1 = "select  * from doing_view  " +
                "where doing_id='"+doing_id+"'";




        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query1);
            rs = stmt.executeQuery();


            while (rs.next()) {
                if(rs.getString("account_id").equals(userid)){
                    btnchooseimage.setText("�ק�");
                    updoing="1";
                }
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }


        //----�ڭn�ѥ[---------------------------------------

        btnchooseimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //�إ�SharedPreferences
                String userid = sharedPreferences.getString("id" , "0"); //��SharedPreferences��Name��

                //Bundle bundle =getIntent().getExtras(); //��e�@���ܼ�
                // String  doing_id=bundle.getString("doing_id"); //����id


                if(updoing.equals("1")){ //�קﬡ��
                    //Toast.makeText(deatial.this, "eeee"+doing_id, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(deatial.this,update_doing.class);
                    final Bundle bundle = new Bundle();
                    bundle.putString("doing_id",doing_id);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
                else {


                    String query = "insert into doing_detail (doing_id, account_id) values('" + doing_id + "','" + userid + "')";
                    try {
                        connect = CONN(un, passwords, db, ip);
                        stmt = connect.prepareStatement(query);
                        rs = stmt.executeQuery();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    //�P�_�ݤ��ݭn���O
                    if (money2.equals("0")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(deatial.this);
                        dialog.setTitle("�ѥ[���\"); //�]�wdialog ��title��ܤ��e
                        dialog.setMessage("�Цb���w�ɶ��ѥ[���ʡI"); //�]�wdialog �����e
                        //dialog.setIcon(android.R.drawable.ic_dialog_alert);//�]�wdialog ��ICON
                        dialog.setCancelable(false); //���� Android �t�Ϊ��D�n�\����(menu,home��...)
                        dialog.setPositiveButton("�O", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(deatial.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    } else {

                        String query3 = "insert into money (money_doing_id, money_account_id,money_sum,money_status) values('" + doing_id + "','" + userid + "','" + money2 + "','0')";
                        try {
                            connect = CONN(un, passwords, db, ip);
                            stmt = connect.prepareStatement(query3);
                            rs = stmt.executeQuery();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        AlertDialog.Builder dialog = new AlertDialog.Builder(deatial.this);
                        dialog.setTitle("�I�ڳq��"); //�]�wdialog ��title��ܤ��e
                        dialog.setMessage("�ݥI�ڬ��ʡA�N�ɦV�I�ڵe��"); //�]�wdialog �����e
                        //dialog.setIcon(android.R.drawable.ic_dialog_alert);//�]�wdialog ��ICON
                        dialog.setCancelable(false); //���� Android �t�Ϊ��D�n�\����(menu,home��...)
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String name = doing_name.getText().toString(); //���ʦW��
                                String value = money2; //���B
                                //����
                                Intent intent = new Intent();
                                intent.setClass(deatial.this, Main2Activity.class);
                                final Bundle bundle = new Bundle();
                                bundle.putString("name", name);
                                bundle.putString("value", value);
                                intent.putExtras(bundle);
                                startActivity(intent);


                                //new MakeNetworkCall().execute("http://fatfat-ting.ics-expo.org/index/Android/index" +
                                        //"?get=4", "Get");
                            }
                        });
                        dialog.show();

                    }
                }

            }
        });

        //----�ڭn�d��---------------------------------------

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



        //---------line����---------------------------------------

        ImageButton imageButton5=(ImageButton)findViewById(R.id.imageButton5);
        imageButton5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shareTo("chirp","���ɤ��e","test");


            }
        });


        //---------FB����---------------------------------------

        ImageButton imageButton4=(ImageButton)findViewById(R.id.imageButton4);
        imageButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shareTo("chirp","���ɤ��e","test");


            }
        });


        //---------�d�ݹL�h����--------------------------------------
        TextView lookdoinged=(TextView)findViewById(R.id.look);
        lookdoinged.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(deatial.this, "eeee"+account_id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(deatial.this,look_doinged.class);
                Bundle bundle=new Bundle();
                bundle.putString("doing_account_id",account_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });





        //---------�[�n��---------------------------------------
        TextView addfriend=(TextView)findViewById(R.id.addfriend);


        //�O�_�w���n��
        String queryyy="select count(*) as a from friend " +
                " where friend_account='"+userid+"' and account_id='"+account_id+"'";
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(queryyy);
            rs = stmt.executeQuery();

            while (rs.next()) {
                if(rs.getString("a").equals("1")){
                    addfriend.setText("�w���n��");
                    addfriend.setEnabled(false);
                }
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }






        addfriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //�إ�SharedPreferences
                String userid = sharedPreferences.getString("id" , "0"); //��SharedPreferences��Name��

                String query = " insert into friend (friend_account, account_id) values('"+account_id+"','"+userid+"')";
                String query2 = " insert into friend (account_id, friend_account) values('"+account_id+"','"+userid+"')";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();

                }catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query2);
                    rs = stmt.executeQuery();

                }catch (SQLException e) {
                    e.printStackTrace();
                }


                AlertDialog.Builder dialog = new AlertDialog.Builder(deatial.this);
                dialog.setTitle("���\�[�n��"); //�]�wdialog ��title��ܤ��e
                dialog.setMessage("�w�����n�͡I"); //�]�wdialog �����e
                //dialog.setIcon(android.R.drawable.ic_dialog_alert);//�]�wdialog ��ICON
                dialog.setCancelable(false); //���� Android �t�Ϊ��D�n�\����(menu,home��...)
                dialog.setPositiveButton("�O", new DialogInterface.OnClickListener() {
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

    //--line����----
    private void shareTo(String subject, String body, String chooserTitle) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "���_��");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "���e");
        startActivity(Intent.createChooser(sharingIntent, chooserTitle));
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getLatLngByAddr();
    }

    private void getLatLngByAddr() {
        try {
            Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE); // �a��:�x�W
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

        // �]�w�����I
        CameraUpdate center = CameraUpdateFactory.newLatLng(START);
        map.moveCamera(center);
        map.animateCamera(CameraUpdateFactory. zoomTo(15), 1000, null );

        // �]�i�H�γo�Ӥ�k���N�W���]�w�����I
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
