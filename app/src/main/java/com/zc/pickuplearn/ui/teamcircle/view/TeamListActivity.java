package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamListAdpter;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.SeachBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 团队列表
 */
public class TeamListActivity extends BaseActivity {

    @BindView(R.id.hv_team_list_head)
    HeadView hvHead;
    @BindView(R.id.sb_search_view)
    SeachBar sbSearchView;
    @BindView(R.id.lrv_list)
    LRecyclerView rcTeamDynamic;
    List<TeamCircleBean> mData = new ArrayList<>();
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     */
    public static int mCurrentCounter = 0;
    public static String SearchText = "";
    private LRecyclerViewAdapter viewAdapter;
    private boolean IS_FIRST_IN = true;//记录是不是第一次进入
    /**
     * 开启本页面的方法
     * @param context 上下文
     */
    public static void startTeamListActivity(Context context) {
        Intent intent = new Intent(context, TeamListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_list;
    }

    @Override
    public void initView() {
        initTopBar();
        initSearBar();
        initRecycleView();
    }

    private void initRecycleView() {
        rcTeamDynamic.setHasFixedSize(true);
        rcTeamDynamic.setLayoutManager(new LinearLayoutManager(this));
        rcTeamDynamic.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        TeamListAdpter adapter = new TeamListAdpter(this, R.layout.item_team_list_item, mData);
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

    private void initTopBar() {
        hvHead.setMyOnClickListener(new HeadView.myOnClickListener() {
            @Override
            public void rightButtonClick() {
                TeamCreateActivity.startTeamCreateActivity(TeamListActivity.this);
            }
        });
        hvHead.setTitle(getResources().getString(R.string.team_circle_discover));
        hvHead.setRightText("创建");
    }

    private void initSearBar() {
        sbSearchView.setSearchEditTextHint("搜索团队");
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
        API.getTeamCircleList(new CommonCallBack<List<TeamCircleBean>>() {
            @Override
            public void onSuccess(List<TeamCircleBean> teamCircleBeen) {
                if (isRefresh) {
                    refreshStatFinish();
                    mData.addAll(teamCircleBeen);
                    mCurrentCounter = mData.size();
                    if (mData.isEmpty()) {
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
        if (rcTeamDynamic!=null){
            rcTeamDynamic.refreshComplete();
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        //每次回到该页面都刷新
        if (IS_FIRST_IN){
            IS_FIRST_IN = false;
        }else {
            isRefresh = true;
            clearData();
            getData();
        }
    }
}
