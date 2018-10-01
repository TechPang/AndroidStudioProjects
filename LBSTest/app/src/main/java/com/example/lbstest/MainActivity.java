package com.example.lbstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*
    public一个定位监听器类 接口BDLocationListener
    接收位置 append经纬度 定位方式通过locType判断 GPS还是网络 最后设置显示

    实例化控件和Client 获取应用全文 注册监听器
    对权限进行组装数组进行申请 将没有申请到权限的添加到权限组 3个
    如果权限组不为空则进行申请 否则执行获取位置方法

    申请权限 通过foreach方法 != finish return 执行获取位置方法 否则发生未知错误 finish
     */

    private LocationClient mLocationClient;
    private TextView positionText;
    private MapView mapView;
    private BaiduMap baiduMap;   //百度地图总控制
    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实例化Client并注册
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        //使用百度地图 初始化操作需要在setContentView前面 不然会报错
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        //把我显示在地图上 需要将此功能开启 退出程序时也要关闭
        baiduMap.setMyLocationEnabled(true);

        positionText = findViewById(R.id.position_text_view);
        //组装权限数组
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        //如果权限数组不为空 则返回权限数组长度 进行申请权限
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this , permissions , 1 );
        }else {
            requestLocation();
        }

    }

    /*
    通过LatLng传入当前位置的经纬度
    再通过MapStatusUpdate传入经纬度进行更新
    再更新缩放级别 一般3-19
    isFirstLocate防止多次调用animateMapStatus方法 因为第一次定位只需要调用一次
     */

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            //缩放级别
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;   //防止多次调用
        }
        //把我显示在地图上
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        //获取经纬度
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        //通过build把经纬度封装到locationData中
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    //申请权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    //通过循环对权限进行判断 有拒绝的直接finish掉
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意全部权限才可以使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
        }
    }

    //执行方法
    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    //实时更新定位
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);   //每隔5秒更新定位
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);   //指定定位模式 默认Height_Accuracy模式 即只要有GPS信号 就自动切换模式
        option.setIsNeedAddress(true);   //获取详细信息
        mLocationClient.setLocOption(option);
    }

    //需要调用onResume onPause onDestroy对百度地图进行管理 及时释放资源
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    //销毁活动时停止定位 否则不断的定位耗电
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();   //同时销毁
        baiduMap.setMyLocationEnabled(false);   //关闭功能
    }

    //public一个定位监听器类 接口BDLocationListener
    public class MyLocationListener extends BDAbstractLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location) {
//            StringBuilder currentPosition = new StringBuilder();
//            //获取经纬度
//            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
//            currentPosition.append("经度：").append(location.getLongitude()).append("\n");
//            //获取定位详细信息
//            currentPosition.append("国家：").append(location.getCountry()).append("\n");
//            currentPosition.append("省份：").append(location.getProvince()).append("\n");
//            currentPosition.append("城市：").append(location.getCity()).append("\n");
//            currentPosition.append("区域：").append(location.getDistrict()).append("\n");
//            currentPosition.append("街道：").append(location.getStreet()).append("\n");
//
//            currentPosition.append("定位方式：");
//            //通过getType获取定位方式
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                currentPosition.append("GPS");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                currentPosition.append("网络");
//            }
//            //显示界面
//            positionText.setText(currentPosition);

            //定位到我的位置
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }

        }
    }

}
