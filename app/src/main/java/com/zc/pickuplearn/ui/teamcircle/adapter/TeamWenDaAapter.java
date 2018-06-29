package com.zc.pickuplearn.ui.teamcircle.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.ImageGridAdapter;
import com.zc.pickuplearn.ui.teamcircle.view.TeamAnswerQuestionActivity;
import com.zc.pickuplearn.ui.teamcircle.view.TeamCirCleQuestionDetailActivity;
import com.zc.pickuplearn.ui.teamcircle.view.TeamQuestionAndAnswerActivity;
import com.zc.pickuplearn.ui.teamcircle.view.TeamWenDaFragment.TeamWenDaType;
import com.zc.pickuplearn.ui.view.NoScrollGridview;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.zc.pickuplearn.utils.UIUtils.getResources;

/**
 * 团队问答适配器
 * 作者： Jonsen
 * 时间: 2017/3/15 17:05
 * 联系方式：chenbin252@163.com
 */

public class TeamWenDaAapter extends CommonAdapter<QuestionBean> {
    private List<QuestionBean> mDatas;
    private Context mContext;
    private final UserBean userInfo;
    private TeamCircleBean mTeamCircleBean;
    private TeamWenDaType FROM;

    public TeamWenDaAapter(Context context, List<QuestionBean> datas, TeamCircleBean teamCircleBean, TeamWenDaType from) {
        this(context, R.layout.item_dynamic_new, datas);
        mTeamCircleBean = teamCircleBean;
        FROM = from;
    }

    private TeamWenDaAapter(Context context, int layoutId, List<QuestionBean> datas) {
        super(context, layoutId, datas);
        userInfo = DataUtils.getUserInfo();
        mDatas = datas;
        mContext = context;
    }

    @Override
    protected void convert(final ViewHolder holder, final QuestionBean questionbean, int position) {
        String targetusername = questionbean.getTARGETUSERNAME();
        if (TeamWenDaType.QUSETION.equals(FROM)) {
            if (!TextUtils.isEmpty(targetusername)) {
                holder.setVisible(R.id.ll_target_user,true);
                holder.setText(R.id.tv_target_answer,targetusername);
            }else {
                holder.setVisible(R.id.ll_target_user,false);
            }
            holder.setVisible(R.id.bt_answer_question,false);
        }
        if (TeamWenDaType.QUSETION.equals(FROM)||TeamWenDaType.ACTION.equals(FROM)){//我来挑战 和 我的问题显示消息
            TextView msg = holder.getView(R.id.tv_question_msg);
            if (msg != null) {//设置消息提示数量
                if (!TextUtils.isEmpty(questionbean.getINFOCOUNT()) && Integer.valueOf(questionbean.getINFOCOUNT()) > 0) {
                    String infocount = questionbean.getINFOCOUNT() + "";
                    // TODO: 2017/10/15
                    msg.setVisibility(View.GONE);
//                    msg.setVisibility(View.VISIBLE);
                    msg.setText(infocount);
                } else {
                    msg.setVisibility(View.GONE);
                }
            }
        }

        if ("1".equals(questionbean.getISSOLVE())){
            holder.setVisible(R.id.iv_solve_tag,true);
        }else {
            holder.setVisible(R.id.iv_solve_tag,false);
        }
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
            ImageLoaderUtil.displayCircleView(mContext, headimg, questionbean.getHEADIMAGE(), false);//加载头像
        } else {
            headimg.setImageResource(R.mipmap.default_user_circle_icon);
        }
        holder.setText(R.id.tv_time, DateUtils.getCompareDate(questionbean.getSYSCREATEDATE()));
        holder.setText(R.id.tv_dynamic_answer_num, TextUtils.isEmpty(questionbean.getANSWERSUM()) ? "0" : questionbean.getANSWERSUM());
        holder.setText(R.id.tv_dynamic_questioner, questionbean.getQUESTIONUSERNAME());//设置提问者名称
        NoScrollGridview TipView = holder.getView(R.id.nsg_biaoqian_);
        if (questionbean.getTip() != null) {
            TipView.setAdapter(new TeamWenDaTipGridAdapter(mContext, questionbean.getTip(), questionbean));
        }
        NoScrollGridview view = holder.getView(R.id.nsg_dynamic_);
        view.setOnTouchBlankPositionListener(new NoScrollGridview.OnTouchBlankPositionListener() {
            @Override
            public void onTouchBlank(MotionEvent event) {
                enterQuestionDetail(questionbean);
            }
        });
        if (!TextUtils.isEmpty(questionbean.getFILEURL())) {
            view.setVisibility(View.VISIBLE);
            view.setAdapter(new ImageGridAdapter(mContext, questionbean.getFILEURL()));
        } else {
            view.setVisibility(View.GONE);
        }
        View item = holder.getView(R.id.card_view);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterQuestionDetail(questionbean);
            }
        });
        holder.setOnClickListener(R.id.tv_explain, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterQuestionDetail(questionbean);
            }
        });

        holder.setOnClickListener(R.id.bt_answer_question, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view1 = holder.getView(R.id.bt_answer_question);
                view1.setClickable(false);
                if (userInfo.getUSERCODE().equals(questionbean.getQUESTIONUSERCODE())) {
                    ToastUtils.showToastCenter(mContext, XFrame.getString(R.string.warning_no_answer_self_question));
                } else {
                    //1.是回答者直接进入追问追答界面 否者进入回答界面
                    API.getTeamAnAnserData(questionbean, new CommonCallBack<List<AnswerBean>>() {
                        @Override
                        public void onSuccess(List<AnswerBean> beanList) {
                            if (beanList.size() > 0) {
                                TeamQuestionAndAnswerActivity.startTeamQuestionAndAnswerActivity(mContext, questionbean, beanList.get(0));
                            } else {
                                TeamAnswerQuestionActivity.startTeamAnswerQuestionActivity(mContext, questionbean);//进入回答页面
                            }
                            view1.setClickable(true);
                        }

                        @Override
                        public void onFailure() {
                            view1.setClickable(true);
                            ToastUtils.showToastCenter(mContext, "服务器正忙！请稍后再试");
                        }
                    });
                }
            }
        });
    }

    /**
     * 进入问题详情页
     *
     * @param questionbean 问题实体对象
     */
    private void enterQuestionDetail(QuestionBean questionbean) {
        TeamCirCleQuestionDetailActivity.startTeamCirCleQuestionDetailActivity(mContext, questionbean, mTeamCircleBean);
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
                d.setBounds(0, -10, d.getIntrinsicWidth() + 10, d.getIntrinsicHeight());
                return d;
            }
        };
        CharSequence charSequence = Html.fromHtml(html + "<font color =#F0B964>" + score + "</font>", imgGetter, null);
        return charSequence;
    }
}
