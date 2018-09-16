package com.example.uicustomviews;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

//自定义控件
public class TitleLayout extends LinearLayout {
    public TitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        /*
        如果想要在构造函数里动态加载 需要使用LayoutInflater 通过form方法构建LayoutInflater对象
        然后通过inflate加载目标布局文件 再添加父类布局

        使用需要完整类名 效果和include引用布局一样
         */

        //注册点击事件
        Button titleBack = findViewById(R.id.title_back);
        Button titleEdit = findViewById(R.id.title_edit);

        titleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });

        titleEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext() , "You clicked edit button" , Toast.LENGTH_SHORT).show();
            }
        });

        /*
        自定义控件好处在于不用写重复代码 直接调用即可
         */

    }
}
