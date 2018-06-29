package com.zc.pickuplearn.ui.question;

import com.zc.pickuplearn.beans.QuestionBean;

/**
 * 事件消息
 * 作者： Jonsen
 * 时间: 2017/3/8 10:39
 * 联系方式：chenbin252@163.com
 */

public class ChangeQustionEvent {
    private QuestionBean questionBean;

    public QuestionBean getQuestionBean() {
        return questionBean;
    }

    public void setQuestionBean(QuestionBean questionBean) {
        this.questionBean = questionBean;
    }
}
