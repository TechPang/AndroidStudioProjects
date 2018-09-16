package com.example.databasetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //实例化SQLiteOpenHelper对象进行创建数据库
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);
        Button createDatabase = findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });

        //添加数据
        Button addData = findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //组装第一条数据
                values.put("name" , "The Da Vinci Code");
                values.put("author" , "Dan Brown");
                values.put("pages" , 454);
                values.put("price" , 16.96);
                //插入第一条数据
                db.insert("Book", null, values);
                values.clear(); //清空数据
                //组装第二条
                values.put("name" , "The Lost Symbol");
                values.put("author" , "Dan Brown");
                values.put("pages" , 510);
                values.put("price" , 19.95);
                db.insert("Book", null, values);
            }
        });

        //更新数据
        Button updateData = findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price" , 10.99);
                db.update("Book", values, "name = ?", new String[] {"The Da Vinci Code"});
                /*
                update方法会接收四个参数 后两个用于约束更新某行或者几行的数据 不指定就默认所有行
                第三个对应sql的where 更新所有name=？的行 ？是个占位符 可以通过字符串数组指定对应内容
                例如上面的 将名字为The Da Vinci Code的书籍价格改为10.99
                 */
            }
        });

        //删除数据
        Button deleteButton = findViewById(R.id.delete_data);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book", "pages > ?", new String[] {"500"});
            }
        });

        //查询数据
        Button queryButton = findViewById(R.id.query_data);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //调用query方法会返回一个cursor对象
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if(cursor.moveToFirst()) {
                    //遍历Cursor对象 取值打印
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG, "book name is " + name);
                        Log.d(TAG, "book author is " + author);
                        Log.d(TAG, "book pages is " + pages);
                        Log.d(TAG, "book price is " + price);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

    }
}
