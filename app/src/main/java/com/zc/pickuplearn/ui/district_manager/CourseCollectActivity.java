package com.zc.pickuplearn.ui.district_manager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.event.CourseCollectChangeEvent;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.district_manager.adapter.CourseWareAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的课件收藏
 */
public class CourseCollectActivity extends EventBusActivity {

    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.bt_right)
    Button btRight;
    @BindView(R.id.header)
    ClassicsHeader header;
    @BindView(R.id.rc_content)
    RecyclerView rcContent;
    @BindView(R.id.x_loading)
    XLoadingView xLoading;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.root)
    LinearLayout root;
    List<CourseWareBean.DatasBean> mData = new ArrayList<>();
    boolean IS_REFRESH = true;
    int mCurrent = 0;
    private CourseWareAdapter courseWareAdapter;

    public static void open(Context context) {
        Intent intent = new Intent(context, CourseCollectActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        initStatusBar(root);
        initToolBar();
        initXloading();
        initRecycleView();
        initRefresh();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_course_collect;
    }

    @Override
    protected void initData() {

    }

    private void initToolBar() {
        tvTitle.setText("我的收藏");
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    private void initXloading() {
        xLoading.setEmptyViewWarning("暂无数据");
    }

    private void initRecycleView() {
        rcContent.setHasFixedSize(true);
        rcContent.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        courseWareAdapter = new CourseWareAdapter(this, mData, CourseWareAdapter.Type.MyCollect);
        rcContent.setAdapter(courseWareAdapter);
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (refreshlayout.isLoadmoreFinished())
                    refreshlayout.setLoadmoreFinished(false);
                IS_REFRESH = true;
                mCurrent = 0;
                getData();
            }
        });
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                IS_REFRESH = false;
                mCurrent = mData.size();
                getData();
            }
        });

        refreshLayout.autoRefresh();
    }

    public void getData() {
        API.getCourseWareCollect(mCurrent, new CommonCallBack<List<CourseWareBean.DatasBean>>() {
            @Override
            public void onSuccess(List<CourseWareBean.DatasBean> datasBeen) {
                if (courseWareAdapter != null) {
                    if (IS_REFRESH) {
                        mData.clear();
                    }
                    mData.addAll(datasBeen);
                    mCurrent = mData.size();
                    courseWareAdapter.notifyDataSetChanged();
                }
                finishRefresh();
            }

            @Override
            public void onFailure() {
                finishRefresh();
            }
        });

    }

    private void finishRefresh() {
        if (refreshLayout != null) {
            if (IS_REFRESH) {
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadmore();
            }
        }
        if (xLoading != null) {
            if (mData.size() == 0) {
                xLoading.showEmpty();
            } else {
                xLoading.showContent();
            }
        }
    }

    @Subscribe
    public void onEventMainThread(CourseCollectChangeEvent event) {
        if (event != null) {
            if (refreshLayout!=null){
                refreshLayout.autoRefresh();
            }
        }
    }
}
