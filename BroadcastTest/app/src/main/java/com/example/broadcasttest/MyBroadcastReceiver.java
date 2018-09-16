package com.example.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context , "received in MyBroadcastReceiver" , Toast.LENGTH_SHORT).show();
        //通过abortBroadcast决定是否继续传递下去
        abortBroadcast();
    }
}
