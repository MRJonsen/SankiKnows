package com.zc.pickuplearn.ui.mine.mine.widget.myanswer.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.ui.answerdetail.ZhuiwenZhuidaActivity;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view.AnswerActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view.QuestionDetailActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.widget.QuestionDetailType;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.model.ImplWenDaModel;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view.IWenDaView;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.ImageGridAdapter;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.TipGridAdapter;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.DynamicMoreActivity;
import com.zc.pickuplearn.ui.mine.mine.widget.myanswer.presenter.ImplMyAnserPresenter;
import com.zc.pickuplearn.ui.view.ClearEditText;
import com.zc.pickuplearn.ui.view.NoScrollGridview;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;

import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;

/**
 * 我的回答
 * created by bin on 2016.9.27
 */
public class MyAnswerFragment extends BaseFragment implements IWenDaView, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.et_search)
    ClearEditText mSearchEditext;//搜索框
    @BindView(R.id.bt_search)
    Button mBtSearch;//搜索按钮
    @BindView(R.id.list)
    LRecyclerView mDynamicRecyclerView;//问答动态
    @BindView(R.id.tv_wenda_more)
    TextView mtvMore;//更多按钮
    @BindView(R.id.rl_more)
    RelativeLayout rlmore;
    @BindView(R.id.tv_warning)
    TextView tvWaring;
    @BindView(R.id.x_loading)
    XLoadingView xLoadingView;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;
    private static final String TAG = "MyAnswerFragment";
    public static final String MY_ANSWER = "我的回答";
    public static final String MY_QUESTION = "我的提问";
    public static final String NEED_MORE = "needrefreshandloadmore";//是否需要刷新好加载
    public static final String HAS_BEAN = "questionbean";
    private int index = 0;
    private boolean IS_FIRST_IN = true;
    private List<QuestionBean> mDynamicDatas;
    private LRecyclerViewAdapter mDynamicadapter;
    private String comefrom;
    private ImplMyAnserPresenter presenter;
    private UserBean userInfo;
    private boolean is_refresh = true;

    /**
     * @param tag
     * @return
     */
    public static MyAnswerFragment newInstance(String tag) {
        Bundle args = new Bundle();
        MyAnswerFragment fragment = new MyAnswerFragment();
        args.putString(TAG, tag);//请求页的标志位
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_question;
    }

    @Override
    public void initView() {
        xLoadingView.showContent();
        xLoadingView.setEmptyViewWarning("好心塞，居然是空的");
        presenter = new ImplMyAnserPresenter(this);
        getParams();
        initDynamicView();
        mDynamicRecyclerView.setRefreshing(true);//初始化刷新
        initFreshView();
    }

    private void initFreshView() {
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new com.scwang.smartrefresh.layout.listener.OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                MyAnswerFragment.this.onRefresh();
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                onLoadMore();
            }
        });
    }

    /**
     * 获取初始化参数
     */
    private void getParams() {
        Bundle arguments = getArguments();
        comefrom = arguments.getString(TAG);
        userInfo = DataUtils.getUserInfo();
    }

    /**
     * 初始化问答动态view
     */
    private void initDynamicView() {
        mDynamicRecyclerView.setHasFixedSize(true);
        mDynamicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDynamicRecyclerView.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        mDynamicDatas = new ArrayList<>();

        CommonAdapter lRecyclerViewAdapter = new CommonAdapter<QuestionBean>(getmCtx(), R.layout.item_dynamic_new, mDynamicDatas) {

            @Override
            protected void convert(final ViewHolder holder, final QuestionBean questionbean, final int position) {
                ImageView solve_iv = holder.getView(R.id.iv_solve_tag);
                if (solve_iv != null) { //设置问题解决标志
                    solve_iv.setVisibility("1".equals(questionbean.getISSOLVE()) ? View.VISIBLE : View.GONE);
                }
                TextView msg = holder.getView(R.id.tv_question_msg);
                if (msg != null) {//设置消息提示数量
                    if (!TextUtils.isEmpty(questionbean.getINFOCOUNT()) && Integer.valueOf(questionbean.getINFOCOUNT()) > 0) {
                        String infocount = questionbean.getINFOCOUNT() + "";
                        msg.setVisibility(View.VISIBLE);
                        msg.setText(infocount);
                    } else {
                        msg.setVisibility(View.GONE);
                    }
                }
                String bonuspoints = questionbean.getBONUSPOINTS();
                TextView tvExplain = holder.getView(R.id.tv_explain);
                if (!TextUtils.isEmpty(bonuspoints)) {
                    tvExplain.setText(fromatTextToHtmlText(bonuspoints));
                    tvExplain.append(questionbean.getQUESTIONEXPLAIN());
                } else {
                    tvExplain.setText(questionbean.getQUESTIONEXPLAIN());
                }
                CircleImageView headimg = holder.getView(R.id.civ_user_head_img);
                if (!questionbean.getHEADIMAGE().isEmpty()) {
                    ImageLoaderUtil.displayCircleView(getContext(), headimg, questionbean.getHEADIMAGE(), false);//加载头像
                } else {
                    headimg.setImageResource(R.mipmap.default_user_circle_icon);
                }
                holder.setText(R.id.tv_time, DateUtils.getCompareDate(questionbean.getSYSCREATEDATE()));
                holder.setText(R.id.tv_dynamic_answer_num, TextUtils.isEmpty(questionbean.getANSWERSUM()) ? "0" : questionbean.getANSWERSUM());
                holder.setText(R.id.tv_dynamic_questioner, TextUtils.isEmpty(questionbean.getNICKNAME()) ? questionbean.getQUESTIONUSERNAME() : questionbean.getNICKNAME());
                if ("1".equals(questionbean.getISANONYMITY())) {
                    holder.setText(R.id.tv_dynamic_questioner, "匿名用户");
                    Glide.with(getmCtx()).load(R.mipmap.default_user_circle_icon).into(headimg);
                }
                NoScrollGridview TipView = holder.getView(R.id.nsg_biaoqian_);
                TipView.setAdapter(new TipGridAdapter(getmCtx(), questionbean.getTip(), questionbean));
                NoScrollGridview view = holder.getView(R.id.nsg_dynamic_);
                view.setOnTouchBlankPositionListener(new NoScrollGridview.OnTouchBlankPositionListener() {
                    @Override
                    public void onTouchBlank(MotionEvent event) {
                        //点击图片空白部分
                        enterQustionDetail(questionbean);
                    }
                });
                view.setAdapter(new ImageGridAdapter(getmCtx(), questionbean.getFILEURL()));
                View item = holder.getView(R.id.card_view);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enterQustionDetail(questionbean);
                    }
                });
                holder.setOnClickListener(R.id.tv_explain, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enterQustionDetail(questionbean);
                    }
                });
                Button bt_answer = holder.getView(R.id.bt_answer_question);
                bt_answer.setVisibility(View.GONE);//隐藏回答按钮
//                holder.setVisible(R.id.bt_answer_question,false);//隐藏回答按钮
                if (MY_QUESTION.equals(comefrom)) {
                    holder.setOnLongClickListener(R.id.ll_item, new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            new AlertDialog(getmCtx()).builder()
                                    .setTitle("提示").setMsg("确定要删除问题？")
                                    .setPositiveButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            API.setQuestionLose(questionbean, new CommonCallBack<String>() {
                                                @Override
                                                public void onSuccess(String s) {
                                                    ToastUtils.showToast(getmCtx(), "删除成功！");
                                                    mDynamicDatas.remove(questionbean);
                                                    notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onFailure() {

                                                }
                                            });
                                        }
                                    }).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                            return false;
                        }
                    });
                }
                holder.setOnClickListener(R.id.bt_answer_question, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View view1 = holder.getView(R.id.bt_answer_question);
                        view1.setClickable(false);
                        if (userInfo.getUSERCODE().equals(questionbean.getQUESTIONUSERCODE())) {
                            ToastUtils.showToastCenter(getmCtx(), "你不能回答自己的问题！");
                            view1.setClickable(true);
                        } else {
                            //1.是回答者直接进入追问追答界面 否者进入回答界面
                            if ("1".equals(questionbean.getISANSWER())) {
                                new ImplWenDaModel().getAnsewData(questionbean, new ImplWenDaModel.GetAnswerDatasCallBack() {
                                    @Override
                                    public void onSuccess(List<AnswerBean> dynamic_data) {
                                        view1.setClickable(true);
                                        ZhuiwenZhuidaActivity.startZhuiwenZhuidaActivity(getmCtx(), questionbean, dynamic_data.get(0));
                                    }

                                    @Override
                                    public void onFailure() {
                                        view1.setClickable(true);
                                        ToastUtils.showToastCenter(getmCtx(), "服务器正忙！请稍后再试");
                                    }
                                });
                            } else {
                                AnswerActivity.startAnswerActivity(getmCtx(), questionbean);//进入回答页面
                                view1.setClickable(true);
                            }

                        }
                    }
                });
            }

        };
        mDynamicadapter = new LRecyclerViewAdapter(lRecyclerViewAdapter);//适配器转换

        mDynamicRecyclerView.setAdapter(mDynamicadapter);
//        mDynamicRecyclerView.setOnRefreshListener(this);
        mDynamicRecyclerView.setLoadMoreEnabled(false);
//        mDynamicRecyclerView.setOnLoadMoreListener(this);
        rlmore.setVisibility(View.GONE);//隐藏更多布局
    }

    private void enterQustionDetail(QuestionBean questionbean) {
        QuestionDetailActivity.startQuestionDetailActivity(getContext(), questionbean, QuestionDetailType.MyQuestion);//进入问题详情
    }

    /**
     * 处理悬赏头部布局与text能混排
     *
     * @param score
     * @return
     */
    public CharSequence fromatTextToHtmlText(String score) {
        String html = "<img src='" + R.mipmap.high_score_icon + "'/>";
        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int id = Integer.parseInt(source);
                Drawable d = getResources().getDrawable(id);
                d.setBounds(0, -10, d.getIntrinsicWidth() + 10, d.getIntrinsicHeight());
                return d;
            }
        };
        CharSequence charSequence = Html.fromHtml(html + "<font color =#F0B964>" + score + "</font>", imgGetter, null);
        return charSequence;
    }

    @OnClick({R.id.bt_search, R.id.tv_wenda_more})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search:
                index = 0;
                KeyBoardUtils.closeKeybord(mBtSearch, getContext());
                if (MY_ANSWER.equals(comefrom)) {
                    presenter.loadMyAnswerData();
                } else {
                    presenter.loadDynamicDatas(null);
                }
                break;
            case R.id.tv_wenda_more:
                DynamicMoreActivity.startDynamicMoreActivity(getmCtx(), comefrom, null, "");
                break;
        }
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

    @Override
    public void onLoadMore() {
        is_refresh = false;
        if (MY_ANSWER.equals(comefrom)) {
            presenter.loadMyAnswerData();
        } else {
            presenter.loadDynamicDatas(null);
        }
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        index = 0;
        is_refresh = true;
        if (refreshLayout.isLoadmoreFinished())
            refreshLayout.setLoadmoreFinished(false);
        if (MY_ANSWER.equals(comefrom)) {
            presenter.loadMyAnswerData();
        } else {
            presenter.loadDynamicDatas(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (IS_FIRST_IN) {
            IS_FIRST_IN = false;
        } else {
            LogUtils.e("刷新我的提问数据");
            onRefresh();
        }
    }

    @Override
    public String getSearchString() {
        return mSearchEditext.getText().toString().trim();
    }

    @Override
    public void clearSearchString() {
        mSearchEditext.setText("");
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void addData(List<QuestionBean> list) {
        if (list == null) {//搜索提示
            xLoadingView.showEmpty();
            if (is_refresh) {
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadmore();
            }
        } else {
            xLoadingView.showContent();
            if (is_refresh) {
                refreshLayout.finishRefresh();
                mDynamicDatas.clear();
                mDynamicDatas.addAll(list);
            } else {
                if (list.size() == 0) {
                    refreshLayout.setLoadmoreFinished(true);
                } else {
                    mDynamicDatas.addAll(list);
                }
                refreshLayout.finishLoadmore();
            }
            mDynamicadapter.notifyDataSetChanged();
        }
        index = mDynamicDatas.size();
        if (mDynamicDatas.isEmpty()) {
            showEmptyView(true);
        }

    }

    @Override
    public void disShowRefreshView() {
        if (null!=refreshLayout){
            if (is_refresh) {
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadmore();
            }
        }

    }

    @Override
    public void loadMoreStatus(LoadingFooter.State state) {
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mDynamicRecyclerView, UrlMethod.PAZE_SIZE, state, null);
    }

    @Override
    public void showEmptyView(boolean isshow) {
        if (isshow) {
            if (xLoadingView!=null)
            xLoadingView.showEmpty();
//            ToastUtils.showToastCenter(getmCtx(), "您还没有提过任何问题，去提一个吧~");
        }
//        tvWaring.setVisibility(isshow ? View.VISIBLE : View.GONE);
    }
}
