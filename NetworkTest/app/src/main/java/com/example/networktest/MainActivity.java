package com.example.networktest;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendRequest = findViewById(R.id.send_request);
        responseText = findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.send_request){
//            sendRequestWithHttpURLConnection();
            sendRequestWithOkHttp();
        }
    }

    //OkHttp
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    //POST提交数据
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("username", "admin")
//                            .add("password" , "123456")
//                            .build();

                    Request request = new Request.Builder()
                            //可以连缀其他方法丰富Request对象
//                            .url("http://192.168.1.117/get_data.xml")
//                            .url("http://192.168.43.136/get_data.xml")
//                            .url("http://192.168.43.136/get_data.json")
                            .url("http://192.168.1.117/get_data.json")

                            //POST提交数据
//                            .post(requestBody)
                            //最终build方法
                            .build();
                    //调用newCall方法执行获取返回数据
                    Response response = client.newCall(request).execute();
                    //通过body得到返回内容
                    String responseData = response.body().string();
//                    showResponse(responseData);

//                    parseXMLWithPull(responseData);   //通过Pull方式解析XML数据
//                    parseXMLWithSAX(responseData);   //通过SAX方式解析XML数据
//                    parseJSONWithJSONObject(responseData);   //通过JSONObject方式解析JSON数据
                    parseJSONWithGSON(responseData);   //通过GSON方式解析JSON数据

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static final String TAG = "MainActivity";

    //通过GSON解析JSON数据
    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        //如果解析的是一段数组 则需要借助TypeToken
        List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>() {
        }.getType());


        for (App app : appList) {
            Log.d(TAG, "id is " + app.getId());
            Log.d(TAG, "name is " + app.getName());
            Log.d(TAG, "version is " + app.getVersion());
        }

        /*
        如果只是简单解析一段数据 例如{"name":"Tom","age":20}
        只需要这样写 gson.fromJson(jsonData , *.class);
         */
    }

    //通过官方的JSONObject解析JSON数据
    private void parseJSONWithJSONObject(String jsonData) {
        try {
            //创建JSONArray对象 传入JSON数组
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //每个元素都是JSONObject对象 通过jsonArray取出
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //再通过getString取出这些数据
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                //打印结果
                Log.d(TAG, "id is " + id);
                Log.d(TAG, "name is " + name);
                Log.d(TAG, "version is " + version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通过SAX方式解析XML数据
    private void parseXMLWithSAX(String xmlData) {
        try {
            //实例化XMLReader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();

            //实例化ContentHandler 并设置到xmlReader中
            ContentHandler handler = new ContentHandler();
            xmlReader.setContentHandler(handler);
            //开始解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //通过Pull方式解析XML数据
    private void parseXMLWithPull(String xmlData) {
        try {
            //先获取一个factory实例 再通过这个实例获取parser对象 再进行setInput方法把服务器返回的XML数据进行解析
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            //获取当前解析事件
            int eventType = xmlPullParser.getEventType();
            String name = "";
            String id = "";
            String version = "";

            //如果eventType不等于END 说明解析还没完成
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //获取节点名
                String nodeName = xmlPullParser.getName();
                //通过事件属性进行解析
                switch (eventType) {
                    //开始解析某节点
                    case XmlPullParser.START_TAG: {
                        if ("id".equals(nodeName)) {
                            //如果发现节点名等于id name version 就调用nextText方法获取具体内容
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    }
                    //解析完某一节点 打印获得的内容
                    case XmlPullParser.END_TAG:{
                        if ("app".equals(nodeName)) {
                            Log.d(TAG, "id is " + id);
                            Log.d(TAG, "name is " + name);
                            Log.d(TAG, "version is " + version);
                        }
                        break;
                    }
                    default:
                        break;
                }
                //继续获取下一个解析事件
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequestWithHttpURLConnection(){
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    //new一个URL对象 传入目标网址 通过openConnection获得实例
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    //设置请求方法 主要有两种 GET获取数据和POST提交数据
                    connection.setRequestMethod("GET");

                    //如果想提交数据给服务器 改请求方法即可 并在获取输入流之前 写好提交数据即可 例如
//                    connection.setRequestMethod("POST");
//                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes("username=admin&password=123456");
                    //每条数据都要以键值对形式存在 每条数据以&隔开

                    //设置一些连接超时读取超时的属性 根据需求自由定制
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    //记一下这里的流程
                    InputStream in = connection.getInputStream();
                    //对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    //显示响应结果
                    showResponse(response.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //关闭连接
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();   //开启线程并运行
    }

    /*
    将返回的数据显示出来

    Android不允许在子线程中进行UI操作 通过runOnUiThread切换到主线程中 进行UI更新
     */
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }

}
