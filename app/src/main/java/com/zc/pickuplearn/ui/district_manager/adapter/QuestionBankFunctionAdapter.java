package com.zc.pickuplearn.ui.district_manager.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.QuestionBankItem2Bean;
import com.zc.pickuplearn.beans.QuestionBankItemBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.base.WebViewActivity;

import java.util.List;

/**
 * Created by chenbin on 2017/12/28.
 */

public class QuestionBankFunctionAdapter extends CommonAdapter<QuestionBankItem2Bean> {
    QuestionBankItemBean.DataBean mParam1;

    public QuestionBankFunctionAdapter(Context context, QuestionBankItemBean.DataBean mParam1, List<QuestionBankItem2Bean> datas) {
        super(context, R.layout.item_question_bank_function, datas);
        this.mParam1 = mParam1;
    }

    @Override
    protected void convert(ViewHolder holder, final QuestionBankItem2Bean questionBankItem2Bean, int position) {
        holder.setText(R.id.tv_function_name, questionBankItem2Bean.getFUNCTIONNAME());
        holder.setText(R.id.tv_function_introduce, questionBankItem2Bean.getACTIONURL());
        final ImageView view = holder.getView(R.id.iv_function_icon);
        ImageLoaderUtil.showImageView(mContext, ResultStringCommonUtils.subUrlToWholeUrl(questionBankItem2Bean.getFUNCTIONURL()), view);
        holder.setOnClickListener(R.id.cv_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (questionBankItem2Bean.getFUNCTIONCODE()) {
                    case "emc6"://实操练习
                        API.getQuestionBankPracticalNorm("PracticalNorm", "module_code", mParam1.getModule_code(), new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                WebViewActivity.skip(mContext, s, "", false, false);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
//                PracticeSpecificationActivity.open(getmCtx());
                        break;
                    case "emc1"://每日一练
                        startDetail("dailyPractice");
                        break;
                    case "emc2"://章节练习
                        startDetail("chapterPractice");
                        break;
                    case "emc3"://模拟考试
                        startDetail("mockExam");
                        break;
                    case "emc4"://随机练习icon_play.png
                        startDetail("randomPractice");
                        break;
                    case "emc5"://难题攻克
                        startDetail("problemTake");
                        break;
                }
            }
        });
    }

    public void startDetail(String methon) {
        API.getQuestionBankDetail(methon, mParam1.getModule_id(), new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                WebViewActivity.skip(mContext, s, "", false, true);
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
