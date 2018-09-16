package com.example.materialtest;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //要多锻炼解决问题能力 代码写少了 bug经历少了 小伙子

    private DrawerLayout mDrawerLayout;

    //用于储存展现的水果
    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter adapter;

    //定义水果数组
    private Fruit[] fruits = {new Fruit("Apple", R.drawable.apple),
            new Fruit("Banana", R.drawable.banana),
            new Fruit("Orange", R.drawable.orange),
            new Fruit("Watermelon", R.drawable.watermelon),
            new Fruit("Pear", R.drawable.pear),
            new Fruit("Grape", R.drawable.grape),
            new Fruit("Pineapple", R.drawable.pineapple),
            new Fruit("Strawberry", R.drawable.strawberry),
            new Fruit("Mango", R.drawable.mango)};

    //刷新控件
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //实例化设置toolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //实例化DrawerLayout
        mDrawerLayout = findViewById(R.id.drawer_layout);
        //获取ActionBar实例
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //显示导航栏按钮 设置图标 点击事件
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        NavigationView navView = findViewById(R.id.nav_view);
        //默认选中菜单项 设置选中监听器
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //这里执行具体逻辑 简单关闭滑动菜单
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //悬浮按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this , "FAB is clicked" , Toast.LENGTH_SHORT).show();
                /*
                Snackbar 给用户提供可交互提醒工具 需要传入View对象 setAction设置动作
                可以被监听是因为悬浮按钮就是子控件 如果传入View对象是DrawerLayout就监听不到了
                 */
                Snackbar.make(v , "Data deleted" , Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        //初始化水果 实例化RecyclerView 设置布局方式 设置适配器
        initFruits();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);   //设置一行两列
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL);   //瀑布流布局 列数 排序方式 还有LinearLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(fruitList);   //传入适配器的构造方法参数即可
        recyclerView.setAdapter(adapter);

        //刷新水果
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新水果
                refreshFruits();
            }
        });

    }

    //刷新水果方法
    private void refreshFruits(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //需要将线程沉睡2秒 因为本地刷新比较快 防止看不到刷新过程
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //切换主线程 进行UI操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //刷新水果数据 通知数据的改变 隐藏刷新条
                        initFruits();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    //重写创建菜单 指定资源文件和传参
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;   //显示菜单 反之
    }
    //设置Menu不是为了看的 重写点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //传参获取id
        switch (item.getItemId()) {
            //规定HomeAsUp的ID
            case android.R.id.home:
                //设置 统一和XML定义的一致 GravityCompat.START
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
                default:
        }
        return true;
    }

    //初始化水果 随机挑选
    private void initFruits(){
        fruitList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

}
