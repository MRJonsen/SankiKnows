package com.zc.pickuplearn.ui.main_pick_up_learn;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.FunctionBean;
import com.zc.pickuplearn.beans.InfoBean;
import com.zc.pickuplearn.event.BaseEvent;
import com.zc.pickuplearn.event.FunctionEditEvent;
import com.zc.pickuplearn.event.PickLearnMainFragmentInfoRefreshEvent;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.EventBusBaseFragment;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view.WenDaFragment;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.DynamicMoreActivity;
import com.zc.pickuplearn.ui.main_pick_up_learn.adapter.EditFunctionAdapter;
import com.zc.pickuplearn.ui.main_pick_up_learn.adapter.FixFunctionAdapter;
import com.zc.pickuplearn.ui.main_pick_up_learn.adapter.InfoAdapter;
import com.zc.pickuplearn.ui.view.ClearEditText;
import com.zc.pickuplearn.ui.view.SimpleFootView;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.StatusBarUtil;
import com.zc.pickuplearn.utils.SystemUtils;
import com.zc.pickuplearn.utils.UIUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * 拾学首页
 */
public class PulMainFragment extends EventBusBaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView rc_fixed;
    private RecyclerView rc_edit;
    private LRecyclerView rc_information;
    private View inflate;
    private SmartRefreshLayout refreshLayout;
    private ArrayList<FunctionBean> editFunctionList;
    private EditFunctionAdapter editFunctionAdapter;
    private ArrayList<InfoBean> infoList;
    private LRecyclerViewAdapter infoAdapter;
    private int INFO_INDEX = 0;//消息索引
    private ClearEditText mSearchEditext;

    public static PulMainFragment newInstance(String param1, String param2) {
        PulMainFragment fragment = new PulMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_pul_main;
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            String mParam1  = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        findView();
        initFixedTab();
        initEditTab();
        initInfoTab();
        //处理状态栏及间距
        StatusBarUtil.darkMode((Activity) getmCtx());
        StatusBarUtil.setPaddingSmart(getmCtx(), getRootView());
        initListener();
    }

    /**
     * 设置监听 加载 刷新
     */
    private void initListener() {
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                /*请求我的功能列表*/
                API.getFunctionList(new CommonCallBack<List<FunctionBean>>() {

                    @Override
                    public void onSuccess(List<FunctionBean> functionBeen) {
                        editFunctionList.clear();
                        editFunctionList.addAll(functionBeen);
                        editFunctionList.add(new FunctionBean(R.mipmap.more, "更多"));
                        editFunctionAdapter.notifyDataSetChanged();
                        refreshlayout.finishRefresh();
                    }

                    @Override
                    public void onFailure() {
                        refreshlayout.finishRefresh();
                    }
                });
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
        mSearchEditext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getId() == R.id.et_search && EditorInfo.IME_ACTION_SEARCH == actionId) {
                    String searchtext = mSearchEditext.getText().toString();
                    KeyBoardUtils.closeKeybord(mSearchEditext, getContext());
                    mSearchEditext.setText("");
                    DynamicMoreActivity.startDynamicMoreActivity(getActivity(), WenDaFragment.THE_SEARCH, null, searchtext);
                    return true;
                }
                return false;
            }
        });
    }

    private void getInfoData(final RefreshLayout refreshlayout, boolean isrefresh) {
        if (isrefresh) {
            INFO_INDEX = 0;
            infoAdapter.removeFooterView();
            if (refreshlayout.isLoadmoreFinished())
                refreshLayout.setLoadmoreFinished(false);

            API.getMessageList(INFO_INDEX, new CommonCallBack<List<InfoBean>>() {
                @Override
                public void onSuccess(List<InfoBean> infoBeen) {
                    INFO_INDEX = infoBeen.size();
                    infoList.clear();
                    infoList.addAll(infoBeen);
                    infoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure() {
                    refreshlayout.finishLoadmore();
                }
            });
        } else {
            /*消息更多加载*/
            API.getMessageList(INFO_INDEX, new CommonCallBack<List<InfoBean>>() {
                @Override
                public void onSuccess(List<InfoBean> infoBeen) {
                    if (infoBeen.size() == 0) {
                        infoAdapter.addFooterView(new SimpleFootView(getmCtx()));
                        infoAdapter.notifyDataSetChanged();
                        refreshlayout.setLoadmoreFinished(true);
                    } else {
                        INFO_INDEX += infoBeen.size();
                        infoList.addAll(infoBeen);
                        infoAdapter.notifyDataSetChanged();
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

    /**
     * 首页动态消息
     */
    private void initInfoTab() {
        infoList = new ArrayList<>();
        rc_information.setHasFixedSize(true);
        rc_information.setLayoutManager(new LinearLayoutManager(getmCtx()));
        rc_information.setItemAnimator(new DefaultItemAnimator());
        infoAdapter = new LRecyclerViewAdapter(new InfoAdapter(getmCtx(), infoList));
        infoAdapter.addHeaderView(inflate);
        rc_information.setAdapter(infoAdapter);
        rc_information.setLoadMoreEnabled(false);

    }

    /**
     * 功能编辑模块
     */
    private void initEditTab() {
        editFunctionList = new ArrayList<>();
        rc_edit.setHasFixedSize(true);
        rc_edit.setBackgroundColor(UIUtils.getResources().getColor(R.color.white));
        rc_edit.setLayoutManager(new GridLayoutManager(getmCtx(), 4));
        rc_edit.setItemAnimator(new DefaultItemAnimator());
        editFunctionAdapter = new EditFunctionAdapter(getmCtx(), editFunctionList);
        rc_edit.setAdapter(editFunctionAdapter);
    }

    /**
     * 找控件
     */
    private void findView() {
        View rootView = getRootView();
        //搜索框
        mSearchEditext = (ClearEditText) rootView.findViewById(R.id.et_search);
        refreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.srl_refresh);
        rc_information = (LRecyclerView) rootView.findViewById(R.id.rc_information);
        inflate = UIUtils.inflate(R.layout.fragment_main_top_view);
        rc_fixed = (RecyclerView) inflate.findViewById(R.id.rc_fixed_function);
        rc_edit = (RecyclerView) inflate.findViewById(R.id.rc_edit_function);
        int screenWidth = SystemUtils.getScreenWidth(getmCtx());
        inflate.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 固定功能模块
     */
    private void initFixedTab() {
        ArrayList<FunctionBean> functionBeanArrayList = new ArrayList<>();
        FunctionBean functionBean = new FunctionBean(R.mipmap.tiku, "能力题库");
        FunctionBean functionBean1 = new FunctionBean(R.mipmap.zhidaowenda, "知道问答");
        FunctionBean functionBean2 = new FunctionBean(R.mipmap.nenglixuetang, "能力学堂");
        FunctionBean functionBean3 = new FunctionBean(R.mipmap.yuangongfazhan, "员工发展");
        functionBeanArrayList.add(functionBean);
        functionBeanArrayList.add(functionBean1);
        functionBeanArrayList.add(functionBean2);
        functionBeanArrayList.add(functionBean3);
        rc_fixed.setHasFixedSize(true);
        rc_fixed.setLayoutManager(new GridLayoutManager(getmCtx(), 4));
        rc_fixed.setItemAnimator(new DefaultItemAnimator());
        rc_fixed.setAdapter(new FixFunctionAdapter(getmCtx(), functionBeanArrayList));
    }

    /*接收功能编辑完成事件*/
    @Subscribe
    public void onEventMainThread(BaseEvent event) {
        if (event instanceof FunctionEditEvent) {
            FunctionEditEvent functionEditEvent = (FunctionEditEvent) event;
            editFunctionList.clear();
            editFunctionList.addAll(functionEditEvent.datas);
            editFunctionList.add(new FunctionBean(R.mipmap.more, "更多"));
            editFunctionAdapter.notifyDataSetChanged();
        }
        if (event instanceof PickLearnMainFragmentInfoRefreshEvent){
            getInfoData(refreshLayout,true);
        }
    }
}
