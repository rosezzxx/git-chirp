package com.example.asus.a0404;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_friend extends Fragment {


    public Fragment_friend() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend,container,false);
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

        listview.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.friends_layout, R.id.Itemname,itemname));

    }

}
