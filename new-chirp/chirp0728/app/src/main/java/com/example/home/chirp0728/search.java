package com.example.home.chirp0728;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class search extends AppCompatActivity {


    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;
    String str;

    EditText datetime,keyword,location;
    Spinner dtype;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";



        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("搜尋活動");


        //---------------時間--------------------------------------


        datetime= (EditText)findViewById(R.id.datetime); //時間
        datetime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dpds();
            }
        });




        //------------類別下拉式-------------------------

       dtype = (Spinner)findViewById(R.id.dtype);

        String query = "  select * from doingtype ";

        List<CItem> lst = new ArrayList<CItem>();
        CItem item;


        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            item = new CItem("", "請選擇");
            lst.add(item);

            while (rs.next()) {
                String type_id =rs.getString("type_id");
                String type_name =rs.getString("type_name");
                item = new CItem(type_id, type_name);
                lst.add(item);
                ArrayAdapter<CItem> myaAdapter = new ArrayAdapter<CItem>(search.this,  android.R.layout.simple_list_item_1, lst);
                dtype.setAdapter(myaAdapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        dtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                str = ((CItem) dtype.getSelectedItem()).GetID().toString();
//                Toast.makeText(like_type.this,
//                        "键:" + like_type.getSelectedItem().toString() + "、" + ((CItem) like_type.getSelectedItem()).GetID() +
//                                "，值:" + ((CItem) like_type.getSelectedItem()).GetValue(),
//                        Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



        //----------------搜尋鈕 -----------------------

        Button button3=(Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                keyword = (EditText)findViewById(R.id.keyword); // 關鍵字
                datetime= (EditText)findViewById(R.id.datetime); //時間
                location= (EditText)findViewById(R.id.location); //地點
                dtype= (Spinner)findViewById(R.id.dtype); //類別

                Intent intent = new Intent();
                intent.setClass(search.this,MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("search","search");
                bundle.putString("keyword",keyword.getText().toString()); // 關鍵字
                bundle.putString("datetime",datetime.getText().toString()); //時間
                bundle.putString("location",location.getText().toString()); //地點
                bundle.putString("dtype",str); //類別
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });




    }


    //時間
    private void dpds() {
        Calendar c = Calendar.getInstance();

        new DatePickerDialog(search.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                // TODO Auto-generated method stub
                if(monthOfYear<10 && dayOfMonth <10){
                    datetime.setText(year+"0"+(monthOfYear+1)+"0"+dayOfMonth);
                }
                else if(monthOfYear<10){
                    datetime.setText(year+"0"+(monthOfYear+1)+""+dayOfMonth);
                }
                else if(dayOfMonth<10){
                    datetime.setText(year+""+(monthOfYear+1)+"0"+dayOfMonth);
                }
                else{
                    datetime.setText(year+""+(monthOfYear+1)+""+dayOfMonth);
                }

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }


    //類別
    public class CItem {
        private String ID;
        private String Value = "";

        public CItem() {
            ID = "";
            Value = "";
        }

        public CItem(String _ID, String _Value) {
            ID = _ID;
            Value = _Value;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return Value;
        }

        public String GetID() {
            return ID;
        }

        public String GetValue() {
            return Value;
        }
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


}
