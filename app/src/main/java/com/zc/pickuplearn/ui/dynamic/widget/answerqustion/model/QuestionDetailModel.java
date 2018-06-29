package com.zc.pickuplearn.ui.dynamic.widget.answerqustion.model;

import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;

/**
 * 作者： Jonsen
 * 时间: 2017/1/13 14:10
 * 联系方式：chenbin252@163.com
 */

public interface QuestionDetailModel {
    void getDynamicDatas(QuestionBean questionBean, final ImplQuestionDetailModel.GetDynamicDatasCallBack callBack);
    void getAnsewListData(QuestionBean bean,ImplQuestionDetailModel.GetAnswerDatasCallBack callBack);
    void goodAnswer(AnswerBean answerBeann, ImplQuestionDetailModel.GoodAnserCallback callback);
    void takeAnswer(AnswerBean answerBean, ImplQuestionDetailModel.TakeAnswerCallback callback);
}
