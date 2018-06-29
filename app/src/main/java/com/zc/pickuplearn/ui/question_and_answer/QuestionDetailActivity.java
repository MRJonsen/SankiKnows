package com.zc.pickuplearn.ui.question_and_answer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiang.android.indicatordialog.IndicatorBuilder;
import com.jiang.android.indicatordialog.IndicatorDialog;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.xframe.XFrame;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.MultiItemTypeAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.QuestionTypeBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.event.QuestionStatusChangeEvent;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.answerdetail.ZhuiwenZhuidaActivity;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view.AnswerActivity;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.model.ImplWenDaModel;
import com.zc.pickuplearn.ui.question.ChangeQuestionActivity;
import com.zc.pickuplearn.ui.question.FunctionType;
import com.zc.pickuplearn.ui.question_and_answer.adapter.AnswerListAdapter;
import com.zc.pickuplearn.ui.view.MaxRecycleView;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.PictureUtil;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.carbs.android.expandabletextview.library.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 问题详情页面（新）
 */
public class QuestionDetailActivity extends EventBusActivity {

    private static final String PARAM1 = QuestionDetailActivity.class.getSimpleName();

    private QuestionBean questionBean;
    private QuestionBean mQuestionBean;
    private AnswerListAdapter answerListAdapter;
    private UserBean userInfo;

    public static void open(Context context, QuestionBean questionBean) {
        Intent intent = new Intent(context, QuestionDetailActivity.class);
        intent.putExtra(PARAM1, questionBean);
        context.startActivity(intent);
    }

    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.bt_right)
    Button btRight;
    @BindView(R.id.civ_head)
    CircleImageView civHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_reward)
    ImageView ivReward;
    @BindView(R.id.tv_reward)
    TextView tvReward;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.nineGrid)
    NineGridView nineGrid;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.iv_reply)
    ImageView ivReply;
    @BindView(R.id.tv_reply)
    TextView tvReply;
    @BindView(R.id.ll_reply)
    LinearLayout llReply;
    @BindView(R.id.iv_zan)
    ImageView ivZan;
    @BindView(R.id.tv_zan)
    TextView tvZan;
    @BindView(R.id.ll_zan)
    LinearLayout llZan;
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R.id.rc_comment)
    MaxRecycleView rcComment;
    @BindView(R.id.iv_order)
    ImageView ivOrder;
    @BindView(R.id.tv_answer_sum)
    TextView tv_answer_sum;
    @BindView(R.id.ll_order)
    LinearLayout ll_order;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.x_loading)
    XLoadingView xLoadingView;
    private int ORDER_TYPE = 1;
    private int etvWidth;
    private HashMap<String, Drawable> imagehold;
    List<AnswerBean> mComment = new ArrayList<>();
    private int ANSWER_INDEX = 0;
    private boolean ISFRESH = true;

    @Override
    public int setLayout() {
        return R.layout.activity_question_detail_new;
    }


    @Override
    public void initVariables() {
        super.initVariables();
        questionBean = (QuestionBean) getIntent().getSerializableExtra(PARAM1);
        userInfo = DataUtils.getUserInfo();
    }

    @Override
    protected void initData() {
        initXloading();
        initComment();
        initRefresh();
    }

    private void initXloading() {
        xLoadingView.setEmptyViewWarning("还没有人回答，快来帮帮TA吧！");
    }

    private void getQuestionData() {
        API.getAnQuestionBean(questionBean, new CommonCallBack<List<QuestionBean>>() {
            @Override
            public void onSuccess(List<QuestionBean> questionBeen) {
                if (questionBeen != null && questionBeen.size() > 0) {
                    try {
                        mQuestionBean = questionBeen.get(0);
                        initToolBar(mQuestionBean);
                        initBottom();
                        initQuestion(mQuestionBean);
                        if (answerListAdapter!=null){
                            answerListAdapter.setQuestionBean(mQuestionBean);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void getCommentData() {
        if (ISFRESH) {
            ANSWER_INDEX = 0;
        }
        API.getAnswerList(questionBean, ANSWER_INDEX,ORDER_TYPE+"",new CommonCallBack<List<AnswerBean>>() {
            @Override
            public void onSuccess(List<AnswerBean> answerBeen) {
                if (ISFRESH) {
                    mComment.clear();
                } else {
                    if (srlRefresh != null && answerBeen.isEmpty()) {
                        srlRefresh.setLoadmoreFinished(true);
                    }
                }
                mComment.addAll(answerBeen);
                ANSWER_INDEX = mComment.size();
                answerListAdapter.notifyDataSetChanged();
                setFefreshStatus();
            }

            @Override
            public void onFailure() {
                setFefreshStatus();
            }
        });
    }

    public void setFefreshStatus() {
        if (srlRefresh == null)
            return;
        if (ISFRESH) {
            srlRefresh.finishRefresh();
        } else {
            srlRefresh.finishLoadmore();
        }
        if (xLoadingView!=null){
            if (ANSWER_INDEX==0){
                xLoadingView.showEmpty();
            }else {
                xLoadingView.showContent();
            }
        }
    }

    private void initRefresh() {
        srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                ISFRESH = true;
                refreshlayout.setLoadmoreFinished(false);
                getQuestionData();
                getCommentData();
            }
        });
        srlRefresh.setEnableLoadmore(true);
        srlRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ISFRESH = false;
                getCommentData();
            }
        });
        srlRefresh.autoRefresh();
    }

    private void initQuestion(final QuestionBean questionBean) {
        //头像
        if (!questionBean.getHEADIMAGE().isEmpty()) {
            if ("1".equals(questionBean.getISANONYMITY())) {
                ImageLoaderUtil.showHeadView(mContext, "", civHead);//加载默认头像
            } else {
                ImageLoaderUtil.showHeadView(mContext, ResultStringCommonUtils.subUrlToWholeUrl(questionBean.getHEADIMAGE()), civHead);//加载头像
            }
        } else {
            ImageLoaderUtil.showHeadView(mContext, "", civHead);//加载默认头像
        }

        //标签
//        StringBuilder tag = new StringBuilder();
//        if (questionBean.getTip() != null) {
//            for (QuestionTypeBean questionTypeBean :
//                    questionBean.getTip()) {
//                tag.append(" " + questionTypeBean.getQUESTIONTYPENAME());
//            }
//        }
//        tvTag.setText(tag.toString());
        tvTag.setText(questionBean.getQUESTIONTYPENAME());
        //回答数
        tv_answer_sum.setText("评论 " + questionBean.getANSWERSUM());
        //名称
        tvName.setText(TextUtils.isEmpty(questionBean.getNICKNAME()) ? questionBean.getQUESTIONUSERNAME() : questionBean.getNICKNAME());
        if ("1".equals(questionBean.getISANONYMITY())) {
            tvName.setText("匿名用户");
        }
        //时间
        tvTime.setText(DateUtils.getCompareDate(questionBean.getSYSCREATEDATE()));

        //回复
        String answersum = questionBean.getANSWERSUM();
        final String isanswer = questionBean.getISANSWER();//1 answer 0 not answer
        int answerColor = "1".equals(isanswer) ? R.color.colorPrimary : R.color.text_color_gray;
        tvReply.setText(TextUtils.isEmpty(answersum) ? "回答" : "0".equals(answersum) ? "回答" : answersum);
        tvReply.setTextColor(getResources().getColor(answerColor));
        ivReply.setImageDrawable(getColorDrawable(R.mipmap.icon_reply_new, answerColor));
        llReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(isanswer)) {
                    // TODO: 2017/11/15 追文追答
                    //1.是回答者直接进入追问追答界面 否者进入回答界面
                    showProgress();
                    if (questionBean.getISANSWER().equals("1")) {
                        new ImplWenDaModel().getAnsewData(questionBean, new ImplWenDaModel.GetAnswerDatasCallBack() {
                            @Override
                            public void onSuccess(List<AnswerBean> dynamic_data) {
                                hideProgress();
                                ZhuiwenZhuidaActivity.startZhuiwenZhuidaActivity(mContext, questionBean, dynamic_data.get(0));
                            }

                            @Override
                            public void onFailure() {
                                hideProgress();
                                ToastUtils.showToastCenter(mContext, "请稍后再试");
                            }
                        });
                    }
                } else {
                    if (userInfo.getUSERCODE().equals(questionBean.getQUESTIONUSERCODE())) {
                        ToastUtils.showToastCenter(mContext, XFrame.getString(R.string.warning_no_answer_self_question));
                    } else {
                        AnswerActivity.startAnswerActivity(mContext, questionBean);//进入回答页面
                    }
                }
            }
        });

        //点赞
        tvZan.setText(questionBean.getGOODNUMS() == 0 ? "点赞" : "" + questionBean.getGOODNUMS());
        String isgood = questionBean.getISGOOD();
        int zanColor = "1".equals(isgood) ? R.color.colorPrimary : R.color.text_color_gray;
        tvZan.setTextColor(getResources().getColor(zanColor));
        ivZan.setImageDrawable(getColorDrawable(R.mipmap.icon_zan_new, zanColor));
        llZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(questionBean.getISGOOD()))
                    return;
                questionBean.setISGOOD("1");
                questionBean.setGOODNUMS(questionBean.getGOODNUMS() + 1);

                API.goodQuestion(questionBean, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
                ivZan.setImageDrawable(getColorDrawable(R.mipmap.icon_zan_new, R.color.colorPrimary));
                tvZan.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvZan.setText(questionBean.getGOODNUMS() == 0 ? "点赞" : "" + questionBean.getGOODNUMS());
            }
        });


        //奖励
        ivReward.setImageDrawable(getColorDrawable(R.mipmap.icon_reward, R.color.text_color_gold));
        String bonuspoints = questionBean.getBONUSPOINTS();
        if (TextUtils.isEmpty(bonuspoints)) {
            ivReward.setVisibility(View.GONE);
        } else {
            ivReward.setVisibility(View.VISIBLE);
        }
        tvReward.setText(bonuspoints);


        //问题内容

        tvQuestion.setText(questionBean.getQUESTIONEXPLAIN());
//        tvQuestion.post(new Runnable() {
//            @Override
//            public void run() {
//                etvWidth = tvQuestion.getWidth();
//            }
//        });
//        tvQuestion.updateForRecyclerView(questionBean.getQUESTIONEXPLAIN(), etvWidth, 0);


        //问题图片
        List<String> imgurls = ResultStringCommonUtils.splitUrls(questionBean.getFILEURL());
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        for (String url :
                imgurls) {
            ImageInfo info = new ImageInfo();
            info.setThumbnailUrl(url);
            info.setBigImageUrl(url);
            imageInfo.add(info);
        }
        nineGrid.setAdapter(new NineGridViewClickAdapter(this, imageInfo));
    }

    @Override
    public void initView() {
        initStatusBar(root);
        initToolBar(questionBean);
        ivOrder.setImageDrawable(getColorDrawable(R.mipmap.icon_order, R.color.question_order));
    }

    public void initBottom() {

        if (userInfo.getUSERCODE().equals(mQuestionBean.getQUESTIONUSERCODE())) {
            ll_bottom.setVisibility(View.GONE);
        }
    }

    private void initToolBar(final QuestionBean questionBean) {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        tvTitle.setText("问题详情");
        if (userInfo.getUSERCODE().equals(questionBean.getQUESTIONUSERCODE())&&!"1".equals(questionBean.getISSOLVE())) {
            btRight.setText("修改");
            btRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChangeQuestionActivity.startChangeQuestionActivity(QuestionDetailActivity.this, mQuestionBean==null?questionBean:mQuestionBean, FunctionType.QUESTION_EDIT);
                }
            });
        }else {
            btRight.setVisibility(View.GONE);
        }
    }

    private void initComment() {
        ll_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDialog(v);
            }
        });
        rcComment.setHasFixedSize(true);
        rcComment.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcComment.setFocusableInTouchMode(false);
        rcComment.setItemAnimator(new DefaultItemAnimator());
        answerListAdapter = new AnswerListAdapter(this,questionBean ,mComment);
        rcComment.setAdapter(answerListAdapter);
    }

    public Drawable getColorDrawable(int res, int color) {
        Drawable drawable;
        if (imagehold == null) {
            imagehold = new HashMap<>();
        }
        drawable = imagehold.get(res + "" + color);
        if (drawable == null) {
            drawable = PictureUtil.tintDrawAble(res, color);
            imagehold.put(res + "" + color, drawable);
        }
        return drawable;
    }

    public void showOrderDialog(View v) {
        final ArrayList<String> objects = new ArrayList<String>();
        CommonAdapter popAdapter = new CommonAdapter<String>(mContext, R.layout.simple_item, objects) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                holder.setText(R.id.tv, s);
                if (position + 1 == ORDER_TYPE) {
                    holder.setTextColor(R.id.tv, XFrame.getColor(R.color.colorPrimary));
                }
            }
        };
        objects.add("按时间");
        objects.add("按热度");
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        final IndicatorDialog dialog = new IndicatorBuilder((Activity) mContext)
                .arrowWidth(UIUtils.dip2px(15))
                .height((int) (height * 0.2))
                .height(-1)
                .width((int) (width * 0.3))
                .ArrowDirection(IndicatorBuilder.BOTTOM)
                .bgColor(Color.WHITE)
                .gravity(IndicatorBuilder.GRAVITY_RIGHT)
                .ArrowRectage(0.8f)
                .radius(8)
                .layoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false))
                .adapter(popAdapter).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show(v);
        popAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (!(ORDER_TYPE == position + 1)) {
                    ORDER_TYPE = position + 1;
                    ISFRESH = true;
                    getCommentData();
                    tvOrder.setText(objects.get(position));
                }
                dialog.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Subscribe
    public void onEventMainThread(QuestionStatusChangeEvent event) {
        if (srlRefresh!=null){
           srlRefresh.autoRefresh();
       }
    }

}
