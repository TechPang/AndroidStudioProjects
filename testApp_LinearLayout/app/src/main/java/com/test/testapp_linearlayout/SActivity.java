package com.test.testapp_linearlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SActivity extends AppCompatActivity {

    private Button bt;

    private String content = "测试成功";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sactivity);

        /*
        第二个页面什么时候回传数据
        回传到第一个页面的实际上是intent对象
         */

        bt = findViewById(R.id.but_first);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent只是回传数据 不作为跳转 不需要指定目标
                Intent data = new Intent();
                //类似map的key和value值 通过putextra保存数据
                data.putExtra("data", content);
                /*
                通过resultCode结果码回传数据
                不同的页面回传数据 结果码都会不一样

                通过请求码和结果码确认是哪个页面给第一个页面回传数据
                 */
                setResult(2,data);

                //结果当前页面
                finish();
            }
        });

    }
}
