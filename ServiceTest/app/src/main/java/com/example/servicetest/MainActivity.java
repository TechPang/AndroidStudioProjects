package com.example.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //绑定服务
    private MyService.DownloadBinder downloadBinder;   //引入DownloadBinder
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder) service;   //向下转型获得引入DownloadBinder实例
            //根据具体场景调用方法 即活动可以指挥服务去干什么
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startService = findViewById(R.id.start_service);
        Button stopService = findViewById(R.id.stop_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        Button bindService = findViewById(R.id.bind_service);
        Button unbindService = findViewById(R.id.unbind_service);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);
        Button startIntentService = findViewById(R.id.start_intent_service);
        startIntentService.setOnClickListener(this);

    }

    private static final String TAG = "MainActivity";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);   //启动服务
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);   //停止服务
                break;

            case R.id.bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                //Intent ServiceConnection实例 第三个参数表示当活动和服务绑定后自动创建服务
                bindService(bindIntent, connection, BIND_AUTO_CREATE);   //绑定服务
                break;
            case R.id.unbind_service:
                unbindService(connection);   //解绑服务
                break;
            case R.id.start_intent_service:
                //同时打印主线程的id 进行对比
                Log.d(TAG, "Thread id is " + Thread.currentThread().getId());
                Intent intentService = new Intent(this, MyIntentService.class);
                startService(intentService);
                default:
                    break;
        }
    }
}
