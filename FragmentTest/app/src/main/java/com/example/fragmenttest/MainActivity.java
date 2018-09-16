package com.example.fragmenttest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);

        //活动关联碎片
//        RightFragment rightFragment = (RightFragment) getSupportFragmentManager().findFragmentById(R.id.right_fragment);
        //通过类似findViewById方法获取碎片实例
        /*
        亦可通过活动关联不同的碎片之间通信
         */

//        replaceFragment(new RightFragment());
    }

    //第一行代码实例参考代码
//    private void replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.right_layout, fragment);
//        transaction.commit();
//    }

    /*
    通过限定符可以让程序判断是单页还是双页
    large为大屏幕 xlarge为超大屏幕
    一般可以通过最小限定符进行灵活判断 例如sw600dp 超过这个值则是双页 反之
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
//                replaceFragment(new AnotherRightFragment());

//                //网上参考代码
//                AnotherRightFragment anotherRightFragment = new AnotherRightFragment();
//                //在FragmentLayout里添加原碎片 实例化替换的碎片
//                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//                //通过getSupportFragmentManager()方法获取FragmentManager
//                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
//                //通过beginTransaction()方法开启事务
//                transaction.replace(R.id.right_layout, anotherRightFragment);
//                //传递待添加或替换的碎片
//                transaction.addToBackStack(null);
//                //用于储存上个碎片 通过back键返回
//                transaction.commit();
//                //提交事务
                break;
                default:
                    break;
        }
    }



}
