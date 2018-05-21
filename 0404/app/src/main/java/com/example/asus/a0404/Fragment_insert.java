package com.example.asus.a0404;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */




public class Fragment_insert extends Fragment {

    EditText dname,dplace,dtotpeo,ddates,dtimes,ddatee,dtimee,dcontent,dtoll;
    CheckBox uppeock,downpeock;
    Spinner dtype;
    String str,dtotpeogo;
    Button nextPageBtn;

    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insert,container,false);
        initListView(view);
        return  view;
        //return inflater.inflate(R.layout.fragment_view, container, false);
    }
    private void initListView(View view) {
        ip = "140.131.114.241";
        un = "chirp2018";
        passwords = "chirp+123";
        db = "107-chirp";

        dname = (EditText)view.findViewById(R.id.dname);
        dplace = (EditText)view.findViewById(R.id.dplace);
        dtype = (Spinner)view.findViewById(R.id.dtype);
        uppeock = (CheckBox)view.findViewById(R.id.uppeock);
        downpeock = (CheckBox)view.findViewById(R.id.downpeock);
        dtotpeo = (EditText)view.findViewById(R.id.dtotpeo);
        ddates = (EditText)view.findViewById(R.id.ddates);
        dtimes = (EditText)view.findViewById(R.id.dtimes);
        ddatee = (EditText)view.findViewById(R.id.ddatee);
        dtimee = (EditText)view.findViewById(R.id.dtimee);
        dcontent = (EditText)view.findViewById(R.id.dcontent);
        dtoll = (EditText)view.findViewById(R.id.dtoll);
        nextPageBtn = (Button)view.findViewById(R.id.button);
        nextPageBtn.setOnClickListener(btnc);
        ddates.setInputType(InputType.TYPE_NULL);
        dtimes.setInputType(InputType.TYPE_NULL);
        ddatee.setInputType(InputType.TYPE_NULL);
        dtimee.setInputType(InputType.TYPE_NULL);
        ddates.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    dpds();
                }
            }
        });
        ddates.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dpds();
            }
        });

        dtimes.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    tpds();
                }
            }
        });
        dtimes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tpds();
            }
        });

        ddatee.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    dpde();
                }
            }
        });
        ddatee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dpde();
            }
        });

        dtimee.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    tpde();
                }
            }
        });
        dtimee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tpde();
            }
        });


        connect = CONN(un, passwords, db, ip);
        String query = "select * from doingtype";

        List<CItem> lst = new ArrayList<CItem>();
        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {

                String type_id = rs.getString("type_id");
                String type_name = rs.getString("type_name");
                //Toast.makeText(getContext(),type_id, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(),type_name, Toast.LENGTH_SHORT).show();
                CItem item = new CItem(type_id, type_name);
                lst.add(item);



            }

            ArrayAdapter<CItem> myaAdapter = new ArrayAdapter<CItem>(getContext(),  android.R.layout.simple_list_item_1, lst);
            dtype.setAdapter(myaAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //拿到被选择项的值
                str = ((CItem) dtype.getSelectedItem()).GetID().toString();

//                Toast.makeText(getContext(),
//                        "键:" + dtype.getSelectedItem().toString() + "、" + ((CItem) dtype.getSelectedItem()).GetID() +
//                                "，值:" + ((CItem) dtype.getSelectedItem()).GetValue(),
//                        Toast.LENGTH_LONG).show();
//                Toast.makeText(getContext(),str, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    private View.OnClickListener btnc = new View.OnClickListener(){
        @Override
        public void onClick(View v){





            if( uppeock.isChecked() == true ){
                dtotpeogo = "up-" + dtotpeo.getText().toString();
            }
            else if( downpeock.isChecked() == true){
                dtotpeogo = "down-" + dtotpeo.getText().toString();
            }
            else{
                Toast.makeText(getContext(),"人數上下限未勾選", Toast.LENGTH_SHORT).show();
            }




            Intent intent = new Intent();
            intent.setClass(getContext() , fragment_insert_1.class);

            Bundle bundle = new Bundle();
            bundle.putString("dname",dname.getText().toString());
            bundle.putString("dplace",dplace.getText().toString());
            bundle.putString("dtype",str);
            bundle.putString("dtotpeo",dtotpeogo);
            bundle.putString("ddates",ddates.getText().toString());
            bundle.putString("dtimes",dtimes.getText().toString());
            bundle.putString("ddatee",ddatee.getText().toString());
            bundle.putString("dtimee",dtimee.getText().toString());
            bundle.putString("dcontent",dcontent.getText().toString());
            bundle.putString("dtoll",dtoll.getText().toString());

            intent.putExtras(bundle);
            startActivity(intent);



        }
    };


    private void dpds() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                if(monthOfYear<10 && dayOfMonth <10){
                    ddates.setText(year+"0"+(monthOfYear+1)+"0"+dayOfMonth);
                }
                else if(monthOfYear<10){
                    ddates.setText(year+"0"+(monthOfYear+1)+""+dayOfMonth);
                }
                else if(dayOfMonth<10){
                    ddates.setText(year+""+(monthOfYear+1)+"0"+dayOfMonth);
                }
                else{
                    ddates.setText(year+""+(monthOfYear+1)+""+dayOfMonth);
                }

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void tpds() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                if(hh<10 && mm<10){
                    dtimes.setText("0"+hh+":0"+mm);
                }
                else if(hh<10){
                    dtimes.setText("0"+hh+":"+mm);
                }
                else if(mm<10){
                    dtimes.setText(hh+":0"+mm);
                }
                else{
                    dtimes.setText(hh+":"+mm);
                }

            }
        },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
    }

    private void dpde() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                if(monthOfYear<10 && dayOfMonth <10){
                    ddatee.setText(year+"0"+(monthOfYear+1)+"0"+dayOfMonth);
                }
                else if(monthOfYear<10){
                    ddatee.setText(year+"0"+(monthOfYear+1)+""+dayOfMonth);
                }
                else if(dayOfMonth<10){
                    ddatee.setText(year+""+(monthOfYear+1)+"0"+dayOfMonth);
                }
                else{
                    ddatee.setText(year+""+(monthOfYear+1)+""+dayOfMonth);
                }

            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void tpde() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                if(hh<10 && mm<10){
                    dtimee.setText("0"+hh+":0"+mm);
                }
                else if(hh<10){
                    dtimee.setText("0"+hh+":"+mm);
                }
                else if(mm<10){
                    dtimee.setText(hh+":0"+mm);
                }
                else{
                    dtimee.setText(hh+":"+mm);
                }
            }
        },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
    }

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


    @SuppressLint("NewApi")
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

