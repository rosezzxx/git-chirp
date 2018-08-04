package com.example.home.chirp0728;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;


public class calendar extends AppCompatActivity {

    //資料庫連線
    Connection con;
    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;
    String daytotal;
    int sum;

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
    //資料庫連線結束


    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("查看行事曆");
        //actionBar.setDisplayHomeAsUpEnabled(false);
        final Date dt=new Date();



        final TextView month = (TextView)findViewById(R.id.textView15);
        month.setText(dateFormatForMonth.format(dt));

        compactCalendar =  (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);




        //資料庫部分
        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";
        connect = CONN(un, passwords, db, ip);
        String query = "SELECT doing_start,doing_end,DATEDIFF(DAY,doing_start,doing_end) as daytotal FROM doing  join doing_detail on doing_detail.doing_id =  doing.doing_id where doing_detail.account_id = 'rosezzxx'";

        try {

            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            ArrayList<Date> datevalue = new ArrayList<Date>();

            while (rs.next()) {
                Context context = getApplicationContext();
                //Toast.makeText(context,"1234",Toast.LENGTH_LONG).show();
                sum = rs.getInt("daytotal");
                Date end = rs.getDate("doing_end");

                data.add(daytotal);
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;


                if (sum == 0){
                    Event ev1 = new Event(Color.RED,end.getTime(),"test");
                    compactCalendar.addEvent(ev1);
                }else{
                    for(int i=0;i<=sum;i++){
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(end);
                        calendar.add(Calendar.DATE, -i);
                        date=calendar.getTime();
                        datevalue.add(date);
                    }
                }
            }
            HashSet<Date> set = new HashSet<Date>(datevalue);
            ArrayList<Date> removeDuplicateDate = new ArrayList<Date>(set);
            Context context1 = getApplicationContext();
            int size = removeDuplicateDate.size();
            Event ev1;
            for (int i=0;i<size;i++){
                ev1 = new Event(Color.RED,removeDuplicateDate.get(i).getTime(),"test");
                compactCalendar.addEvent(ev1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        //資料庫部分
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date date) {
                Context context = getApplicationContext();
                Toast.makeText(context,"" + date,Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.setClass(calendar.this,MainActivity.class);

                final Bundle bundle = new Bundle();

                String strTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
                bundle.putString("date",strTime);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //actionBar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
                month.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });
    }



}

