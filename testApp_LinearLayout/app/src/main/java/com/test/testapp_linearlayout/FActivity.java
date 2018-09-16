package com.test.testapp_linearlayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FActivity extends AppCompatActivity {

    private Button bt1;
    private  Button bt2;
    private TextView tv;

    //Context上下文全局变量
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factivity);

        bt1 = findViewById(R.id.but_first);

        context = this;

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过Intent跳转页面
                //第一个参数是上下文对象this
                //第二个参数是目标文件
                //通过设置Context全局变量 匿名内部类访问this 也可以写当前类名.this
                Intent intent = new Intent(context , SActivity.class);
                startActivity(intent);
            }
        });

        //通过startActivityForresult跳转 接受返回数据的方法
        bt2 = findViewById(R.id.but_second);
        tv = findViewById(R.id.textView1);

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SActivity.class);

                /*
                第一个参数为intent对象
                第二个参数为请求识别码
                 */

                startActivityForResult(intent , 1);
            }
        });

    }

    /*
    通过startActivityForResult跳转 接收返回值的方法
    requestCode 请求识别码 例如十个layout 需要判断是哪个在请求
    resultCode 第二个页面返回的识别码 双重确认 进行返回值传递
    data 第二个页面传回的数据
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2){
            //获取什么类型的数据
            String content = data.getStringExtra("data");
            //设置文本
            tv.setText(content);
        }
    }
}
