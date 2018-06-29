package com.zc.pickuplearn.ui.question_and_answer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.AnswerSubBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.http.ResultStringCommonUtils;

import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class CommentsAdapter extends BaseAdapter {
    private Context context;
    private List<AnswerSubBean.DatasBean> evaluationReplies;
    private QuestionBean mQuestionBean;
    private AnswerBean answerBean;
    public CommentsAdapter(Context context, @NonNull List<AnswerSubBean.DatasBean> answerSubBeen, QuestionBean mQuestionBean, AnswerBean answerBean) {
        this.context = context;
        this.evaluationReplies = answerSubBeen;
        this.mQuestionBean = mQuestionBean;
        this.answerBean = answerBean;
    }

    @Override
    public int getCount() {
        return evaluationReplies.size();
    }

    @Override
    public AnswerSubBean.DatasBean getItem(int position) {
        return evaluationReplies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_answer_reply, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        AnswerSubBean.DatasBean item = getItem(position);
        String questioner = TextUtils.isEmpty(mQuestionBean.getNICKNAME()) ? mQuestionBean.getQUESTIONUSERNAME() : mQuestionBean.getNICKNAME();
        if ("1".equals(mQuestionBean.getISANONYMITY())) {
            questioner="匿名用户";
        }
        String answer = answerBean.getANSWERUSERNAME();
        String text ="";
        if (answerBean.getANSWERUSERCODE().equals(item.getSYSCREATORID())){
            text = answer +"回复"+ questioner+"：";
        }else {
            text = questioner +" 回复 "+ answer+"：";
        }
//        if (!TextUtils.isEmpty(item.getFILEURL())){
            try{
                //问题图片
                ArrayList<ImageInfo> imageInfo = new ArrayList<>();
                List<String> imgurls = ResultStringCommonUtils.splitUrls(item.getFILEURL());
                for (String url : imgurls) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(url);
                    info.setBigImageUrl(url);
                    imageInfo.add(info);
                }
                holder.nineGridView.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
            }catch (Exception e){
                e.printStackTrace();
            }
//        }

        if (position==1){
            holder.total.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(answerBean.getQACOUNT())&&Integer.parseInt(answerBean.getQACOUNT())>2){
                holder.total.setText("共"+answerBean.getQACOUNT()+"条回复 >");
            }
        }else {
            holder.total.setVisibility(View.GONE);
        }

        SpannableString msp = new SpannableString(text+ item.getEXPLAIN());
        msp.setSpan(new ForegroundColorSpan(XFrame.getColor(R.color.question_order)), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.reply.setText(msp);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_reply)
        TextView reply;
        @BindView(R.id.nineGrid)
        NineGridView nineGridView;
        @BindView(R.id.tv_total)
        TextView total;
        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}