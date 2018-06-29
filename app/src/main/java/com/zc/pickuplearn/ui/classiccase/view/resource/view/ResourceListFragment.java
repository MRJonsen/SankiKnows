package com.zc.pickuplearn.ui.classiccase.view.resource.view;


import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.SourceBean;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.classiccase.view.resource.widget.ResourceListAdapter;
import com.zc.pickuplearn.ui.view.ExpandableTextView;
import com.zc.pickuplearn.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author bin E-mail: chenbin252@163.COM
 * @version 创建时间：2016-10-25 下午4:47:48
 * @Describe
 */
public class ResourceListFragment extends BaseFragment {

    public static final String TAG = "ResourceListFragment";
    @BindView(R.id.list)
    LRecyclerView mContent;
    private List<SourceBean> mData = new ArrayList<>();
    private LRecyclerViewAdapter mClassicCaseListAdapter;

    public static ResourceListFragment newInstance(ClassicCaseBean classicCaseBean) {
        Bundle args = new Bundle();
        ResourceListFragment fragment = new ResourceListFragment();
        args.putSerializable(TAG, classicCaseBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.refresh_loadmore_contentlayout;
    }

    @Override
    public void initView() {
        ClassicCaseBean caseBean = getInitData();
        initlayout();
        initHeader(caseBean);
        initData(caseBean);
    }

    /**
     * t头部简介布局
     */
    private void initHeader(ClassicCaseBean bean) {
        View view = UIUtils.inflate(R.layout.res_list_header_item);
        mClassicCaseListAdapter.addHeaderView(view);
        if (bean != null) {
            ExpandableTextView etv_introduction = ButterKnife.findById(view, R.id.etv_introduction);
            etv_introduction.setText(bean.getCASEEXPLAIN());
        }
    }

    /**
     * 初始化数据
     *
     * @param caseBean
     */
    private void initData(ClassicCaseBean caseBean) {
        if (caseBean != null) {
            ArrayList<SourceBean> sourceBeen1 = new ArrayList<>();
            if (!TextUtils.isEmpty(caseBean.getCOURSEFILEURL()) && !TextUtils.isEmpty(caseBean.getCOURSEFILENAME())) {
                List<SourceBean> sourceBeen = ResultStringCommonUtils.doSplitUrls(caseBean.getCOURSEFILEURL(), caseBean.getCOURSEFILENAME());
                sourceBeen1.addAll(sourceBeen);
            }
            if (!TextUtils.isEmpty(caseBean.getH5URL()) && !TextUtils.isEmpty(caseBean.getH5URLNAME())) {
                List<SourceBean> h5sourcebeen = doH5Resource(caseBean.getH5URL(), caseBean.getH5URLNAME());
                sourceBeen1.addAll(h5sourcebeen);
            }
            if (sourceBeen1.size() > 0) {
                addData(sourceBeen1);
            }
        }
    }


    /**
     * 处理h5资源
     *
     * @param sourceurl
     * @param sourcename
     * @return
     */
    public List<SourceBean> doH5Resource(String sourceurl, String sourcename) {
        ArrayList<SourceBean> sourceList = new ArrayList<SourceBean>();
        if (!TextUtils.isEmpty(sourceurl)
                && !TextUtils.isEmpty(sourcename)) {
            String[] urls = sourceurl.split(",");
            String[] names = sourcename.split(",");
            for (int j = 0; j < urls.length; j++) {
                SourceBean sourceBean = new SourceBean();
                sourceBean.url = urls[j];
                sourceBean.name = names[j];
                sourceList.add(sourceBean);
            }
        }
        return sourceList;
    }

    /**
     * 添加数据
     */
    private void addData(List<SourceBean> list) {
        if (mData != null && list != null) {
            mData.clear();
            mClassicCaseListAdapter.notifyDataSetChanged();
            mData.addAll(list);
            mClassicCaseListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化布局
     */
    private void initlayout() {
        mContent.setHasFixedSize(true);
        mContent.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置Item增加、移除动画
        mContent.setItemAnimator(new DefaultItemAnimator());
        mContent.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        ResourceListAdapter classicCaseListAdapter = new ResourceListAdapter(getContext(), R.layout.item_download_res, mData);
        //适配器转换
        mClassicCaseListAdapter = new LRecyclerViewAdapter(classicCaseListAdapter);
        mContent.setAdapter(mClassicCaseListAdapter);
        mContent.setPullRefreshEnabled(false);
        mContent.setLoadMoreEnabled(false);
    }

    /**
     * 获取初始化参数
     *
     * @return
     */
    public ClassicCaseBean getInitData() {
        return (ClassicCaseBean) getArguments().getSerializable(TAG);
    }

}
