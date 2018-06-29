package com.zc.pickuplearn.ui.teamcircle.view;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.TeamRankPersonBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamCompareDetailAapter;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.LogUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * 团队比一比详情界面 问答榜 回答榜
 */
public class TeamCompareDetailFragment extends BaseFragment {

    @BindView(R.id.lc_list)
    LRecyclerView rcTeamDynamic;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.rb4)
    RadioButton rb4;
    @BindView(R.id.rb5)
    RadioButton rb5;
    @BindView(R.id.rb6)
    RadioButton rb6;
    @BindView(R.id.rg)
    RadioGroup rg;
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;
    private static final String FORM_TAG = "TeamCompareDetailFragment_from";
    private static final String TEAM_CIRCLE_BEAN_TAG = "TeamCompareDetailFragment_team_circle_bean";
    List<TeamRankPersonBean> mData = new ArrayList<>();
    private String period;
    private TeamCircleBean teamCircleBean;
    private LRecyclerViewAdapter viewAdapter;
    private Type type_from;
    private List<String> dateList;

    public static TeamCompareDetailFragment newInstance(Type from, TeamCircleBean teamCircleBean) {
        TeamCompareDetailFragment fragment = new TeamCompareDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(FORM_TAG, from);
        args.putSerializable(TEAM_CIRCLE_BEAN_TAG, teamCircleBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_compare_detail;
    }

    @Override
    public void initView() {
        dateList = DateUtils.getDateList(new Date(), 5);
        setTimeViewData();
        period = dateList.get(dateList.size() - 1);
        teamCircleBean = (TeamCircleBean) getArguments().getSerializable(TEAM_CIRCLE_BEAN_TAG);
        type_from = (Type) getArguments().getSerializable(FORM_TAG);
        initRecycleView();
        onRGcheckChange();
    }

    public void setTimeViewData() {
        rb1.setText(DateUtils.formatDigit(dateList.get(0)));
        rb2.setText(DateUtils.formatDigit(dateList.get(1)));
        rb3.setText(DateUtils.formatDigit(dateList.get(2)));
        rb4.setText(DateUtils.formatDigit(dateList.get(3)));
        rb5.setText(DateUtils.formatDigit(dateList.get(4)));
        rb6.setText(DateUtils.formatDigit(dateList.get(5)));

    }

    private void initRecycleView() {
        rcTeamDynamic.setHasFixedSize(true);
        rcTeamDynamic.setLayoutManager(new LinearLayoutManager(getmCtx()));
        rcTeamDynamic.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        TeamCompareDetailAapter adapter = new TeamCompareDetailAapter(getmCtx(), mData, teamCircleBean, type_from);
        //适配器转换
        viewAdapter = new LRecyclerViewAdapter(adapter);
        rcTeamDynamic.setAdapter(viewAdapter);
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
        rcTeamDynamic.setRefreshing(true);//自动刷新
    }

    public void getData() {
//        if (Type.QUESTION.equals(type_from)) {
        LogUtils.e("比一比访问");
        API.getTeamCompare(type_from, mCurrentCounter, period, teamCircleBean, new CommonCallBack<List<TeamRankPersonBean>>() {
            @Override
            public void onSuccess(List<TeamRankPersonBean> questionBeen) {
                if (isRefresh) {
                    refreshStatFinish();
                    mData.addAll(questionBeen);
                    mCurrentCounter = mData.size();
                    if (mData.isEmpty()) {
                        RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.TheEnd);
                    }
                } else {
                    mData.addAll(questionBeen);
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

    public void onRGcheckChange() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        period = dateList.get(0);
                        break;
                    case R.id.rb2:
                        period = dateList.get(1);
                        break;
                    case R.id.rb3:
                        period = dateList.get(2);
                        break;
                    case R.id.rb4:
                        period = dateList.get(3);
                        break;
                    case R.id.rb5:
                        period = dateList.get(4);
                        break;
                    case R.id.rb6:
                        period = dateList.get(5);
                        break;

                }
                isRefresh = true;
                clearData();
                getData();
            }
        });
    }

    /**
     * 枚举用到该页面的类型
     */
    public enum Type {
        ANSWER, QUESTION, DILIGENCE//提问榜，回答榜，勤奋榜
    }
}
