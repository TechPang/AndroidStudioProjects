package com.ryg.chapter_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ryg.chapter_2.manager.UserManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "UserManage.sUserId=" + UserManager.sUserId);
        UserManager.sUserId = 2;

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "UserManage.sUserId=" + UserManager.sUserId);
    }


//    {
//        try {
//            User user = new User(0, "jake", true);
//            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("cache.text"));
//            out.writeObject(user);
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    {
//        try {
//            ObjectInputStream in = new ObjectInputStream(new FileInputStream("cache.txt"));
//            User newUser = (User) in.readObject();
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
