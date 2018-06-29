package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

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
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamWenDaAapter;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.SeachBar;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TeamQusQustionSearchActivity extends BaseActivity {

    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.sb_search_view)
    SeachBar sbSearchView;
    @BindView(R.id.lrc_rv)
    LRecyclerView rcTeamDynamic;
    List<QuestionBean> mData = new ArrayList<>();
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     */
    public  int mCurrentCounter = 0;
    private static final String TAG_TEAM_CIRCLE_BEAN = "TeamQusQustionSearchActivity_teamcircle_bean";
    public  String SearchText = "";
    private LRecyclerViewAdapter viewAdapter;
    private TeamCircleBean teamCircleBean;

    /**
     * 开启本页面的方法
     *
     * @param context 上下文
     */
    public static void startTeamQusQustionSearchActivity(Context context,TeamCircleBean teamCircleBean) {
        Intent intent = new Intent(context, TeamQusQustionSearchActivity.class);
        intent.putExtra(TAG_TEAM_CIRCLE_BEAN,teamCircleBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_qus_qustion_search;
    }

    @Override
    public void initView() {
        teamCircleBean = (TeamCircleBean) getIntent().getSerializableExtra(TAG_TEAM_CIRCLE_BEAN);
        initTopBar();
        initSearBar();
        initRecycleView();
    }

    private void initRecycleView() {
        rcTeamDynamic.setHasFixedSize(true);
        rcTeamDynamic.setLayoutManager(new LinearLayoutManager(this));
        rcTeamDynamic.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        TeamWenDaAapter adapter = new TeamWenDaAapter(this,mData,teamCircleBean,null);
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
//        rcTeamDynamic.setRefreshing(true);//自动刷新
    }

    private void initTopBar() {
        hvHead.setTitle("问题搜索");
    }

    private void initSearBar() {
        sbSearchView.setSearchEditTextHint("搜索问题");
        sbSearchView.setSearchButtonOnClickListener(new SeachBar.SearchButtonOnClickListener() {
            @Override
            public void onClick() {
                SearchText = sbSearchView.getSearchText();
                isRefresh = true;
                clearData();
                getData();
            }
        });
    }

    @Override
    protected void initData() {

    }

    public void getData() {
        if (!TextUtils.isEmpty(sbSearchView.getSearchText())){
            SearchText = sbSearchView.getSearchText();
        }
        if (TextUtils.isEmpty(SearchText)){
            ToastUtils.showToast(this,"请输入查询内容");
            return;
        }
        API.teamQustionSearch(SearchText,mCurrentCounter,teamCircleBean,new CommonCallBack<List<QuestionBean>>() {
            @Override
            public void onSuccess(List<QuestionBean> teamCircleBeen) {
                if (isRefresh) {
                    refreshStatFinish();
                    mData.addAll(teamCircleBeen);
                    mCurrentCounter = mData.size();
                    if (mData.isEmpty()) {
                        showToast("暂无数据!");
                        RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.TheEnd);
                    }
                } else {
                    mData.addAll(teamCircleBeen);
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
            RecyclerViewStateUtils.setFooterViewState(this, rcTeamDynamic, UrlMethod.PAZE_SIZE, LoadingFooter.State.TheEnd, null);
            return;
        }
        if (count % UrlMethod.PAZE_SIZE == 0) {
            RecyclerViewStateUtils.setFooterViewState(this, rcTeamDynamic, UrlMethod.PAZE_SIZE, LoadingFooter.State.Normal, null);
        }
    }
}
