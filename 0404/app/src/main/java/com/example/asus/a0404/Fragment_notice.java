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
public class Fragment_notice extends Fragment {


    public Fragment_notice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice,container,false);
        initListView(view);
        return  view;
    }


    private void initListView(View view) {
        String[] itemname ={
                "通知1",
                "通知2",
                "通知3",
                "通知4",
                "通知5",
                "通知6"
        };
    ListView listview = (ListView)view.findViewById(R.id.listview_view);

        listview.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.notice_layout, R.id.Itemname,itemname));

    }
}
