package com.example.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyIntentService extends IntentService {

    private static final String TAG = "MyIntentService";

    public MyIntentService() {
        super("MyIntentService");   //调用父类的有参构造方法
    }

    //该方法已经在子线程运行 不用担心ANR问题
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //打印当前线程id
        Log.d(TAG, "Thread id is " + Thread.currentThread().getId());
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy executed");
    }
}
