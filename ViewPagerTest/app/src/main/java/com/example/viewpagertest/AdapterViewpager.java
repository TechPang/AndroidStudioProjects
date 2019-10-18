package com.example.viewpagertest;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class AdapterViewpager extends PagerAdapter {

    //实现最基本的PagerAdapter 需要四个方法

    private List<View> mViewList;
    public AdapterViewpager(List<View> mViewList) {
        this.mViewList = mViewList;   //规范 一般构造方法都是传参赋值给this
    }

    @Override
    public int getCount() {
        return mViewList.size();   //必须实现
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;   //必须实现
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);   //必须实现 实例化
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViewList.get(position));   //必须实现 销毁
    }
}
