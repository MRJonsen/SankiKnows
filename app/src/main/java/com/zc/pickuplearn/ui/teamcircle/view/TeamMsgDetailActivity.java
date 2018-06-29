package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamMessageBean;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.view.ExpandableTextView;
import com.zc.pickuplearn.ui.view.FullViewPager;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.SHsegmentControl.SegmentControl;
import com.zc.pickuplearn.utils.DateUtils;

import butterknife.BindView;

/**
 * 团队通知详细
 */
public class TeamMsgDetailActivity extends BaseActivity {

    @BindView(R.id.item)
    LinearLayout ll_item;//回执布局
    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.et_mark)
    EditText etMark;
    @BindView(R.id.tv_pusher)
    TextView tvPusher;
    @BindView(R.id.tv_push_time)
    TextView tvPushTime;
    @BindView(R.id.segment_control)
    SegmentControl mSegmentHorzontal;
    @BindView(R.id.vp_content)
    FullViewPager mViewPage;
    String[] mTitle = new String[]{"未回执(0)", "已回执(0)"};
    private static final String Tag = "TeamMsgDetailActivity";
    private TeamMessageBean messageBean;
    SparseArray<BaseFragment> mFragment = new SparseArray<>();//存放fragment

    public static void startTeamMsgDetailActivity(Context context, TeamMessageBean messageBean) {
        Intent intent = new Intent(context, TeamMsgDetailActivity.class);
        intent.putExtra(Tag, messageBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_msg_detail;
    }

    @Override
    public void initView() {
        hvHead.setTitle("团队通知");
    }

    @Override
    protected void initData() {
        messageBean = (TeamMessageBean) getIntent().getSerializableExtra(Tag);
        setMsgView(messageBean);
        if (messageBean!=null&&!("1".equals(messageBean.getISRECEIPT()))){
            ll_item.setVisibility(View.GONE);
            return;
        }
        mSegmentHorzontal.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                mViewPage.setCurrentItem(index);
            }
        });
        initFragement();
        mViewPage.setAdapter(new PersonLisgFragmentAdapter(getSupportFragmentManager(), mFragment));
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSegmentHorzontal.setSelectedIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void setSegmentText(int index, String num) {
        switch (index) {
            case 0:
                if (!TextUtils.isEmpty(num)) {
                    mTitle[index] = "未回执(" + num + ")";
                }
                break;
            case 1:
                if (!TextUtils.isEmpty(num)) {
                    mTitle[index] = "已回执(" + num + ")";
                }
                break;
        }
        mSegmentHorzontal.setText(mTitle[0], mTitle[1]);

    }


    private void initFragement() {
            mFragment.put(0, TeamMsgStatusPersonFragment.newInstance(TeamMsgStatusPersonFragment.Type.NO_RESP, messageBean));
            mFragment.put(1, TeamMsgStatusPersonFragment.newInstance(TeamMsgStatusPersonFragment.Type.IS_RESP, messageBean));
    }

    private void setMsgView(TeamMessageBean messageBean) {
        if (messageBean != null) {
            tvPusher.setText(messageBean.getUSERNAME());
            tvPushTime.setText(DateUtils.dataFormatNow("yyyy-MM-dd HH:mm:ss", messageBean.getSYSCREATEDATE()));
            etMsg.setText(messageBean.getMESSAGE());
            etMark.setText(messageBean.getREMARK());
        }
    }

    public class PersonLisgFragmentAdapter extends FragmentPagerAdapter {
        SparseArray<BaseFragment> mData;

        public PersonLisgFragmentAdapter(FragmentManager fm, SparseArray<BaseFragment> mFragment) {
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

    }
}
