package com.zc.pickuplearn.ui.mine.mine.widget.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.answerdetail.ZhuiwenZhuidaActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view.QuestionDetailActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.widget.QuestionDetailType;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.model.ImplWenDaModel;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.ImageGridAdapter;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamWenDaTipGridAdapter;
import com.zc.pickuplearn.ui.view.NoScrollGridview;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.zc.pickuplearn.utils.UIUtils.getResources;

/**
 * 我的问答适配器
 * 作者： Jonsen
 * 时间: 2017/3/15 17:05
 * 联系方式：chenbin252@163.com
 */

public class MyAnswerAapter extends CommonAdapter<QuestionBean> {
    private List<QuestionBean> mDatas;
    private Context mContext;
    private final UserBean userInfo;

    public MyAnswerAapter(Context context, List<QuestionBean> datas) {
        this(context, R.layout.item_dynamic_new, datas);
    }

    private MyAnswerAapter(Context context, int layoutId, List<QuestionBean> datas) {
        super(context, layoutId, datas);
        userInfo = DataUtils.getUserInfo();
        mDatas = datas;
        mContext = context;
    }

    @Override
    protected void convert(final ViewHolder holder, final QuestionBean questionbean, int position) {
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
        if ("1".equals(questionbean.getISANONYMITY())) {
            holder.setText(R.id.tv_dynamic_questioner, "匿名用户");
        }
        NoScrollGridview TipView = holder.getView(R.id.nsg_biaoqian_);
        if (questionbean.getTip() != null) {
            TipView.setAdapter(new TeamWenDaTipGridAdapter(mContext, questionbean.getTip(), questionbean));
        }
        final NoScrollGridview view = holder.getView(R.id.nsg_dynamic_);
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
                new ImplWenDaModel().getAnsewData(questionbean, new ImplWenDaModel.GetAnswerDatasCallBack() {
                    @Override
                    public void onSuccess(List<AnswerBean> dynamic_data) {
                        view1.setClickable(true);
                        ZhuiwenZhuidaActivity.startZhuiwenZhuidaActivity(mContext, questionbean, dynamic_data.get(0));
                    }

                    @Override
                    public void onFailure() {
                        view1.setClickable(true);
                        ToastUtils.showToastCenter(mContext, "服务器正忙！请稍后再试");
                    }
                });
            }
        });
    }

    /**
     * 进入问题详情页
     *
     * @param questionbean 问题实体对象
     */
    private void enterQuestionDetail(QuestionBean questionbean) {
        QuestionDetailActivity.startQuestionDetailActivity(mContext, questionbean, QuestionDetailType.MyAnswer);//进入问题详情
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
