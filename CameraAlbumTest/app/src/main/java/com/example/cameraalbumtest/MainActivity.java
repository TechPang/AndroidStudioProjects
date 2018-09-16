package com.example.cameraalbumtest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    private ImageView picture;
    private Uri imageUri;

    //从相册中选择照片
    public static final int CHOOSE_PHOTO = 2;

    //处理请求码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        //通过decodeStream解析照片为Bitmap对象 设置到ImageView中
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

                //从相册中选择照片
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {

                    //判断手机系统版本进行处理照片
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    }else {
                        //低于4.4系统的使用该方法
                        handleImageBeforeKitKat(data);
                    }
                }
                break;

                default:
                    break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //实例化控件
        Button takePhoto = findViewById(R.id.take_photo);
        picture = findViewById(R.id.picture);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //创建File对象 储存拍照后的照片
                File outputImage = new File(getExternalCacheDir(), "outputImage.jpg");
                /*
                调用getExternalCacheDir应用关联缓存目录可以跳过运行时权限
                 */
                try {
                    //如果存在则删除
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    //创建新文件
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                //针对SDK版本进行处理
                if(Build.VERSION.SDK_INT >=24){
                    //通过FileProvider的getUriForFile 三个参数
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
                }else {
                    //如果低于Android7.0版本 则通过fromFile获取图片的真实路径
                    imageUri = Uri.fromFile(outputImage);
                }

                //启动相机
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //指定图片输出地址
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent , TAKE_PHOTO);

            }
        });

        //从相册中选择照片
        Button chooseFromAlbum = findViewById(R.id.choose_form_album);
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有权限
                if(ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} , 1);
                }else {
                    openAlbum();
                }
            }
        });
    }

    //打开相册
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        //设置必要的参数
        intent.setType("image/*");
        startActivityForResult(intent , CHOOSE_PHOTO);
    }

    //申请权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openAlbum();
            }else {
                Toast.makeText(this , "You denied the permission" ,Toast.LENGTH_SHORT).show();
            }
            break;
            default:
        }
    }

    //针对不同系统的手机处理图片
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();

        //如果是document类型的Uri 则通过document id处理
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);

            //media 需要再次解析 通过字符串分割的方式取出后半部分获取真正的数字id
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

                //downloads
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
        //如果是content类型的Uri 则通过普通方式处理
        else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri, null);
        }
        //如果是file类型的Uri 直接获取图片路径即可
        else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        //根据图片路径显示图片
        displayImage(imagePath);
    }

    //低于4.4系统的手机则通过该方法处理照片
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    //获取图片路径
    private String getImagePath(Uri uri , String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //显示照片
    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this, "failed to get image" , Toast.LENGTH_SHORT).show();
        }
    }

}
