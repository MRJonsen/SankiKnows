package com.zc.pickuplearn.ui.msgbox;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.SystemMsgBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.msgbox.adpter.SystemMsgListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 系统消息
 */
public class SystemMsgFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    @BindView(R.id.rlc_list)
    LRecyclerView lrcList;
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;
    List<SystemMsgBean> mData = new ArrayList<>();
    private LRecyclerViewAdapter viewAdapter;
    public SystemMsgListAdapter personMsgListAdapter;

    public static SystemMsgFragment newInstance(String param1, String param2) {
        SystemMsgFragment fragment = new SystemMsgFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_system_msg;
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initRecycleView();
    }

    private void initRecycleView() {
        lrcList.setHasFixedSize(true);
        lrcList.setLayoutManager(new LinearLayoutManager(getmCtx()));
        lrcList.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        personMsgListAdapter = new SystemMsgListAdapter(getmCtx(), mData);
        //适配器转换
        viewAdapter = new LRecyclerViewAdapter(personMsgListAdapter);
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
        API.getSystemMessage(mCurrentCounter,new CommonCallBack<List<SystemMsgBean>>() {
            @Override
            public void onSuccess(List<SystemMsgBean> systemMsgBean) {
                if (isRefresh) {
                    refreshStatFinish();
                    mData.addAll(systemMsgBean);
                    mCurrentCounter = mData.size();
                    if (mData.isEmpty()) {
                        RecyclerViewStateUtils.setFooterViewState(lrcList, LoadingFooter.State.TheEnd);
                    }
                } else {
                    mData.addAll(systemMsgBean);
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


//    /**
//     * 清空消息提示框
//     */
//    private void showClearAlert() {
//        if (clearAlertDialog == null) {
//            clearAlertDialog = new AlertDialog(getmCtx()).builder();
//            clearAlertDialog
//                    .setTitle("提示").setMsg("是否清空消息?")
//                    .setPositiveButton("是", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            API.deletPersonMessage(true, null, new CommonCallBack<String>() {
//                                @Override
//                                public void onSuccess(String personMsgBeen) {
//                                 personMsgListAdapter.clearData();
//                                }
//
//                                @Override
//                                public void onFailure() {
//
//                                }
//                            });
//                        }
//                    }).setNegativeButton("否", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
//        clearAlertDialog.show();
//    }

    /**
     * 刷新结束
     */
    public void refreshStatFinish() {
        lrcList.refreshComplete();
    }

    public void setLoadMoreStats(int count) {
        if (count % UrlMethod.PAZE_SIZE != 0) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), lrcList, UrlMethod.PAZE_SIZE, LoadingFooter.State.TheEnd, null);
            return;
        }
        if (count % UrlMethod.PAZE_SIZE == 0) {
            RecyclerViewStateUtils.setFooterViewState(getActivity(), lrcList, UrlMethod.PAZE_SIZE, LoadingFooter.State.Normal, null);
        }
    }
}
