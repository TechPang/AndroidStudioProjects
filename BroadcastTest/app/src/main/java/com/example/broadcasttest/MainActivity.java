package com.example.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


//    private IntentFilter intentFilter;
//    private NetworkChangeReceiver networkChangeReceiver;

    //本地广播 新建内部类
    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context , "received local broadcast" , Toast.LENGTH_SHORT).show();
        }
    }

    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取实例
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        //添加action并实例化localReceiver 注册广播接收器
        intentFilter = new IntentFilter("com.example.broadcasttest.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver , intentFilter);


        //发送标准广播
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
//                sendBroadcast(intent);
                //发送有序广播 通过Manifest设置优先级 通过abortBroadcast决定是否继续传递下去
//                sendOrderedBroadcast(intent , null);

                //本地广播
                Intent intent = new Intent("com.example.broadcasttest.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);
            }
        });

        //实例化IntentFilter 并通过addAction方法添加要接收的广播
//        intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        /*
        添加一条网络状态发生变化的广播
        接收什么广播就添加相应的action
         */

        //实例化接收器并注册
//        networkChangeReceiver = new NetworkChangeReceiver();
//        registerReceiver(networkChangeReceiver, intentFilter);
    }

    //动态注册的广播接收器一定要取消注册
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(networkChangeReceiver);
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    //新建内部类 继承BroadcastReceiver 重写onReceive方法
    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context , "network changed" , Toast.LENGTH_SHORT).show();

            //创建ConnectivityManager并实例化 获取系统服务
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取网络状态
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isAvailable()){
                Toast.makeText(context , "network is available" , Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context , "network is unavailable" , Toast.LENGTH_SHORT).show();
            }

        }
    }
}
