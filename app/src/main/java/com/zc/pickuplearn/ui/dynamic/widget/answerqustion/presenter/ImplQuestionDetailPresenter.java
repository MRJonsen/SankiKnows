package com.zc.pickuplearn.ui.dynamic.widget.answerqustion.presenter;

import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.model.ImplQuestionDetailModel;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view.IQuestionDetailView;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/13 14:06
 * 联系方式：chenbin252@163.com
 */

public class ImplQuestionDetailPresenter implements QuestionDetailPresenter {
    private IQuestionDetailView mView;
    private final ImplQuestionDetailModel model;

    public ImplQuestionDetailPresenter(IQuestionDetailView view) {
        mView = view;
        model = new ImplQuestionDetailModel();
    }

    @Override
    public void getAnswerData(QuestionBean bean) {
        model.getAnsewListData(bean, new ImplQuestionDetailModel.GetAnswerDatasCallBack() {
            @Override
            public void onSuccess(List<AnswerBean> dynamic_data) {
                mView.setAnswerData(dynamic_data);
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
