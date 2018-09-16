package com.example.activitytest;

import android.content.Intent;
import android.net.Uri;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends BaseActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main , menu);
        return true;

        /*
        inflate接收参数
        第一个指定我们通过哪个资源文件创建菜单
        第二个指定返回到哪个Menu对象
        返回true显示菜单 反之
         */
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(this , "You clicked Add" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this , "You clicked Remove" , Toast.LENGTH_SHORT).show();
                break;
                default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //活动的启动模式
//        Log.d(TAG, this.toString());

        setContentView(R.layout.first_layout);

        Button button1 = findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(FirstActivity.this, "You clicked button1" , Toast.LENGTH_SHORT).show();
                /*
                Toast提醒信息
                 */

//                finish();
                /*
                添加finish()方法可以销毁活动
                 */

//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                startActivity(intent);
                /*
                显式intent
                第一个参数传入一个启动活动的上下文
                第二个参数传入目标活动的class
                再通过startActivity启动活动
                 */

//                Intent intent = new Intent("com.example.activitytest.ACTION_START");
//                intent.addCategory("com.example.activitytest.MY_CATEGORY");
//                startActivity(intent);
                /*
                隐式intent
                匹配action和category信息进行启动活动
                每个intent只能指定一个action 可以多个category
                添加新的category 需要在intent-filter里声明
                 */

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("https://www.baidu.com/"));
//                startActivity(intent);

//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:10086"));
//                startActivity(intent);
                /*
                隐式intent不仅可以内部活动 也可以启动其他程序活动 例如调用本地浏览器
                通过Intent.ACTION_VIEW内置动作执行
                可以通过data标签精确指定响应什么类型的数据
                一般不需要指定太多 通过scheme指定数据的协议就够 例如http tel部分
                也可以地理位置geo 拨号tel之类的
                DIAL拨号内置动作
                 */

                //向下一个活动传递数据
//                String data = "Hello SecondActivity";
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                intent.putExtra("extra_data", data);
//                startActivity(intent);

                //返回数据给上一个活动
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                startActivityForResult(intent , 1);
                /*
                设置请求码
                 */

                //活动的启动模式
//                Intent intent = new Intent(FirstActivity.this, FirstActivity.class);
//                startActivity(intent);

                //singleTop
//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                startActivity(intent);
                /*
                活动的启动模式分4种
                standard singleTop singleTask singleInstance
                在activity里面添加LaunchMode即可

                一般活动默认standard模式
                缺点是不断创建新的活动实例 需要不断的点击back键才可以退出程序

                singleTop
                当活动处于栈顶 不会重新创建
                缺点是当重新返回到这个活动时 需要重新创建

                singleTask
                只会创建一个实例 执行检查 没有则创建

                singleInstance
                设置SecondActivity启动模式为singleInstance
                可以实现共享一个活动的实例
                因为同个活动返回不同的返回栈 会创建新的实例
                被指定这个模式的活动会创建一个新的返回栈进行管理

                当处于同一个返回栈的活动都back完了 会显示另一个返回栈 同理back完 自然退出程序
                 */

                //活动的最佳写法
                SecondActivity.actionStart(FirstActivity.this , "data1" , "data2");
                /*
                保持好的代码习惯
                该写法可以通过参数了解启动SecondActivity需要传递什么数据
                并可以简写启动活动的代码
                 */

            }
        });

        textView = findViewById(R.id.textView1);

        //活动的启动模式singleInstance
        Log.d(TAG, "Task id is" + getTaskId());

    }

    //活动的启动模式singleTask
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d(TAG, "onRestart: 执行了");
//    }

    private TextView textView;
    private static final String TAG = "FirstActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //第一行代码 方法一
//        switch (requestCode){
//            case 1:
//                if(resultCode == RESULT_OK){
//                    String returnedData = data.getStringExtra("data_return");
//                    Log.d(TAG, returnedData);
//                }
//                break;
//                default:
//        }
        /*
        先传入请求码判断数据来源 再判断处理结果 取值打印
         */

        //慕课网 方法二
        if(requestCode == 1 && resultCode == RESULT_OK){
            String returnedData = data.getStringExtra("data_return");
            textView.setText(returnedData);
        }
        /*
        方法二更容易理解 双重判断requestCode和resultCode进行取值 setText设置文本

        注意 如果想要在重写方法中调用控件 需要private实例化控件 不可以直接TextView实例化
         */

    }

}
