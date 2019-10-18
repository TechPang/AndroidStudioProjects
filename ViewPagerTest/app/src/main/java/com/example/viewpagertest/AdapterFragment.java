package com.example.viewpagertest;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class AdapterFragment extends FragmentPagerAdapter {

    //FragmentPagerAdapter和FragmentStatePagerAdapter的实现一样

    private List<Fragment> mFragments;
    public AdapterFragment(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);   //必须实现
    }

    @Override
    public int getCount() {
        return mFragments.size();   //必须实现
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getClass().getSimpleName();   //选择性实现
    }
}
