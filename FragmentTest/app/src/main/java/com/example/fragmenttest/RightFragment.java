package com.example.fragmenttest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RightFragment extends Fragment {

    //碎片关联活动
//    MainActivity activity = (MainActivity) getActivity();

    private static final String TAG = "RightFragment";

    //碎片和活动关联
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    //创建
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    //创建碎片视图 动态加载
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.right_fragment, container, false);
        return view;

    }

    //确保和碎片关联的活动已经创建好了
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
    }

    //启动
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    //获取焦点
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    //失去焦点
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    //暂停
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    //解除碎片视图
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    //销毁
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    //解除碎片和活动的关联
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }
}
