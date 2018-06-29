package com.zc.pickuplearn.ui.teamcircle.view;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RadioGroup;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamMessageBean;
import com.zc.pickuplearn.beans.TeamMsgStatsUserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamMsgStatusPersonListAapter;
import com.zc.pickuplearn.ui.view.LayoutManager.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TeamMsgStatusPersonFragment extends BaseFragment {

    @BindView(R.id.lc_list)
    LRecyclerView rcTeamDynamic;
    @BindView(R.id.rg)
    RadioGroup rg;
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;
    private static final String PARAM_1 = "PARAM_1";
    private static final String PARAM_2 = "PARAM_2";
    List<TeamMsgStatsUserBean> mData = new ArrayList<>();
    private TeamMessageBean teamMessageBean;
    private LRecyclerViewAdapter viewAdapter;
    private Type type_from;
    private TeamMsgDetailActivity teamMsgDetailActivity;

    public static TeamMsgStatusPersonFragment newInstance(Type from, TeamMessageBean teamMessageBean) {
        TeamMsgStatusPersonFragment fragment = new TeamMsgStatusPersonFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_1, from);
        args.putSerializable(PARAM_2, teamMessageBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_compare_detail;
    }

    @Override
    public void initView() {
        teamMessageBean = (TeamMessageBean) getArguments().getSerializable(PARAM_2);
        type_from = (Type) getArguments().getSerializable(PARAM_1);
        teamMsgDetailActivity = (TeamMsgDetailActivity) getmCtx();
        initRecycleView();
    }

    private void initRecycleView() {
        rg.setVisibility(View.GONE);
        rcTeamDynamic.setHasFixedSize(true);
        FullyLinearLayoutManager layout = new FullyLinearLayoutManager(getmCtx()){
            @Override
            public boolean canScrollVertically() {
                return false; //解决与上级页面的滑动冲突
            }
        };
        rcTeamDynamic.setLayoutManager(layout);
        rcTeamDynamic.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        TeamMsgStatusPersonListAapter adapter = new TeamMsgStatusPersonListAapter(getmCtx(), mData);
        //适配器转换
        viewAdapter = new LRecyclerViewAdapter(adapter);
        rcTeamDynamic.setAdapter(viewAdapter);
        rcTeamDynamic.setPullRefreshEnabled(false);
        rcTeamDynamic.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                clearData();
                getData();
            }
        });
        rcTeamDynamic.setLoadMoreEnabled(false);
        rcTeamDynamic.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isRefresh = false;
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rcTeamDynamic);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                getData();
            }
        });
//        rcTeamDynamic.setRefreshing(true);//自动刷新
        // TODO: 2017/10/26 屏蔽刷新功能 手动拉取数据
        isRefresh = true;
        clearData();
        getData();
    }

    public void getData() {
        if (Type.IS_RESP.equals(type_from)) {
            API.getTeamMessageResponse(mCurrentCounter, teamMessageBean, new CommonCallBack<List<TeamMsgStatsUserBean>>() {
                @Override
                public void onSuccess(List<TeamMsgStatsUserBean> teamMsgStatsUserBeen) {
                    if (isRefresh) {
                        refreshStatFinish();
                        mData.addAll(teamMsgStatsUserBeen);
                        mCurrentCounter = mData.size();
                        teamMsgDetailActivity.setSegmentText(1, mCurrentCounter + "");
                        if (mData.isEmpty()) {
                            RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.TheEnd);
                        }

                    } else {
                        mData.addAll(teamMsgStatsUserBeen);
                        mCurrentCounter = mData.size();
                        setLoadMoreStats(mCurrentCounter);
                    }
                    viewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure() {
                    refreshStatFinish();
                    RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.NetWorkError);
                }
            });
        } else if (Type.NO_RESP.equals(type_from)) {
            API.getTeamMessageNoResponse(mCurrentCounter, teamMessageBean, new CommonCallBack<List<TeamMsgStatsUserBean>>() {
                @Override
                public void onSuccess(List<TeamMsgStatsUserBean> teamMsgStatsUserBeen) {
                    if (isRefresh) {
                        refreshStatFinish();
                        mData.addAll(teamMsgStatsUserBeen);
                        mCurrentCounter = mData.size();
                        teamMsgDetailActivity.setSegmentText(0, mCurrentCounter + "");
                        if (mData.isEmpty()) {
                            RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.TheEnd);
                        }
                    } else {
                        mData.addAll(teamMsgStatsUserBeen);
                        mCurrentCounter = mData.size();
                        setLoadMoreStats(mCurrentCounter);
                    }
                    viewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure() {
                    refreshStatFinish();
                    RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.NetWorkError);
                }
            });
        }
    }

    public void clearData() {
        mCurrentCounter = 0;
        mData.clear();
        RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.Normal);
        viewAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新结束
     */
    public void refreshStatFinish() {
        rcTeamDynamic.refreshComplete();
    }

    public void setLoadMoreStats(int count) {
        if (count % UrlMethod.PAZE_SIZE != 0) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), rcTeamDynamic, UrlMethod.PAZE_SIZE, LoadingFooter.State.TheEnd, null);
            return;
        }
        if (count % UrlMethod.PAZE_SIZE == 0) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), rcTeamDynamic, UrlMethod.PAZE_SIZE, LoadingFooter.State.Normal, null);
        }
    }

    /**
     * 枚举用到该页面的类型
     */
    public enum Type {
        IS_RESP, NO_RESP
    }
}
