package com.example.servicebestpractice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

public class DownloadService extends Service {

//    public DownloadService() {
//    }

    private DownloadTask downloadTask;
    private String downloadUri;

    private DownloadListener listener = new DownloadListener() {
        @Override
        //前台服务显示进度条
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Download...", progress));
        }

        @Override
        public void onSuccess() {
            //下载成功将前台服务关闭 并发出下载成功的通知
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Success",-1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            //下载失败时
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Failed", -1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            //暂停下载
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            //取消下载
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    //实例化DownloadBinder 在onBind方法中返回
    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //创建Binder对象对下载功能进行管理
    class DownloadBinder extends Binder {

        //开始下载
        public void startDownload(String uri) {
            if (downloadTask == null) {
                //传入下载Uri 实例化下载任务 执行任务
                downloadUri = uri;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUri);
                //设置前台服务 发出通知
                startForeground(1, getNotification("Download...", 0));
                Toast.makeText(DownloadService.this, "Download...", Toast.LENGTH_SHORT).show();
            }
        }

        //暂停下载
        public void pauseDownload(){
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        //取消下载
        public void cancelDownload(){
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            }else {
                /*
                如果取消下载了 需要把文件删除 还有通知关闭
                获取文件 如果存在则删除
                通过通知管理器关闭通知 并把前台服务关闭了 Toast
                 */
                if (downloadUri != null) {
                    //获取文件
                    String fileName = downloadUri.substring(downloadUri.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    //如果存在则删除
                    if (file.exists()) {
                        file.delete();
                    }
                    //关闭通知
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    //通知栏部分
    private NotificationManager getNotificationManager(){
        //创建通知栏管理器
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    //通知栏
    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this , "default");
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        //只有当进度大于或等于0的时候才显示进度条
        if (progress >= 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);   //最后个参数为是否使用模糊进度条
        }
        return builder.build();
    }
}
