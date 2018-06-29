package com.zc.pickuplearn.ui.group.widget.professional.view;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.ui.base.EventBusBaseFragment;
import com.zc.pickuplearn.ui.group.widget.professional.presenter.ProfessionPresenterImpl;
import com.zc.pickuplearn.ui.group.widget.professional.widget.ProfessionAdapter;
import com.zc.pickuplearn.ui.mine.mine.widget.MyCircleActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 专业圈
 * created by bin 2016/12/13 10:38
 */

public class ProfessionFragment extends EventBusBaseFragment implements IProfessionView {
    @BindView(R.id.rv_profession_content)
    LRecyclerView mContent;

    private List<ProfessionalCircleBean> mData = new ArrayList<>();
    private ProfessionPresenterImpl presenter;
    public final static String mTAG = "ProfessionFragment";
    private String from;
    private LRecyclerViewAdapter lRecyclerViewAdapter;

    public static ProfessionFragment newInstance(String TAG) {
        Bundle args = new Bundle();
        ProfessionFragment fragment = new ProfessionFragment();
        args.putString(mTAG, TAG);//判断启动来源 （我的，还是主页）
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profession;
    }

    /**
     *
     */
    @Override
    public void initView() {
        Bundle arguments = getArguments();
        from = arguments.getString(mTAG);
        initContentView();
        presenter = new ProfessionPresenterImpl(this);
        presenter.getAllProfessionalData();//初始化获取数据
    }

    /**
     * 初始化控件
     */
    private void initContentView() {
        mContent.setHasFixedSize(true);
        mContent.setLayoutManager(new LinearLayoutManager(getContext()));
//        mContent.setLayoutManager(new GridLayoutManager(getContext(), 3));//gridView效果
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//瀑布流
        //设置Item增加、移除动画
        mContent.setItemAnimator(new DefaultItemAnimator());
//        mContent.addItemDecoration(new RecyclerViewDivider(getContext(), GridLayoutManager.VERTICAL, UIUtils.dip2px(1), R.color.divider_color));
        ProfessionAdapter mContentAdapter;
        mContentAdapter = new ProfessionAdapter(getContext(), mData,from);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(mContentAdapter);
        mContent.setAdapter(lRecyclerViewAdapter);
        mContent.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getAllProfessionalData();
            }
        });
        mContent.setLoadMoreEnabled(false);//去掉加载更多布局
    }

    @Override
    public void addData(List<ProfessionalCircleBean> data) {
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            doData(null);
            lRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void disShowRefresh() {
        if (mContent != null) {
            mContent.refreshComplete();
        }
    }


    /**
     * 过滤 不是我加入的圈子的数据
     *
     * @param msg
     */
    private void doData(ProfessionalCircleBean msg) {
        if (from != null && from.equals(MyCircleActivity.TAG) && mData.size() > 0) {
            //来自我的圈子请求
            ArrayList<ProfessionalCircleBean> professionalCircleBeen = new ArrayList<>();
            for (ProfessionalCircleBean bean : mData) {
                if (!bean.getISJOIN()) {
                    professionalCircleBeen.add(bean);
                }
            }
            mData.removeAll(professionalCircleBeen);
            if (msg != null && msg.getISJOIN()) {
                mData.add(msg);
            }
//            if (mData.size() == 0) {
//                ToastUtils.showToastCenter(getmCtx(), "您还没有关注的圈子!");
//            }
        }
    }

    @Subscribe
    public void onEventMainThread(ProfessionalCircleBean msg) {
        if (lRecyclerViewAdapter != null) {
            for (ProfessionalCircleBean bean :
                    mData) {
                if (bean.getPROCIRCLECODE().equals(msg.getPROCIRCLECODE())) {
                    bean.setISJOIN(msg.getISJOIN());
                }
            }
            doData(msg);
            lRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

}
