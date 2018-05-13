package com.example.asus.a0404;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_my extends Fragment implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener {



    //-------page--------
    private android.support.design.widget.TabLayout mTabs;
    private ViewPager mViewPager;
    private my_goActivity fragment1 = new my_goActivity();
    private my_foundActivity fragment2 = new my_foundActivity();
    private my_mapActivity fragment3 = new my_mapActivity();


    public Fragment_my() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my,container,false);
        initListView(view);
        return  view;
    }

    private void initListView(View view) {


        mTabs = (android.support.design.widget.TabLayout) view.findViewById(R.id.my_tabs);
        mViewPager = (ViewPager)view.findViewById(R.id.my_viewpager);


        mViewPager.addOnPageChangeListener(this);
        mTabs.addOnTabSelectedListener(this);


        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragment1;
                    case 1:
                        return fragment2;
                    case 2:
                        return fragment3;
                }
                return null ;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTabs.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }





}
