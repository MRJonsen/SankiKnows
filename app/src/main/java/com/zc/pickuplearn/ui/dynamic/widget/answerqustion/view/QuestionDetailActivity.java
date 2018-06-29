package com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.youth.xframe.XFrame;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.question.FunctionType;
import com.zc.pickuplearn.ui.view.NoScrollListView;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.answerdetail.ZhuiwenZhuidaActivity;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.question.ChangeQuestionActivity;
import com.zc.pickuplearn.ui.question.QuestionChangeEvent;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.model.ImplQuestionDetailModel;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.presenter.ImplQuestionDetailPresenter;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.widget.AnswerListAdapter;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.widget.QuestionDetailType;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.model.ImplWenDaModel;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.ImageGridAdapter;
import com.zc.pickuplearn.ui.view.NoScrollGridview;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 问题详细信息页面
 */
public class QuestionDetailActivity extends EventBusActivity implements IQuestionDetailView {
    @BindView(R.id.tv_title)
    TextView tvTitle;//标题
    @BindView(R.id.bt_answer)
    Button btAnswer;//回答
    @BindView(R.id.tv_question)
    TextView tvQuestion;//问题
    @BindView(R.id.gv_content)
    NoScrollGridview gvContent;//问题图片布局
    @BindView(R.id.tv_time)
    TextView tvTime;//问题时间
    @BindView(R.id.answer_question_teamName)
    TextView tv_king;//问题类型
    @BindView(R.id.tv_answer_times)
    TextView tvAnswerSum;//回答次数
    @BindView(R.id.lv_answer_list)
    NoScrollListView lvAnswerList;//答案列表
    @BindView(R.id.x_loading)
    XLoadingView xLoadingView;
    public static final String TAG = "QuestionDetailActivity";
    public static final String TAG_FROM = "QuestionDetailActivity_from";//请求该页面的来源
    private QuestionBean questionBean;
    private ImageGridAdapter gvadapter;
    private ImplQuestionDetailPresenter presenter;
    private List<AnswerBean> mAnswerData = new ArrayList<>();
    private AnswerListAdapter answerQuestionAdapter;
    private boolean IS_SELF;//是不是提问者的标志位

    public static QuestionDetailType comfrom;//提供给消息使用的标志位

    public static void startQuestionDetailActivity(Context context, QuestionBean bean, QuestionDetailType type) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra(TAG, bean);
        intent.putExtra(TAG_FROM, type);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_question_detail;
    }

    @Override
    public void initView() {
        getParam();
        setData();
        setAnsewView();
    }

    private void setAnsewView() {
        xLoadingView.setEmptyViewWarning("还没有人回答，快来帮帮TA吧！");
        answerQuestionAdapter = new AnswerListAdapter(this,mAnswerData,comfrom);
        lvAnswerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//答案点击跳转追问追答
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZhuiwenZhuidaActivity.startZhuiwenZhuidaActivity(QuestionDetailActivity.this, questionBean, mAnswerData.get(position));
            }
        });
        lvAnswerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//长按监听 采纳
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final AnswerBean answerBean = mAnswerData.get(position);
                UserBean userInfo = DataUtils.getUserInfo();
                if (!"1".equals(questionBean.getISSOLVE()) && questionBean.getQUESTIONUSERCODE().equals(userInfo.getUSERCODE())) {
                    new AlertDialog(QuestionDetailActivity.this).builder()
                            .setTitle("提示").setMsg(XFrame.getString(R.string.warning_take_answer))
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new ImplQuestionDetailModel().takeAnswer(answerBean, new ImplQuestionDetailModel.TakeAnswerCallback() {
                                        @Override
                                        public void onSuccess() {
                                            ToastUtils.showToast(QuestionDetailActivity.this, XFrame.getString(R.string.warning_take_answer_success));
                                            questionBean.setISSOLVE("1");
                                            setData();
                                            answerBean.setISPASS("1");
                                            answerQuestionAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFail() {
                                            ToastUtils.showToast(QuestionDetailActivity.this, "服务器正忙！稍后再试！");
                                        }
                                    });
                                }
                            }).setNegativeButton(XFrame.getString(R.string.warning_let_me_think), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }else if (userInfo.getUSERCODE().equals(answerBean.getANSWERUSERCODE())&&!"1".equals(answerBean.getISPASS())){
                    new AlertDialog(QuestionDetailActivity.this).builder()
                            .setTitle("提示").setMsg("是否要删除回答？")
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    API.deletAnswer(questionBean, answerBean, new CommonCallBack<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            mAnswerData.remove(answerBean);
                                            answerQuestionAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure() {
                                            showToast("操作失败！");
                                        }
                                    });
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }
                return true;
            }
        });
        lvAnswerList.setAdapter(answerQuestionAdapter);
    }

    private void setData() {
        if (questionBean != null) {
            IS_SELF = DataUtils.getUserInfo().getUSERCODE().equals(questionBean.getQUESTIONUSERCODE());
            btAnswer.setText(IS_SELF ? "修改" : "回答");
            if ("1".equals(questionBean.getISSOLVE())){
                btAnswer.setVisibility(View.GONE);//已解决问题不让修改
            }
            tv_king.setText(questionBean.getQUESTIONTYPENAME());
            tvQuestion.setText(questionBean.getQUESTIONEXPLAIN());
            tvAnswerSum.setText(TextUtils.isEmpty(questionBean.getANSWERSUM()) ? "0" : questionBean.getANSWERSUM());
            tvTitle.setText(TextUtils.isEmpty(questionBean.getNICKNAME()) ? questionBean.getQUESTIONUSERNAME() + "的提问" : questionBean.getNICKNAME() + "的提问");
            if ("1".equals(questionBean.getISANONYMITY())) {
                tvTitle.setText("匿名用户的提问");
            }
            tvTime.setText(DateUtils.getCompareDate(questionBean.getSYSCREATEDATE()));
            if (gvadapter != null) {
                gvadapter.notifyDataSetChanged();
            } else {
                gvadapter = new ImageGridAdapter(this, questionBean.getFILEURL());
                gvContent.setAdapter(gvadapter);
            }
        }
    }

    @Override
    protected void initData() {
        tvQuestion.postInvalidate();
        presenter = new ImplQuestionDetailPresenter(this);
        getAnswetData();
    }

    private void getAnswetData() {
        presenter.getAnswerData(questionBean);
    }

    @Override
    public void disShowAnswerButton() {
        btAnswer.setVisibility(View.GONE);
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void setAnswerData(List<AnswerBean> list) {
        if (list != null) {
            if (list.size()==0){
                xLoadingView.showEmpty();
            }else {
                xLoadingView.showContent();
            }
            mAnswerData.clear();
            mAnswerData.addAll(list);
            answerQuestionAdapter.notifyDataSetChanged();
        }else {
            xLoadingView.showEmpty();
        }
    }


    @OnClick({R.id.bt_back, R.id.bt_answer})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                finishActivity();
                break;
            case R.id.bt_answer:
                enterAnswerViewOrZhuiwenZhuiDaView();
                break;

        }
    }

    /**
     * 右上角按钮点击逻辑
     */
    public void enterAnswerViewOrZhuiwenZhuiDaView() {
        if (IS_SELF) {
            ChangeQuestionActivity.startChangeQuestionActivity(this, questionBean, FunctionType.QUESTION_EDIT);
            return;
        }
        btAnswer.setClickable(false);
        //1.是回答者直接进入追问追答界面 否 进入回答界面
        if ("1".equals(questionBean.getISANSWER())||!(DataUtils.getUserInfo().getUSERCODE().equals(questionBean.getQUESTIONUSERCODE()))) {
            LogUtils.e("回答问题");
            new ImplWenDaModel().getAnsewData(questionBean, new ImplWenDaModel.GetAnswerDatasCallBack() {
                @Override
                public void onSuccess(List<AnswerBean> dynamic_data) {
                    ZhuiwenZhuidaActivity.startZhuiwenZhuidaActivity(QuestionDetailActivity.this, questionBean, dynamic_data.get(0));
                    btAnswer.setClickable(true);
                }

                @Override
                public void onFailure() {
                    AnswerActivity.startAnswerActivity(QuestionDetailActivity.this, questionBean);//进入回答页面
                    btAnswer.setClickable(true);
                }
            });
        } else {
            btAnswer.setClickable(true);
            if ("1".equals(questionBean.getISSOLVE())){
                showToast("问题已解决,不能回答！");
                return;
            }
            AnswerActivity.startAnswerActivity(QuestionDetailActivity.this, questionBean);//进入回答页面
        }
    }

    private boolean IS_FIRST_IN = true;//标记是不是第一次进入

    @Override
    protected void onResume() {
        super.onResume();
        if (IS_FIRST_IN) {
            IS_FIRST_IN = false;
        } else {
            getAnswetData();//g更新答案数据
        }
    }

    /**
     * 获取初始化数据
     */
    public void getParam() {
        questionBean = (QuestionBean) getIntent().getSerializableExtra(TAG);
        comfrom = (QuestionDetailType) getIntent().getSerializableExtra(TAG_FROM);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        comfrom = null;
    }

    @Subscribe
    public void onEventMainThread(QuestionChangeEvent event) {
        //修改问题成功事件
        if (event != null) {
            API.getAnQuestion(questionBean, new CommonCallBack<List<QuestionBean>>() {
                @Override
                public void onSuccess(List<QuestionBean> questionBeen) {
                    if (questionBeen!=null&&questionBeen.size()>0){
                        questionBean = questionBeen.get(0);
                        setData();
                    }
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }
}
