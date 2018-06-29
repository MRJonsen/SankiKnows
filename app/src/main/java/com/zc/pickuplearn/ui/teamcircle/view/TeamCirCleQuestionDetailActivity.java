package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.youth.xframe.XFrame;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.ui.question.FunctionType;
import com.zc.pickuplearn.ui.teamcircle.question.TeamAskQuestionActivity;
import com.zc.pickuplearn.ui.view.LayoutManager.FullyLinearLayoutManager;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.ImageGridAdapter;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamQuestionAnswerListAdapter;
import com.zc.pickuplearn.ui.view.NoScrollGridview;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 班组圈问题详情页
 */
public class TeamCirCleQuestionDetailActivity extends BaseActivity {

    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.bt_answer)
    Button btAnswer;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.gv_content)
    NoScrollGridview gvContent;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.answer_question_teamName)
    TextView answerQuestionTeamName;
    @BindView(R.id.tv_answer_times)
    TextView tvAnswerTimes;
    @BindView(R.id.rlv_answer_list)
    LRecyclerView rlvAnswerList;
    @BindView(R.id.ll_target)
    LinearLayout mlltarget;
    @BindView(R.id.tv_target_answer)
    TextView mTargetAnswer;
    @BindView(R.id.x_loading)
    XLoadingView xLoadingView;
    List<AnswerBean> mData = new ArrayList<>();
    private boolean isRefresh = false;
    /**
     * 已经获取到多少条数据了
     */
    public int mCurrentCounter = 0;
    private LRecyclerViewAdapter viewAdapter;
    private boolean IS_SELF;//是不是提问者的标志位
    private final static String TAG_QUESTIONBEAN = "TeamCirCleQuestionDetailActivity_questionbean";
    private final static String TAG_TEAMCIRCLEBEAN = "TeamCirCleQuestionDetailActivity_teamcirclebean";
    private QuestionBean questionBean;//传入的问题实例
    private ImageGridAdapter gvadapter;//问题图片适配器
    private TeamCircleBean teamCircleBean;
    private boolean IS_FIRST_IN = true;

    /**
     * 开启本页面的方法
     *
     * @param context      上下文
     * @param questionBean 问题bean
     */
    public static void startTeamCirCleQuestionDetailActivity(Context context, QuestionBean questionBean, TeamCircleBean teamCircleBean) {
        Intent intent = new Intent(context, TeamCirCleQuestionDetailActivity.class);
        intent.putExtra(TAG_QUESTIONBEAN, questionBean);
        intent.putExtra(TAG_TEAMCIRCLEBEAN, teamCircleBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_cir_cle_question_detail;
    }

    @Override
    public void initView() {
        xLoadingView.setEmptyViewWarning("还没有人回答，快来帮帮TA吧！");
        questionBean = (QuestionBean) getIntent().getSerializableExtra(TAG_QUESTIONBEAN);
        teamCircleBean = (TeamCircleBean) getIntent().getSerializableExtra(TAG_TEAMCIRCLEBEAN);
        initRecycleView();
    }

    @Override
    protected void initData() {
        setData();
    }

    private void initRecycleView() {
        rlvAnswerList.setHasFixedSize(true);
        rlvAnswerList.setLayoutManager(new FullyLinearLayoutManager(this));
        rlvAnswerList.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        TeamQuestionAnswerListAdapter adapter = new TeamQuestionAnswerListAdapter(this, mData, questionBean);
        //适配器转换
        viewAdapter = new LRecyclerViewAdapter(adapter);
        rlvAnswerList.setAdapter(viewAdapter);
        rlvAnswerList.setLoadMoreEnabled(false);
        rlvAnswerList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                clearData();
                getData();
            }
        });
//        rlvAnswerList.setLoadMoreEnabled(false);//去掉加载更多布局
//        rlvAnswerList.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                isRefresh = false;
//                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rlvAnswerList);
//                if (state == LoadingFooter.State.Loading) {
//                    return;
//                }
//                getData();
//            }
//        });
        viewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TeamQuestionAndAnswerActivity.startTeamQuestionAndAnswerActivity(TeamCirCleQuestionDetailActivity.this, questionBean, mData.get(position));
            }
        });
        viewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final AnswerBean answerBean = mData.get(position);
                UserBean userInfo = DataUtils.getUserInfo();
                if (!"1".equals(questionBean.getISSOLVE()) && questionBean.getQUESTIONUSERCODE().equals(userInfo.getUSERCODE())) {
                    new AlertDialog(TeamCirCleQuestionDetailActivity.this).builder()
                            .setTitle("提示").setMsg(XFrame.getString(R.string.warning_take_answer))
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    API.teamTakeAnswer(answerBean, new CommonCallBack<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            ToastUtils.showToast(TeamCirCleQuestionDetailActivity.this, XFrame.getString(R.string.warning_take_answer_success));
                                            questionBean.setISSOLVE("1");
                                            setData();
                                            answerBean.setISPASS("1");
                                            viewAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure() {
                                            ToastUtils.showToast(TeamCirCleQuestionDetailActivity.this, "服务器正忙！稍后再试！");
                                        }
                                    });
                                }
                            }).setNegativeButton(XFrame.getString(R.string.warning_let_me_think), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }
            }
        });
        rlvAnswerList.setRefreshing(true);//自动刷新
    }

    private void setData() {
        if (questionBean != null) {
            IS_SELF = DataUtils.getUserInfo().getUSERCODE().equals(questionBean.getQUESTIONUSERCODE());
//            if (IS_SELF) {
//                btAnswer.setVisibility(View.GONE);
            btAnswer.setText(IS_SELF ? "修改" : "回答");
//            }
            if ("1".equals(questionBean.getISSOLVE())){
                btAnswer.setVisibility(View.GONE);//已解决问题不让修改
            }
            answerQuestionTeamName.setText(questionBean.getQUESTIONTYPENAME());
            tvQuestion.setText(questionBean.getQUESTIONEXPLAIN());
            tvAnswerTimes.setText(TextUtils.isEmpty(questionBean.getANSWERSUM()) ? "0" : questionBean.getANSWERSUM());
            tvTitle.setText(questionBean.getQUESTIONUSERNAME() + "的提问");
            tvTime.setText(DateUtils.getCompareDate(questionBean.getSYSCREATEDATE()));
            String targetusername = questionBean.getTARGETUSERNAME();
            if (TextUtils.isEmpty(targetusername)){
                mlltarget.setVisibility(View.GONE);
            }else {
                mlltarget.setVisibility(View.VISIBLE);
                mTargetAnswer.setText(targetusername);
            }
            if (gvadapter != null) {
                gvadapter.notifyDataSetChanged();
            } else {
                gvadapter = new ImageGridAdapter(this, questionBean.getFILEURL());
                gvContent.setAdapter(gvadapter);
            }
        }
    }

    @OnClick({R.id.bt_back, R.id.bt_answer})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_answer:
                enterAnswerViewOrZhuiwenZhuiDaView();
                break;

        }
    }

    /**
     * 进入追问追答或者修改问题页面
     */
    public void enterAnswerViewOrZhuiwenZhuiDaView() {
        if (IS_SELF) {
            TeamAskQuestionActivity.startTeamAskQuestionActivity(this, questionBean, teamCircleBean, FunctionType.TEAM_QUESTION_EDIT);//修改问题
            return;
        }
        btAnswer.setClickable(false);

        //1.是回答者直接进入追问追答界面 否者进入回答界面
//        if ("1".equals(questionBean.getISANSWER())) {
//            ToastUtils.showToast(this, "追问追答");
//        } else {
//            // TODO: 2017/3/16  进入回答页面
//        }
        //1.是回答者直接进入追问追答界面 否者进入回答界面
        API.getTeamAnAnserData(questionBean, new CommonCallBack<List<AnswerBean>>() {
            @Override
            public void onSuccess(List<AnswerBean> beanList) {
                if (beanList.size() > 0) {
                    TeamQuestionAndAnswerActivity.startTeamQuestionAndAnswerActivity(TeamCirCleQuestionDetailActivity.this, questionBean, beanList.get(0));
                } else {
                    TeamAnswerQuestionActivity.startTeamAnswerQuestionActivity(TeamCirCleQuestionDetailActivity.this, questionBean);//进入回答页面
                }
                btAnswer.setClickable(true);
            }

            @Override
            public void onFailure() {
                ToastUtils.showToastCenter(TeamCirCleQuestionDetailActivity.this, "服务器正忙！请稍后再试");
                btAnswer.setClickable(true);
            }
        });
    }

    /**
     * 获取答案列表数据
     */
    public void getData() {
        API.getTeamAnswerList(questionBean, new CommonCallBack<List<AnswerBean>>() {
            @Override
            public void onSuccess(List<AnswerBean> beanList) {
                if (isRefresh) {
                    refreshStatFinish();
                    mData.addAll(beanList);
                    mCurrentCounter = mData.size();
                    if (mData.isEmpty()) {
                        RecyclerViewStateUtils.setFooterViewState(rlvAnswerList, LoadingFooter.State.TheEnd);
                    }
                } else {
                    mData.addAll(beanList);
                    mCurrentCounter = mData.size();
                    setLoadMoreStats(mCurrentCounter);
                }
                if (mData.size()>0){
                    xLoadingView.showContent();
                }else {
                    xLoadingView.showEmpty();
                }
                viewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                xLoadingView.showContent();
                refreshStatFinish();
                RecyclerViewStateUtils.setFooterViewState(rlvAnswerList, LoadingFooter.State.NetWorkError);
            }
        });
    }

    public void clearData() {
        mCurrentCounter = 0;
        mData.clear();
        RecyclerViewStateUtils.setFooterViewState(rlvAnswerList, LoadingFooter.State.Normal);
        viewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IS_FIRST_IN) {
            IS_FIRST_IN = false;
        } else {
            isRefresh = true;
            clearData();
            getData();
        }
    }

    /**
     * 刷新结束
     */
    public void refreshStatFinish() {
        rlvAnswerList.refreshComplete();
    }

    public void setLoadMoreStats(int count) {
        if (count % UrlMethod.PAZE_SIZE != 0) {
            RecyclerViewStateUtils.setFooterViewState(this, rlvAnswerList, UrlMethod.PAZE_SIZE, LoadingFooter.State.TheEnd, null);
            return;
        }
        if (count % UrlMethod.PAZE_SIZE == 0) {
            RecyclerViewStateUtils.setFooterViewState(this, rlvAnswerList, UrlMethod.PAZE_SIZE, LoadingFooter.State.Normal, null);
        }
    }
}
