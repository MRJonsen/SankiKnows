package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.FunctionBean;
import com.zc.pickuplearn.beans.InfoBean;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.TeamMessageBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamMessageListAdapter;
import com.zc.pickuplearn.utils.LogUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamMessageManageActivity extends EventBusActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ib_team_sendmsg)
    ImageButton ibTeamSendmsg;
    @BindView(R.id.lrc_list)
    LRecyclerView lrcList;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.x_loading)
    XLoadingView xLoadingView;
    //    private boolean isRefresh = false;
    public static final String TEAM_MSG_MANAGE_REFRESH = "TEAM_MSG_MANAGE_REFRESH";
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;//当前数据index
    List<TeamMessageBean> mData = new ArrayList<>();
    private LRecyclerViewAdapter viewAdapter;

    private final static String Tag = "TeamMessageManageActivity";
    private TeamCircleBean teamCircleBean;

    public static void startTeamMessageManageActivity(Context context, TeamCircleBean teamCircleBean) {
        Intent intent = new Intent(context, TeamMessageManageActivity.class);
        intent.putExtra(Tag, teamCircleBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_message_manage;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void initData() {
        teamCircleBean = (TeamCircleBean) getIntent().getSerializableExtra(Tag);
        initRecycleView();
        initXloadindView();
        initListener();
    }

    private void initXloadindView() {
        xLoadingView.showContent();
        xLoadingView.setEmptyView(R.drawable.xloading_empty_normal);
        xLoadingView.setEmptyViewWarning("还没有通知哦~");
    }

    private void initRecycleView() {
        lrcList.setHasFixedSize(true);
        lrcList.setLayoutManager(new LinearLayoutManager(this));
        lrcList.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        final TeamMessageListAdapter adapter = new TeamMessageListAdapter(this, mData);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                adapter.getDatas();
                if (adapter.getDatas().size() == 0) {
                    xLoadingView.showEmpty();
                }
            }
        });
        //适配器转换
        viewAdapter = new LRecyclerViewAdapter(adapter);
        lrcList.setAdapter(viewAdapter);
        lrcList.setLoadMoreEnabled(false);
    }

    /**
     * 设置监听 加载 刷新
     */
    private void initListener() {
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new com.scwang.smartrefresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                /*请求消息*/
                getInfoData(refreshlayout, true);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                getInfoData(refreshlayout, false);
            }
        });
    }

    private void getInfoData(final RefreshLayout refreshlayout, boolean isrefresh) {
        if (isrefresh) {
            mCurrentCounter = 0;
            if (refreshlayout.isLoadmoreFinished())
                refreshLayout.setLoadmoreFinished(false);

            API.getTeamMessage(mCurrentCounter, teamCircleBean, new CommonCallBack<List<TeamMessageBean>>() {
                @Override
                public void onSuccess(List<TeamMessageBean> teamMessageBeen) {
                    mCurrentCounter = teamMessageBeen.size();
                    mData.clear();
                    mData.addAll(teamMessageBeen);
                    viewAdapter.notifyDataSetChanged();
                    refreshlayout.finishRefresh();
                    xLoadingView.showContent();
                }

                @Override
                public void onFailure() {
                    xLoadingView.showEmpty();
                    refreshlayout.finishRefresh();
                }
            });
        } else {
            API.getTeamMessage(mCurrentCounter, teamCircleBean, new CommonCallBack<List<TeamMessageBean>>() {
                @Override
                public void onSuccess(List<TeamMessageBean> teamMessageBeen) {

                    if (teamMessageBeen.size() == 0) {
                        refreshlayout.setLoadmoreFinished(true);
                    } else {
                        mData.addAll(teamMessageBeen);
                        mCurrentCounter = mData.size();
                        viewAdapter.notifyDataSetChanged();
                    }
                    refreshlayout.finishLoadmore();
                }

                @Override
                public void onFailure() {
                    refreshlayout.finishLoadmore();
                }
            });
        }
    }

    @OnClick({R.id.iv_back, R.id.ib_team_sendmsg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ib_team_sendmsg:
                TeamSendMsgActivity.starTeamSendMsgActivity(this, teamCircleBean);//跳转发送通知界面
                break;
        }
    }

    /**
     * 发送团队消息成功
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(String event) {
        if (TEAM_MSG_MANAGE_REFRESH.equals(event)) {
            refreshLayout.autoRefresh();
        }
    }
}
