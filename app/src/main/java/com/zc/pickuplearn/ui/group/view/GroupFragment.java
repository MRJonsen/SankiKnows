package com.zc.pickuplearn.ui.group.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.group.widget.professional.view.ProfessionFragment;
import com.zc.pickuplearn.ui.msgbox.PersonalMsgActivity;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;

/**
 * 圈子模块
 * created by bin 2016/11/30 14:09
 */

public class GroupFragment extends BaseFragment {
    @BindView(R.id.hv_headbar)
    HeadView mHeadView;
    @BindView(R.id.tl_indicator)
    TabLayout mTablayout;
    @BindView(R.id.vp_circle_content)
    ViewPager mViewPage;
    @BindView(R.id.tv_mine_new_msg_num)
    TextView tvMyNewMsg;
    SparseArray<BaseFragment> mFragment = new SparseArray<>();

    @Override
    public void initView() {
//        mHeadView.setLeftBtnVisable(false);
        mHeadView.setTitle("圈子");
        mFragment.put(0, ProfessionFragment.newInstance(ProfessionFragment.mTAG));
//        mFragment.put(1, new TeamFragment());
        mViewPage.setAdapter(new GroupFragmentAdapter(getChildFragmentManager(), mFragment));
        mTablayout.setupWithViewPager(mViewPage);
        mViewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_group;
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
    public void setMyNewMsg(String msg){
        if (tvMyNewMsg!=null){
            int allUnReadMsgCount = JMessageClient.getAllUnReadMsgCount();
            LogUtils.e("会话消息"+allUnReadMsgCount);
            if (!TextUtils.isEmpty(msg)){
                Integer integer = Integer.valueOf(msg);
                if (integer >0){
                    allUnReadMsgCount=allUnReadMsgCount+integer;
                }
            }
            if (allUnReadMsgCount>0){
                LogUtils.e("消息总数"+allUnReadMsgCount);
                tvMyNewMsg.setVisibility(View.VISIBLE);
                if (allUnReadMsgCount > 99) {
                    tvMyNewMsg.setText("99+");
                } else {
                    tvMyNewMsg.setText(allUnReadMsgCount + "");
                }
            }else {
                tvMyNewMsg.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.rl_mine_new_msg)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rl_mine_new_msg:
                PersonalMsgActivity.startPersonalMsgActivity(getmCtx());
                break;
        }
    }
}
