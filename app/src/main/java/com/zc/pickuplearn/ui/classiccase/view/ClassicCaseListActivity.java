package com.zc.pickuplearn.ui.classiccase.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;
import com.zc.pickuplearn.ui.classiccase.model.ClassCaseTypeEvent;
import com.zc.pickuplearn.ui.classiccase.presenter.ClassicCaseListPresenterImpl;
import com.zc.pickuplearn.ui.classiccase.widget.ClassicCaseListAdapter;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.UIUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ClassicCaseListActivity extends EventBusActivity implements IClassicCaseListView, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.hv_headbar)
    HeadView headView;
    @BindView(R.id.list)
    LRecyclerView mContent;//内容区域
    @BindView(R.id.rl_type)
    RelativeLayout rl_type;// 类型选择布局
    @BindView(R.id.tv_type)
    TextView bt_type;
    QusetionTypeBean mType;
    private List<ClassicCaseBean> mListData = new ArrayList<>();
    private int index = 0;//索引
    private boolean IS_REFRESH = true;//刷新标志
    private String TYPE_NOW = UrlMethod.TYPE_ALL;// 初始化筛选类型
    private ClassicCaseListPresenterImpl mPresenter;
    private LRecyclerViewAdapter mClassicCaseListAdapter;
    private static final String TAG = "ClassicCaseListActivity";
    private ProfessionalCircleBean circleBean;

    public static void startClassicCaseListActivity(Context context, ProfessionalCircleBean bean) {
        Intent intent = new Intent(context, ClassicCaseListActivity.class);
        intent.putExtra(TAG, bean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_classic_case_list;
    }

    @Override
    public void initView() {
        headView.setTitle(UIUtils.getString(R.string.home_classiccase));
        circleBean = (ProfessionalCircleBean) getIntent().getSerializableExtra(TAG);
        setTypeName(circleBean.getPROCIRCLENAME());
        initDynamicView();
        mPresenter = new ClassicCaseListPresenterImpl(this);
    }

    @Override
    protected void initData() {
        mContent.setRefreshing(true);//初始化刷新
    }


    /**
     * 初始化经典案列view
     */
    private void initDynamicView() {
        mContent.setHasFixedSize(true);
        mContent.setLayoutManager(new LinearLayoutManager(this));
        //设置Item增加、移除动画
        mContent.setItemAnimator(new DefaultItemAnimator());
        mContent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        ClassicCaseListAdapter classicCaseListAdapter = new ClassicCaseListAdapter(this, R.layout.item_classic_case_list, mListData);
        //适配器转换
        mClassicCaseListAdapter = new LRecyclerViewAdapter(classicCaseListAdapter);
        mContent.setOnLoadMoreListener(this);
        mContent.setOnRefreshListener(this);
        mContent.setAdapter(mClassicCaseListAdapter);
    }


    @Override
    public void setRefreshProgressStatus() {
        mContent.refreshComplete();
    }

    @Override
    public void setLoadMoreProgressStatus(LoadingFooter.State state) {
        RecyclerViewStateUtils.setFooterViewState(this, mContent, UrlMethod.PAZE_SIZE, state, null);
    }

    /**
     * 加载更多状态
     *
     * @param count
     */
    public void setLoadMoreStats(int count) {
        if (count % UrlMethod.PAZE_SIZE != 0) {
            RecyclerViewStateUtils.setFooterViewState(this, mContent, UrlMethod.PAZE_SIZE, LoadingFooter.State.TheEnd, null);
            return;
        }
        if (count % UrlMethod.PAZE_SIZE == 0) {
            RecyclerViewStateUtils.setFooterViewState(this, mContent, UrlMethod.PAZE_SIZE, LoadingFooter.State.Normal, null);
        }
    }

    @Override
    public void addCaseListData(List<ClassicCaseBean> list) {
//        if (list != null) {
//            mListData.addAll(list);
//            index = index + list.size();
//        }
//        if (getRefreshStatuts()) {
//            mContent.refreshComplete();
//        }
//        if (mListData.size() % UrlMethod.PAZE_SIZE != 0) {
//            setLoadMoreProgressStatus(LoadingFooter.State.TheEnd);
//        } else {
//            setLoadMoreProgressStatus(LoadingFooter.State.Normal);
//        }
//        if (mListData.size() == 0) {
//            setLoadMoreProgressStatus(LoadingFooter.State.NetWorkError);
//        }
//        notifyDataChange();
        if (list != null) {
            if (IS_REFRESH) {
                setRefreshProgressStatus();
                mListData.addAll(list);
                index = mListData.size();
                if (mListData.isEmpty()) {
                    RecyclerViewStateUtils.setFooterViewState(mContent, LoadingFooter.State.TheEnd);
                }
            } else {
                mListData.addAll(list);
                index = mListData.size();
                setLoadMoreStats(index);
            }
            mClassicCaseListAdapter.notifyDataSetChanged();
        } else {
            setRefreshProgressStatus();
            RecyclerViewStateUtils.setFooterViewState(mContent, LoadingFooter.State.TheEnd);
        }
    }

    @Override
    public void notifyDataChange() {
        if (mClassicCaseListAdapter != null) {
            mClassicCaseListAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 清除数据
     */
    public void clearData() {
        if (mListData != null) {
//            setIndex(0);
//            mListData.clear();
//            notifyDataChange();
            index = 0;
            mListData.clear();
            RecyclerViewStateUtils.setFooterViewState(mContent, LoadingFooter.State.Normal);
            mClassicCaseListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showTypeView(boolean b) {
        rl_type.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public String getTypeNow() {
        return TYPE_NOW;
    }

    @Override
    public int getIndex() {
        return index;
    }


    public void setIndex(int i) {
        index = i;
    }


    public boolean getRefreshStatuts() {
        return IS_REFRESH;
    }


    public void setRefreshStatus(boolean isRefresh) {
        IS_REFRESH = isRefresh;
    }


    @OnClick({R.id.tv_type_choose, R.id.tv_type_all, R.id.tv_type_video,
            R.id.tv_type_picture, R.id.tv_type_txt, R.id.tv_type_other,
            R.id.tv_hot, R.id.tv_new, R.id.rl_type})
    public void onClick(View v) {
        String typeNow = getTypeNow();
        IS_REFRESH = true;
        showTypeView(false);
        switch (v.getId()) {
            case R.id.tv_type_choose://类型布局
                ScrollGridActivity.startScrollGridActivity(this, TypeGreenDaoEvent.ClassicCase);
//                showTypeView(true);
                break;
            case R.id.tv_type_all://全部
                TYPE_NOW = UrlMethod.TYPE_ALL;
                break;
            case R.id.tv_hot: //最热
                TYPE_NOW = UrlMethod.TYPE_HOT;
                break;
            case R.id.tv_new://最新
                TYPE_NOW = UrlMethod.TYPE_NEW;
                break;
            case R.id.tv_type_picture://图片
                break;
            case R.id.tv_type_video://视频
                break;
            case R.id.tv_type_txt://文本
                break;
            case R.id.tv_type_other://其他
                break;
            default:
                break;
        }
        if (!typeNow.equals(getTypeNow())) {
            mContent.setRefreshing(true);
        }
    }

    @Override
    public void onLoadMore() {
        LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mContent);
        if (state == LoadingFooter.State.Loading) {
            LogUtils.e("加载中");//防止重复
            return;
        }

        if (index % UrlMethod.PAZE_SIZE == 0) {
            setRefreshStatus(false);
            mPresenter.loadClassicCaseListData(mType, circleBean);
            setLoadMoreProgressStatus(LoadingFooter.State.Loading);
        } else {
            //结束
            setLoadMoreProgressStatus(LoadingFooter.State.TheEnd);
        }
    }

    @Override
    public void onRefresh() {
        LogUtils.e("刷新请求");
        IS_REFRESH = true;
        clearData();
        mPresenter.loadClassicCaseListData(mType, circleBean);
//        clearData();
//        setRefreshStatus(true);
//        mPresenter.loadClassicCaseListData();
    }

    /**
     * 接收分类树页面选择结果
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(ClassCaseTypeEvent event) {
        if (event != null) {
            mType = event.getQuestionBean();
            if (mType != null) {
                setTypeName(mType.getNAME());
                onRefresh();
            }
        }
    }

    private void setTypeName(String name) {
        bt_type.setText(name);
    }
}
