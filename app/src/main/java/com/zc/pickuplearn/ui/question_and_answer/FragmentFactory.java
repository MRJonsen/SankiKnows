package com.zc.pickuplearn.ui.question_and_answer;


import android.util.SparseArray;

import com.zc.pickuplearn.ui.base.BaseFragment;


/**
 * 知道问答功能模块fragment持有类
 * created by bin 2016/12/2 19:19
 */

class FragmentFactory {
    private static SparseArray<BaseFragment> holder = new SparseArray<>();

    static BaseFragment createFragment(int position,DynamicType dynamicType) {
        BaseFragment baseFragment;
        baseFragment = holder.get(position);
        if (baseFragment != null) {
            //根据索引获取到了fragment对象,直接返回即可
            return baseFragment;
        } else {
            switch (position) {
                case 0:
                    baseFragment = QuestionListFragment.newInstance(TypeQuestion.NEWLY,null,dynamicType);
                    break;
                case 1:
                    baseFragment = QuestionListFragment.newInstance(TypeQuestion.RECOMMEND,null,dynamicType);
                    break;
                case 2:
                    baseFragment = QuestionListFragment.newInstance(TypeQuestion.REWARD,null,dynamicType);
                    break;
                default:
                    baseFragment = null;
                    break;
            }
            holder.put(position, baseFragment);
            return baseFragment;
        }
    }

    public static void clear(){
            if (holder!=null){
                holder.clear();
            }
    }
}
