package com.example.asus.a0404;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class map extends AppCompatActivity {

    private EditText et_address ;
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
    String ad;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_map);
        et_address = (EditText) this.findViewById(R.id. address_et);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        ad = bundle.getString("address");
        et_address.setText(ad);

        // 先打開GPS定位
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        //map.setMyLocationEnabled(true);
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE );
        //getGps ();
        getLatLngByAddr();




    }

    private void getLatLngByAddr() {

        Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE); // 地區:台灣

        try {
            List<Address> addresses = gc.getFromLocationName(et_address.getText().toString().trim(), 1);

            if (addresses != null && !addresses.isEmpty()) {
                double latitude = ((Address) addresses.get(0)).getLatitude();
                double longitude = ((Address) addresses.get(0)).getLongitude();
                String addrline = ((Address) addresses.get(0))
                        .getAddressLine(0);

                if (addrline != null) {
                    START = new LatLng(latitude, longitude);
                    setMapMarker(et_address.getText().toString().trim());
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private void stopGps() {
        mgr.removeUpdates( mLocationListenerNETWORK);
        mgr.removeUpdates( mLocationListenerGPS);
    }

    private void initLocationListener() {
        mLocationListenerGPS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //設定位置
                setLocation(location);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log. d("mLocationListenerGPS" , "onProviderDisabled" );
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log. d("mLocationListenerGPS" , "onProviderEnabled" );
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log. d("mLocationListenerGPS" , "onStatusChanged" );
            }
        };

        mLocationListenerNETWORK = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //設定位置
                setLocation(location);
            }

            @Override
            public void onProviderDisabled(String provider) {
                //Log. d("mLocationListenerNETWORK" , "onProviderDisabled" );
            }

            @Override
            public void onProviderEnabled(String provider) {
                // Log. d("mLocationListenerNETWORK" , "onProviderEnabled" );
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                // Log. d("mLocationListenerNETWORK" , "onStatusChanged" );
            }

        };
    }

    private Runnable timeOutRunnable = new Runnable() {
        public void run() {
            Log. d("GpsTools" , "" + currentTime );
            currentTime++;
            if ( currentTime > TIMEOUT_SEC) {
                //停止定位
                stopGps();
                getLocationFlag = true;
            }

            if (! getLocationFlag) {
                // 如為false,表示尚未取得，再試一次
                handler.postDelayed( this, 1000);
            } else {

            }

        }
    };

    private void startTimeoutMechanism() {
        handler.postDelayed( timeOutRunnable, 1000);
    }

    private void setLocation (Location location) {
        String addrline = null;
        Double longitude = null;
        Double latitude = null;

        Log. d("GpsTools" , "longitude" + location.getLongitude());
        Log. d("GpsTools" , "latitude" + location.getLatitude());

        try {
            Geocoder gc = new Geocoder(map. this,
                    Locale. TRADITIONAL_CHINESE); // 地區:台灣
            // 自經緯度取得地址
            List<Address> lstAddress = gc.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1);
            Address address = lstAddress.get(0);
            addrline = address.getAddressLine(0);

            et_address.setText(addrline); //顯示定位後的地址

            longitude = address.getLongitude();
            latitude = address.getLatitude();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (addrline != null) {
            stopGps();
            // 取到位置了,將flag設為true,讓timeout機制不要再跑了
            getLocationFlag = true;
            START = new LatLng(latitude, longitude);
            setMapMarker( "現在位置" );
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