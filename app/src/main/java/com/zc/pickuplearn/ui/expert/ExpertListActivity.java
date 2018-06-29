package com.zc.pickuplearn.ui.expert;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.Professor;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.event.ExpertTypeEvent;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.expert.adapter.ExpertListAdapter;
import com.zc.pickuplearn.ui.type.view.PopScrollGridActivity;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;
import com.zc.pickuplearn.ui.view.ClearEditText;
import com.zc.pickuplearn.ui.view.MaxRecycleView;
import com.zc.pickuplearn.utils.KeyBoardUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 专家列表页面
 */
public class ExpertListActivity extends EventBusActivity {

    @BindView(R.id.root)
    LinearLayout root;

    public static void open(Context context) {
        Intent intent = new Intent(context, ExpertListActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.ib_type)
    ImageButton ibType;
    @BindView(R.id.et_search)
    ClearEditText etSearch;
    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.tv_hot)
    TextView tvHot;
    @BindView(R.id.rc_content)
    MaxRecycleView rcContent;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.tv_title)
    TextView tv_title;
    List<Professor> mCaseData = new ArrayList<>();
    private ExpertListAdapter expertListAdapter;
    private int mCurrent_index = 0;
    private boolean IS_REFRESH = true;
    private int Order_Type = 1;//1 评级 2 解疑数 3 分类 4 关键字
    private String keyword = "";
    private String typecode = "";
    @Override
    protected void initData() {
        initToolBar();
        initContent();
        initRefresh();
    }


    @Override
    public int setLayout() {
        return R.layout.activity_expert_list;
    }

    private void initRefresh() {
        srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (refreshlayout.isLoadmoreFinished())
                    refreshlayout.setLoadmoreFinished(false);
                mCurrent_index = 0;
                IS_REFRESH = true;
                getData();
            }
        });
        srlRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                IS_REFRESH = false;
                getData();
            }
        });
        srlRefresh.autoRefresh();
    }

    private void getData() {
        API.getExpertList(mCurrent_index, Order_Type,keyword,typecode,new CommonCallBack<List<Professor>>() {
            @Override
            public void onSuccess(List<Professor> industryStandardBeen) {
                if (IS_REFRESH) {
                    mCaseData.clear();
                    mCaseData.addAll(industryStandardBeen);
                } else {
                    mCaseData.addAll(industryStandardBeen);
                    if (industryStandardBeen.size() == 0) {
                        if (srlRefresh != null) {
                            srlRefresh.setLoadmoreFinished(true);
                        }
                    }
                }
                if (mCaseData != null) {
                    mCurrent_index = mCaseData.size();
                }
                if (expertListAdapter != null) {
                    expertListAdapter.notifyDataSetChanged();
                }
                setFreshStatus();
            }

            @Override
            public void onFailure() {
                setFreshStatus();
            }
        });

    }

    public void setFreshStatus() {
        if (IS_REFRESH) {
            if (srlRefresh != null) {
                srlRefresh.finishRefresh();
            }
        } else {
            if (srlRefresh != null) {
                srlRefresh.finishLoadmore();
            }
        }
    }

    private void initToolBar() {
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        tv_title.setText(XFrame.getString(R.string.expert_answer));
        ibType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//分类
                PopScrollGridActivity.startScrollGridActivity(ExpertListActivity.this, TypeGreenDaoEvent.EXPERT);
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getId() == R.id.et_search && EditorInfo.IME_ACTION_SEARCH == actionId) {
                    keyword = etSearch.getText().toString();
                    KeyBoardUtils.closeKeybord(etSearch, ExpertListActivity.this);
                    etSearch.setText("");
                    Order_Type = 4;
                    if (srlRefresh!=null){
                        srlRefresh.autoRefresh();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void initView() {
        initStatusBar(root);
    }


    public void initContent() {
        rcContent.setHasFixedSize(true);
        rcContent.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        expertListAdapter = new ExpertListAdapter(this, mCaseData);
        rcContent.setAdapter(expertListAdapter);
    }

    @OnClick({R.id.tv_hot, R.id.tv_new})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hot:
                if (Order_Type != 2) {
                    Order_Type = 2;
                    srlRefresh.autoRefresh();
                }
                break;
            case R.id.tv_new:
                if (Order_Type != 1) {
                    Order_Type = 1;
                    srlRefresh.autoRefresh();
                }
                break;
        }
    }


    @Subscribe
    public void onEventMainThread(ExpertTypeEvent event) {
        if (event != null) {
            if (srlRefresh != null){
                QuestionBean questionBean = event.getQuestionBean();
                Order_Type = 3;
                typecode = questionBean.getQUESTIONTYPECODE();
                srlRefresh.autoRefresh();
            }

        }
    }
}
