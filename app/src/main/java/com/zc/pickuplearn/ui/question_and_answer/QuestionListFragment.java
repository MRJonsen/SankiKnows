package com.zc.pickuplearn.ui.question_and_answer;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.Professor;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.event.QuestionStatusChangeEvent;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.EventBusBaseFragment;
import com.zc.pickuplearn.ui.question_and_answer.adapter.QuestionListAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 问题列表 fragment
 */
public class QuestionListFragment extends EventBusBaseFragment {
    private static String ARG_PARAM1 = "ARG_PARAM1";
    private static String ARG_PARAM2 = "ARG_PARAM2";
    private static String ARG_PARAM3 = "ARG_PARAM3";
    private TypeQuestion type;
    private int mCurrent = 0;
    private QuestionBean mSearchData = new QuestionBean();
    private UserBean userBean;
    private DynamicType from;

    public static QuestionListFragment newInstance(TypeQuestion typeQuestion, UserBean userBean,DynamicType dynamicType) {
        QuestionListFragment fragment = new QuestionListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, typeQuestion);
        args.putSerializable(ARG_PARAM2, userBean);
        args.putSerializable(ARG_PARAM3, dynamicType);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rc_content)
    RecyclerView rcContent;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.x_loading)
    XLoadingView xLoadingView;
    //    public int mCurrentCounter = 0;
    boolean IS_REFRESH = true;
    private QuestionListAdapter questionListAdapter;
    private List<QuestionBean> mData = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.fragment_question_list;
    }

    @Override
    public void initView() {
        initVariables();
        initXloading();
        initRecycleView();
        initRefresh();
    }

    private void initXloading() {
        switch (type) {
            case PERSONQUESTION:
                xLoadingView.setEmptyViewWarning("好心塞，居然是空的");
                break;
            case PERSONANSER:
                xLoadingView.setEmptyViewWarning("好心塞，居然是空的");
                break;
            default:
                xLoadingView.setEmptyView(R.drawable.xloading_empty_normal);
                xLoadingView.setEmptyViewWarning("暂无数据");
                break;
        }
    }


    private void initVariables() {
        if (getArguments() != null) {
            type = (TypeQuestion) getArguments().getSerializable(ARG_PARAM1);
            userBean = (UserBean) getArguments().getSerializable(ARG_PARAM2);
            from = (DynamicType) getArguments().getSerializable(ARG_PARAM3);
        }
    }

    private void initRecycleView() {
        rcContent.setHasFixedSize(true);
        rcContent.setLayoutManager(new LinearLayoutManager(getmCtx()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        questionListAdapter = new QuestionListAdapter(getmCtx(), mData);
        rcContent.setAdapter(questionListAdapter);
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (refreshlayout.isLoadmoreFinished())
                    refreshlayout.setLoadmoreFinished(false);
                IS_REFRESH = true;
                mCurrent = 0;
                mData.clear();
                getData();
            }
        });
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                IS_REFRESH = false;
                getData();
            }
        });

        refreshLayout.autoRefresh();
    }

    public void getData() {
        switch (type) {
            case EXPERT:
                Professor professor = new Professor();
                professor.setUSERCODE(userBean.getUSERCODE());
                API.professorQustionSearch(mSearchData.getQUESTIONEXPLAIN(), mCurrent, professor, new CommonCallBack<List<QuestionBean>>() {
                    @Override
                    public void onSuccess(List<QuestionBean> questionBeen) {
                        if (mData != null && questionListAdapter != null) {
                            mData.addAll(questionBeen);
                            questionListAdapter.notifyDataSetChanged();
                            mCurrent = mData.size();
                            if (questionBeen.size() == 0)
                                if (refreshLayout != null)
                                    refreshLayout.setLoadmoreFinished(true);
                        }
                        finishRefresh();
                    }

                    @Override
                    public void onFailure() {
                        finishRefresh();
                    }
                });

                break;
            case PERSONANSER:
                API.getMyAnswerQuestionList(mCurrent, "", new CommonCallBack<List<QuestionBean>>() {
                    @Override
                    public void onSuccess(List<QuestionBean> questionBeen) {
                        if (mData != null && questionListAdapter != null) {
                            for (QuestionBean questionbean :
                                    questionBeen) {
                                questionbean.setISANSWER("1");
                            }
                            mData.addAll(questionBeen);
                            questionListAdapter.notifyDataSetChanged();
                            mCurrent = mData.size();
                            if (questionBeen.size() == 0)
                                if (refreshLayout != null)
                                    refreshLayout.setLoadmoreFinished(true);
                        }
                        finishRefresh();
                    }

                    @Override
                    public void onFailure() {
                        finishRefresh();
                    }
                });
                break;
            default:
                API.getQuestionList(from,userBean, mSearchData, type, mCurrent, 20, new CommonCallBack<List<QuestionBean>>() {
                    @Override
                    public void onSuccess(List<QuestionBean> questionBeen) {
                        if (mData != null && questionListAdapter != null) {
                            mData.addAll(questionBeen);
                            questionListAdapter.notifyDataSetChanged();
                            mCurrent = mData.size();
                            if (questionBeen.size() == 0)
                                if (refreshLayout != null)
                                    refreshLayout.setLoadmoreFinished(true);
                        }
                        finishRefresh();
                    }

                    @Override
                    public void onFailure() {
                        finishRefresh();
                    }
                });
                break;
        }
    }


    public void setSearchData(QuestionBean questionBean) {
        mSearchData = questionBean;
        if (refreshLayout != null) {
            refreshLayout.autoRefresh();
        }
    }

    private void finishRefresh() {
        if (refreshLayout != null) {
            if (IS_REFRESH) {
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadmore();
            }
        }
        if (xLoadingView != null) {
            if (mCurrent == 0) {
                xLoadingView.showEmpty();
            } else {
                xLoadingView.showContent();
            }
        }
    }

    @Subscribe
    public void onEventMainThread(QuestionStatusChangeEvent event) {
        if (refreshLayout != null && event != null) {
            refreshLayout.autoRefresh();
        }
    }


}
