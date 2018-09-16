package com.example.webviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.web_view);
        //设置属性 简单开启浏览器支持JavaScript脚本即可
        webView.getSettings().setJavaScriptEnabled(true);
        //设置setWebViewClient属性是为了点击新网页仍然显示在WebView中
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.baidu.com");

        //需要网络权限

    }
}
