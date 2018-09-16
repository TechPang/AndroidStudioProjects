package com.example.servicetest;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    //创建DownloadBinder类 继承Binder
    class DownloadBinder extends Binder{
        //开始下载
        public void startDownload(){
            Log.d(TAG, "startDownload executed");
        }
        //获取进度
        public int getProgress(){
            Log.d(TAG, "getProgress executed");
            return 0;
        }
    }

    //实例化
    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        //返回实例
        return mBinder;
    }

    //每个服务中最常用的3个方法
    private static final String TAG = "MyService";
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate executed");

        //创建前台服务
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this, "default")
                .setContentTitle("this is contentTitle")
                .setContentText("This is ContentText")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setAutoCancel(true)   //点击后消失
                .build();
        startForeground(1 , notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy executed");
    }

    //如果想让服务自己停下来 可以调用stopSelf方法

}
