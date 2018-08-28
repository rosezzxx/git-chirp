package com.example.home.chirp0728;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by HOME on 2018/8/14.
 */

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

