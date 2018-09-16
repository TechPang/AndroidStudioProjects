package com.example.servicebestpractice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //传入DownloadBinder 创建ServiceConnection 进行活动和服务绑定
    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startDownload = findViewById(R.id.start_download);
        Button pauseDownload = findViewById(R.id.pause_download);
        Button cancelDownload = findViewById(R.id.cancel_download);
        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        cancelDownload.setOnClickListener(this);

        //启动服务 绑定服务 判断是否有权限
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);   //启动服务
        bindService(intent, connection, BIND_AUTO_CREATE);   //三个参数 绑定服务
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    //申请权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                finish();
            }
            break;
            default:
        }
    }

    //解绑服务
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private static final String TAG = "MainActivity";

    @Override
    public void onClick(View v) {
        //未绑定的情况
        if (downloadBinder == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.start_download:
                Log.d(TAG, "开始下载");
                String url = "https://dldir1.qq.com/weixin/android/weixin672android1340.apk";
                downloadBinder.startDownload(url);
                break;
            case R.id.pause_download:
                Log.d(TAG, "暂停下载");
                downloadBinder.pauseDownload();
                break;
            case R.id.cancel_download:
                Log.d(TAG, "取消下载");
                downloadBinder.cancelDownload();
                break;
            default:
                break;
        }

    }
}
