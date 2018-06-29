package com.zc.pickuplearn.ui.dynamic.widget.answerqustion.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.model.ImplQuestionDetailModel;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget.ImageGridAdapter;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.List;

public class AnswerListAdapter extends BaseAdapter {
    private List<AnswerBean> list;
    private Context context;
    private LayoutInflater mInflater;
    private QuestionDetailType mCome;
    private UserBean mUserinfo;
    public AnswerListAdapter(Context context, List<AnswerBean> list, QuestionDetailType comefrom) {
        super();
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mCome = comefrom;
        mUserinfo = DataUtils.getUserInfo();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.answer_question_list_item, null);
            holder.huidaz = (TextView) convertView.findViewById(R.id.answer_question_list_item_answersName);
            holder.lv = (TextView) convertView.findViewById(R.id.answer_question_list_item_answersLV);
            holder.theAnswer = (TextView) convertView.findViewById(R.id.answer_question_list_item_theAnswer);
            holder.time = (TextView) convertView.findViewById(R.id.answer_question_list_item_answersTime);
            holder.bestAnswer = (ImageView) convertView.findViewById(R.id.answer_question_list_item_best_answerImage);
            holder.isExpertAnswer = (TextView) convertView.findViewById(R.id.answer_question_list_item_isExpertAnswer);
            holder.gridView = (GridView) convertView.findViewById(R.id.answer_question_list_item_gridview);
            holder.dianzan = (LinearLayout) convertView.findViewById(R.id.answer_question_list_iten_dianzan);
            holder.dianzanNum = (TextView) convertView.findViewById(R.id.answer_question_list_iten_dianzanNum);
            holder.dianzanIcon = (ImageView) convertView.findViewById(R.id.answer_question_list_iten_dianzaniCON);
            holder.msg = (TextView) convertView.findViewById(R.id.tv_answer_msg);//消息
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final AnswerBean answerBean = list.get(position);
        if (mCome.equals(QuestionDetailType.MyAnswer)&&mUserinfo.getUSERCODE().equals(answerBean.getANSWERUSERCODE())) {//设置消息提示数量
            if (!TextUtils.isEmpty(answerBean.getQUESTIONCOUNT()) && Integer.valueOf(answerBean.getQUESTIONCOUNT()) > 0) {
                String infocount = answerBean.getQUESTIONCOUNT() + "";
                holder.msg.setVisibility(View.VISIBLE);
                holder.msg.setText(infocount);
            } else {
                holder.msg.setVisibility(View.GONE);
            }
        } else if (mCome.equals(QuestionDetailType.MyQuestion)) {
            LogUtils.e("设置我的提问 回答消息条数");
            if (!TextUtils.isEmpty(answerBean.getANSWERCOUNT()) && Integer.valueOf(answerBean.getANSWERCOUNT()) > 0) {
                String infocount = answerBean.getANSWERCOUNT() + "";
                holder.msg.setVisibility(View.VISIBLE);
                holder.msg.setText(infocount);
            } else {
                holder.msg.setVisibility(View.GONE);
            }
        }
        holder.bestAnswer.setVisibility("1".equals(answerBean.getISPASS()) ? View.VISIBLE : View.GONE);
        holder.dianzanIcon.setImageResource("1".equals(answerBean.getISGOOD()) ? R.mipmap.icon_zaned : R.mipmap.icon_zan);
        holder.huidaz.setText(TextUtils.isEmpty(answerBean.getNICKNAME()) ? answerBean.getANSWERUSERNAME() : answerBean.getNICKNAME());
        holder.lv.setText(TextUtils.isEmpty(answerBean.getANSWERLEVEL()) ? "LV1" : "LV" + answerBean.getANSWERLEVEL());
        holder.theAnswer.setText(answerBean.getANSWEREXPLAIN());
        holder.time.setText(DateUtils.getCompareDate(answerBean.getSYSCREATEDATE()));
        holder.dianzanNum.setText(TextUtils.isEmpty(answerBean.getGOODNUMS()) ? "0" : answerBean.getGOODNUMS());
        holder.isExpertAnswer.setText(TextUtils.isEmpty(answerBean.getQACOUNT()) ? "" : answerBean.getQACOUNT() + "追问追答");
        ImageGridAdapter adapter1 = new ImageGridAdapter(context, answerBean.getFILEURL());
        holder.gridView.setAdapter(adapter1);
        holder.dianzan.setTag(position);
        final Holder finalHolder = holder;
        holder.dianzan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.dianzan.setClickable(false);
                String isgood = answerBean.getISGOOD();
                final String answerId = answerBean.getSEQKEY();
                if (!"1".equals(isgood)) {
                    new ImplQuestionDetailModel().goodAnswer(answerBean, new ImplQuestionDetailModel.GoodAnserCallback() {
                        @Override
                        public void onSuccess() {
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
                            finalHolder.dianzan.setClickable(true);
                        }

                        @Override
                        public void onFail() {
                            finalHolder.dianzan.setClickable(true);
                            ToastUtils.showToast(context, "服务器繁忙！");
                        }
                    });
                } else {
                    finalHolder.dianzan.setClickable(true);
                    ToastUtils.showToast(context, "不能重复点赞！");
                }
            }
        });
        return convertView;
    }

    private class Holder {
        TextView huidaz;//回答者
        TextView lv;//等级
        TextView theAnswer;//回答的内容
        TextView time;//时间
        TextView isExpertAnswer;//专家解答
        TextView msg;//消息提示
        GridView gridView;
        ImageView bestAnswer;//最佳答案
        LinearLayout dianzan;//点赞
        TextView dianzanNum;//点赞数
        ImageView dianzanIcon;
    }
}
