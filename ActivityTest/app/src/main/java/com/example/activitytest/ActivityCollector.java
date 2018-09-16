package com.example.activitytest;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

//随时退出程序
/*
写一个活动管理器
储存活动 提供add添加活动 remove移除活动 finish销毁活动
在BaseActivity中调用addActivity()方法对新建的实例进行添加 重写onDestroy方法对要销毁的活动进行移除
想要在哪个地方退出程序 调用finishAll()方法即可
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity : activities) {
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }

}
