package com.example.asus.chirp;



import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class listall extends Fragment {


    public listall() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_listall, container, false);

        View view = inflater.inflate(R.layout.fragment_listall,container,false);
        initListView(view);
        return  view;



    }
    private LinearLayout L1;


    private void initListView(View view) {


        L1 = (LinearLayout) view.findViewById(R.id.doo);
        L1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(getContext() ,detail.class);
                startActivity(intent);
            }

        });




        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fabadd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(getContext() ,add.class);
                startActivity(intent);
            }
        });




    }





}
