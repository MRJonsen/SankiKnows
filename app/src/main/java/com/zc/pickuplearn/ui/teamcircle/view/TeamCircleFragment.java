package com.zc.pickuplearn.ui.teamcircle.view;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.dalong.francyconverflow.FancyCoverFlow;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.msgbox.PersonalMsgActivity;
import com.zc.pickuplearn.ui.teamcircle.adapter.MyFancyCoverFlowAdapter;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamDynamicAdpter;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamDynamicBean;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;


public class TeamCircleFragment extends BaseFragment {

    @BindView(R.id.fancyCoverFlow)
    FancyCoverFlow mfancyCoverFlow;
    MyFancyCoverFlowAdapter mMyFancyCoverFlowAdapter;
    @BindView(R.id.hv_headbar)
    HeadView hvHeadbar;//顶部导航栏
    @BindView(R.id.rc_team_dynamic)
    LRecyclerView rcTeamDynamic;
    @BindView(R.id.tv_mine_new_msg_num)
    TextView tvMyNewMsg;
    List<TeamCircleBean> mFancyCoverFlows = new ArrayList<>();
    List<TeamCircleBean> mTeamCircleBean = new ArrayList<>();//提供查询的
    List<TeamDynamicBean> mTeamDynamicBean = new ArrayList<>();//提供查询的
    private LRecyclerViewAdapter lRecyclerViewAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_circle;
    }

    @Override
    public void initView() {
        initTopBar();
        initTopView();
        initTeamDynamic();
        //处理状态栏及间距
        hvHeadbar.setBarBg(getResources().getColor(R.color.transparent));
        StatusBarUtil.darkMode((Activity) getmCtx());
        StatusBarUtil.setPaddingSmart(getmCtx(), getRootView());
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
    /**
     * 班组圈动态控件初始化
     */
    private void initTeamDynamic() {
        rcTeamDynamic.setHasFixedSize(true);
        rcTeamDynamic.setLayoutManager(new LinearLayoutManager(getContext()));
        rcTeamDynamic.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        TeamDynamicAdpter teamDynamicAdpter = new TeamDynamicAdpter(getContext(), mTeamDynamicBean);
        //适配器转换
        lRecyclerViewAdapter = new LRecyclerViewAdapter(teamDynamicAdpter);
        rcTeamDynamic.setAdapter(lRecyclerViewAdapter);
        rcTeamDynamic.setLoadMoreEnabled(false);
        rcTeamDynamic.setPullRefreshEnabled(false);
    }

    /**
     * topBar 初始化
     */
    private void initTopBar() {
        if (hvHeadbar != null) {
            hvHeadbar.setTitle(getResources().getString(R.string.tab_tag_team));
            hvHeadbar.setLeftBtnVisable(false);
        }
    }

    /**
     * 头部旋转控件初始化
     */
    private void initTopView() {
        for (int i = 0; i < 2; i++) {
            TeamCircleBean teamCircleBean = new TeamCircleBean();
            mFancyCoverFlows.add(teamCircleBean);
        }
        mMyFancyCoverFlowAdapter = new MyFancyCoverFlowAdapter(getmCtx(), mFancyCoverFlows);
        mfancyCoverFlow.setAdapter(mMyFancyCoverFlowAdapter);
        mMyFancyCoverFlowAdapter.notifyDataSetChanged();
        mfancyCoverFlow.setUnselectedAlpha(0.5f);//通明度
        mfancyCoverFlow.setUnselectedSaturation(0.5f);//设置选中的饱和度
        mfancyCoverFlow.setUnselectedScale(0.3f);//设置选中的规模
        mfancyCoverFlow.setSpacing(0);//设置间距
        mfancyCoverFlow.setMaxRotation(0);//设置最大旋转
        mfancyCoverFlow.setScaleDownGravity(0.5f);
        mfancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
        int num = Integer.MAX_VALUE / 2 % mFancyCoverFlows.size();
//        int selectPosition = Integer.MAX_VALUE / 2 - num;
        int selectPosition = 1;
        mfancyCoverFlow.setSelection(selectPosition);
        mfancyCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = (position % mFancyCoverFlows.size());
                if (position == 0 || position == mFancyCoverFlows.size() - 1) {
                    TeamListActivity.startTeamListActivity(getmCtx());
                } else {
                    TeamCircleBean teamCircleBean = mFancyCoverFlows.get(position);
                    teamCircleBean.setSEQKEY(teamCircleBean.getTEAMCODE());
                    TeamDetailActivity.starTeamDetailActivity(getmCtx(), teamCircleBean, TeamWenDaFragment.TeamWenDaType.ACTION);//点击头部进去团队 默认你问我答界面
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void clearHeadData() {
        mFancyCoverFlows.clear();
        for (int i = 0; i < 2; i++) {
            TeamCircleBean teamCircleBean = new TeamCircleBean();
            mFancyCoverFlows.add(teamCircleBean);
        }
        mMyFancyCoverFlowAdapter.notifyDataSetChanged();
    }

    private void initData() {
        API.getMyJoinTeamList(new CommonCallBack<List<TeamCircleBean>>() {
            @Override
            public void onSuccess(List<TeamCircleBean> teamCircleBeen) {
                clearHeadData();
                mTeamCircleBean.clear();
                mTeamCircleBean.addAll(teamCircleBeen);
                mFancyCoverFlows.addAll(1, teamCircleBeen);
                mMyFancyCoverFlowAdapter.notifyDataSetChanged();
                //查询动态
                API.getTeamDynamic(mTeamCircleBean, new CommonCallBack<List<TeamDynamicBean>>() {
                    @Override
                    public void onSuccess(List<TeamDynamicBean> teamDynamicBeen) {
                        mTeamDynamicBean.clear();
                        mTeamDynamicBean.addAll(teamDynamicBeen);
                        lRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });
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
