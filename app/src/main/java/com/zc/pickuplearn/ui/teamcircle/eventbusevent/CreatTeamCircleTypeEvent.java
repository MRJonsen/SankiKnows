package com.zc.pickuplearn.ui.teamcircle.eventbusevent;

import com.zc.pickuplearn.ui.category.QusetionTypeBean;

/**
 * 事件消息
 * 作者： Jonsen
 * 时间: 2017/3/8 10:39
 * 联系方式：chenbin252@163.com
 */

public class CreatTeamCircleTypeEvent {
    private QusetionTypeBean questionBean;

    public QusetionTypeBean getQuestionBean() {
        return questionBean;
    }

    public void setQuestionBean(QusetionTypeBean questionBean) {
        this.questionBean = questionBean;
    }
}
