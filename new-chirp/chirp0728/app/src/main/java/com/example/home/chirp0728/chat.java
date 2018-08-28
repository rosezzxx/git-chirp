package com.example.home.chirp0728;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class chat extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener{

    //-------page--------
    private android.support.design.widget.TabLayout mTabs;
    private ViewPager mViewPager;

    private friends_location fragment1 = new friends_location(); // 參加活動
    private friends_list fragment2 = new friends_list(); //創辦活動
    private friends_chat fragment3 = new friends_chat(); //創辦活動

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mTabs = (android.support.design.widget.TabLayout) findViewById(R.id.my_tabs);
        mViewPager = (ViewPager)findViewById(R.id.my_viewpager);


        mViewPager.addOnPageChangeListener(this);
        mTabs.addOnTabSelectedListener(this);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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


}
