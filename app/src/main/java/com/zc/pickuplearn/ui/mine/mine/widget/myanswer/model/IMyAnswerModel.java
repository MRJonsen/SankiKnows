package com.zc.pickuplearn.ui.mine.mine.widget.myanswer.model;

import com.zc.pickuplearn.beans.AnswerBean;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/12 12:55
 * 联系方式：chenbin252@163.com
 */

public interface IMyAnswerModel {
    /**
     *
     * @param search 检索字段
     * @param index 下标
     * @param callBack 回调
     */
    void getDynamicDatas(String search, int index, List<AnswerBean> bean, ImplMyAnswerModel.GetDynamicDatasCallBack callBack);
    void getAnswerData(ImplMyAnswerModel.GetAnswerDatasCallBack callBack);
}
