package com.example.activitytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ThirdActivity extends BaseActivity {

    private static final String TAG = "ThirdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //活动的启动模式singleInstance
        Log.d(TAG, "Task id is" + getTaskId());

        setContentView(R.layout.third_layout);

        Button button3 = findViewById(R.id.button_3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //随时退出程序 调用该方法即可
                ActivityCollector.finishAll();
                //只适合当前程序 不可以用该方法杀掉其他程序
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

    }
}
