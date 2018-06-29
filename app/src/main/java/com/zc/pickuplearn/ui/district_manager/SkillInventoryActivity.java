package com.zc.pickuplearn.ui.district_manager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hss01248.dialog.StyledDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.xframe.XFrame;
import com.youth.xframe.common.XActivityStack;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.SkillBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.district_manager.adapter.SkillInventoryAdapter;
import com.zc.pickuplearn.ui.view.MaxRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 技能清单
 */
public class SkillInventoryActivity extends BaseActivity {

    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.pass_content)
    MaxRecycleView passContent;
    @BindView(R.id.ll_more_pass)
    LinearLayout llMorePass;
    @BindView(R.id.no_pass_content)
    MaxRecycleView noPassContent;
    @BindView(R.id.ll_more_no_pass)
    LinearLayout llMoreNoPass;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;
    List<SkillBean.DataBean> passData = new ArrayList<>();
    List<SkillBean.DataBean> noPassData = new ArrayList<>();
    @BindView(R.id.no_pass_view)
    LinearLayout noPassView;
    @BindView(R.id.pass_view)
    LinearLayout passView;
    private SkillInventoryAdapter noPassAdapter;
    private SkillInventoryAdapter passAdapter;
    private Dialog show;

    public static void open(Context context) {
        Intent intent = new Intent(context, SkillInventoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_skill_inventory;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initView() {
        initStatusBar(root);
        initToolBar();
        initPassContent();
        initNoPassContent();
        initRefresh();
    }

    private void initToolBar() {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        tvTitle.setText(XFrame.getString(R.string.skill_list));
    }


    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (refreshlayout.isLoadmoreFinished())
                    refreshlayout.setLoadmoreFinished(false);
                passData.clear();
                noPassData.clear();
                getPassData();
                getNoPassData();
            }
        });
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.autoRefresh();
    }

    public void initPassContent() {
        passContent.setHasFixedSize(true);
        passContent.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        passContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        passAdapter = new SkillInventoryAdapter(this, passData);
        passContent.setAdapter(passAdapter);
    }

    public void initNoPassContent() {
        noPassContent.setHasFixedSize(true);
        noPassContent.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        noPassContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        noPassAdapter = new SkillInventoryAdapter(this, noPassData);
        noPassContent.setAdapter(noPassAdapter);
    }

    @OnClick({R.id.ll_more_no_pass, R.id.ll_more_pass})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_more_pass:
                showProgressDialog();
                getPassData();
                break;
            case R.id.ll_more_no_pass:
                showProgressDialog();
                getNoPassData();
                break;
        }
    }

    public void getPassData() {
        API.getInstance().getSkillBill(1, passData.size(), new CommonCallBack<List<SkillBean.DataBean>>() {
            @Override
            public void onSuccess(List<SkillBean.DataBean> dataBeen) {
                if (passAdapter != null) {
                    passData.addAll(dataBeen);
                    passAdapter.notifyDataSetChanged();
                    if (passData.size()==0){
                        passView.setVisibility(View.GONE);
                    }else {
                        passView.setVisibility(View.VISIBLE);
                    }
                }
                finishRefresh();
            }

            @Override
            public void onFailure() {
                finishRefresh();
            }
        });
    }

    public void showProgressDialog() {
        show = StyledDialog.buildLoading("拼命加载中..").setActivity(XActivityStack.getInstance().topActivity()).show();

    }

    public void dimissProgressDialog() {
        if (show != null) {
            show.dismiss();
        }
    }

    public void getNoPassData() {
        API.getInstance().getSkillBill(2, noPassData.size(), new CommonCallBack<List<SkillBean.DataBean>>() {
            @Override
            public void onSuccess(List<SkillBean.DataBean> dataBeen) {
                if (noPassAdapter != null) {
                    noPassData.addAll(dataBeen);
                    noPassAdapter.notifyDataSetChanged();
                    if (noPassData.size()==0){
                        noPassView.setVisibility(View.GONE);
                    }else {
                        noPassView.setVisibility(View.VISIBLE);
                    }
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
            refreshLayout.finishRefresh();
            dimissProgressDialog();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
