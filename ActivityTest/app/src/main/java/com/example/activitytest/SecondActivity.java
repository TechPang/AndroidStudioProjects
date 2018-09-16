package com.example.activitytest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends BaseActivity {

    private static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        //向下一个活动传递数据
//        Intent intent = getIntent();
//        String data = intent.getStringExtra("extra_data");
//        Log.d(TAG, data);
        /*
        通过getIntent()方法获取启动活动的intent
        再通过getStringExtra获取键所对应的值
        如果是整型getIntExtra 以此类推
        打印输出
         */

        //返回数据给上一个活动
        Button button2 = findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("data_return", "Hello FirstActivity");
//                setResult(RESULT_OK , intent);
//                finish();
                /*
                处理结果 setResult一般两个参数 RESULT_OK和RESULT_CANCELED
                 */

                //singleTop
//                Intent intent = new Intent(SecondActivity.this, FirstActivity.class);
//                startActivity(intent);

                //singleInstance
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);

            }
        });

        //活动的启动模式
//        Log.d(TAG, this.toString());

        //活动的启动模式singleInstance
        Log.d(TAG, "Task id is" + getTaskId());

        //活动的最佳写法的运用 测试打印结果
        Intent intent = getIntent();
        String data = intent.getStringExtra("param2");
        Log.d(TAG, data);

    }

    //活动的启动模式singleTask
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, "onDestroy: 执行了");
//    }

    //可以通过back键返回数据
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data_return", "Hello FirstActivity");
        setResult(RESULT_OK , intent);
        finish();
    }
    /*
    如果用户想通过Back键返回值 重写onBackPressed方法即可
     */

    //启动活动的最佳写法
    public static void actionStart(Context context , String data1 , String data2){
        Intent intent = new Intent(context , SecondActivity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);

    }

}
