package com.test.testapp_linearlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private AutoCompleteTextView acTextView;
    private String[] res = {"beijing1","beijing2","bejing3",
                            "shanghai1","shang3","hai2"};

    private MultiAutoCompleteTextView macTextView;

    private ToggleButton tb;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        AutoCompleteTextView 单适配
        第一步：初始化控件
        第二步：适配器ArrayAdapter
        第三步：初始化数据源res
        第四步：setAdapter绑定控件
         */

        acTextView = findViewById(R.id.AutoCompleteTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this , R.layout.support_simple_spinner_dropdown_item , res );

        acTextView.setAdapter(adapter);

        /*
        MultiAutoCompleteTextView 多适配
        第一步：初始化控件
        第二步：适配器ArrayAdapter
        第三步：初始化数据源res
        第四步：setAdapter绑定控件
        第五步：设置分隔符setTokenizer
         */

        macTextView = findViewById(R.id.MultiAutoCompleteTextView);
        macTextView.setAdapter(adapter);

        //设置以逗号为分隔符
        macTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        /*
        ToggleButton
        通过改变按钮状态做出动作
        第一步 初始化控件
        设置监听器
        通过setBackgroundResource切换状态
         */

        tb = findViewById(R.id.ToggleButton);
        img = findViewById(R.id.ImageView);

        tb.setOnCheckedChangeListener(this);


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        /*
        当tb被点击后 当前方法会执行
        buttonView 代表被点击的控件
        isChecked 代表被点击的控件状态

        当点击tb后 改变img的背景
         */

        img.setBackgroundResource(isChecked?R.drawable.on:R.drawable.off);
        //进行判断 如果是on状态显示on照片 否则off

    }
}
