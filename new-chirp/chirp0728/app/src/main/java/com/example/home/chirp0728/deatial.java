package com.example.home.chirp0728;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public class deatial extends AppCompatActivity implements OnMapReadyCallback {

    private Button btn_query;
    private GoogleMap map;
    private LocationManager mgr;
    private LocationListener mLocationListenerGPS;
    private LocationListener mLocationListenerNETWORK;
    private final int TIMEOUT_SEC = 20;
    private int currentTime = 0;
    private Handler handler = new Handler();
    private boolean getLocationFlag = false;
    private LatLng START = null; // 啟始座標
    private TextView et_address2;
    String et_address;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatial);



        et_address2 = (TextView)findViewById(R.id.addresss);
        et_address = et_address2.getText().toString();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mgr = (LocationManager) getSystemService(LOCATION_SERVICE );
        //getLatLngByAddr();
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getLatLngByAddr();
    }

    private void getLatLngByAddr() {
        try {
            Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE); // 地區:台灣
            List<Address> addresses = gc.getFromLocationName(et_address.trim(), 1);

            if (addresses != null && !addresses.isEmpty()) {
                double latitude = ((Address) addresses.get(0)).getLatitude();
                double longitude = ((Address) addresses.get(0)).getLongitude();
                String addrline = ((Address) addresses.get(0))
                        .getAddressLine(0);

                if (addrline != null) {
                    START = new LatLng(latitude, longitude);
                    setMapMarker(et_address.trim());
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    private void setMapMarker(String addrStr) {
        if ( map != null) {
            map.clear();
            Marker note1 = map.addMarker( new MarkerOptions().position(START).title(addrStr));
        }

        // 設定中心點
        CameraUpdate center = CameraUpdateFactory.newLatLng(START);
        map.moveCamera(center);
        map.animateCamera(CameraUpdateFactory. zoomTo(15), 1000, null );

        // 也可以用這個方法取代上面設定中心點
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(START, 15));
        //

    }
}
