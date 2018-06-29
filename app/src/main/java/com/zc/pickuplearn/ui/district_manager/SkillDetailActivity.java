package com.zc.pickuplearn.ui.district_manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.beans.SkillBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.district_manager.adapter.CourseWareAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 技能详情页面
 */
public class SkillDetailActivity extends BaseActivity {
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_level)
    ImageView iv_level;
    @BindView(R.id.rc_content)
    RecyclerView rcContent;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;
    boolean IS_REFRESH = true;
    @BindView(R.id.tv_level_request)
    TextView tvLevelRequest;
    @BindView(R.id.tv_tv_level_introduce)
    TextView tvTvLevelIntroduce;
    private int mCurrent = 0;
    private CourseWareAdapter courseWareAdapter;
    List<CourseWareBean.DatasBean> mData = new ArrayList<>();
    private static final String PARAM = SkillDetailActivity.class.getSimpleName();
    private SkillBean.DataBean bean;

    public static void open(Context context, SkillBean.DataBean bean) {
        Intent intent = new Intent(context, SkillDetailActivity.class);
        intent.putExtra(PARAM, bean);
        context.startActivity(intent);
    }

    @Override
    public void initVariables() {
        super.initVariables();
        bean = (SkillBean.DataBean) getIntent().getSerializableExtra(PARAM);
    }

    @Override
    public void initView() {

    }

    @Override
    public int setLayout() {
        return R.layout.activity_skill_detail;
    }

    @Override
    protected void initData() {
        initStatusBar(root);
        initToolBar();
        initRecycleView();
        initRefresh();
        initTopView();
    }

    private void initTopView() {
        if (bean!=null){
            ImageLoaderUtil.showImageViewByResourceId(this, getLevelIcon(bean.getABILITY_LEVEL()), iv_level);
//            tvLevelRequest.setText(bean.getABILITY_LEVEL());
            tvTvLevelIntroduce.setText(bean.getABILITY_LEVEL_DESC());
        }
    }


    private void initToolBar() {
        tvTitle.setText(bean.getABILITYNAME());
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
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
        courseWareAdapter = new CourseWareAdapter(this, mData, CourseWareAdapter.Type.Other);
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
        API.getInstance().getRelatCourseWare(mCurrent, bean, new CommonCallBack<List<CourseWareBean.DatasBean>>() {
            @Override
            public void onSuccess(List<CourseWareBean.DatasBean> datasBeen) {
                if (IS_REFRESH) {
                    mData.clear();
                }
                if (courseWareAdapter != null) {
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
    }

    public int getLevelIcon(String level) {
        int icon = R.mipmap.icon_skill_level_1;
        if ("I".equals(level))
            icon = R.mipmap.icon_skill_level_1;
        if ("II".equals(level))
            icon = R.mipmap.icon_skill_level_2;
        if ("III".equals(level))
            icon = R.mipmap.icon_skill_level_3;

        return icon;
    }
}
