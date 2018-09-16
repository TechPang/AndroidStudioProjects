package com.example.photowallfallsdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyScrollView extends ScrollView implements View.OnTouchListener {

    public static final int PAGE_SIZE = 15;   //每页需要加载的图片数量
    private int page;   //记录加载到第几页
    private int columnWidth;   //每一列的宽度

    private int firstColumnHeight;   //当前第一列的高度
    private int secondColumnHeight;   //当前第二列的高度
    private int thirdColumnHeight;   //当前第三列的高度

    private boolean loadOnce;   //是否加载过Layout 初始化只需要加载一次
    private ImageLoader imageLoader;   //对图片进行管理的工具类

    private LinearLayout firstColumn;   //第一列布局
    private LinearLayout secondColumn;   //第二列布局
    private LinearLayout thirdColumn;   //第三列布局

    //记录正在下载或者等待下载的任务
    private static Set<LoadImageTask> taskCollection;

    private static View scrollLayout;   //MyScrollView下的直接子布局
    private static int scrollViewHeight;   //MyScrollView布局的高度
    private static int lastScrollY = -1;  //记录垂直方向的滚动距离
    private List<ImageView> imageViewList = new ArrayList<ImageView>();   //记录界面上的照片 可以控制对图片的释放

    //对图片的可见性检查进行判断 以及加载更多图片的操作
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //获取滚动距离
            MyScrollView myScrollView = (MyScrollView) msg.obj;
            int scrollY = myScrollView.getScrollY();
            //如果滚动位置和上次一样 则表示已停止滚动
            if (scrollY == lastScrollY) {
                //当滚动到底部时 并且当前没有正在下载任务 开始加载下一页的图片
                if (scrollViewHeight + scrollY >= scrollLayout.getHeight() && taskCollection.isEmpty()) {
                    myScrollView.loadMoreImages();
                }
                //对图片的可见性进行检查
                myScrollView.checkVisibility();
            }else {
                lastScrollY = scrollY;
                Message message = new Message();
                message.obj = myScrollView;
                //5毫秒再进行滚动位置进行判断
                handler.sendMessageDelayed(message, 5);
            }
        }
    };

    //遍历imageViewList中每张图片 对图片的可见性进行检查 如果图片已经离开可见区域 则换成空图
    public void checkVisibility(){
        //遍历图片
        for (int i = 0; i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);

            //获取位置 传入图片地址进行加载
            int borderTop = (Integer) imageView.getTag(R.string.border_top);
            int borderBottom = (Integer) imageView.getTag(R.string.border_bottom);
            if (borderBottom > getScrollY() && borderTop < getScrollY() + scrollViewHeight) {
                String imageUrl = (String) imageView.getTag(R.string.image_url);
                Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);
                //当图片重新为可见时 直接从LruCache中获取
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }else {
                    //如果已经被移除 则开启下载任务 重新加载
                    LoadImageTask task = new LoadImageTask(imageView);
                    task.execute(i);   //传入图片的位置
                }
            }else {
                //将不可见的图片切换成空图 可以保证程序不会占用过高内存
                imageView.setImageResource(R.drawable.empty_photo);
            }
        }
    }

    //MyScrollView构造函数
    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取实例
        imageLoader = ImageLoader.getInstance();
        taskCollection = new HashSet<LoadImageTask>();
        setOnTouchListener(this);
    }

    //关键性初始化操作 获取MyScrollView的高度 以及第一列的宽度 开始加载第一页的图片
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //开始初始化
        if (changed && !loadOnce) {
            scrollViewHeight = getHeight();
            scrollLayout = getChildAt(0);   //获取第一列
            firstColumn = findViewById(R.id.first_column);
            secondColumn = findViewById(R.id.second_column);
            thirdColumn = findViewById(R.id.third_column);
            columnWidth = firstColumn.getWidth();   //获取第一列的宽度
            loadOnce = true;   //布局加载完
            loadMoreImages();   //加载图片
        }
    }

    //监听用户触屏事件 当用户离开屏幕时开始滚动检测
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //当用户离开屏幕时
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Message message = new Message();
            message.obj = this;
            handler.sendMessageDelayed(message, 5);
        }
        return false;
    }

    //开始加载下一页图片 每张图片开启异步线程去下载
    public void loadMoreImages() {
        if (hasSDCard()) {
            int startIndex = page * PAGE_SIZE;
            int endIndex = page * PAGE_SIZE + PAGE_SIZE;
            if (startIndex < Images.imageUrls.length) {
                Toast.makeText(getContext(), "正在加载中...", Toast.LENGTH_SHORT).show();
                if (endIndex > Images.imageUrls.length) {
                    endIndex = Images.imageUrls.length;
                }
                //加载图片任务
                for (int i = startIndex; i < endIndex; i++) {
                    LoadImageTask task = new LoadImageTask();
                    taskCollection.add(task);
                    task.execute(i);   //传入图片的位置
                }
                page++;
            }else {
                Toast.makeText(getContext(), "已没有更多图片", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    //判断是否有SD卡
    private boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    //异步下载图片的任务 修改泛型为Integer
    class LoadImageTask extends AsyncTask<Integer, Void, Bitmap> {

        private String mImageUrl;
        private ImageView mImageView;   //可重复使用的ImageView

        private int mItemPosition;   //记录每张图片的位置

        public LoadImageTask() {
        }

        //将可重复使用的ImageView传入
        public LoadImageTask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {

            //接收Integer数组 传入的参数代表了图片的位置
            mItemPosition = params[0];
            mImageUrl = Images.imageUrls[mItemPosition];

            Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
            if (imageBitmap == null) {
                //传入图片Url地址加载图片
                imageBitmap = loadImage(mImageUrl);
            }
            return imageBitmap;
        }

        //执行后
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                //计算比例高度
                double ratio = bitmap.getWidth() / (columnWidth * 1.0);
                int scaledHeight = (int) (bitmap.getHeight() / ratio);
                //添加图片
                addImage(bitmap, columnWidth, scaledHeight);
            }
            taskCollection.remove(this);
        }

        //加载图片方法 根据传入的Url进行加载 如果已存在SD卡 直接读取 反之
        private Bitmap loadImage(String imageUrl) {
            //根据Url获取文件路径
            File imageFile = new File(getImagePath(imageUrl));
            if (!imageFile.exists()) {
                downloadImage(imageUrl);   //如果文件不存在则加载
            }
            if (imageUrl != null) {
                //如果Url不为空 进行图片处理
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(), columnWidth);
                if (bitmap != null) {
                    //如果bitmap不为空 则把图片添加到LruCache
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                    return bitmap;
                }
            }
            return null;
        }

        //向ImageView中添加一张图片
        private void addImage(Bitmap bitmap, int imageWidth, int imageHeight) {
            //设置布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            if (mImageView != null) {
                mImageView.setImageBitmap(bitmap);
            }else {
                //设置ImageView
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);   //不按比例缩放图片 塞满整个View
                imageView.setPadding(5, 5, 5, 5);
                imageView.setTag(R.string.image_url, mImageUrl);

                //查看图片 点击事件
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ImageDetailsActivity.class);
                        intent.putExtra("image_position", mItemPosition);   //传递图片对应的位置
                        getContext().startActivity(intent);
                    }
                });

                //判断添加到哪一列中
                findColumnToAdd(imageView, imageHeight).addView(imageView);
                imageViewList.add(imageView);
            }
        }

        //判断应该添加图片到哪一列 原则是添加到当前高度最小的那一列
        private LinearLayout findColumnToAdd(ImageView imageView, int imageHeight) {
            if (firstColumnHeight <= secondColumnHeight) {
                if (firstColumnHeight <= thirdColumnHeight) {
                    //对比高度之后 如果第一列高度最小 则添加到第一列
                    imageView.setTag(R.string.border_top, firstColumnHeight);
                    firstColumnHeight += imageHeight;
                    //将新的高度值赋予border_bottom
                    imageView.setTag(R.string.border_bottom, firstColumnHeight);
                    return firstColumn;
                }
                imageView.setTag(R.string.border_top, thirdColumnHeight);
                thirdColumnHeight += imageHeight;
                imageView.setTag(R.string.border_bottom, thirdColumnHeight);
                return thirdColumn;
            }else {
                if (secondColumnHeight <= thirdColumnHeight) {
                    imageView.setTag(R.string.border_top, secondColumnHeight);
                    secondColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, secondColumnHeight);
                    return secondColumn;
                }
                imageView.setTag(R.string.border_top, thirdColumnHeight);
                thirdColumnHeight += imageHeight;
                imageView.setTag(R.string.border_bottom, thirdColumnHeight);
                return thirdColumn;
            }
        }

        //将图片下载到SD卡缓存起来
        private void downloadImage(String imageUrl) {
            HttpURLConnection con = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File imageFile = null;

            try {
                URL url = new URL(imageUrl);   //传入图片地址
                con = (HttpURLConnection) url.openConnection();   //打开连接
                //设置超时参数
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(15 * 1000);
                //打开输出入端
                con.setDoOutput(true);
                con.setDoInput(true);

                bis = new BufferedInputStream(con.getInputStream());   //获取输入流
                imageFile = new File(getImagePath(imageUrl));
                fos = new FileOutputStream(imageFile);
                bos = new BufferedOutputStream(fos);   //接收文件输出流

                byte[] b = new byte[1024];
                int length;
                while ((length = bis.read(b)) != -1) {
                    bos.write(b, 0, length);
                    bos.flush();   //写入SD卡
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //各种善后
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    if (con != null) {
                        con.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //将图片添加到LruCache
            if (imageFile != null) {
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(), columnWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                }
            }
        }

        //获取图片本地路径
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
            //返回图片路径
            String imagePath = imageDir + imageName;
            return imagePath;
        }

    }

}
