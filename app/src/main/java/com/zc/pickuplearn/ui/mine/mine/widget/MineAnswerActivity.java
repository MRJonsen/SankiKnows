package com.zc.pickuplearn.ui.mine.mine.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.mine.mine.widget.adapter.MyAnswerAapter;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.SeachBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//import com.zc.pickuplearn.utils.ToastUtils;

public class MineAnswerActivity extends BaseActivity {


    @BindView(R.id.hv_team_list_head)
    HeadView hvHead;
    @BindView(R.id.sb_search_view)
    SeachBar sbSearchView;
    @BindView(R.id.lrv_list)
    LRecyclerView rcTeamDynamic;
    @BindView(R.id.x_loading)
    XLoadingView xLoadingView;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;
    List<QuestionBean> mData = new ArrayList<>();
    private boolean IS_FIRST_IN = true;//是不是第一次进入
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;
    public String SearchText = "";
    private LRecyclerViewAdapter viewAdapter;

    /**
     * 开启本页面的方法
     *
     * @param context 上下文
     */
    public static void startMineAnswerActivity(Context context) {
        Intent intent = new Intent(context, MineAnswerActivity.class);
        context.startActivity(intent);
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
            getData(SearchText);
        }
    }

    @Override
    public int setLayout() {
        return R.layout.activity_mine_answer;
    }

    @Override
    public void initView() {
        xLoadingView.showContent();
        xLoadingView.setEmptyViewWarning("好心塞，居然是空的");
        initTopBar();
        initSearBar();
        initRecycleView();
        initListener();
    }

    private void initListener() {
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new com.scwang.smartrefresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                isRefresh = true;
                if (refreshlayout.isLoadmoreFinished())
                    refreshLayout.setLoadmoreFinished(false);
                clearData();
                getData(SearchText);
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                isRefresh = false;
                getData(SearchText);
            }
        });
    }

    private void initRecycleView() {
        rcTeamDynamic.setHasFixedSize(true);
        rcTeamDynamic.setLayoutManager(new LinearLayoutManager(this));
        rcTeamDynamic.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        MyAnswerAapter adapter = new MyAnswerAapter(this, mData);
        //适配器转换
        viewAdapter = new LRecyclerViewAdapter(adapter);
        rcTeamDynamic.setAdapter(viewAdapter);
    }

    private void initTopBar() {
        hvHead.setTitle("我的回答");
        hvHead.setRightBtVisable(false);
    }

    private void initSearBar() {
        sbSearchView.setSearchEditTextHint("搜索问题");
        sbSearchView.setSearchButtonOnClickListener(new SeachBar.SearchButtonOnClickListener() {
            @Override
            public void onClick() {
                SearchText = sbSearchView.getSearchText();
                isRefresh = true;
                clearData();
                getData(SearchText);
                SearchText = "";
            }
        });
    }

    @Override
    protected void initData() {

    }

    public void getData(String text) {
        API.getMyAnswerQuestionList(mCurrentCounter, text, new CommonCallBack<List<QuestionBean>>() {
            @Override
            public void onSuccess(List<QuestionBean> questionBeen) {
                xLoadingView.showContent();
                mData.addAll(questionBeen);
                if (isRefresh) {
                    refreshLayout.finishRefresh();
                } else {
                    if (questionBeen.size() == 0) {
                        refreshLayout.setLoadmoreFinished(true);
                    }
                    refreshLayout.finishLoadmore();
                }
                if (mData.size() == 0) {
                    xLoadingView.showEmpty();
//                    ToastUtils.showToastCenter(MineAnswerActivity.this, "快去帮助小伙伴解决他们的疑问吧！");
                }
                mCurrentCounter = mData.size();
                viewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                if (refreshLayout != null) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                    } else {
                        refreshLayout.finishLoadmore();
                    }
                }
            }
        });
    }

    public void clearData() {
        mCurrentCounter = 0;
        mData.clear();
        viewAdapter.notifyDataSetChanged();
    }
}
