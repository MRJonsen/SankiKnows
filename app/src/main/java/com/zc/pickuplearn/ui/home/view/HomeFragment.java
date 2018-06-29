package com.zc.pickuplearn.ui.home.view;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.MultiItemTypeAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.classiccase.view.ClassicCaseDetailActivity;
import com.zc.pickuplearn.ui.classiccase.view.ClassicCaseListActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view.QuestionDetailActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.widget.QuestionDetailType;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view.WenDaFragment;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.ImageGridAdapter;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.DynamicMoreActivity;
import com.zc.pickuplearn.ui.home.presenter.HomePresenterImpl;
import com.zc.pickuplearn.ui.home.widget.ClassicCaseAdapter;
import com.zc.pickuplearn.ui.view.ClearEditText;
import com.zc.pickuplearn.ui.view.MixtureTextView;
import com.zc.pickuplearn.ui.view.NoScrollGridview;
import com.zc.pickuplearn.ui.view.RecyclerViewGridDivider;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;

import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;

/**
 * 主页模块
 * created by bin 2016/11/30 14:04
 */

public class HomeFragment extends BaseFragment implements OnRefreshListener, View.OnClickListener, IHomeView {
    @BindView(R.id.et_search)
    ClearEditText mSearchEditext;//搜索框
    @BindView(R.id.bt_search)
    Button mBtSearch;//搜索按钮
    @BindView(R.id.list)
    LRecyclerView mDynamicRecyclerView;//问答动态

    RecyclerView mClassCaseRecyclerView;//经典案列
    LinearLayout mLLSign;//签到布局
    TextView mtv_sign_number;//签到天数
    TextView mtv_go_sign;//去签到
    TextView mtv_help_number;//帮助人数

    private List<ClassicCaseBean> mCaseDatas;
    private List<QuestionBean> mDynamicDatas;
    private ClassicCaseAdapter mClassiccaseadapter;
    private LRecyclerViewAdapter mDynamicadapter;
    private HomePresenterImpl homePresenter;

    @Override
    public void initView() {
        homePresenter = new HomePresenterImpl(this);
        initDynamicView();
//        initClassicCaseView();
        mDynamicRecyclerView.setRefreshing(true);//初始化刷新
    }

    /**
     * 初始化搜索条儿
     */
    @OnTextChanged(value = R.id.et_search, callback = AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            if (!mBtSearch.isShown()) {
                mBtSearch.setVisibility(View.VISIBLE);
            }
        } else {
            mBtSearch.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化问答动态view
     */
    private void initDynamicView() {
        mDynamicRecyclerView.setHasFixedSize(true);
        mDynamicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mDynamicRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));//gridView效果
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//瀑布流
        mDynamicRecyclerView.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
//        mDynamicRecyclerView.addItemDecoration(new RecyclerViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, UIUtils.dip2px(1), R.color.divider_color));
        mDynamicDatas = new ArrayList<>();
        CommonAdapter lRecyclerViewAdapter = new CommonAdapter<QuestionBean>(getActivity(), R.layout.item_dynamic_new, mDynamicDatas) {

            @Override
            protected void convert(ViewHolder holder, final QuestionBean questionbean, final int position) {
                MixtureTextView tvquestion = holder.getView(R.id.tv_dynamic_question);
                tvquestion.setText(questionbean.getQUESTIONEXPLAIN());
                CircleImageView headimg = holder.getView(R.id.civ_user_head_img);
                if (!questionbean.getHEADIMAGE().isEmpty()) {
                    ImageLoaderUtil.displayCircleView(getContext(), headimg, questionbean.getHEADIMAGE(), false);//加载头像
                }
                holder.setText(R.id.tv_time, DateUtils.getCompareDate(questionbean.getSYSCREATEDATE()));
                holder.setText(R.id.tv_dynamic_answer_num, TextUtils.isEmpty(questionbean.getANSWERSUM()) ? "0" : questionbean.getANSWERSUM());
                holder.setText(R.id.tv_dynamic_team_name, questionbean.getQUESTIONTYPENAME());
                holder.setText(R.id.tv_dynamic_questioner, TextUtils.isEmpty(questionbean.getNICKNAME()) ? questionbean.getQUESTIONUSERNAME() : questionbean.getNICKNAME());
                holder.setOnClickListener(R.id.tv_dynamic_team_name, new View.OnClickListener() { //分类文字监听
                    @Override
                    public void onClick(View v) {
                        DynamicMoreActivity.startDynamicMoreActivity(getActivity(), "", questionbean,"");//根据分类去找找问题
                    }
                });
                NoScrollGridview view = holder.getView(R.id.nsg_dynamic_);
                view.setAdapter(new ImageGridAdapter(getmCtx(), questionbean.getFILEURL()));
                holder.setOnClickListener(R.id.tv_dynamic_question, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuestionDetailActivity.startQuestionDetailActivity(getContext(), questionbean, QuestionDetailType.Home);
                    }
                });
            }
        };
        mDynamicadapter = new LRecyclerViewAdapter(lRecyclerViewAdapter);//适配器转换
        mDynamicRecyclerView.setAdapter(mDynamicadapter);
        mDynamicRecyclerView.setOnRefreshListener(this);
        mDynamicRecyclerView.setLoadMoreEnabled(false);//去掉加载更多布局
    }

    /**
     * 初始化经典案咧view
     */
    private void initClassicCaseView() {
        View headView = UIUtils.inflate(R.layout.fragment_home_header);
        mDynamicadapter.addHeaderView(headView);
        mClassCaseRecyclerView = ButterKnife.findById(headView, R.id.rv_classic_case);
        mLLSign = ButterKnife.findById(headView, R.id.ll_sign);
        mtv_sign_number = ButterKnife.findById(headView, R.id.tv_sign_number);
        mtv_go_sign = ButterKnife.findById(headView, R.id.tv_go_sign);
        mtv_help_number = ButterKnife.findById(headView, R.id.tv_help_number);
        TextView mMoreClassic = ButterKnife.findById(headView, R.id.tv_home_more_classicCase);
        TextView mMoreQustion = ButterKnife.findById(headView, R.id.tv_home_more_question);
        mMoreClassic.setOnClickListener(this);
        mMoreQustion.setOnClickListener(this);//更多问答监听
        mtv_go_sign.setOnClickListener(this);
        mClassCaseRecyclerView.setHasFixedSize(true);
        mClassCaseRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));//gridView效果
        mClassCaseRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        mClassCaseRecyclerView.addItemDecoration(new RecyclerViewGridDivider(getContext()));
        mCaseDatas = new ArrayList<>();
        mClassiccaseadapter = new ClassicCaseAdapter(getActivity(), mCaseDatas);
        mClassCaseRecyclerView.setAdapter(mClassiccaseadapter);
        mClassiccaseadapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                homePresenter.enterClassicCaseDetail(mCaseDatas.get(position));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    public void disShowRefreshProgress() {
        mDynamicRecyclerView.refreshComplete();
    }

    @Override
    public void addClassCaseData(List<ClassicCaseBean> list) {
        if (list != null) {
            mCaseDatas.clear();
            mCaseDatas.addAll(list);
            mClassiccaseadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addDynamicData(List<QuestionBean> list) {
        LogUtils.e("数据添加" + list.size());
        if (list != null) {
            mDynamicDatas.clear();
            mDynamicDatas.addAll(list);
            mDynamicadapter.notifyDataSetChanged();
        }
    }

    @Override
    public String getSearchText() {
        return mSearchEditext.getText().toString();
    }

    @Override
    public void setSignNum(String num) {
        if (mtv_sign_number != null && !TextUtils.isEmpty(num)) {
            mtv_sign_number.setText(num);
        }
    }

    @Override
    public void setHelpNum(String num) {
        if (mtv_help_number != null && !TextUtils.isEmpty(num)) {
            mtv_help_number.setText(num);
        }
    }

    @Override
    public void closeSignLayout() {
        if (mLLSign != null && mLLSign.isShown()) {
            mLLSign.setVisibility(View.GONE);
        }
    }

    @Override
    public void enterClassicCaseListView() {
        getContext().startActivity(new Intent(getContext(), ClassicCaseListActivity.class));
    }

    @Override
    public void enterClassicCaseDetailView(ClassicCaseBean classicCaseBean) {
        ClassicCaseDetailActivity.startClassicDetailActivity(getContext(), classicCaseBean);
    }

    @Override
    public void clearSearchString() {
        mSearchEditext.setText("");
    }


    @OnClick({R.id.bt_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search:
                KeyBoardUtils.closeKeybord(mBtSearch, getContext());
                LogUtils.e("点击了");
                homePresenter.loadDynamicDatas();
                break;
            case R.id.tv_home_more_classicCase:
                homePresenter.enterClassicCaseList();
                break;
            case R.id.tv_home_more_question:
                DynamicMoreActivity.startDynamicMoreActivity(getActivity(), WenDaFragment.THE_RECOMMENT, null,"");
                break;
            case R.id.tv_go_sign:
                ToastUtils.showToast(getContext(), "签到");
                break;
        }
    }


    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
//        homePresenter.loadClassicCaseDatas();
        homePresenter.loadDynamicDatas();
    }
}

