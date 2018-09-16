package com.example.androidthreadtest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView text;

    //用于更新TextView动作
    public static final int UPDATE_TEXT = 1;
    //异步消息处理方法
    private Handler handler = new Handler() {
        @Override
        //重写handleMessage方法 已经在主线程上运行 可以进行UI操作
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //如果等于这个值 则进行操作
                case UPDATE_TEXT:
                    //这里进行UI操作
                    text.setText("Nice to meet you");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        Button changText = findViewById(R.id.change_text);
        changText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_text:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        text.setText("Nice to meet you");
                        //实例化Message
                        Message message = new Message();
                        //指定what值
                        message.what = UPDATE_TEXT;
                        //通过Handler的sendMessage方法发送Message对象 然后进行判断处理
                        handler.sendMessage(message);
                    }
                }).start();
                break;
                default:
                    break;
        }
    }
}
