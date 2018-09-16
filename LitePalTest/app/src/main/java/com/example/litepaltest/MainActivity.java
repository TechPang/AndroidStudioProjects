package com.example.litepaltest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建数据库
        Button createDatabase = findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "创建数据库");
                //只要一行 不管多少模型类需要映射 都要添加到list
                LitePal.getDatabase();
            }
        });

        //添加数据
        Button addData = findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "添加数据");
                //如果需要对数据库进行CRUD操作 则模型类需要继承LitePalSupport

                //添加数据只需要实例化Book 对数值进行设置 最后save即可
                Book book = new Book();
                book.setName("The Da Vinci Code");
                book.setAuthor("Dan Brown");
                book.setPages(454);
                book.setPrice(16.96);
                book.setPress("Unknow");
                book.save();
            }
        });

        //更新数据
        Button updateData = findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "更新数据");
                Book book = new Book();
//                book.setName("The Da Vinci Code");
//                book.setAuthor("Dan Brown");
//                book.setPages(454);
//                book.setPrice(16.96);
//                book.setPress("Unknow");
//                book.save();
//                //可以通过重新赋值进行更新数据
//                book.setPrice(10.99);
//                book.save();
                //通过updateAll方法进行约束列更新数据
                book.setPrice(14.95);
                book.setPress("Anchor");
                book.updateAll("name = ? and author = ?" , "The Lost Symbol" , "Dan Brown");
                //例如上面 意思是把以上是这个书名和作者的价格和出版社改为set值

                //如果想设置默认值 通过setToDefault传入列名即可
//                book.setToDefault("pages");
//                book.updateAll();
            }
        });

        //删除数据
        Button deleteButton = findViewById(R.id.delete_data);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "删除数据");
                //调用deleteAll方法传入类名即可 第二个参数为约束条件
                LitePal.deleteAll(Book.class , "price < ?" , "15");
            }
        });

        //查询数据
        Button queryButton = findViewById(R.id.query_data);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "查询数据");
                List<Book> books = LitePal.findAll(Book.class);
                for (Book book : books) {
                    Log.d(TAG, "book name is " + book.getName());
                    Log.d(TAG, "book author is " + book.getAuthor());
                    Log.d(TAG, "book pages is " + book.getPages());
                    Log.d(TAG, "book price is " + book.getPrice());
                    Log.d(TAG, "book price is " + book.getPress());
                }
            }
        });

    }
}
