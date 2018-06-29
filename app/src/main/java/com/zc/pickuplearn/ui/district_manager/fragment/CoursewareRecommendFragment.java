package com.zc.pickuplearn.ui.district_manager.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.district_manager.adapter.CourseWareAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 课件列表 推荐
 */
public class CoursewareRecommendFragment extends BaseFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rc_content)
    RecyclerView rcContent;
    @BindView(R.id.x_loading)
    XLoadingView xLoading;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;

    private RecommendBean mParam1;
    private String abilitytype;
    private int orderType = 1;//1时间 2 热度
    List<CourseWareBean.DatasBean> mData = new ArrayList<>();
    boolean IS_REFRESH = true;
    private int mCurrent = 0;
    private CourseWareAdapter courseWareAdapter;
    private String SearchText = "";

    public CoursewareRecommendFragment() {
        // Required empty public constructor
    }

    public static CoursewareRecommendFragment newInstance(RecommendBean recommendBean,String abilitytype) {
        CoursewareRecommendFragment fragment = new CoursewareRecommendFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, recommendBean);
        args.putString(ARG_PARAM2, abilitytype);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_courseware_recommend;
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            mParam1 = (RecommendBean) getArguments().getSerializable(ARG_PARAM1);
            abilitytype = getArguments().getString(ARG_PARAM2);
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
        courseWareAdapter = new CourseWareAdapter(getmCtx(), mData, CourseWareAdapter.Type.Other);
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

        switch (mParam1.getType()) {
            case Recommend:
                API.getInstance().getCourseWareRecommend(mCurrent, orderType + "",abilitytype, new CommonCallBack<List<CourseWareBean.DatasBean>>() {
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
                break;
            case Relative:
                API.getInstance().getRelatCourseWare(mCurrent, mParam1.getBean(), new CommonCallBack<List<CourseWareBean.DatasBean>>() {
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
                break;
            case Search:
                if (TextUtils.isEmpty(SearchText)){
                    finishRefresh();
                    return;
                }
                API.getInstance().searchCourseWare(mCurrent, SearchText, new CommonCallBack<List<CourseWareBean.DatasBean>>() {
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
                break;
        }


    }


    public void setSearchText(String searchText) {
        SearchText = searchText;
        if (refreshLayout != null)
            refreshLayout.autoRefresh();
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

    public void setOrderType(int order) {
        if (refreshLayout != null) {
            orderType = order;
            refreshLayout.autoRefresh();
        }
    }

    public static class RecommendBean implements Serializable {
        Type type;
        CourseWareBean.DatasBean bean;

        public CourseWareBean.DatasBean getBean() {
            return bean;
        }

        public void setBean(CourseWareBean.DatasBean bean) {
            this.bean = bean;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }
    }

    public enum Type {
        Recommend, Relative, Search
    }
}
