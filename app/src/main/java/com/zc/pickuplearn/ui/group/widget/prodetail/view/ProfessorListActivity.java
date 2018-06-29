package com.zc.pickuplearn.ui.group.widget.prodetail.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.beans.Professor;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.group.widget.prodetail.widget.ProfessorListAdapter;
import com.zc.pickuplearn.ui.view.HeadView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProfessorListActivity extends BaseActivity {

    @BindView(R.id.hv_list_head)
    HeadView hvHead;
    @BindView(R.id.lrv_list)
    LRecyclerView rcTeamDynamic;

    List<Professor> mData = new ArrayList<>();
    private final static String TAG = "ProfessorListActivity";
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;
    private LRecyclerViewAdapter viewAdapter;
    private ProfessionalCircleBean professionalCircleBean;

    public static void startProfessorList(Context context, ProfessionalCircleBean professionalCircleBean) {
        Intent intent = new Intent(context, ProfessorListActivity.class);
        intent.putExtra(TAG, professionalCircleBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_professor_list;
    }

    @Override
    public void initView() {
        professionalCircleBean = (ProfessionalCircleBean) getIntent().getSerializableExtra(TAG);
    }

    @Override
    protected void initData() {
        initTopBar();
        initRecycleView();
    }

    private void initTopBar() {
        hvHead.setTitle("专家信息");
    }
    private void initRecycleView() {
        rcTeamDynamic.setHasFixedSize(true);
        rcTeamDynamic.setLayoutManager(new LinearLayoutManager(this));
        rcTeamDynamic.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        ProfessorListAdapter adapter = new ProfessorListAdapter(this, mData);
        //适配器转换
        viewAdapter = new LRecyclerViewAdapter(adapter);
        rcTeamDynamic.setAdapter(viewAdapter);
        rcTeamDynamic.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                clearData();
                getData();
            }
        });
        rcTeamDynamic.setLoadMoreEnabled(true);
        rcTeamDynamic.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isRefresh = false;
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rcTeamDynamic);
                if (state == LoadingFooter.State.Loading) {
                    return;
                }
                getData();
            }
        });
        rcTeamDynamic.setRefreshing(true);//自动刷新
    }

    public void getData() {
        API.getProfessorList(professionalCircleBean,mCurrentCounter,"",new CommonCallBack<List<Professor>>() {
            @Override
            public void onSuccess(List<Professor> professorList) {
                if (isRefresh) {
                    refreshStatFinish();
                    mData.addAll(professorList);
                    mCurrentCounter = mData.size();
                    if (mData.isEmpty()) {
                        RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.TheEnd);
                    }
                } else {
                    mData.addAll(professorList);
                    mCurrentCounter = mData.size();
                    setLoadMoreStats(mCurrentCounter);
                }
                viewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                refreshStatFinish();
                RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.NetWorkError);
            }
        });
    }

    public void clearData() {
        mCurrentCounter = 0;
        mData.clear();
        RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.Normal);
        viewAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新结束
     */
    public void refreshStatFinish() {
        rcTeamDynamic.refreshComplete();
    }

    public void setLoadMoreStats(int count) {
        if (count % UrlMethod.PAZE_SIZE != 0) {
            RecyclerViewStateUtils.setFooterViewState(this, rcTeamDynamic, UrlMethod.PAZE_SIZE, LoadingFooter.State.TheEnd, null);
            return;
        }
        if (count % UrlMethod.PAZE_SIZE == 0) {
            RecyclerViewStateUtils.setFooterViewState(this, rcTeamDynamic, UrlMethod.PAZE_SIZE, LoadingFooter.State.Normal, null);
        }
    }
}
