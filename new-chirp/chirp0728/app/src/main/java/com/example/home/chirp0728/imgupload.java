package com.example.home.chirp0728;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.util.DisplayMetrics;
import android.net.Uri;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.content.ContentValues;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.widget.EditText;
import org.w3c.dom.Text;
import android.util.Log;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.StrictMode;
import android.util.AndroidRuntimeException;


import android.widget.ProgressBar;

public class imgupload extends AppCompatActivity {
    //宣告相片相關
    public static final int requestcode = 1;
    ImageView img;
    Button btnupload, btnchooseimage;
    EditText edtname;
    byte[] byteArray;

    String encodedImage = null;
    TextView txtmsg,t1;

    ProgressBar pg;

    ResultSet rs;
    Connection con,connect;
    String un;
    String password;
    String db;
    String ip;
    String user,pass,name,phone,email,sex,address,nickname;


    //db連線變數
    String ip2,db2,un2,passwords2;
    PreparedStatement stmt;

    @SuppressLint("NewApi")
    private Connection ConnectionHelper(String user, String password, String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";" + "databaseName=" + database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return connection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgupload);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("設定大頭貼");

        //取得上一頁value
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        sex = bundle.getString("sex");
        address = bundle.getString("address");
        nickname = bundle.getString("nickname");

        //t1 = (TextView)findViewById(R.id.t1);
        //t1.setText(sex);

        img = (ImageView) findViewById(R.id.imageview);
        btnupload = (Button) findViewById(R.id.btnupload);
        btnchooseimage = (Button) findViewById(R.id.btnchooseimage);
        txtmsg = (TextView) findViewById(R.id.txtmsg);

        ip = "140.131.114.241";
        un = "chirp2018";
        password = "chirp+123";
        db = "107-chirp";
        con = ConnectionHelper(un, password, db, ip);

        btnchooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UploadtoDB();
            }
        });
    }

    public void UploadtoDB() {

        if (encodedImage == null){
            Toast.makeText(imgupload.this, "未上傳頭像，請選擇照片", Toast.LENGTH_SHORT).show();

        }else{
            String msg = "unknown";
            try {
                con = ConnectionHelper(un, password, db, ip);
                String commands = "INSERT INTO account(account_id,account_pwd,email,username,sex,birthday,location,phonenumber,updatetime,img,nickname) VALUES ('"+user+"','"+pass+"','"+email+"','"+name+"','"+sex+"','1997-07-17','"+address+"','"+phone+"','1997-07-17','"+encodedImage+"','"+nickname+"')";
                // encodedImage which is the Base64 String
                PreparedStatement preStmt = con.prepareStatement(commands);
                preStmt.executeUpdate();
                msg = "Inserted Successfully";

            } catch (SQLException ex) {
                msg = ex.getMessage().toString();
                Log.d("hitesh", msg);

            } catch (IOError ex) {
                // TODO: handle exception
                msg = ex.getMessage().toString();
                Log.d("hitesh", msg);
            } catch (AndroidRuntimeException ex) {
                msg = ex.getMessage().toString();
                Log.d("hitesh", msg);

            } catch (NullPointerException ex) {
                msg = ex.getMessage().toString();
                Log.d("hitesh", msg);
            }

            catch (Exception ex) {
                msg = ex.getMessage().toString();
                Log.d("hitesh", msg);
            }

            ip2 = "140.131.114.241";
            un2 = "chirp2018";
            passwords2 = "chirp+123";
            db2 = "107-chirp";
            String query = "INSERT INTO bank(bank_user,bank_code,bank_account)VALUES('"+user+"',null,null)";
            try {
                connect = ConnectionHelper(un2, passwords2, db2, ip2);
                stmt = connect.prepareStatement(query);
                rs = stmt.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            txtmsg.setText(msg);
            Toast.makeText(imgupload.this, "註冊成功，請重新登入", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setClass(imgupload.this,login.class);
            startActivity(intent);

        }


    }

    public void ChooseImage() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);

        } else {
            Toast.makeText(imgupload.this, "No activity found to perform this task", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(imgupload.this, "Conversion Done", Toast.LENGTH_SHORT).show();
            }
        } else {
            //txtmsg.setText("There's an error if this code doesn't work, thats all I know");

        }
    }

}
