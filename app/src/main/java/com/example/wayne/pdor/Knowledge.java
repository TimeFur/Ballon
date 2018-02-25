package com.example.wayne.pdor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Knowledge extends AppCompatActivity {

    String _url = "https://www.google.com.tw";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

        //setting
        WebView web_view = (WebView)findViewById(R.id.webView1);
        WebSettings web_view_setting = web_view.getSettings();
        web_view_setting.setJavaScriptEnabled(true);
        web_view.loadUrl(_url);
    }
}
