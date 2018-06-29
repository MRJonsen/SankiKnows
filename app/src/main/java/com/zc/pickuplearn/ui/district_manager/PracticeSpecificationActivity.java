package com.zc.pickuplearn.ui.district_manager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.district_manager.adapter.PracticeSpecificationListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 实操规范
 */
public class PracticeSpecificationActivity extends BaseActivity {
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rc_content)
    RecyclerView rcContent;
    @BindView(R.id.x_loading)
    XLoadingView xLoading;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;
    List<String> mData = new ArrayList<>();
    boolean IS_REFRESH = true;
    private int mCurrent = 0;
    private PracticeSpecificationListAdapter practiceSpecificationListAdapter;

    public static void open(Context context) {
        Intent intent = new Intent(context, PracticeSpecificationActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
    }

    @Override
    public int setLayout() {
        return R.layout.activity_practice_specification;
    }


    @Override
    protected void initData() {
        initStatusBar(root);
        initToolBar();
        initXloading();
        initRecycleView();
        initRefresh();
    }

    private void initToolBar() {
        tvTitle.setText("实操规范");
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
        practiceSpecificationListAdapter = new PracticeSpecificationListAdapter(this, mData);
        rcContent.setAdapter(practiceSpecificationListAdapter);
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
        if (IS_REFRESH) {
            mData.clear();
        }

        for (int i = 0; i < 5; i++) {
            mData.add("1");
        }
        mCurrent = mData.size();
        finishRefresh();
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
            if (mCurrent == 0) {
                xLoading.showEmpty();
            } else {
                xLoading.showContent();
            }
        }
    }

}
