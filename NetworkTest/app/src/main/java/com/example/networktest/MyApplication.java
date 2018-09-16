package com.example.networktest;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;

    //重写父类方法 获得Application级别的Context
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    //再提供静态getContext 返回context
    private static Context getContext(){
        return context;
    }

}
