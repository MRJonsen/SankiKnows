package com.zc.pickuplearn.ui.group.widget.prodetail.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.Professor;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.group.widget.prodetail.widget.ProfessorHistoryAnswerListAapter;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.SeachBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProfessorHistroyAnswerActivity extends BaseActivity {

    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.sb_search_view)
    SeachBar sbSearchView;
    @BindView(R.id.lrc_rv)
    LRecyclerView rcTeamDynamic;
    @BindView(R.id.x_loading)
    XLoadingView xLoadingView;
    List<QuestionBean> mData = new ArrayList<>();
    private boolean isRefresh = false;
    private static final String TAG = "ProfessorHistroyAnswerActivity";
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;
    public String SearchText = "";
    private LRecyclerViewAdapter viewAdapter;
    private Professor professor;


    public static void startProfessorHistoryAnswerActivity(Context context, Professor professor) {
        Intent intent = new Intent(context, ProfessorHistroyAnswerActivity.class);
        intent.putExtra(TAG, professor);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_professor_histroy_answer;
    }

    @Override
    public void initView() {
        xLoadingView.setEmptyViewWarning("好心塞，居然是空的");
        professor = (Professor) getIntent().getSerializableExtra(TAG);
        initTopBar();
        initSearBar();
        initRecycleView();
    }

    private void initRecycleView() {
        rcTeamDynamic.setHasFixedSize(true);
        rcTeamDynamic.setLayoutManager(new LinearLayoutManager(this));
        rcTeamDynamic.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        ProfessorHistoryAnswerListAapter adapter = new ProfessorHistoryAnswerListAapter(this, mData, professor);
        //适配器转换
        viewAdapter = new LRecyclerViewAdapter(adapter);
        rcTeamDynamic.setAdapter(viewAdapter);
        rcTeamDynamic.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                SearchText ="";
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

    private void initTopBar() {
        hvHead.setTitle("专家解答");
    }

    private void initSearBar() {
        sbSearchView.setSearchEditTextHint("搜索问题");
        sbSearchView.setSearchButtonOnClickListener(new SeachBar.SearchButtonOnClickListener() {
            @Override
            public void onClick() {
                SearchText = sbSearchView.getSearchText();
                if (TextUtils.isEmpty(SearchText)) {
                    showToast("请输入查询内容");
                    return;
                }
                isRefresh = true;
                clearData();
                getData();
            }
        });
    }

    @Override
    protected void initData() {

    }

    public void getData() {
        API.professorQustionSearch(SearchText, mCurrentCounter, professor, new CommonCallBack<List<QuestionBean>>() {
            @Override
            public void onSuccess(List<QuestionBean> teamCircleBeen) {
                xLoadingView.showContent();
                if (isRefresh) {
                    refreshStatFinish();
                    mData.addAll(teamCircleBeen);
                    mCurrentCounter = mData.size();
                    if (mData.isEmpty()) {
                        xLoadingView.showEmpty();
                        RecyclerViewStateUtils.setFooterViewState(rcTeamDynamic, LoadingFooter.State.TheEnd);
                    }
                } else {
                    mData.addAll(teamCircleBeen);
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
