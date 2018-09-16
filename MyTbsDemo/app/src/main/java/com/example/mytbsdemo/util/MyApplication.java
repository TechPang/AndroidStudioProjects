package com.example.mytbsdemo.util;

import android.app.Application;
import android.content.Context;

import com.tencent.smtt.sdk.QbSdk;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initX5();   //初始化X5
    }

    public static Context getContext(){
        return context;
    }

    //初始化X5
    private void initX5(){
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {

            }
        });
    }

}
