package com.example.uibestpractice;

public class Msg {

    //接收信息
    public static final int TYPE_RECEIVED = 0;
    //发送信息
    public static final int TYPE_SENT = 1;

    //设置content内容和type消息类型两个字段
    private String content;
    private int type;

    public Msg(String content , int type){
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
