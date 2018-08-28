package com.example.home.chirp0728;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class friends_list extends Fragment {


    public friends_list() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        initListView(view);
        return  view;
    }

    private void initListView(View view) {
        String[] itemname ={
                "曾意淳",
                "吳昱萱",
                "劉玉婷",
                "好友1",
                "好友2",
                "好友3"
        };

        ListView listview = (ListView)view.findViewById(R.id.listview_view);
        listview.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.friends_layout_2, R.id.Itemname,itemname));
    }


}
