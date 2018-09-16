package com.example.servicebestpractice;

public interface DownloadListener {

    //定义回调接口 对下载过程中的状态进行监听回调
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}
