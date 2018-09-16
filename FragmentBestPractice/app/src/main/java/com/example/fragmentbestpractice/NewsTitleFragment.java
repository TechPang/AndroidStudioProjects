package com.example.fragmentbestpractice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsTitleFragment extends Fragment {

    private boolean isTwoPane;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //判断加载模式
        if (getActivity().findViewById(R.id.news_content_layout) != null){
            isTwoPane = true; //如果可以找到news_content_layout 加载双页模式
        }else {
            isTwoPane = false; //反之
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.news_title_frag , container , false);

        //实例化RecyclerView
        RecyclerView newsTitleRecyclerView = view.findViewById(R.id.news_title_recycler_view);
        //实例化布局方式并设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        newsTitleRecyclerView.setLayoutManager(layoutManager);
        //实例化适配器并设置
        NewsAdapter adapter = new NewsAdapter(getNews());
        newsTitleRecyclerView.setAdapter(adapter);

        return view;

    }

    //生成随机新闻内容
    private String getRandomLengthContent(String content){
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(content);
        }
        return builder.toString();
    }

    //新闻内容数据源
    private List<News> getNews(){
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            News news = new News();
            news.setTitle("This is news title " + i);
            news.setContent(getRandomLengthContent("This is news content" + i + "."));
            newsList.add(news);
        }
        return newsList;
    }

    //创建内部类作为RecyclerView的适配器
    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

        //创建全局变量mNewsList
        private List<News> mNewsList;
        public NewsAdapter(List<News> newsList){
            mNewsList = newsList;
        }

        //传入子项并实例化ViewHolder返回
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);

            //创建点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //获取当前位置的新闻内容实例
                    News news = mNewsList.get(holder.getAdapterPosition());
                    //判断模式进行加载新闻内容
                    if(isTwoPane){
                        //双页模式 实例化碎片需要类似于控件的向下转型
                        NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
                        //刷新界面 获取内容
                        newsContentFragment.refresh(news.getTitle() , news.getContent());
                    }else {
                        //单页模式 直接启动NewsContentActivity
                        NewsContentActivity.actionStart(getActivity() , news.getTitle() , news.getContent());
                    }

                }
            });

            return holder;
        }

        //获取当前位置输出
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            //通过List获取位置
            News news = mNewsList.get(position);
            holder.newsTitleText.setText(news.getTitle());

        }

        //返回长度
        @Override
        public int getItemCount() {
            return mNewsList.size();
        }

        //实例化子项控件
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView newsTitleText;
            public ViewHolder(View view) {
                super(view);
                newsTitleText = view.findViewById(R.id.news_title);
            }
        }

    }

}
