package com.example.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String , Integer , Integer> {

    //定义4个常量表示下载状态
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    //默认状态
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    //定义DownloadListener 通过这个参数对下载状态进行回调
    private DownloadListener listener;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {

        /*
        定义各种参数 InputStream RandomAccessFile File
        try记录已下载的文件长度 获取下载链接
        获取文件名 指定下载路径 实例化文件
        如果已存在 获取文件长度
        获取下载文件的长度 写个方法
        判断下载量 返回结果
        实例化网络请求方式 断点下载 指定字节下载addHeader
        执行
         */

        //定义参数
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;

        //下载流程
        try {
            long downloadedLength = 0;
            String downloadUrl = params[0];
            //获取下载文件名 指定下载路径
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            //如果文件已存在 读取已下载量 启用断点续传功能
            if (file.exists()) {
                downloadedLength = file.length();
            }
            //获取下载文件的长度 进行判断
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;   //下载失败
            } else if (contentLength == downloadedLength) {
                return TYPE_SUCCESS;   //下载完成
            }
            //发送网络请求
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //断点续传 指定哪个字节开始下载 添加header 下载过的不需要再下载
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();

            /*
            读取服务器响应数据 通过Java的文件流方式 不断的读取写入 直到下载完成
            还需要监控用户是否暂停取消操作
            有则调用方法 返回TYPE_PAUSED TYPE_CANCELED进行中断下载
            没有则实时计算下载量 通过publishProgress方法进行通知

            读取响应数据 跳过已下载的字节
            定义byte字节数组 total总共数 len长度
            while对字节的读取 != 对用户的操作进行监控
            else加载total读取了多少 write写入了多少
            计算已下载的百分比 通过publishProgress方法进行通知

            最后关闭响应数据 返回成功常量 finally收尾工作 返回失败常量
             */

            if (response != null) {
                is = response.body().byteStream();
                //读取文件 不存在则试图创建
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength);   //跳过已下载的字节
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    //对用户操作进行监控
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    }
                    else {
                        //获取读取量和写入量
                        total += len;
                        //和write(byte[] b)不同的是 不会增加文件大小 写入多余的字节
                        savedFile.write(b, 0 , len);
                        //计算已下载的百分比 通过publishProgress进行通知
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                //下载完成
                response.close();
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                //如果用户取消下载 则删除文件
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    //当后台任务调用了publishProgress方法 就会调用onProgressUpdate方法 参数是后台任务传递过来的 可以进行UI操作
    @Override
    protected void onProgressUpdate(Integer... values) {
        //获取进度 进行对比 通过listener.onProgress更新进度
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    //通过下载状态进行回调
    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                default:
                    break;
        }
    }

    //获取下载文件的长度
    private long getContentLength(String downloadUri) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUri)
                .build();
        Response response = client.newCall(request).execute();
        //如果响应数据不为空并执行成功 则获取返回内容长度 并关闭返回
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

    //暂停下载
    public void pauseDownload(){
        isPaused = true;
    }

    //取消下载
    public void cancelDownload(){
        isCanceled = true;
    }

}
