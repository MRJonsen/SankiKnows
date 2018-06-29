package com.zc.pickuplearn.ui.teamcircle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.ImageGridAdapter;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/3/15 22:36
 * 联系方式：chenbin252@163.com
 */

public class TeamQuestionAnswerListAdapter extends CommonAdapter<AnswerBean> {
    private List<AnswerBean> mData;
    private Context mContext;
    private QuestionBean mQuestionBean;
    private UserBean mUserInfo;
    public TeamQuestionAnswerListAdapter(Context context, List<AnswerBean> datas, QuestionBean questionBean) {
        this(context, R.layout.answer_question_list_item, datas);
        mQuestionBean = questionBean;
        mUserInfo = DataUtils.getUserInfo();
    }

    private TeamQuestionAnswerListAdapter(Context context, int layoutId, List<AnswerBean> datas) {
        super(context, layoutId, datas);
        mContext = context;
        mData = datas;
    }

    @Override
    protected void convert(final ViewHolder holder, final AnswerBean answerBean, int position) {
        holder.setVisible(R.id.answer_question_list_item_best_answerImage, "1".equals(answerBean.getISPASS()));
        holder.setImageResource(R.id.answer_question_list_iten_dianzaniCON, "1".equals(answerBean.getISGOOD()) ? R.mipmap.icon_zaned : R.mipmap.icon_zan);
        holder.setText(R.id.answer_question_list_item_answersName, answerBean.getANSWERUSERNAME());
        holder.setText(R.id.answer_question_list_item_answersLV, TextUtils.isEmpty(answerBean.getANSWERLEVEL()) ? "LV1" : "LV" + answerBean.getANSWERLEVEL());
        holder.setText(R.id.answer_question_list_item_theAnswer, answerBean.getANSWEREXPLAIN());
        holder.setText(R.id.answer_question_list_item_answersTime, DateUtils.getCompareDate(answerBean.getSYSCREATEDATE()));
        holder.setText(R.id.answer_question_list_iten_dianzanNum, TextUtils.isEmpty(answerBean.getGOODNUMS()) ? "0" : answerBean.getGOODNUMS());
        holder.setText(R.id.answer_question_list_item_isExpertAnswer, TextUtils.isEmpty(answerBean.getQACOUNT()) ? "" : answerBean.getQACOUNT() + "追问追答");
        ImageGridAdapter adapter1 = new ImageGridAdapter(mContext, answerBean.getFILEURL());
        GridView gridView = holder.getView(R.id.answer_question_list_item_gridview);
        gridView.setAdapter(adapter1);
        holder.setTag(R.id.answer_question_list_iten_dianzan, position);
        holder.setOnClickListener(R.id.answer_question_list_iten_dianzan, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = holder.getView(R.id.answer_question_list_iten_dianzan);
                view.setClickable(false);
                String isgood = answerBean.getISGOOD();
                final String answerId = answerBean.getSEQKEY();
                if (!"1".equals(isgood)) {
                    API.teamAnserGood(mQuestionBean, answerBean, new CommonCallBack<String>() {
                        @Override
                        public void onSuccess(String s) {
                            //点赞成功后数据处理 并通知更新界面
                            answerBean.setISGOOD("1");
                            if (!TextUtils.isEmpty(answerBean.getGOODNUMS())) {
                                String goodnums = answerBean.getGOODNUMS();
                                Integer integer = Integer.valueOf(goodnums);
                                answerBean.setGOODNUMS(integer + 1 + "");
                            } else {
                                answerBean.setGOODNUMS("1");
                            }
                            notifyDataSetChanged();
                            view.setClickable(true);
                        }

                        @Override
                        public void onFailure() {
                            view.setClickable(true);
                            ToastUtils.showToast(mContext, "服务器繁忙！");
                        }
                    });
                } else {
                    view.setClickable(true);
                    ToastUtils.showToast(mContext, "不能重复点赞！");
                }
            }
        });
        //设置消息提示
        if (mUserInfo.getUSERCODE().equals(answerBean.getANSWERUSERCODE())) {//设置消息提示数量
            if (!TextUtils.isEmpty(answerBean.getQUESTIONCOUNT()) && Integer.valueOf(answerBean.getQUESTIONCOUNT()) > 0) {
                String infocount = answerBean.getQUESTIONCOUNT() + "";
                holder.setVisible(R.id.tv_answer_msg,true);
                holder.setText(R.id.tv_answer_msg,infocount);
            } else {
                holder.setVisible(R.id.tv_answer_msg,false);
            }
        } else  {
            LogUtils.e("设置我的提问 回答消息条数");
            if (!TextUtils.isEmpty(answerBean.getANSWERCOUNT()) && Integer.valueOf(answerBean.getANSWERCOUNT()) > 0) {
                String infocount = answerBean.getANSWERCOUNT() + "";
                holder.setVisible(R.id.tv_answer_msg,true);
                holder.setText(R.id.tv_answer_msg,infocount);
            } else {
                holder.setVisible(R.id.tv_answer_msg,false);
            }
        }
    }
}
