package com.zc.pickuplearn.ui.classiccase.cases;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.classiccase.cases.adapter.CaseItemAdapter;
import com.zc.pickuplearn.ui.classiccase.cases.adapter.GlideImageLoader;
import com.zc.pickuplearn.ui.classiccase.view.ClassicCaseDetailActivity;
import com.zc.pickuplearn.ui.view.MaxRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CaseHomeActivity extends BaseActivity {

    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.bt_right)
    Button btRight;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_new_more)
    TextView tvNewMore;
    @BindView(R.id.new_content)
    MaxRecycleView newContent;
    @BindView(R.id.tv_hot_more)
    TextView tvHotMore;
    @BindView(R.id.hot_content)
    MaxRecycleView hotContent;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.root)
    LinearLayout root;
    List<ClassicCaseBean> mBannerData = new ArrayList<>();
    List<ClassicCaseBean> mNewData = new ArrayList<>();
    List<ClassicCaseBean> mHotData = new ArrayList<>();
    private CaseItemAdapter hotAdapter;
    private CaseItemAdapter newAdapter;
    private int mCurrent_index = 0;
    private boolean IS_REFRESH = true;

    public static void open(Context context) {
        Intent intent = new Intent(context, CaseHomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_case_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initView() {
        initStatusBar(root);
        initToolBar();
        initBanner();
        initHotContent();
        initNewContent();
        initRefresh();
    }

    public void initRefresh() {
        srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                IS_REFRESH = true;
                getData();
            }
        });
        srlRefresh.setEnableLoadmore(false);
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
        API.getClassicCaseList(mCurrent_index, "searchtop", new CommonCallBack<List<ClassicCaseBean>>() {
            @Override
            public void onSuccess(List<ClassicCaseBean> classicCaseBeen) {
                if (IS_REFRESH) {
                    mBannerData.clear();
                    mBannerData.addAll(classicCaseBeen);
                    if (banner != null) {
                        banner.setImages(mBannerData);
                        banner.start();
                    }
                }
            }

            @Override
            public void onFailure() {

            }
        });

        API.getClassicCaseList(mCurrent_index, "searchnew", new CommonCallBack<List<ClassicCaseBean>>() {
            @Override
            public void onSuccess(List<ClassicCaseBean> classicCaseBeen) {
                if (IS_REFRESH) {
                    mNewData.clear();
                    mNewData.addAll(classicCaseBeen);
                } else {
                    mNewData.addAll(classicCaseBeen);
                }
                if (newAdapter != null) {
                    newAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure() {

            }
        });

        API.getClassicCaseList(mCurrent_index, "searchhot", new CommonCallBack<List<ClassicCaseBean>>() {
            @Override
            public void onSuccess(List<ClassicCaseBean> classicCaseBeen) {
                if (IS_REFRESH) {
                    mHotData.clear();
                    mHotData.addAll(classicCaseBeen);
                } else {
                    mHotData.addAll(classicCaseBeen);

                }
                if (hotAdapter != null) {
                    hotAdapter.notifyDataSetChanged();
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

    public void initNewContent() {
        newContent.setHasFixedSize(true);
        newContent.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        newContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        newAdapter = new CaseItemAdapter(this, mNewData, CaseItemAdapter.Type.CASE_HOME);
        newContent.setAdapter(newAdapter);
    }

    public void initHotContent() {
        hotContent.setHasFixedSize(true);
        hotContent.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        hotContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        hotAdapter = new CaseItemAdapter(this, mHotData, CaseItemAdapter.Type.CASE_HOME);
        hotContent.setAdapter(hotAdapter);
    }

    private void initBanner() {
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(mBannerData);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ClassicCaseDetailActivity.startClassicDetailActivity(mContext, mBannerData.get(position));
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void initToolBar() {
        tvTitle.setText(XFrame.getString(R.string.home_classiccase));
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    @OnClick({R.id.tv_new_more, R.id.tv_hot_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_new_more:
                CaseListActivity.open(this);
                break;
            case R.id.tv_hot_more:
                CaseListActivity.open(this);
                break;
        }
    }
}
