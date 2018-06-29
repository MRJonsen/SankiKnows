package com.zc.pickuplearn.ui.teamcircle.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.UrlType;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.base.WebViewFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class TeamTestLibraryMainFragment extends BaseFragment {

    @BindView(R.id.tl_indicator)
    TabLayout mTablayout;
    @BindView(R.id.vp_home_content)
    ViewPager mViewPage;
    public SparseArray<BaseFragment> mFragment = new SparseArray<>();//存放fragment
    ArrayList<String> title = new ArrayList<>();
    private static final String TAG_INDEX = "TeamTestLibraryMainFragment_index";
    private static final String TEAM_CIRCLE_BEAN_TAG = "TeamTestLibraryMainFragment_team_circle_bean";
    public static TeamTestLibraryMainFragment newInstance(TeamCircleBean teamCircleBean) {
        TeamTestLibraryMainFragment fragment = new TeamTestLibraryMainFragment();
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
        mFragment.put(0, WebViewFragment.newInstance("", "", false));
//        mFragment.put(0, WebViewFragment.newInstance("http://117.149.2.229:1623/ecm/ZUI/pages/dzd/taskPractice.html?userID=90000400&teamID=6474", "", false));
//        mFragment.put(1,EmptyFragment.newInstance());
//        mFragment.put(2, WebViewFragment.newInstance(UIUtils.getString(R.string.test_library), "", false));
        title.add("每日一练");
//        title.add("团队测试");
//        title.add("配置管理");
        mViewPage.setOffscreenPageLimit(1);//ViewPage设置缓存的页面个数解决后面数据加载重复问题
        mViewPage.setAdapter(new DynamicFragmentAdapter(getChildFragmentManager(), mFragment));
        mTablayout.setupWithViewPager(mViewPage);
        mTablayout.setVisibility(View.GONE);
        mViewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        API.getTestLibraryUrl(teamCircleBean, UrlType.DailyPractice,new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                WebViewFragment baseFragment = (WebViewFragment) mFragment.get(0);
                baseFragment.reload(s);
            }

            @Override
            public void onFailure() {
                WebViewFragment baseFragment = (WebViewFragment) mFragment.get(0);
                baseFragment.reload("http://117.149.2.229:1623/ecm/ZUI/pages/dzd/taskPractice.html?userID=90000400&teamID=6474");
            }
        });
    }

    public WebViewFragment getWebFragment(){
        return (WebViewFragment) mFragment.get(0);
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
