package com.example.photowallfallsdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.io.File;

public class ImageDetailsActivity extends Activity implements ViewPager.OnPageChangeListener{

    //用于管理图片的滑动及显示图片页数
    private ViewPager viewPager;
    private TextView pageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
        setContentView(R.layout.image_details);

        int imagePosition = getIntent().getIntExtra("image_position", 0);
        pageText = findViewById(R.id.page_text);
        viewPager = findViewById(R.id.view_pager);
        //设置适配器及参数
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(imagePosition);
        viewPager.addOnPageChangeListener(this);
        //设置当前页数及总页数
        pageText.setText((imagePosition + 1) + "/" + Images.imageUrls.length);
    }

    //ViewPager适配器
    class ViewPagerAdapter extends PagerAdapter {

        //实例化项目
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //获取图片路径进行编译
            String imagePath = getImagePath(Images.imageUrls[position]);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.empty_photo);
            }
            //实例化View并返回
            View view = LayoutInflater.from(ImageDetailsActivity.this).inflate(R.layout.zoom_image_layout, null);
            ZoomImageView zoomImageView = view.findViewById(R.id.zoom_image_view);
            //设置Bitmap对象
            zoomImageView.setImageBitmap(bitmap);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return Images.imageUrls.length;   //获取图片总数
        }

        @Override
        public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
            return arg0 == arg1;   //判断两个参数是否相等
        }

        //把应该销毁的View对象进行回收 防止图片过多导致OOM
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    //获取图片本地存储路径
    private String getImagePath(String imageUrl) {

        //获取文件名
        int lastSlashIndex = imageUrl.lastIndexOf("/");
        String imageName = imageUrl.substring(lastSlashIndex + 1);
        //获取文件夹
        String imageDir = Environment.getExternalStorageDirectory().getPath() + "/PhotoWallFalls/";
        File file = new File(imageDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        //返回文件路径
        String imagePath = imageDir + imageName;
        return imagePath;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int currentPage) {
        //每当页数发生改变时 重新设定当前页数及总页数
        pageText.setText((currentPage + 1) + "/" + Images.imageUrls.length);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
