package com.example.filepersistencetest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.InflaterOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit = findViewById(R.id.edit);

        //从文件中读取数据
        String inputText = load();
        //TextUtils.isEmpty该方法可以不用单独判断null和空的字符串
        if(!TextUtils.isEmpty(inputText)){
            edit.setText(inputText);
            //将输入光标移至末尾以便继续输入
            edit.setSelection(inputText.length());
            Toast.makeText(this , "Restoring succeeded" , Toast.LENGTH_SHORT).show();
        }

    }

    //保证在销毁前保存数据
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = edit.getText().toString();
        save(inputText);
    }

    //将数据储存到文件中
    public void save(String inputText) {
        //openFileOutput返回的是一个FileOutputStream对象
        FileOutputStream out;
        BufferedWriter writer = null;
        try {
            //文件的操作模式主要两种 MODE_PRIVATE和APPEND
            out = openFileOutput("data", Context.MODE_PRIVATE);
            //通过OutputStreamWriter构建对象 可以将内容写入文件中
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //从文件中读取数据
    public String load() {
        StringBuilder content = new StringBuilder();
        FileInputStream in;
        BufferedReader reader = null;
        try{
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            //当line不为空时 追加内容
            String line;
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

}
