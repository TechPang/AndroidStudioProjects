package com.example.recyclerviewtest;

//定义一个Fruit类 作为ListView适配器的适配类型
public class Fruit {

    private String name;
    private int imageId;

    //创建构造函数 返回name和imageId值
    public Fruit(String name , int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
