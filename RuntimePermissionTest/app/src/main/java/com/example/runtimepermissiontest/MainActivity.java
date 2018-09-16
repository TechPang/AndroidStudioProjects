package com.example.runtimepermissiontest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //通过隐式Intent直接打电话
        Button makeCall = findViewById(R.id.make_call);
        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //对权限进行判断
                if(ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    //如果没有权限 则申请权限
                    ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.CALL_PHONE} , 1);
                }else {
                    call();
                }

            }
        });

    }

    //封装call方法
    private void call(){
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //申请权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                }else {
                    Toast.makeText(this , "You denied the permission" , Toast.LENGTH_SHORT).show();
                }
                break;
                default:
        }
    }
}
