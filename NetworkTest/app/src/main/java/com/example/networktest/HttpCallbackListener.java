package com.example.networktest;

//Java的回调机制
public interface HttpCallbackListener {

    //当服务器成功响应的时候调用 参数代表返回服务器的数据
    void onFinish(String response);
    //当网络操作错误的时候调用 参数代表记录着错误的信息
    void onError(Exception e);

}
