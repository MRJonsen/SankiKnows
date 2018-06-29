package com.zc.pickuplearn.ui.group.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.IndustryStandardBean;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;
import com.zc.pickuplearn.ui.group.adapter.IndustryStandardAdapter;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.IndustryStandardTypeEvent;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 行业规范列表页面
 */
public class IndustryStandardActivity extends EventBusActivity {

    @BindView(R.id.tv_type_name)
    TextView tvTypeName;
    @BindView(R.id.lrc_list)
    LRecyclerView lrcList;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ib_search)
    ImageButton ibSearch;
    @BindView(R.id.bt_type)
    Button btType;
    @BindView(R.id.bt_hot)
    Button btHot;
    @BindView(R.id.bt_new)
    Button btNew;
    private String type = UrlMethod.TYPE_HOT;//默认按点击量最高查询
    private boolean isRefresh = false;
    QusetionTypeBean mType;
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;
    List<IndustryStandardBean> mData = new ArrayList<>();
    private LRecyclerViewAdapter viewAdapter;

    private static final String Tag = "IndustryStandardActivity";
    private ProfessionalCircleBean circleBean;

    public static void startIndustryStandardActivity(Context context, ProfessionalCircleBean professionalCircleBean) {
        Intent intent = new Intent(context, IndustryStandardActivity.class);
        intent.putExtra(Tag, professionalCircleBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_industry_standard;
    }

    @Override
    public void initView() {
        circleBean = (ProfessionalCircleBean) getIntent().getSerializableExtra(Tag);
        setTypeName(circleBean.getPROCIRCLENAME());
        initRecycleView();
    }

    /**
     * 设置类型名称
     *
     * @param typeName
     */
    private void setTypeName(String typeName) {
        tvTypeName.setText(typeName);
    }

    @Override
    protected void initData() {

    }

    private void initRecycleView() {
        lrcList.setHasFixedSize(true);
        lrcList.setLayoutManager(new LinearLayoutManager(this));
        lrcList.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        IndustryStandardAdapter adapter = new IndustryStandardAdapter(this, mData);
        //适配器转换
        viewAdapter = new LRecyclerViewAdapter(adapter);
        lrcList.setAdapter(viewAdapter);
        lrcList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        lrcList.setLoadMoreEnabled(true);
        lrcList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadmore();
            }
        });
        lrcList.setRefreshing(true);//自动刷新
    }

    private void loadmore() {
        isRefresh = false;
        LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(lrcList);
        if (state == LoadingFooter.State.Loading) {
            return;
        }
        getData();
    }

    private void refresh() {
        isRefresh = true;
        clearData();
        getData();
    }

    public void getData() {
        API.getIndustryStandard(mType,"", mCurrentCounter, type, circleBean, new CommonCallBack<List<IndustryStandardBean>>() {
            @Override
            public void onSuccess(List<IndustryStandardBean> industryStandardBeen) {
                if (isRefresh) {
                    refreshStatFinish();
                    mData.addAll(industryStandardBeen);
                    mCurrentCounter = mData.size();
                    if (mData.isEmpty()) {
                        RecyclerViewStateUtils.setFooterViewState(lrcList, LoadingFooter.State.TheEnd);
                    }
                } else {
                    mData.addAll(industryStandardBeen);
                    mCurrentCounter = mData.size();
                    setLoadMoreStats(mCurrentCounter);
                }
                viewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                refreshStatFinish();
                RecyclerViewStateUtils.setFooterViewState(lrcList, LoadingFooter.State.NetWorkError);
            }
        });
    }

    public void clearData() {
        mCurrentCounter = 0;
        mData.clear();
        RecyclerViewStateUtils.setFooterViewState(lrcList, LoadingFooter.State.Normal);
        viewAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新结束
     */
    public void refreshStatFinish() {
        lrcList.refreshComplete();
    }

    public void setLoadMoreStats(int count) {
        if (count % UrlMethod.PAZE_SIZE != 0) {
            RecyclerViewStateUtils.setFooterViewState(this, lrcList, UrlMethod.PAZE_SIZE, LoadingFooter.State.TheEnd, null);
            return;
        }
        if (count % UrlMethod.PAZE_SIZE == 0) {
            RecyclerViewStateUtils.setFooterViewState(this, lrcList, UrlMethod.PAZE_SIZE, LoadingFooter.State.Normal, null);
        }
    }

    @OnClick({R.id.iv_back, R.id.ib_search, R.id.bt_type, R.id.bt_hot, R.id.bt_new})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ib_search:
                IndustryStandardSearchActivity.startIndustryStandardSearchActivity(this);
                break;
            case R.id.bt_type:
                ScrollGridActivity.startScrollGridActivity(this, TypeGreenDaoEvent.IndustryStandard);
                break;
            case R.id.bt_hot:
                if (type.equals(UrlMethod.TYPE_HOT)) {
                    return;
                } else {
                    type = UrlMethod.TYPE_HOT;
                    refresh();
                }
                break;
            case R.id.bt_new:
                if (type.equals(UrlMethod.TYPE_NEW)) {
                    return;
                } else {
                    type = UrlMethod.TYPE_NEW;
                    refresh();
                }
                break;
        }
    }

    /**
     * 接收分类树页面选择结果
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(IndustryStandardTypeEvent event) {
        if (event != null) {
            mType = event.getQuestionBean();
            if (mType != null) {
                setTypeName(mType.getNAME());
                refresh();
            }
        }
    }
}
