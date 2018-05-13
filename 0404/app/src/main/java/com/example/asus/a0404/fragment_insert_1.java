package com.example.asus.a0404;

import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class fragment_insert_1 extends AppCompatActivity {

    String dname,dplace,dtype,dtotpeo,ddates,dtimes,ddatee,dtimee,dcontent,dtoll;
    EditText dsdates,dsdatee,dstimes,dstimee,t33;
    TextView t11,t12;
    Button btnclick;
    String datenow ;

    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_insert_1);

        SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
        String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值


        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";
        connect = CONN(un, passwords, db, ip);

        Bundle bundle = getIntent().getExtras();
        dname = bundle.getString("dname" );
        dplace = bundle.getString("dplace" );
        dtype = bundle.getString("dtype" );
        dtotpeo = bundle.getString("dtotpeo" );
        ddates = bundle.getString("ddates" );
        dtimes = bundle.getString("dtimes" );
        ddatee = bundle.getString("ddatee" );
        dtimee = bundle.getString("dtimee" );
        dcontent = bundle.getString("dcontent" );
        dtoll = bundle.getString("dtoll" );

        btnclick = (Button)findViewById(R.id.button);

//        Calendar c = Calendar.getInstance();
//        datenow=c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH) +  c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE);

        t11=(TextView)findViewById(R.id.t11);
        //t11.setText(dname+"," + dplace+","+ dtype+"," + dtotpeo+"," + ddates+"," + dtimes+"," + ddatee+"," + dtimee+"," +dcontent+"," +dtoll);
        t11.setText(dtotpeo);

        btnclick.setOnClickListener(btnc);

    }

    private View.OnClickListener btnc = new View.OnClickListener(){
        @Override
        public void onClick(View v){

            t12=(TextView)findViewById(R.id.t12);

            t33 = (EditText)findViewById(R.id.t33);

            dsdates = (EditText)findViewById(R.id.dsdates);
            dstimes = (EditText)findViewById(R.id.dstimes);
            dsdatee = (EditText)findViewById(R.id.dsdatee);
            dstimee = (EditText)findViewById(R.id.dstimee);

            SharedPreferences sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE); //建立SharedPreferences
            String userid = sharedPreferences.getString("Name" , "0"); //抓SharedPreferences內Name值

            String query = "insert into doing(doing_name,account_id, type_id, doing_start, doing_end, totalpeople, doing_place, doing_content, doing_date, sign_start, sign_end, pay_money) " +
                    "values('"+dname+"','"+userid+"','1','"+ddates+' '+dtimes+"','"+ddatee+' '+dtimee+"','"+dtotpeo+"','"+dplace+"','"+dcontent+"',getdate(),'"+dsdates.getText()+' ' +dstimes.getText()+"','"+dsdatee.getText()+' ' +dstimee.getText()+"','"+dtoll+"')";





            t12.setText(query);

            try {
                connect = CONN(un, passwords, db, ip);
                stmt = connect.prepareStatement(query);
                rs = stmt.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }

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

