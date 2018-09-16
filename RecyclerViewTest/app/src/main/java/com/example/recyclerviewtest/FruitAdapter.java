package com.example.recyclerviewtest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

//继承RecyclerView适配器 指定泛型ViewHolder(内部类)
public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    //创建泛型为Fruit的List
    private List<Fruit> mFruitList;
    public FruitAdapter(List<Fruit> fruitList){
        mFruitList = fruitList;
    }

    //创建ViewHolder传入子项布局并实例化
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
//        ViewHolder holder = new ViewHolder(view);

        //RecyclerView的点击事件
        final ViewHolder holder = new ViewHolder(view);

        //最外层布局的点击事件 也可写个TextView的点击事件
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(v.getContext() , "You Clicked View" + fruit.getName() , Toast.LENGTH_SHORT).show();
            }
        });

        //ImageView的点击事件
        holder.fruitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(v.getContext() , "you Clicked Image" + fruit.getName() , Toast.LENGTH_SHORT).show();
            }
        });

        return holder;
    }

    //获取当前项并输出
    @Override
    public void onBindViewHolder(@NonNull FruitAdapter.ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getName());
    }

    //返回mFruitList的数量
    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    //实例化控件 储存在ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {

        //RecyclerView的点击事件
        View fruitView;

        ImageView fruitImage;
        TextView fruitName;

        public ViewHolder(View view) {
            super(view);

            fruitView = view;

            fruitImage = view.findViewById(R.id.fruit_image);
            fruitName = view.findViewById(R.id.fruit_name);
        }
    }
}
