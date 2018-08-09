package com.example.home.chirp0728;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOError;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class login extends AppCompatActivity {

    int sum = 0;
    int sum_fb = 0;
    String total;
    String total_fb;
    String accountid;
    String accountid_Fb;


    Connection con;
    String ip, db, un, passwords;
    String ip2, db2, un2, passwords2;
    Connection connect,connect2;
    PreparedStatement stmt,stmt2;
    ResultSet rs,rs2;

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


    private CallbackManager callbackManager;
    private TextView info;
    private Button loginButton;


    private GoogleApiClient mGoogleSignInClient;
    private SignInButton mGoogleBtn;
    public static  final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private FirebaseAuth .AuthStateListener mAuthListener;
    private static final String TAG = "MAIN_ACTIVITY";
    private AccessToken accessToken;
    private String fbemail,fbname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //建立FacebookManger
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);



        //連線
        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";
        //connect = CONN(un, passwords, db, ip);

        //google
        // Configure Google Sign In
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    //startActivity(new Intent(login.this,MainActivity.class));
                }
            }
        };
        mGoogleBtn = (SignInButton)findViewById(R.id.signInButton);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(login.this,"OK",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



        //fb登入

        info = (TextView)findViewById(R.id.info);
        loginButton = (Button)findViewById(R.id.fb_login);

        loginButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(login.this, Arrays.asList("public_profile", "user_friends", "email"));
            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            //當RESPONSE回來的時候
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                //讀出姓名、ID、網頁連結
                                Log.d("FB" , "complete");
                                Log.d("FB" , object.optString("name"));
                                Log.d("FB" , object.optString("link"));
                                Log.d("FB" , object.optString("id"));
                                Log.d("FB" , object.optString("email"));
                                try {
                                    fbemail = object.getString("email");
                                    fbname = object.getString("name");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(login.this, fbname, Toast.LENGTH_SHORT).show();

                                //檢查第一次登入
                                ip2 = "140.131.114.241";
                                un2 = "chirp2018";
                                passwords2 = "chirp+123";
                                db2 = "107-chirp";
                                String query2 = "SELECT count(*) as total FROM account Where account_id = '"+fbemail+"' ";
                                try {
                                    connect2 = CONN(un2, passwords2, db2, ip2);
                                    stmt2 = connect2.prepareStatement(query2);
                                    rs2 = stmt2.executeQuery();
                                    ArrayList<String> data3 = new ArrayList<String>();

                                    while (rs2.next()) {
                                        total_fb = rs2.getString("total");
                                        data3.add(total_fb);
                                    }

                                    if (total_fb.equals("0")){
                                        Toast.makeText(login.this, "第一次", Toast.LENGTH_SHORT).show();
                                        SharedPreferences settings = getSharedPreferences("User", MODE_PRIVATE);
                                        // Writing data to SharedPreferences
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("Name", fbname);
                                        editor.commit();
                                        Intent intent = new Intent();
                                        intent.setClass(login.this,MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(login.this, "不是第一次使用facebook登入", Toast.LENGTH_SHORT).show();
                                        SharedPreferences settings = getSharedPreferences("User", MODE_PRIVATE);
                                        // Writing data to SharedPreferences
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("Name", fbname);
                                        editor.commit();
                                        Intent intent = new Intent();
                                        intent.setClass(login.this,MainActivity.class);
                                        startActivity(intent);
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                //檢查第一次登入

                            }});
                //包入你想要得到的資料，送出 request
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,link,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
                //Toast.makeText(login.this, fbemail, Toast.LENGTH_SHORT).show();
        }

            @Override
            public void onCancel() {
                info.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });


        //一般登入檢查
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText user_1 = (EditText)findViewById(R.id.user);
                String userid = user_1.getText().toString();
                EditText psw_1 = (EditText)findViewById(R.id.psw);
                String psw_2 = psw_1.getText().toString();

                //建立SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE);
                //連線
                ip = "140.131.114.241";
                un = "chirp2018";
                passwords = "chirp+123";
                db = "107-chirp";
                String query = "SELECT account_id,count(*) as total FROM account Where account_id = '"+userid+"' and account_pwd = '"+psw_2+"' group by account_id";
                try {
                    connect = CONN(un, passwords, db, ip);
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();
                    ArrayList<String> data = new ArrayList<String>();
                    ArrayList<String> data2 = new ArrayList<String>();
                    while (rs.next()) {

                        total = rs.getString("total");
                        accountid = rs.getString("account_id");
                        data.add(total);
                        data2.add(accountid);
                        sum = Integer.parseInt(total);
                    }

                    if(sum == 1){

                        Toast.makeText(login.this, "登入成功", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setClass(login.this,MainActivity.class);
                        startActivity(intent);

                        //存使用者id進sharedPreferences
                        sharedPreferences.edit().putString("Name", accountid).apply();

                    }else{
                        Toast.makeText(login.this, "帳號密碼有誤，請重新輸入", Toast.LENGTH_SHORT).show();

                    }

                    //Toast.makeText(login.this, total, Toast.LENGTH_SHORT).show();



                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

        //切換到註冊
        Button sign = (Button)findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login.this,sign.class);
                startActivity(intent);
            }
        });
    }


    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(intent,RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Intent intent = new Intent();
                intent.setClass(login.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(login.this, "登入成功", Toast.LENGTH_SHORT).show();
            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /* @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }*/

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                        // ...
                    }
                });
    }
}
