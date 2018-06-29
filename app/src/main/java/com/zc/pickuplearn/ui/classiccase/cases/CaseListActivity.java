package com.zc.pickuplearn.ui.classiccase.cases;

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
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.event.ClassicCaseTypeEvent;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.classiccase.cases.adapter.CaseItemAdapter;
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
 * 经典案例列表
 */
public class CaseListActivity extends EventBusActivity {

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
    List<ClassicCaseBean> mCaseData = new ArrayList<>();
    @BindView(R.id.root)
    LinearLayout root;
    private CaseItemAdapter caseItemAdapter;
    private int mCurrent_index = 0;
    private boolean IS_REFRESH = true;
    private int Order_Type = 1;//1 new 2 hot 3分类 4 关键字
    private String keyword = "";
    private String typecode = "";

    public static void open(Context context) {
        Intent intent = new Intent(context, CaseListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_case_list;
    }

    @Override
    protected void initData() {
        initStatusBar(root);
        initToolBar();
        initContent();
        initRefresh();
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
        API.getClassicCaseList(mCurrent_index, Order_Type, keyword, typecode, new CommonCallBack<List<ClassicCaseBean>>() {
            @Override
            public void onSuccess(List<ClassicCaseBean> classicCaseBeen) {
                if (IS_REFRESH) {
                    mCaseData.clear();
                    mCaseData.addAll(classicCaseBeen);
                } else {
                    mCaseData.addAll(classicCaseBeen);
                    if (classicCaseBeen.size() == 0) {
                        if (srlRefresh != null) {
                            srlRefresh.setLoadmoreFinished(true);
                        }
                    }
                }
                if (mCaseData != null) {
                    mCurrent_index = mCaseData.size();
                }
                if (caseItemAdapter != null) {
                    caseItemAdapter.notifyDataSetChanged();
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
        tv_title.setText(XFrame.getString(R.string.home_classiccase));
        ibType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopScrollGridActivity.startScrollGridActivity(CaseListActivity.this, TypeGreenDaoEvent.CASE);
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getId() == R.id.et_search && EditorInfo.IME_ACTION_SEARCH == actionId) {
                    keyword = etSearch.getText().toString();
                    KeyBoardUtils.closeKeybord(etSearch, CaseListActivity.this);
                    etSearch.setText("");
                    Order_Type = 4;
                    if (srlRefresh != null) {
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
        caseItemAdapter = new CaseItemAdapter(this, mCaseData, CaseItemAdapter.Type.CASE_LIST);
        rcContent.setAdapter(caseItemAdapter);
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
    public void onEventMainThread(ClassicCaseTypeEvent event) {
        if (event != null) {
            if (srlRefresh != null) {
                QuestionBean questionBean = event.getQuestionBean();
                Order_Type = 3;
                typecode = questionBean.getQUESTIONTYPECODE();
                srlRefresh.autoRefresh();
            }

        }
    }

}
