package com.zc.pickuplearn.ui.main_pick_up_learn;


import android.util.SparseArray;

import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.mine.mine.view.MineFragment;
import com.zc.pickuplearn.ui.msgbox.PersonalMsgFragment;
import com.zc.pickuplearn.ui.teamcircle.view.TeamCircleFragment;

/**
 * 主界面四个功能模块fragment管理类
 * created by bin 2016/12/2 19:19
 */

public class FragmentFactory {
    public static SparseArray<BaseFragment> hashMap = new SparseArray<>();

    public static BaseFragment createFragment(int position) {
        BaseFragment baseFragment;
        baseFragment = hashMap.get(position);
        if (baseFragment != null) {
            //根据索引获取到了fragment对象,直接返回即可
            return baseFragment;
        } else {
            //在没有此索引指向fragment的时候创建逻辑
            switch (position) {
                case 0:
                    baseFragment = PulMainFragment.newInstance("", "");
                    break;
                case 1:
                    baseFragment = new TeamCircleFragment();
                    break;
                case 2:
                    baseFragment = PersonalMsgFragment.newInstance("","");
                    break;
                case 3:
                    baseFragment = new MineFragment();
                    break;
                default:
                    baseFragment = null;
                    break;
            }
            hashMap.put(position, baseFragment);
            return baseFragment;
        }
    }
}
