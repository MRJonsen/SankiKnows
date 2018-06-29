package com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.answerdetail.ZhuiwenZhuidaActivity;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view.AnswerActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view.QuestionDetailActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.widget.QuestionDetailType;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.model.ImplWenDaModel;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.presenter.ImplWenDaPresenter;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.ImageGridAdapter;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.TipGridAdapter;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.DynamicMoreActivity;
import com.zc.pickuplearn.ui.view.ClearEditText;
import com.zc.pickuplearn.ui.view.NoScrollGridview;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;

import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;

public class WenDaFragment extends BaseFragment implements IWenDaView, OnRefreshListener, OnLoadMoreListener {

    private boolean needmore;
    private String comefrom;
    private ImplWenDaPresenter presenter;
    private QuestionBean mQuestionBean;
    private String searchString;
    private UserBean userInfo;
    /**
     * @param tag
     * @param loadmoreenadble 是否需要加载更多
     * @param searchtext      搜索的文本
     * @return
     */
    public static WenDaFragment newInstance(String tag, boolean loadmoreenadble, QuestionBean bean, String searchtext) {
        Bundle args = new Bundle();
        WenDaFragment fragment = new WenDaFragment();
        args.putString(TAG, tag);//请求页的标志位
        args.putBoolean(NEED_MORE, loadmoreenadble);
        args.putString(SEARCH_TEXT, searchtext);
        args.putSerializable(HAS_BEAN, bean);
        fragment.setArguments(args);
        return fragment;
    }


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
    @BindView(R.id.ll_search)
    LinearLayout llsearch;//搜索更多布局
    @BindView(R.id.tv_warning)
    TextView tvWaring;
    private static final String TAG = "WenDaFragment";
    public static final String THE_RECOMMENT = "推荐";
    public static final String THE_NEW = "最新";
    public static final String THE_SCORE = "悬赏";
    public static final String THE_SEARCH = "搜索";
    public static final String NEED_MORE = "needrefreshandloadmore";//是否需要刷新好加载
    public static final String HAS_BEAN = "questionbean";
    public static final String SEARCH_TEXT = "SEARCH_TEXT";
    public static final String TAG_REFRESH = "WenDaFragmentRrefresh";
    private int index = 0;
    private int FIRST_IN = 0;
    private List<QuestionBean> mDynamicDatas;
    private LRecyclerViewAdapter mDynamicadapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wen_da;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        presenter = new ImplWenDaPresenter(this);
        getParams();
        initDynamicView();
        mDynamicRecyclerView.setRefreshing(true);//初始化刷新
    }

    /**
     * 获取初始化参数
     */
    private void getParams() {
        Bundle arguments = getArguments();
        needmore = arguments.getBoolean(NEED_MORE);
        comefrom = arguments.getString(TAG);
        searchString = arguments.getString(SEARCH_TEXT);
        mQuestionBean = (QuestionBean) arguments.getSerializable(HAS_BEAN);
        userInfo = DataUtils.getUserInfo();
    }

    /**
     * 初始化问答动态view
     */
    private void initDynamicView() {
        mDynamicRecyclerView.setHasFixedSize(true);
        mDynamicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        mDynamicRecyclerView.setLayoutManager(new MyLinearLayoutManager(getmCtx(), LinearLayoutManager.VERTICAL,true));
//        mDynamicRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));//gridView效果
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));//瀑布流
        mDynamicRecyclerView.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
//        mDynamicRecyclerView.addItemDecoration(new RecyclerViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, UIUtils.dip2px(1), R.color.divider_color));
        mDynamicDatas = new ArrayList<>();

        CommonAdapter lRecyclerViewAdapter = new CommonAdapter<QuestionBean>(getActivity(), R.layout.item_dynamic_new, mDynamicDatas) {

            @Override
            protected void convert(final ViewHolder holder, final QuestionBean questionbean, final int position) {
                String bonuspoints = questionbean.getBONUSPOINTS();
                TextView tvExplain = holder.getView(R.id.tv_explain);
                if (!TextUtils.isEmpty(bonuspoints)) {
                    tvExplain.setText(fromatTextToHtmlText(bonuspoints));
                    tvExplain.append(questionbean.getQUESTIONEXPLAIN());
                    tvExplain.setGravity(Gravity.CENTER_VERTICAL);
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
                //标签
//                holder.setText(R.id.tv_dynamic_team_name, questionbean.getQUESTIONTYPENAME());
                holder.setText(R.id.tv_dynamic_questioner, TextUtils.isEmpty(questionbean.getNICKNAME()) ? questionbean.getQUESTIONUSERNAME() : questionbean.getNICKNAME());
                if (questionbean.getISANONYMITY().equals("1")) {
                    holder.setText(R.id.tv_dynamic_questioner, "匿名用户");
                    Glide.with(getmCtx()).load(R.mipmap.default_user_circle_icon).into(headimg);
                }
                NoScrollGridview TipView = holder.getView(R.id.nsg_biaoqian_);
                if (questionbean.getTip() != null) {
                    TipView.setAdapter(new TipGridAdapter(getmCtx(), questionbean.getTip(), questionbean));
                }
//                if (mQuestionBean == null) {
//                    holder.setOnClickListener(R.id.tv_dynamic_team_name, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            DynamicMoreActivity.startDynamicMoreActivity(getActivity(), "", questionbean, "");//根据标签分类去找找问题
//                        }
//                    });
//                }
                NoScrollGridview view = holder.getView(R.id.nsg_dynamic_);
                view.setOnTouchBlankPositionListener(new NoScrollGridview.OnTouchBlankPositionListener() {
                    @Override
                    public void onTouchBlank(MotionEvent event) {
                        QuestionDetailActivity.startQuestionDetailActivity(getContext(), questionbean,QuestionDetailType.Home);//进入问题详情
                    }
                });
                if (!TextUtils.isEmpty(questionbean.getFILEURL())) {
                    view.setVisibility(View.VISIBLE);
                    view.setAdapter(new ImageGridAdapter(getmCtx(), questionbean.getFILEURL()));
                } else {
                    view.setVisibility(View.GONE);
                }
                View item = holder.getView(R.id.card_view);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuestionDetailActivity.startQuestionDetailActivity(getContext(), questionbean,QuestionDetailType.Home);//进入问题详情
                    }
                });
                holder.setOnClickListener(R.id.tv_explain, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuestionDetailActivity.startQuestionDetailActivity(getContext(), questionbean,QuestionDetailType.Home);//进入问题详情
                    }
                });

                holder.setOnClickListener(R.id.bt_answer_question, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View view1 = holder.getView(R.id.bt_answer_question);
                        view1.setClickable(false);
                        if (userInfo.getUSERCODE().equals(questionbean.getQUESTIONUSERCODE())) {
                            ToastUtils.showToastCenter(getmCtx(), XFrame.getString(R.string.warning_no_answer_self_question));
                            view1.setClickable(true);
                        } else {
                            //1.是回答者直接进入追问追答界面 否者进入回答界面
                            if (questionbean.getISANSWER().equals("1")) {
                                new ImplWenDaModel().getAnsewData(questionbean, new ImplWenDaModel.GetAnswerDatasCallBack() {
                                    @Override
                                    public void onSuccess(List<AnswerBean> dynamic_data) {
                                        ZhuiwenZhuidaActivity.startZhuiwenZhuidaActivity(getmCtx(), questionbean, dynamic_data.get(0));
                                        view1.setClickable(true);
                                    }

                                    @Override
                                    public void onFailure() {
                                        ToastUtils.showToastCenter(getmCtx(), "服务器正忙！请稍后再试");
                                        view1.setClickable(true);
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
        mDynamicRecyclerView.setOnRefreshListener(this);
//        if (mQuestionBean != null) {
//            llsearch.setVisibility(View.GONE);//如果是分类过来的 隐藏搜索按钮
//        }
//        if (needmore) {//根据需要是否开启加载更多功能
//            mDynamicRecyclerView.setLoadMoreEnabled(true);
//            mDynamicRecyclerView.setOnLoadMoreListener(this);
//            rlmore.setVisibility(View.GONE);
//        } else {
//            mDynamicRecyclerView.setLoadMoreEnabled(false);//去掉加载更多布局
//        }
        // TODO: 2017/2/9   该布局去掉搜索框 去掉更多条目
        llsearch.setVisibility(View.GONE);
        rlmore.setVisibility(View.GONE);
        mDynamicRecyclerView.setLoadMoreEnabled(true);
        mDynamicRecyclerView.setOnLoadMoreListener(this);
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
                // TODO Auto-generated method stub
                int id = Integer.parseInt(source);
                Drawable d = getResources().getDrawable(id);
                d.setBounds(0, -10, d.getIntrinsicWidth()+10, d.getIntrinsicHeight());
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
                presenter.loadDynamicDatas(comefrom, needmore, mQuestionBean);
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
        LogUtils.e("加载更多");
        LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mDynamicRecyclerView);
        if (state == LoadingFooter.State.Loading) {
            LogUtils.e("加载中");//防止重复
            return;
        }
        if (index % UrlMethod.PAZE_SIZE == 0) {
            presenter.loadDynamicDatas(comefrom, needmore, mQuestionBean);
            loadMoreStatus(LoadingFooter.State.Loading);
        } else {
            //结束
            loadMoreStatus(LoadingFooter.State.TheEnd);
        }
    }
    @Override
    public void addData(List<QuestionBean> list) {
        if (list == null) {//搜索提示
            LogUtils.e("搜错");
            tvWaring.setVisibility(View.VISIBLE);
        } else {
            tvWaring.setVisibility(View.GONE);
        }
        if (list != null) {
            if (index == 0) {
                mDynamicDatas.clear();
            }
            mDynamicDatas.addAll(list);
            LogUtils.e("needmore:" + needmore);
            if (needmore && index != 0) {
                LogUtils.e("重置状态了");
                if (mDynamicDatas.size() % UrlMethod.PAZE_SIZE != 0) {
                    LogUtils.e("重置状态了TheEnd");
                    loadMoreStatus(LoadingFooter.State.TheEnd);
                } else {
                    LogUtils.e("重置状态了Normal");
                    loadMoreStatus(LoadingFooter.State.Normal);
                }
                if (list.size()==0){
                    loadMoreStatus(LoadingFooter.State.TheEnd);
                }
                if (mDynamicDatas.size() == 0) {
                    LogUtils.e("重置状态了NetWorkError");
                    loadMoreStatus(LoadingFooter.State.NetWorkError);
                }
            }
            index = index + list.size();
            LogUtils.e(index + "数据长度" + comefrom + list.size());
            if (index==0){
                LogUtils.e("数据长度true");
                showEmptyView(true);
            }else {
                LogUtils.e("数据长度false");
                showEmptyView(false);
            }
            mDynamicadapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        index = 0;
        presenter.loadDynamicDatas(comefrom, needmore, mQuestionBean);
    }

    @Override
    public String getSearchString() {
//        return mSearchEditext.getText().toString().trim();
        return searchString;
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
    public void disShowRefreshView() {
        if (mDynamicRecyclerView != null) {
            mDynamicRecyclerView.refreshComplete();
        }
    }

    @Override
    public void loadMoreStatus(LoadingFooter.State state) {
        RecyclerViewStateUtils.setFooterViewState(getActivity(), mDynamicRecyclerView, UrlMethod.PAZE_SIZE, state, null);
    }

    @Override
    public void showEmptyView(boolean isshow) {
        if (tvWaring!=null)
        tvWaring.setVisibility(isshow ? View.VISIBLE : View.GONE);
    }

    @Subscribe
    public void onEventMainThread(String msg) {
        if (WenDaFragment.TAG_REFRESH.equals(msg)) {
            onRefresh();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (FIRST_IN != 0) {
//            onRefresh();
//        }
//        ++FIRST_IN;
//        LogUtils.e("刷新onStart" + comefrom);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
