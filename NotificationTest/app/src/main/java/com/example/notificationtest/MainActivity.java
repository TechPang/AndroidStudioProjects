package com.example.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendNotice = findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notice:

                //创建意图
                Intent intent = new Intent(this, NotificationActivity.class);
                /*
                PendingIntent简单理解为延迟执行的Intent
                可以根据需求选择getActivity getBroadcast getService
                四个参数 一般第二个和第四个传入0即可
                 */
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

                //实例化通知管理器
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                //实例化通知 旧代码需要添加String channelId 该作用是将通知进行分类 例如设置优先级
                Notification notification = new NotificationCompat.Builder(this, "default")
                        .setContentTitle("This is content title")
//                        .setContentText("This is content text")
                        //显示通知被创建时间
                        .setWhen(System.currentTimeMillis())
                        //显示在状态栏的图标
                        .setSmallIcon(R.mipmap.ic_launcher)
                        //下拉通知显示的图标
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        //传入PendingIntent实例
                        .setContentIntent(pi)
                        //点击通知后取消掉 亦可通过manager.cancel传入通知id取消
                        .setAutoCancel(true)

                        //显示通知全文字
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Since elements, text nodes, comments, processing instructions, etc. " +
                                "cannot exist outside the context of a Document, the Document interface also contains the factory methods needed to " +
                                "create these objects. The Node objects created have a ownerDocument attribute which associates them with the Document " +
                                "within whose context they were created."))
                        //通知加载图片
//                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources() , R.drawable.big_image)))
                        //设置通知的重要程度 5个值 DEFAULT默认效果 MIN特定场合 例如下拉状态栏 LOW缩小通知 改变显示顺序 HIGH放大通知 改变显示顺序 MAX直接弹出横幅
                        .setPriority(NotificationCompat.PRIORITY_MAX)

                        //丰富的通知效果
                        //通过播放音频提醒用户
//                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                        //通过震动提醒用户 通过长整型数据设置震动时长 例如下面的代码 下标0为静止时间 1为震动时间 以此类推 需要添加权限
//                        .setVibrate(new long[]{0 , 1000 , 1000 , 1000})
                        //设置闪光灯 颜色 亮起 暗去
//                        .setLights(Color.YELLOW , 1000 , 1000)
                        //默认效果
                        .setDefaults(NotificationCompat.DEFAULT_ALL)

                        //最终build方法
                        .build();

                //通过notify方法 传入id和通知 每个通知的id都不同
                manager.notify(1 , notification);

                break;

                default:
                    break;

        }
    }
}
