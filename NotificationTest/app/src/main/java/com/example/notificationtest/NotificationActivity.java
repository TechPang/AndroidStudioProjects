package com.example.notificationtest;

import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);

//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //通过manager.cancel方法传入想取消掉的通知id
//        manager.cancel(1);

    }
}
