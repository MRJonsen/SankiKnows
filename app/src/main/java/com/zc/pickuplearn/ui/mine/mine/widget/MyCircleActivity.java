package com.zc.pickuplearn.ui.mine.mine.widget;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.group.widget.professional.view.ProfessionFragment;
import com.zc.pickuplearn.ui.view.HeadView;

import butterknife.BindView;

/**
 * 我的圈子
 */
public class MyCircleActivity extends BaseActivity {
    @BindView(R.id.hv_header)
    HeadView mHead;
    @BindView(R.id.tl_indicator)
    TabLayout mTablayout;
    @BindView(R.id.vp_circle_content)
    ViewPager mViewPage;

    SparseArray<BaseFragment> mFragment = new SparseArray<>();
    private FragmentManager fragmentManager;
    public static final String TAG = "MyCircleActivity";

    public static void startMyCircleActivity(Context context){
        Intent intent = new Intent(context,MyCircleActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int setLayout() {
        return R.layout.activity_my_circle;
    }

    @Override
    public void initView() {
        mHead.setTitle(getResources().getString(R.string.my_circle));
    }

    @Override
    protected void initData() {
        mFragment.put(0, ProfessionFragment.newInstance(TAG));
//        mFragment.put(1, new TeamFragment());
        mViewPage.setAdapter(new GroupFragmentAdapter(getSupportFragmentManager(), mFragment));
        mTablayout.setupWithViewPager(mViewPage);
        mViewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
    }
    public class GroupFragmentAdapter extends FragmentPagerAdapter {
        SparseArray<BaseFragment> mData;

        public GroupFragmentAdapter(FragmentManager fm, SparseArray<BaseFragment> mFragment) {
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
                    title = "专业圈";
                    break;
                case 1:
                    title = "班组圈";
                    break;
            }
            return title;
        }
    }
}
