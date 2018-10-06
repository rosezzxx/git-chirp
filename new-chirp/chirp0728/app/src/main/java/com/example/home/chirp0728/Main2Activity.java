package com.example.home.chirp0728;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
<<<<<<< HEAD
import android.view.MotionEvent;
import android.view.View;
=======
>>>>>>> master
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
<<<<<<< HEAD
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    String name = null;
    String value = null;
=======

public class Main2Activity extends AppCompatActivity {

    String gettext = null;
>>>>>>> master
    WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //取得前一頁值
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
<<<<<<< HEAD
        name = bundle.getString("name");
        value = bundle.getString("value");
=======
        gettext = bundle.getString("get");
>>>>>>> master

        mWebview = (WebView)findViewById(R.id.webView);

        mWebview.setWebViewClient(new WebViewClient());

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebview.setWebChromeClient(new WebChromeClient());
        //WebSettings.setSupportMultipleWindows(true);



        /*String url = m_webView.getUrl();
        url.indexOf("/OrderResultURL") < 0 */


        //webView顯示網頁
<<<<<<< HEAD
        mWebview.loadUrl("http://fatfat-ting.ics-expo.org/index/Android/index?name=" + name +"& value=" + value);
        //mWebview.loadUrl("http://fatfat-ting.ics-expo.org/index/Android/OrderResultURL");


        mWebview.setOnTouchListener(new View.OnTouchListener(){
            @Override

            public boolean onTouch(View v, MotionEvent event){
                String url = mWebview.getUrl();
                //int ooo = url.indexOf("http://fatfat-ting.ics-expo.org/index/Android/OrderResultURL");

                if(url.indexOf("OrderResultURL") > 0 ){
                    mWebview.goBack();
                    mWebview.canGoBack();
                    Toast.makeText(getApplicationContext(),"Ok go Back", Toast.LENGTH_SHORT).show();
                    //super.onBackPressed();
                    //onCloseWindow(mWebview);
                    finish();

                    Intent intent = new Intent();
                    intent.setClass(Main2Activity.this,MainActivity.class);
                    final Bundle bundle = new Bundle();
                    startActivity(intent);

                }
                //Toast.makeText(getApplicationContext(),url.indexOf("http://fatfat-ting.ics-expo.org/index/Android/OrderResultURL") ,Toast.LENGTH_SHORT).show();
                return false;

            }

        });
=======
        mWebview.loadUrl("http://fatfat-ting.ics-expo.org/index/Android/index?get=" + gettext);
        //mWebview.loadUrl("http://fatfat-ting.ics-expo.org/index/Android/OrderResultURL");
>>>>>>> master
    }
}
