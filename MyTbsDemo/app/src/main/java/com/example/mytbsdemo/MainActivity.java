package com.example.mytbsdemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wang.avi.AVLoadingIndicatorView;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //初始化控件
    private Button btnOpenUrl;
    private FrameLayout web_view_layout;
    private WebView mWebView;
    private String openUrl = "http://mp.weixin.qq.com/mp/homepage?__biz=MzAxOTg3MDQxNQ==&hid=2&sn=8609dfd9cf67c19b5e9b93f12c633928&scene=18#wechat_redirect";
//    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //X5设置键盘 防止遮住输入光标
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();

    }

    @Override
    public void onClick(View v) {
        if (v == btnOpenUrl) {
            initWebView();
        }
        //加载其他视频格式
    }

    //播放视频

    //初始化View
    private void initView() {
        btnOpenUrl = findViewById(R.id.btn_open_url);
        web_view_layout = findViewById(R.id.web_view_layout);
        btnOpenUrl.setOnClickListener(this);

//        avi = findViewById(R.id.avi);

    }

    //初始化webView
    private void initWebView(){
        //采用new WebView进行动态添加 使用的是com.tencent.smtt.sdk.WebView的包
        mWebView = new WebView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(layoutParams);

        mWebView.loadUrl(openUrl);
        WebSettings settings = mWebView.getSettings();
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
//                startAnim();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //设定加载结束的操作
//                stopAnim();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                switch (errorCode) {

                }
            }

            //处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.proceed();   //表示等待证书响应
                //handler.cancel();    //表示挂起连接，为默认方式
                //handler.handleMessage(null);   //可做其他处理
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                }else {
                    // to do something...
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {

            }
        });

        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);   //图片调整webView大小

        //设置加载图片
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);   //不建议该方法
        settings.setDefaultTextEncodingName("utf-8");   //避免乱码
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setNeedInitialFocus(false);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);   //适应屏幕
        settings.setLoadsImagesAutomatically(true);   //自动加载图片
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        //视频全屏播放形态
//        Bundle data = new Bundle();
//        data.putBoolean("standardFullScreen", false);
//        data.putBoolean("supportLiteWnd", false);
//        data.putInt("DefaultVideoScreen", 1);
//        mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);

        //将webView添加到布局中
        web_view_layout.removeAllViews();
        web_view_layout.addView(mWebView);
    }

    //监听返回键
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        //同时销毁webView 如果不执行这个操作 会出现退出应用视频仍在播放
        if (mWebView != null) {
            mWebView.destroy();
            web_view_layout.removeView(mWebView);
            mWebView = null;
        }
        super.onDestroy();
    }

//    private void startAnim(){
//        avi.setVisibility(View.VISIBLE);
//        avi.show();
//    }
//    private void stopAnim(){
//        avi.hide();
//    }

}
