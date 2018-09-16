package com.example.providertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String newId;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //添加数据
        Button addData = findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "添加数据");
                //解析Uri对象
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
                //添加数据
                ContentValues values = new ContentValues();
                values.put("name" , "A Clash of Kings");
                values.put("author" , "George Martin");
                values.put("pages" , 1040);
                values.put("price", 22.85);
                //insert方法会返回一个新的Uri对象 亦可以不返回 是为了获取新id
                Uri newUri = getContentResolver().insert(uri, values);
                //通过getPathSegments方法取出新的id
                newId = newUri.getPathSegments().get(1);
            }
        });

        //查询数据
        Button queryData = findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "查询数据");
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d(TAG, "Book name is " + name);
                        Log.d(TAG, "Book author is " + author);
                        Log.d(TAG, "Book pages is " + pages);
                        Log.d(TAG, "Book price is " + price);
                    }
                    cursor.close();
                }
            }
        });

        //更新数据
        Button updateData = findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "更新数据");
                //指定id是为了避免其他行受影响 只更新newId的数据
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book/" + newId);
                ContentValues values = new ContentValues();
                values.put("name" , "A Storm of Swords");
                values.put("pages", 1216);
                values.put("price", 24.05);
                getContentResolver().update(uri, values, null, null);
            }
        });

        //删除数据
        Button deleteData = findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "删除数据");
                //指定id 删除某行数据
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book/" + newId);
                getContentResolver().delete(uri, null, null);
            }
        });

    }
}
