package com.zc.pickuplearn.ui.dynamic.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.category.QuestionClassification;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view.WenDaFragment;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 动态（问答）模块
 * created by bin 2016/11/30 14:12
 */

public class DynamicFragment extends BaseFragment implements IDynamicView {
    @BindView(R.id.tl_indicator)
    TabLayout mTablayout;
    @BindView(R.id.vp_dynamic_content)
    ViewPager mViewPage;
    SparseArray<BaseFragment> mFragment = new SparseArray<>();//存放fragment

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public void initView() {
        mFragment.put(0, WenDaFragment.newInstance(WenDaFragment.THE_RECOMMENT,false,null,""));
        mFragment.put(1, WenDaFragment.newInstance(WenDaFragment.THE_NEW,false,null,""));
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
            String title = "";
            switch (position) {
                case 0:
                    title = "推荐";
                    break;
                case 1:
                    title = "最新";
                    break;
            }
            return title;
        }
    }
    @OnClick({R.id.bt_category,R.id.bt_ask})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_ask:
               QuestionClassification.startQuestionClassfication(getmCtx(),QuestionClassification.TIWEN);
                break;
            case R.id.bt_category:
               QuestionClassification.startQuestionClassfication(getmCtx(),QuestionClassification.WENTI);
                break;
        }
    }
}
