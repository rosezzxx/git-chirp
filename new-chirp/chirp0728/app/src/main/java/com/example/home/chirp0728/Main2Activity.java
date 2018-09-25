package com.example.home.chirp0728;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Main2Activity extends AppCompatActivity {

    String gettext = null;
    WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //取得前一頁值
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        gettext = bundle.getString("get");

        mWebview = (WebView)findViewById(R.id.webView);

        mWebview.setWebViewClient(new WebViewClient());

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebview.setWebChromeClient(new WebChromeClient());
        //WebSettings.setSupportMultipleWindows(true);



        /*String url = m_webView.getUrl();
        url.indexOf("/OrderResultURL") < 0 */


        //webView顯示網頁
        mWebview.loadUrl("http://fatfat-ting.ics-expo.org/index/Android/index?get=" + gettext);
        //mWebview.loadUrl("http://fatfat-ting.ics-expo.org/index/Android/OrderResultURL");
    }
}
