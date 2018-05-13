package com.example.asus.a0404;

/**
 * Created by ASUS on 2018/5/11.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkTools {
    public boolean checkInternetConnection(Context c){
        ConnectivityManager cm=(ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni=cm.getActiveNetworkInfo();
        if(ni!=null && ni.isConnected()){
            // System.out.println("ni.isConnected() = "+ni.isConnected());
            return ni.isConnected();
        }else{
            // System.out.println("ni.isConnected() = "+ni.isConnected());
            return false;
        }
    }
}