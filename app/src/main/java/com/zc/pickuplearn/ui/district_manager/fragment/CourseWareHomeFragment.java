package com.zc.pickuplearn.ui.district_manager.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.CourseWareHomeBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.district_manager.adapter.CourseWareHomeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 */
public class CourseWareHomeFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String abilitytype;
    private String mParam2;

    @BindView(R.id.rc_content)
    RecyclerView rcContent;
    @BindView(R.id.x_loading)
    XLoadingView xLoading;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;
    List<CourseWareHomeBean> mData = new ArrayList<>();
    boolean IS_REFRESH = true;
    private int mCurrent = 0;
    private CourseWareHomeAdapter courseWareHomeAdapter;
    private int  orderType = 1;//1时间 2 热度
    public CourseWareHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     *
     * @return A new instance of fragment CourseWareHomeFragment.
     */
    public static CourseWareHomeFragment newInstance(String abilitytype) {
        CourseWareHomeFragment fragment = new CourseWareHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, abilitytype);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_course_ware_home;
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            abilitytype = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initXloading();
        initRecycleView();
        initRefresh();
    }

    private void initXloading() {
        xLoading.setEmptyViewWarning("暂无数据");
    }

    private void initRecycleView() {
        rcContent.setHasFixedSize(true);
        rcContent.setLayoutManager(new LinearLayoutManager(getmCtx()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        courseWareHomeAdapter = new CourseWareHomeAdapter(getmCtx(), mData);
        rcContent.setAdapter(courseWareHomeAdapter);
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
        refreshLayout.setEnableLoadmore(false);
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
        API.getInstance().getCourseWareHomePage(mCurrent, orderType+"",abilitytype,new CommonCallBack<List<CourseWareHomeBean>>() {

            @Override
            public void onSuccess(List<CourseWareHomeBean> courseWareHomeBeen) {
                if (courseWareHomeAdapter != null) {
                    if (IS_REFRESH) {
                        mData.clear();
                    }
                    mData.addAll(courseWareHomeBeen);
                    mCurrent = mData.size();
                    courseWareHomeAdapter.notifyDataSetChanged();
                }
                finishRefresh();
            }

            @Override
            public void onFailure() {
                finishRefresh();
            }
        });

    }


    public void setOrderType(int order){
        if (refreshLayout!=null){
            orderType = order;
            if (courseWareHomeAdapter!=null)
                courseWareHomeAdapter.setOrderType(order);
            refreshLayout.autoRefresh();
        }
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
