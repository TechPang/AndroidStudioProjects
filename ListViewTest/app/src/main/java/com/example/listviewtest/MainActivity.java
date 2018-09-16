package com.example.listviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //ListView简单用法
//    private String[] data = { "Apple" , "Banana" , "Orange" , "Watermelon" , "Pear" , "Grape" ,
//            "Pineapple" , "StrawBerry" , "Cherry" , "Mango" , "Apple" , "Banana" , "Orange" ,
//            "Watermelon" , "Pear" , "Grape" ,"Pineapple" , "StrawBerry" , "Cherry" , "Mango"};

    //定制ListView界面
    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ListView简单用法
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
//        ListView listView = findViewById(R.id.list_view);
//        listView.setAdapter(adapter);
        /*
        创建一组数据作为展示
        创建适配器并实例化 依次传参 上下文 ListView子项布局id data数据 simple_list_item_1是系统内置布局文件 只有一个TextView
        实例化控件 并setAdapter
         */

        //定制ListView界面
        initFruits();
        //初始化水果数据
        FruitAdapter adapter = new FruitAdapter(MainActivity.this, R.layout.fruit_item, fruitList);
        //实例化适配器 上下文 子项布局id 数据
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        //ListView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fruit fruit = fruitList.get(position);
                Toast.makeText(MainActivity.this , fruit.getName() , Toast.LENGTH_SHORT).show();
            }
        });

    }

    //定制ListView界面
    private void initFruits(){
        for (int i = 0; i < 2; i++) {
            Fruit apple = new Fruit("Apple", R.drawable.apple_pic);
            fruitList.add(apple);
            Fruit banana = new Fruit("Banana", R.drawable.banana_pic);
            fruitList.add(banana);
            Fruit orange = new Fruit("Orange", R.drawable.orange_pic);
            fruitList.add(orange);
            Fruit watermelon = new Fruit("Watermelon", R.drawable.watermelon_pic);
            fruitList.add(watermelon);
            Fruit pear = new Fruit("Pear", R.drawable.pear_pic);
            fruitList.add(pear);
            Fruit grape = new Fruit("Grape", R.drawable.grape_pic);
            fruitList.add(grape);
            Fruit pineapple = new Fruit("Pineapple", R.drawable.pineapple_pic);
            fruitList.add(pineapple);
            Fruit strawberry = new Fruit("Strawberry", R.drawable.strawberry_pic);
            fruitList.add(strawberry);
            Fruit cherry = new Fruit("Cherry", R.drawable.cherry_pic);
            fruitList.add(cherry);
            Fruit mango = new Fruit("Mango", R.drawable.mango_pic);
            fruitList.add(mango);
        }
    }

}
