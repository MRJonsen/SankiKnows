package com.zc.pickuplearn.ui.district_manager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.xframe.XFrame;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.SkillMoudleBean;
import com.zc.pickuplearn.beans.SkillMoudleLearnBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.district_manager.adapter.SkillMoudleLearnAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 技能模块学习
 */
public class SkillMoudleLearnActivity extends BaseActivity {

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
    boolean IS_REFRESH = true;
    private int mCurrent = 0;
    private SkillMoudleLearnAdapter skillMoudleLearnAdapter;
    private List<SkillMoudleBean> mData = new ArrayList<>();
    private String functioncode;

    public static void openSkillMoudleLearn(Context context,String functioncode) {
        Intent intent = new Intent(context, SkillMoudleLearnActivity.class);
        intent.putExtra("functioncode",functioncode);
        context.startActivity(intent);
    }


    @Override
    public int setLayout() {
        return R.layout.activity_skill_moudle_learn;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initVariables() {
        super.initVariables();
        functioncode = getIntent().getStringExtra("functioncode");
    }

    @Override
    public void initView() {
        SkillMoudleBean data1 = new SkillMoudleBean("实操学习", new ArrayList<SkillMoudleLearnBean>());
        SkillMoudleBean data2 = new SkillMoudleBean("理论学习", new ArrayList<SkillMoudleLearnBean>());
        mData.add(data1);
        mData.add(data2);
        initToolBar();
        initXloading();
        initRecycleView();
        initRefresh();
    }

    private void initToolBar() {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        tvTitle.setText(XFrame.getString(R.string.skill_moudle_learning));
    }

    private void initXloading() {
        xLoading.setEmptyViewWarning("暂无数据");
    }

    private void initRecycleView() {
        rcContent.setHasFixedSize(true);
        rcContent.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        skillMoudleLearnAdapter = new SkillMoudleLearnAdapter(mContext, mData);
        rcContent.setAdapter(skillMoudleLearnAdapter);
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (refreshlayout.isLoadmoreFinished())
                    refreshlayout.setLoadmoreFinished(false);
                IS_REFRESH = true;
//                mCurrent = 0;
//                mData.clear();
                getData();
            }
        });
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                IS_REFRESH = false;
                getData();
            }
        });

        refreshLayout.autoRefresh();
        refreshLayout.setEnableRefresh(false);


        IS_REFRESH = true;
        getData();
    }

    public void getData() {
        mCurrent = mData.size();
        skillMoudleLearnAdapter.notifyDataSetChanged();
        finishRefresh();
        API.getInstance().getSkillMoudleLearn(functioncode,new CommonCallBack<List<SkillMoudleLearnBean>>() {

            @Override
            public void onSuccess(List<SkillMoudleLearnBean> courseWareHomeBeen) {
                if (skillMoudleLearnAdapter != null) {
                    if (IS_REFRESH) {
//                        mData.clear();
                        for (SkillMoudleBean bean:
                        mData) {
                            bean.getData().clear();
                        }
                    }

                    for (SkillMoudleLearnBean skill: courseWareHomeBeen) {
                        if ("1".equals(skill.getMODTYPE())){
                            mData.get(0).getData().add(skill);
                        }else {
                            mData.get(1).getData().add(skill);
                        }
                    }

                    mCurrent = mData.size();
                    skillMoudleLearnAdapter.notifyDataSetChanged();
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
            if (mCurrent == 0) {
                xLoading.showEmpty();
            } else {
                xLoading.showContent();
            }
        }
    }
}
