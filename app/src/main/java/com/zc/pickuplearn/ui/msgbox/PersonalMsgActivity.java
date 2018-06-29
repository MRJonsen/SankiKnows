package com.zc.pickuplearn.ui.msgbox;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.MsgBean;
import com.zc.pickuplearn.event.IMMessageEvent;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.im.chatting.ConversationListFragment;
import com.zc.pickuplearn.ui.msgbox.event.EventPersonMsg;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.SystemUtils;
import com.zc.pickuplearn.utils.UIUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;

/**
 * 个人消息
 */
public class PersonalMsgActivity extends EventBusActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.vp_home_content)
    ViewPager mViewPage;
    @BindView(R.id.cb_edit)
    CheckBox mCbEdit;
    @BindView(R.id.ctl_indicator)
    SlidingTabLayout ctl_indicator;
    ArrayList<String> title = new ArrayList<>();
    private boolean isEditMode = false;
    SparseArray<Fragment> mFragment = new SparseArray<>();//存放fragment

    public static void startPersonalMsgActivity(Context context) {
        Intent intent = new Intent(context, PersonalMsgActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_presonal_msg;
    }

    @Override
    public void initView() {
        title.add("通知");
        title.add("系统");
        title.add("会话");
        mFragment.put(0, PersonMSGFragment.newInstance("1", "2"));
        mFragment.put(1, SystemMsgFragment.newInstance("", ""));
        mFragment.put(2, ConversationListFragment.newInstance(ConversationListFragment.ListType.MIX));
        mViewPage.setOffscreenPageLimit(2);//ViewPage设置缓存的页面个数解决后面数据加载重复问题
        mViewPage.setAdapter(new DynamicFragmentAdapter(getSupportFragmentManager(), mFragment));
//        mTablayout.setupWithViewPager(mViewPage);
//        mViewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        String location = getIntent().getStringExtra("location");
        if (!TextUtils.isEmpty(location)) {
            mViewPage.setCurrentItem(2);
        }
        ctl_indicator.setViewPager(mViewPage);
        setTabWidth();
    }

    private void setTabWidth() {
        int screenWidth = SystemUtils.getScreenWidth(this);
        ctl_indicator.setTabWidth(UIUtils.px2dip(screenWidth / 3));
        getTab1Msg();
    }

    @Override
    protected void initData() {

    }

    public void getTab1Msg() {
        API.getUserInfoAndMsg(new CommonCallBack<MsgBean>() {
            @Override
            public void onSuccess(MsgBean msgBean) {
                if (msgBean != null) {
                    if (!TextUtils.isEmpty(msgBean.getPERINFOCOUNT())) {
                        if (Integer.valueOf(msgBean.getPERINFOCOUNT()) > 0) {
                            setTab1Msg(Integer.valueOf(msgBean.getPERINFOCOUNT()));//给我的 设置消息提示
                        } else {
                            setTab1Msg(0);
                        }
                    } else {
                        setTab1Msg(0);
                    }
                } else {
                    setTab1Msg(0);
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }
    public void setTab1Msg(final int msg) {
        if (msg <= 0) {
            ctl_indicator.hideMsg(0);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ctl_indicator.showDot(0);
                    ctl_indicator.showMsg(0, msg);
                }
            });
        }
    }


    public void setTab3Msg() {
        final int allUnReadMsgCount = JMessageClient.getAllUnReadMsgCount();
        Log.e("重新", "" + allUnReadMsgCount);
        if (allUnReadMsgCount <= 0) {
            ctl_indicator.hideMsg(2);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ctl_indicator.showDot(2);
                    ctl_indicator.showMsg(2, allUnReadMsgCount);
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTab3Msg();
    }

    @OnClick({R.id.iv_back, R.id.cb_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.cb_edit:
//                    if (mCbEdit.isChecked()) {
//                        mCbEdit.setText("取消");
//                    } else {
//                        mCbEdit.setText("编辑");
//                    }
//                    if (mViewPage.getCurrentItem() == 0) {
//                        isEditMode = !isEditMode;
//                        PersonMSGFragment personMSGFragment = (PersonMSGFragment) mFragment.get(0);
//                        personMSGFragment.setMsgEditMode(isEditMode);
//                    }
                if (mViewPage.getCurrentItem() == 0) {
                    if (mCbEdit.isChecked()) {
                        isEditMode = true;
                        mCbEdit.setText("取消");
                    } else {
                        isEditMode = false;
                        mCbEdit.setText("编辑");
                    }
                    PersonMSGFragment personMSGFragment = (PersonMSGFragment) mFragment.get(0);
                    personMSGFragment.setMsgEditMode(isEditMode);
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(IMMessageEvent event) {
        setTab3Msg();
    }

    @Subscribe
    public void onEventMainThread(EventPersonMsg event) {
        getTab1Msg();
    }

    public class DynamicFragmentAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> mData;

        public DynamicFragmentAdapter(FragmentManager fm, SparseArray<Fragment> mFragment) {
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
