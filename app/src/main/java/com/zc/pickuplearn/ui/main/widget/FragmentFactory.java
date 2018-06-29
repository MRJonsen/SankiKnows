package com.zc.pickuplearn.ui.main.widget;


import android.util.SparseArray;

import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.group.view.GroupFragment;
import com.zc.pickuplearn.ui.home_new.view.HomeNewFragment;
import com.zc.pickuplearn.ui.mine.mine.view.MineFragment;
import com.zc.pickuplearn.ui.teamcircle.view.TeamCircleFragment;

/**
  *  主界面四个功能模块fragment持有类
  *  created by bin 2016/12/2 19:19
  */

class FragmentFactory {
//	private static HashMap<Integer, BaseFragment> hashMap = new HashMap<Integer, BaseFragment>();
	public static SparseArray<BaseFragment> hashMap = new SparseArray<>();
	static BaseFragment createFragment(int position) {
		BaseFragment baseFragment;
		baseFragment = hashMap.get(position);
		if(baseFragment !=null){
			//根据索引获取到了fragment对象,直接返回即可
			return baseFragment;
		}else{
			//在没有此索引指向fragment的时候创建逻辑
			switch (position) {
			case 0:
				baseFragment = new HomeNewFragment();
				break;
			case 1:
				baseFragment = new GroupFragment();
				break;
			case 2:
//				baseFragment = new DynamicFragment();
				baseFragment = new TeamCircleFragment();
//				baseFragment = WebViewFragment.newInstance(UIUtils.getString(R.string.school_url),UIUtils.getString(R.string.tab_tag_school));
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
