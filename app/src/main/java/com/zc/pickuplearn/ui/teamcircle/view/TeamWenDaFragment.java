package com.zc.pickuplearn.ui.teamcircle.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.EventBusBaseFragment;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.RefreshEvent;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamWenDaAapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TeamWenDaFragment extends EventBusBaseFragment {

    @BindView(R.id.lrc_rv)
    LRecyclerView rcTeamDynamic;
    List<QuestionBean> mData = new ArrayList<>();
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;

    private boolean IS_FIRST_IN = true;//标记是不是第一次进入该页面
    private LRecyclerViewAdapter viewAdapter;
    private static final String FORM_TAG = "TeamWenDaFragment_from";
    private static final String TEAM_CIRCLE_BEAN_TAG = "TeamWenDaFragment_team_circle_bean";
    private TeamCircleBean teamCircleBean;
    private TeamWenDaType type_from;

    public static TeamWenDaFragment newInstance(TeamWenDaFragment.TeamWenDaType from, TeamCircleBean teamCircleBean) {
        TeamWenDaFragment fragment = new TeamWenDaFragment();
        Bundle args = new Bundle();
        args.putSerializable(FORM_TAG, from);
        args.putSerializable(TEAM_CIRCLE_BEAN_TAG, teamCircleBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_wen_da;
    }

    @Override
    public void initView() {
        teamCircleBean = (TeamCircleBean) getArguments().getSerializable(TEAM_CIRCLE_BEAN_TAG);
        type_from = (TeamWenDaType) getArguments().getSerializable(FORM_TAG);
        initRecycleView();
    }

    private void initRecycleView() {
        rcTeamDynamic.setHasFixedSize(true);
        rcTeamDynamic.setLayoutManager(new LinearLayoutManager(getmCtx()));
        rcTeamDynamic.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        TeamWenDaAapter adapter = new TeamWenDaAapter(getmCtx(), mData, teamCircleBean,type_from);
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
        rcTeamDynamic.setLoadMoreEnabled(true);
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
        if (TeamWenDaType.ACTION.equals(type_from)) {
            API.teamQuestionAction(mCurrentCounter, teamCircleBean, new CommonCallBack<List<QuestionBean>>() {
                @Override
                public void onSuccess(List<QuestionBean> questionBeen) {
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
        } else if (TeamWenDaType.CHALLENGE.equals(type_from)) {
            API.teamQuestionChallenge(mCurrentCounter, teamCircleBean, new CommonCallBack<List<QuestionBean>>() {
                @Override
                public void onSuccess(List<QuestionBean> questionBeen) {
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
        } else if (TeamWenDaType.QUSETION.equals(type_from)) {
            API.teamQuestionMyQuestion(mCurrentCounter
                    , teamCircleBean, new CommonCallBack<List<QuestionBean>>() {
                        @Override
                        public void onSuccess(List<QuestionBean> questionBeen) {
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
        }else if(TeamWenDaType.SOLVE.equals(type_from)){
            API.teamQuestionSolve(mCurrentCounter
                    , teamCircleBean, new CommonCallBack<List<QuestionBean>>() {
                        @Override
                        public void onSuccess(List<QuestionBean> questionBeen) {
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
        if (rcTeamDynamic!=null)
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
     * 监听问答 和回答成功之后请求刷新
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        if (event != null) {
            isRefresh = true;
            clearData();
            getData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //处理回来就刷新
        if (IS_FIRST_IN) {
            IS_FIRST_IN = false;
        } else {
            isRefresh = true;
            clearData();
            getData();
        }
    }

    /**
     * 枚举用到该页面的类型
     */
    public enum TeamWenDaType {
        ACTION, CHALLENGE, QUSETION ,SOLVE//我来挑战，你们我答 ，我的问题 ,已解决
    }
}
