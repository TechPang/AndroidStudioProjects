package com.test.testapp_linearlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class testAc extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener  {

    //这样可以避免每次Log需要重写标签
    final String TAG = "finalTag";

    private CheckBox checkBox;
    private RadioGroup radioGroup;

    private Button bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testlinearlayout2);

        //初始化CheckBox控件
        checkBox = findViewById(R.id.checkbox);
        
        //通过设置CheckBox监听事件判断CheckBox是否被选中
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //监听CheckBox的状态并输出
                Log.i(TAG, isChecked+"");

                if(isChecked){
                    String text = checkBox.getText().toString();
                    Log.i("TAG", text);
                }
                
            }
        });

        radioGroup = findViewById(R.id.radioGroup);
        //接口方式实现监听事件
        radioGroup.setOnCheckedChangeListener(this);

        Log.i(TAG, "onCreate: 创建执行了");

        //通过Intent启动第二个页面
        bt = findViewById(R.id.but_test);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置两个class进行intent连接
                Intent intent = new Intent(testAc.this,second_class.class);
                startActivity(intent);
            }
        });



    }

    //从创建到销毁的Activity生命周期

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: 启动执行了");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: 获取焦点执行了");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: 失去焦点执行了");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: 停止执行了");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: 销毁执行了");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: 重启执行了");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.radio1:
                Log.i(TAG, "当前选择的是男性");
                break;
            case R.id.radio2:
                Log.i(TAG, "当前选择的是女性");
                break;
            default:
                break;
        }

    }
}
