package com.zc.pickuplearn.event;

import com.zc.pickuplearn.beans.QuestionBean;

/**
 * 获取分类事件
 * Created by chenbin on 2017/11/16.
 */

public class ClassicCaseTypeEvent extends BaseEvent {
    public QuestionBean getQuestionBean() {
        return questionBean;
    }

    public void setQuestionBean(QuestionBean questionBean) {
        this.questionBean = questionBean;
    }

    private QuestionBean questionBean;
    public ClassicCaseTypeEvent(QuestionBean questionBean) {
        this.questionBean = questionBean;
    }
}
