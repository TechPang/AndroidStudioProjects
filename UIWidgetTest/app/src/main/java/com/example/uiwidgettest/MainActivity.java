package com.example.uiwidgettest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //注意用法
//    private EditText editText;
//    private ImageView imageView;
    private ProgressBar progressBar;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        //注意用法
//        editText = findViewById(R.id.edit_text);
//        imageView = findViewById(R.id.image_view);
        progressBar = findViewById(R.id.progress_bar);

        //匿名内部类
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //待处理的逻辑
//            }
//        });

        //接口方式
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                //待处理的逻辑

                //获取输入框内容并显示
//                String inPutText = editText.getText().toString();
//                Toast.makeText(MainActivity.this , inPutText , Toast.LENGTH_SHORT).show();

                //点击更换图片
//                imageView.setImageResource(R.drawable.img_4);

                //进度条
//                if(progressBar.getVisibility() == View.GONE){
//                    progressBar.setVisibility(View.VISIBLE);
//                }else {
//                    progressBar.setVisibility(View.GONE);
//                }
                /*
                设置可见度 visible可见 gone不可见 invisible不可见但占位置
                 */

//                int progress = progressBar.getProgress();
//                progress = progress + 10;
//                progressBar.setProgress(progress);
                /*
                设置进度条max值
                通过点击按钮动态更新进度条
                 */

                //对话框 确认操作
//                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                dialog.setTitle("This is dialog");
//                dialog.setMessage("Something important.");
//                dialog.setCancelable(false);
//                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d(TAG, "你选择了OK");
//                    }
//                });
//                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d(TAG, "你选择了Cancel");
//                    }
//                });
//                dialog.show();
                /*
                实例化AlertDialog
                设置标题内容 可否back键返回操作
                设置确认和取消按钮的点击事件 最后show()方法显示对话框
                 */

                //加载进度条
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("This is ProgressDialog");
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                /*
                类似AlertDialog
                如果对setCancelable传入false 需要调用dismiss()方法关闭对话框
                 */

                break;
                default:
                    break;
        }
    }
}
