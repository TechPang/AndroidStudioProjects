package com.example.photowallfallsdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

public class ImageLoader {

    //图片缓存技术核心类 用于缓存下载好的图片 当程序内存到达设定值时 将最近少用的图片资源释放掉
    private static LruCache<String, Bitmap> mMemoryCache;

    //实例化ImageLoader
    private static ImageLoader mImageLoader;

    private ImageLoader() {
        //获取程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;   //设置图片缓存大小为程序可用内存的1/8

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    //获取ImageLoader实例
    public static ImageLoader getInstance(){
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader();
        }
        return mImageLoader;
    }

    //将一张图片存储到LruCache中
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);   //key传入图片Url地址 bitmap传入网上下载的Bitmap对象
        }
    }

    //从LruCache中获取图片 如果不存在则返回null
    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    //计算样本大小
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {

        //源图片宽度
        final int width = options.outWidth;
        int inSampleSize = 1;
        //计算获取样本大小
        if (width > reqWidth) {
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }

    //对图片进行处理
    public static Bitmap decodeSampledBitmapFromResource(String pathName, int reqWidth) {
        //第一次解析将inJustDecodeBounds设置为true 用于获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(pathName, options);
        //调用方法计算样本大小
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        //使用获取到的样本大小再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

}
