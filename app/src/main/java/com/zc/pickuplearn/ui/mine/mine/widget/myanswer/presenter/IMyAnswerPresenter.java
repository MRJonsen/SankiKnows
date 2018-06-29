package com.zc.pickuplearn.ui.mine.mine.widget.myanswer.presenter;

import com.zc.pickuplearn.beans.AnswerBean;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/12 12:53
 * 联系方式：chenbin252@163.com
 */

public interface IMyAnswerPresenter {
    /**
     * 获取数据的逻辑
     * @param from 页面从哪个地方请求
     */
    void loadDynamicDatas(List<AnswerBean> beanList);
    void loadMyAnswerData();
}
