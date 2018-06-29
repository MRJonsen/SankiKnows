package com.zc.pickuplearn.ui.teamcircle.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.ui.base.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class TeamDetailMainFragment extends BaseFragment {

    @BindView(R.id.tl_indicator)
    TabLayout mTablayout;
    @BindView(R.id.vp_home_content)
    ViewPager mViewPage;
    SparseArray<BaseFragment> mFragment = new SparseArray<>();//存放fragment
    ArrayList<String> title = new ArrayList<>();
    private static final String TEAM_CIRCLE_BEAN_TAG = "TeamDetailMainFragment_team_circle_bean";
    private static final String TAG_INDEX = "TeamDetailMainFragment_index";

    public static TeamDetailMainFragment newInstance(TeamCircleBean teamCircleBean, TeamWenDaFragment.TeamWenDaType type) {
        TeamDetailMainFragment fragment = new TeamDetailMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(TEAM_CIRCLE_BEAN_TAG, teamCircleBean);
        args.putSerializable(TAG_INDEX, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_detail_main;
    }

    @Override
    public void initView() {
        TeamCircleBean teamCircleBean = (TeamCircleBean) getArguments().getSerializable(TEAM_CIRCLE_BEAN_TAG);
        TeamWenDaFragment.TeamWenDaType wenDaType = (TeamWenDaFragment.TeamWenDaType) getArguments().getSerializable(TAG_INDEX);
        mFragment.put(0, TeamWenDaFragment.newInstance(TeamWenDaFragment.TeamWenDaType.ACTION, teamCircleBean));
        mFragment.put(1, TeamWenDaFragment.newInstance(TeamWenDaFragment.TeamWenDaType.CHALLENGE, teamCircleBean));
        mFragment.put(2, TeamWenDaFragment.newInstance(TeamWenDaFragment.TeamWenDaType.SOLVE, teamCircleBean));
        mFragment.put(3, TeamWenDaFragment.newInstance(TeamWenDaFragment.TeamWenDaType.QUSETION, teamCircleBean));
        title.add("我来挑战");
        title.add("你问我答");
        title.add("已解决");
        title.add("我的问题");
        mViewPage.setOffscreenPageLimit(4);//ViewPage设置缓存的页面个数解决后面数据加载重复问题
        mViewPage.setAdapter(new DynamicFragmentAdapter(getChildFragmentManager(), mFragment));
        mTablayout.setupWithViewPager(mViewPage);
        mViewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));

        if (TeamWenDaFragment.TeamWenDaType.ACTION.equals(wenDaType)) {
            mViewPage.setCurrentItem(0);
        } else if (TeamWenDaFragment.TeamWenDaType.CHALLENGE.equals(wenDaType)) {
            mViewPage.setCurrentItem(1);
        } else if (TeamWenDaFragment.TeamWenDaType.QUSETION.equals(wenDaType)) {
            mViewPage.setCurrentItem(3);
        }
    }


    public class DynamicFragmentAdapter extends FragmentPagerAdapter {
        SparseArray<BaseFragment> mData;

        public DynamicFragmentAdapter(FragmentManager fm, SparseArray<BaseFragment> mFragment) {
            super(fm);
            mData = mFragment;
        }

        @Override
        public Fragment getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }
}
