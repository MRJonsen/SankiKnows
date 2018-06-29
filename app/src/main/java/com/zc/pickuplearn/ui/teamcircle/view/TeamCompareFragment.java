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


public class TeamCompareFragment extends BaseFragment {

    @BindView(R.id.tl_indicator)
    TabLayout mTablayout;
    @BindView(R.id.vp_home_content)
    ViewPager mViewPage;
    SparseArray<BaseFragment> mFragment = new SparseArray<>();//存放fragment
    ArrayList<String> title = new ArrayList<>();
    private static final String TEAM_CIRCLE_BEAN_TAG = "TeamCompareFragment_team_circle_bean";

    public static TeamCompareFragment newInstance(TeamCircleBean teamCircleBean) {
        TeamCompareFragment fragment = new TeamCompareFragment();
        Bundle args = new Bundle();
        args.putSerializable(TEAM_CIRCLE_BEAN_TAG, teamCircleBean);
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
        mFragment.put(0, TeamCompareDetailFragment.newInstance(TeamCompareDetailFragment.Type.QUESTION,teamCircleBean));
        mFragment.put(1, TeamCompareDetailFragment.newInstance(TeamCompareDetailFragment.Type.ANSWER,teamCircleBean));
        title.add("提问榜");
        title.add("回答榜");
        mViewPage.setOffscreenPageLimit(2);//ViewPage设置缓存的页面个数解决后面数据加载重复问题
        mViewPage.setAdapter(new DynamicFragmentAdapter(getChildFragmentManager(), mFragment));
        mTablayout.setupWithViewPager(mViewPage);
        mViewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
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
