package com.example.asus.a0404;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


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

        str = (String) dtype.getSelectedItem();

        connect = CONN(un, passwords, db, ip);
        String query = "select type_name from doingtype";

        try {
            connect = CONN(un, passwords, db, ip);
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();
            ArrayList<String> data = new ArrayList<String>();
            while (rs.next()) {
                String id = rs.getString("type_name");
                data.add(id);

            }
            String[] array = data.toArray(new String[0]);
            ArrayAdapter NoCoreAdapter = new ArrayAdapter(getContext(),
                    android.R.layout.simple_list_item_1, data);
            dtype.setAdapter(NoCoreAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        dtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //拿到被选择项的值
                str = (String) dtype.getSelectedItem();
                //把该值传给 TextView
                //tv.setText(str);
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

