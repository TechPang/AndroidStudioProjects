package com.example.listviewtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FruitAdapter extends ArrayAdapter<Fruit> {

    private int resourceId;

    //构造函数 传递上下文 子项布局id 数据
    public FruitAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Fruit> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    //重写getView方法 当子项滚动到屏幕内时调用
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fruit fruit = getItem(position);
        //获得当前项的实例
//        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        //加载当前项的布局
        /*
        LayoutInflater.from 创建对象
        inflate传入布局id 父布局
        第三个参数设置false 如果View有了父布局 不会被添加到ListView
         */

        //优化性能 convertView
        View view;

        //优化性能 ViewHolder
        ViewHolder viewHolder;

        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

            //优化性能 ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.fruitImage = view.findViewById(R.id.fruit_image);
            viewHolder.fruitName = view.findViewById(R.id.fruit_name);
            view.setTag(viewHolder);
            //把控件实例化到ViewHolder并储存到view
        }else {
            view = convertView;

            //优化性能 ViewHolder
            viewHolder = (ViewHolder) view.getTag();
            //直接调用ViewHolder

        }
        /*
        getView方法会对布局每次重新加载 这对性能有所要求
        convertView会对布局进行缓存
        当convertView为空时 则通过LayoutInflater加载布局 反之重用
         */

//        ImageView fruitImage = view.findViewById(R.id.fruit_image);
//        TextView fruitName = view.findViewById(R.id.fruit_name);
        //获取fruitImage和fruitName的实例
//        fruitImage.setImageResource(fruit.getImageId());
//        fruitName.setText(fruit.getName());

        //优化性能 ViewHolder
        viewHolder.fruitImage.setImageResource(fruit.getImageId());
        viewHolder.fruitName.setText(fruit.getName());
        /*
        通过该方法可以直接通过ViewHolder取值 不用每次实例化控件
         */

        return view;
    }

    //新增内部类ViewHolder 实例化两个控件
    class ViewHolder {
        ImageView fruitImage;
        TextView fruitName;
    }
}
