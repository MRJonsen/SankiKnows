package com.zc.pickuplearn.ui.msgbox;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.PersonMsgBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.msgbox.adpter.PersonMsgListAdapter;
import com.zc.pickuplearn.ui.msgbox.event.EventPersonMsg;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息 fragment
 */
public class PersonMSGFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    AlertDialog clearAlertDialog;
    @BindView(R.id.rl_edit)
    RelativeLayout mRl_edit;
    @BindView(R.id.rlc_list)
    LRecyclerView lrcList;
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;
    List<PersonMsgBean> mData = new ArrayList<>();
    private LRecyclerViewAdapter viewAdapter;
    public PersonMsgListAdapter personMsgListAdapter;

    public static PersonMSGFragment newInstance(String param1, String param2) {
        PersonMSGFragment fragment = new PersonMSGFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_person_msg;
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
        personMsgListAdapter = new PersonMsgListAdapter(getmCtx(), mData,false);
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
        API.getPersonMessage(mCurrentCounter,new CommonCallBack<List<PersonMsgBean>>() {
            @Override
            public void onSuccess(List<PersonMsgBean> personMsgBeen) {
                LogUtils.e("个人消息数量"+personMsgBeen.size());
                if (isRefresh) {
                    refreshStatFinish();
                    mData.addAll(personMsgBeen);
                    mCurrentCounter = mData.size();
                    if (mData.isEmpty()) {
                        RecyclerViewStateUtils.setFooterViewState(lrcList, LoadingFooter.State.TheEnd);
                    }
                } else {
                    mData.addAll(personMsgBeen);
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

    public void setMsgEditMode(boolean mode){
        if (mode){
            mRl_edit.setVisibility(View.VISIBLE);
        }else {
            mRl_edit.setVisibility(View.GONE);
        }
        personMsgListAdapter.setInEditMode(mode);
    }

    public void clearData() {
        mCurrentCounter = 0;
        mData.clear();
        RecyclerViewStateUtils.setFooterViewState(lrcList, LoadingFooter.State.Normal);
        viewAdapter.notifyDataSetChanged();
    }

    /**
     * 设置消息全部已读
     */
    public void setReadStatus() {
        API.setPersonMsgReadStatus(true,null, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                personMsgListAdapter.setReadData();
                EventBus.getDefault().post(new EventPersonMsg());//通知通知页面刷新提示条数
            }

            @Override
            public void onFailure() {
            }
        });
    }

    /**
     * 清空消息提示框
     */
    private void showClearAlert() {
        if (clearAlertDialog == null) {
            clearAlertDialog = new AlertDialog(getmCtx()).builder();
            clearAlertDialog
                    .setTitle("提示").setMsg("是否清空消息?")
                    .setPositiveButton("是", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            API.deletPersonMessage(true, null, new CommonCallBack<String>() {
                                @Override
                                public void onSuccess(String personMsgBeen) {
                                 personMsgListAdapter.clearData();
                                    EventBus.getDefault().post(new EventPersonMsg());//通知通知页面刷新提示条数
                                }

                                @Override
                                public void onFailure() {

                                }
                            });
                        }
                    }).setNegativeButton("否", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        clearAlertDialog.show();
    }

    @OnClick({R.id.tv_all_delete,R.id.tv_all_read,R.id.tv_delete})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_all_delete:
                showClearAlert();
                break;
            case R.id.tv_all_read:
                setReadStatus();
                break;
            case R.id.tv_delete:
                personMsgListAdapter.clearSomeData();
                break;
        }
    }
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
