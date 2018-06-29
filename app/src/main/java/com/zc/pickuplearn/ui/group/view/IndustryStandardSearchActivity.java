package com.zc.pickuplearn.ui.group.view;

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
import com.zc.pickuplearn.beans.IndustryStandardBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.group.adapter.IndustryStandardAdapter;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.SeachBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class IndustryStandardSearchActivity extends BaseActivity {

    @BindView(R.id.hv_team_list_head)
    HeadView hvHead;
    @BindView(R.id.sb_search_view)
    SeachBar sbSearchView;
    @BindView(R.id.lrv_list)
    LRecyclerView rcTeamDynamic;
    List<IndustryStandardBean> mData = new ArrayList<>();
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     *
     */
    public  int mCurrentCounter = 0;
    public  String SearchText = "";
    private LRecyclerViewAdapter viewAdapter;
    /**
     * 开启本页面的方法
     * @param context 上下文
     */
    public static void startIndustryStandardSearchActivity(Context context) {
        Intent intent = new Intent(context, IndustryStandardSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_industry_standard_search;
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
        IndustryStandardAdapter adapter = new IndustryStandardAdapter(this,mData);
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
        hvHead.setTitle(getResources().getString(R.string.industry_standard));
        hvHead.setRightBtVisable(false);
    }

    private void initSearBar() {
        sbSearchView.setSearchEditTextHint("行业规范搜索");
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
        API.getIndustryStandardByName(mCurrentCounter,SearchText,new CommonCallBack<List<IndustryStandardBean>>() {
            @Override
            public void onSuccess(List<IndustryStandardBean> teamCircleBeen) {
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
