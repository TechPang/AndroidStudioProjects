package com.example.networktest;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    //一般都把常用的网络操作写进一个公共类中 并提供静态方法 需要网络请求的时候 只需要调用即可

    //HttpURLConnection网络请求
    public static void sendHttpRequest(final  String address , final HttpCallbackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {
                    //实例化connection
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    //请求方法 自由定制
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //获取输入流 进行读取
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    //获取数据并返回
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    //子线程无法通过return数据
//                    return response.toString();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return e.getMessage();
//                }finally {
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                }

                    //需要通过HttpCallbackListener接口
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    listener.onError(e);
                }finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        }).start();

    }

    //调用的时候 还需要将HttpCallbackListener实例传入
//    HttpUtil.sendHttpRequest(address , new HttpCallbackListener() {
//        @Override
//        public void onFinish(String response){
//            //根据返回内容执行具体逻辑
//        }
//        @Override
//        public void onError(Exception e){
//            //针对异常情况进行处理
//        }
//    });

    //需要网络请求的时候 获取到服务器响应的数据后就可以解析了
//    String address = "https://www.baidu.com";
//    String response = HttpUtil.sendHttpRequest(address);

    //OkHttp网络请求
    private static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        /*
        okhttp3.Callback是OkHttp自带的回调接口
        通过enqueue传入 而且已开启子线程 会在里面执行http请求 最终结果返回callback参数
         */
        client.newCall(request).enqueue(callback);
    }

    //调用的时候
//    HttpUtil.sendOkHttpRequest("https://www.baidu.com" , new okhttp3.Callback(){
//        @Override
//            public void onResponse(Call call , Response response) throws IOException{
//                //得到服务器返回的内容
//                String responseData = response.body().string();
//        }
    //throws是方法可能抛出异常的声明 语法例如上面 throw是语句抛出一个异常 语法throw e
//        @Override
//            public void OnFailure(Call call , IOException e){
//                //针对异常情况进行处理
//        }
//    });



}
