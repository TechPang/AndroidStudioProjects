package com.example.seniorskilltest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class LongRunningService extends Service {

//    public LongRunningService() {
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    实例化得到AlarmManager
    设置时间 定时任务 设置PendingIntent
    最后设置 三个参数
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //逻辑操作比较耗时 放在主线程会对定时任务造成影响
        new  Thread(new Runnable() {
            @Override
            public void run() {
                //执行具体的逻辑操作
            }
        }).start();
        //定时任务 每一个小时再次执行任务 永久循环
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60 * 60 * 1000;
        //SystemClock.elapsedRealtime可以获取系统开机到至今时间 System.currentTimeMillis则从1970.1.1.00:开始
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, LongRunningService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        /*
        ELAPSED_REALTIME开机时间至今 RTC则从1970
        从系统4.4开始 系统会检测有多少Alarm任务 统一执行 减少唤醒CPU次数 想要准确无误可以用setExact方法
         */
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        /*
        想要启动定时服务时 创建Intent启动即可
         */

        return super.onStartCommand(intent, flags, startId);
    }
}
