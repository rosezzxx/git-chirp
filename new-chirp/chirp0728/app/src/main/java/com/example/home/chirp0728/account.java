package com.example.home.chirp0728;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class account extends AppCompatActivity {

    Connection con;
    String ip, db, un, passwords;
    String ip2, db2, un2, passwords2;
    Connection connect,connect2;
    PreparedStatement stmt,stmt2;
    ResultSet rs,rs2;

    String sex,location,phonenumber,nickname,image;

    byte[] byteArray;
    String encodedImage = null;
    ImageButton img;


    //db連線
    @SuppressLint("NewApi")
    private Connection CONN(String _user, String _pass, String _DB, String _server) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("修改基本資料");

        img = (ImageButton)findViewById(R.id.imageButton);
        final EditText nicknamexml = (EditText)findViewById(R.id.nicknamexml);
        final EditText phonexml = (EditText)findViewById(R.id.phonexml);
        final Spinner spnadxml = (Spinner)findViewById(R.id.spnadxml);
        final Spinner radsexxml = (Spinner)findViewById(R.id.radsexxml);
        final Button update = (Button)findViewById(R.id.update);

        //平台登入修改個人資料
        //取得SharedPreferences的資料(帳號.登入方式)
        SharedPreferences settings = getSharedPreferences("User", MODE_PRIVATE);
        final String namevalue = settings.getString("id", "");
        String way = settings.getString("way", "");
        String name = settings.getString("name", "");
        String userID = settings.getString("imgid", "");
        TextView username = (TextView)findViewById(R.id.username);
        username.setText(name);
        TextView way_2 = (TextView)findViewById(R.id.way);
        way_2.setText(way);

        //連線
        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";
        String query = "SELECT *  FROM account Where account_id = '"+namevalue+"'";
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            ArrayList<String> sex_data = new ArrayList<String>();
            ArrayList<String> location_data = new ArrayList<String>();
            ArrayList<String> phonenumber_data = new ArrayList<String>();
            ArrayList<String> nickname_data = new ArrayList<String>();
            ArrayList<String> img_data = new ArrayList<String>();
            while (rs.next()) {
                sex = rs.getString("sex");
                location = rs.getString("location");
                phonenumber = rs.getString("phonenumber");
                nickname = rs.getString("nickname");
                image = rs.getString("img");
                sex_data.add(sex);
                location_data.add(location);
                phonenumber_data.add(phonenumber);
                nickname_data.add(nickname);
                img_data.add(image);
            }

            if (nickname == ""){
                nicknamexml.setText("");
            }else{
                nicknamexml.setText(nickname);
            }

            if (phonenumber == ""){
                phonexml.setText("");
            }else{
                phonexml.setText(phonenumber);
            }


            //居住地
            /*ArrayAdapter myAdap = (ArrayAdapter) spnadxml.getAdapter();//cast to an ArrayAdapter
            int spinnerPosition = myAdap.getPosition(location);
            spnadxml.setSelection(spinnerPosition,true);*/

            //性別
            /*ArrayAdapter myAdap2 = (ArrayAdapter) radsexxml.getAdapter();//cast to an ArrayAdapter
            int spinnerPosition2 = myAdap.getPosition(sex);
            radsexxml.setSelection(spinnerPosition,true);*/

            //頭像

            if (image != null){
                encodedImage = image;
                String base64String = image;
                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img.setImageBitmap(decodedByte);

            }else if (way.equals("facebook")){
                Toast.makeText(account.this, "第一次登入抓取fb大頭照", Toast.LENGTH_SHORT).show();
                try {
                    URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
                    Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    img.setImageBitmap(bitmap);//放入imagebutton
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{

                
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //當選取區域
        spnadxml.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //當選取性別
        radsexxml.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //修改大頭照
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });

        //按下修改
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取值
                final String nickname_2 = nicknamexml.getText().toString();
                final String phonenumber_2 = phonexml.getText().toString();

                if (nickname_2.matches("") || phonenumber_2.matches("") || sex.matches("")|| location.matches("") ){
                    //Toast toast = Toast.makeText(sign.this, "欄位填寫未完成，請確實填寫", Toast.LENGTH_LONG);
                    Toast.makeText(account.this, "欄位填寫未完成，請確實填寫", Toast.LENGTH_SHORT).show();
                }else{
                    ip2 = "140.131.114.241";
                    un2 = "chirp2018";
                    passwords2 = "chirp+123";
                    db2 = "107-chirp";
                    String query2 = "UPDATE account SET sex = '"+sex+"',location = '"+location+"',phonenumber = '"+phonenumber_2+"',nickname = '"+nickname_2+"',img  = '"+encodedImage+"' WHERE account_id = '"+namevalue+"'";
                    try {
                        connect2 = CONN(un2, passwords2, db2, ip2);
                        stmt2 = connect2.prepareStatement(query2);
                        rs2 = stmt2.executeQuery();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(account.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(account.this,MainActivity.class);
                    startActivity(intent);

                }

            }
        });

    }

    //修改頭像
    public void ChooseImage() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);

        } else {
            Toast.makeText(account.this, "No activity found to perform this task", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap originBitmap = null;
            Uri selectedImage = data.getData();
            //Toast.makeText(uploadimg.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
            //txtmsg.setText(selectedImage.toString());
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);

            } catch (FileNotFoundException e) {

                //txtmsg.setText(e.getMessage().toString());
            }
            if (originBitmap != null) {
                this.img.setImageBitmap(originBitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                originBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();

                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Toast.makeText(account.this, "Conversion Done", Toast.LENGTH_SHORT).show();
            }
        } else {
            //txtmsg.setText("There's an error if this code doesn't work, thats all I know");

        }
    }
}
