package com.zc.pickuplearn.ui.expert.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.application.JChatDemoApplication;
import com.zc.pickuplearn.beans.Professor;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.SearchActivity;
import com.zc.pickuplearn.ui.group.widget.prodetail.view.ProfessTalkListActivity;
import com.zc.pickuplearn.ui.group.widget.prodetail.view.ProfessorDetailActivity;
import com.zc.pickuplearn.ui.group.widget.prodetail.view.ProfessorHistroyAnswerActivity;
import com.zc.pickuplearn.ui.im.chatting.ChatActivity;
import com.zc.pickuplearn.ui.question_and_answer.TypeQuestion;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.UIUtils;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 专家列表
 * Created by chen bin on 2017/11/20.
 */

public class ExpertListAdapter extends CommonAdapter<Professor> {


    private UserBean opertor;

    public ExpertListAdapter(Context context, List<Professor> data) {
        this(context, R.layout.item_expert, data);
        opertor = DataUtils.getUserInfo();
    }

    private ExpertListAdapter(Context context, int layoutId, List<Professor> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(final ViewHolder holder, final Professor professor, int position) {


        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfessorDetailActivity.startProfessorDetail(mContext, professor);
            }
        });

        holder.setOnClickListener(R.id.ll_expert_online, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opertor.getUSERCODE().equals(professor.getUSERCODE())) {
                    ProfessTalkListActivity.startProfessorttalkActivity(mContext);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(JChatDemoApplication.CONV_TITLE, professor.getUSERNAME());
                    intent.putExtra(JChatDemoApplication.TARGET_ID, professor.getUSERCODE());
                    intent.putExtra(JChatDemoApplication.TARGET_APP_KEY, UIUtils.getString(R.string.APP_KEY));
                    intent.setClass(mContext, ChatActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.setOnClickListener(R.id.ll_expert_history_answer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserBean userBean = new UserBean();
                userBean.setUSERCODE(professor.getUSERCODE());
                SearchActivity.starAction(mContext, TypeQuestion.EXPERT, userBean);
//                ProfessorHistroyAnswerActivity.startProfessorHistoryAnswerActivity(mContext, professor);//专家历时解答
            }
        });
        holder.setText(R.id.tv_expert_grade, professor.getGRADENAME());
        holder.setText(R.id.tv_expert_answer_num, professor.getCLICKCOUNT() + "");
        holder.setText(R.id.tv_expert_score, "0.0");
        if (!TextUtils.isEmpty(professor.getSCORE())) {
            double v = Double.parseDouble(professor.getSCORE());
            DecimalFormat df = new DecimalFormat("######0.0");
            String format = df.format(v);
            holder.setText(R.id.tv_expert_score, format);
        }
        holder.setText(R.id.tv_expert_online_status, "1".equals(professor.getISONLINE()) ? "" : "[离线请留言]");//ISONLINE 0不在线，1在线
        holder.setText(R.id.tv_expert_name, professor.getUSERNAME());
        holder.setText(R.id.tv_expert_speciality, "擅长：" + professor.getGOODFIELD());
        CircleImageView view = holder.getView(R.id.iv_expert_head_img);
        ImageLoaderUtil.showHeadView(mContext, ResultStringCommonUtils.subUrlToWholeUrl(professor.getFILEURL()), view);

        // 是否在线 状态设置  ISONLINE 0不在线，1在线
        switch (professor.getISONLINE()) {
            case "0":
                holder.setVisible(R.id.fl_cover, true);
                holder.setImageResource(R.id.iv_grade, R.mipmap.icon_expert_grade_off_line);
                holder.setTextColor(R.id.tv_expert_name, XFrame.getColor(R.color.expert_off_line_text));
                holder.setTextColor(R.id.tv_expert_speciality, XFrame.getColor(R.color.expert_off_line_text));
                holder.setTextColor(R.id.tv_expert_score, XFrame.getColor(R.color.expert_off_line_text));
                holder.setTextColor(R.id.tv_expert_grade, XFrame.getColor(R.color.expert_off_line_text));
                holder.setTextColor(R.id.tv_expert_answer_warning, XFrame.getColor(R.color.expert_off_line_text));
                holder.setTextColor(R.id.tv_expert_answer_num, XFrame.getColor(R.color.expert_off_line_text));
                break;
            case "1":
                holder.setImageResource(R.id.iv_grade, R.mipmap.icon_expert_grade);
                holder.setVisible(R.id.fl_cover, false);
                holder.setTextColor(R.id.tv_expert_name, XFrame.getColor(R.color.colorPrimary));
                holder.setTextColor(R.id.tv_expert_speciality, XFrame.getColor(R.color.text_color_black));
                holder.setTextColor(R.id.tv_expert_score, XFrame.getColor(R.color.text_color_gold));
                holder.setTextColor(R.id.tv_expert_grade, XFrame.getColor(R.color.text_color_black));
                holder.setTextColor(R.id.tv_expert_answer_warning, XFrame.getColor(R.color.text_color_black));
                holder.setTextColor(R.id.tv_expert_answer_num, XFrame.getColor(R.color.text_color_black));
                break;
        }
//        holder.setText(R.id.tv_case_tag, caseBean.getQUESTIONTYPENAME());
//        holder.setText(R.id.tv_case_name, industryStandardBean.getSTANDARDNAME());
//        holder.setText(R.id.tv_case_click_times, industryStandardBean.getCLICKCOUNT()+"");
//        ImageView view = holder.getView(R.id.iv_case_face);
//        ImageLoaderUtil.showImageView(mContext, ResultStringCommonUtils.subUrlToWholeUrl(caseBean.getFILEURL()), view);
    }

}
