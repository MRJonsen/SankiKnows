package com.zc.pickuplearn.ui.main_pick_up_learn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zc.pickuplearn.ui.main_pick_up_learn.FragmentFactory;

/**
 * 首页切换适配器
 * Created by chenbin on 2017/9/4.
 */

public class TabViewPageAdapter extends FragmentPagerAdapter {
    private int size;

    public TabViewPageAdapter(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(int position) {
        return  FragmentFactory.createFragment(position);
    }

    @Override
    public int getCount() {
        return size;
    }

}
