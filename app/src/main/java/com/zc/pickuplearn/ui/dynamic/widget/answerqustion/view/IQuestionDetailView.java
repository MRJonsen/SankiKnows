package com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view;

import com.zc.pickuplearn.beans.AnswerBean;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/12 16:19
 * 联系方式：chenbin252@163.com
 */

public interface IQuestionDetailView {
    void disShowAnswerButton();
    void finishActivity();
    void setAnswerData(List<AnswerBean> list);
}
