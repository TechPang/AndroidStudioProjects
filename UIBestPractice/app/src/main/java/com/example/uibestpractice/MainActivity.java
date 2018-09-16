package com.example.uibestpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //创建泛型为Msg的List
    private List<Msg> msgList = new ArrayList<>();
    //实例化控件
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        draw9patch作图 保证图片在拉伸时不失真
        鼠标控制 shift擦除
        黑边记忆
        左边是用于在竖直方向无限拉伸
        顶部是用于在水平方向无限拉伸
        右边是用于竖直方向内容显示区域
        底部是用于水平方向内容显示区域
         */

        //初始化消息数据源
        initMsgs();
        //实例化控件
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        //创建LinearLayoutManager的RecyclerView布局方式 效果类似于ListView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        //实例化适配器并设置
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //捕捉输入框内容 当非空时添加到msgList
                String content = inputText.getText().toString();
                if(!"".equals(content)){
                    Msg msg = new Msg(content , Msg.TYPE_SENT);
                    msgList.add(msg);
                    //当有新消息时 刷新RecyclerView中的显示
                    adapter.notifyItemInserted(msgList.size() - 1);
                    //将RecyclerView定位到最后一行
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    //清空输入框
                    inputText.setText("");
                }
            }
        });

    }

    //预设消息数据
    private void initMsgs(){
        Msg msg1 = new Msg("Hello guy.", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello.Who is that?", Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is Tom.Nice talking to you.", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }

}
