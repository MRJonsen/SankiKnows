package com.zc.pickuplearn.ui.dynamic.widget.recANDnew.model;

import com.zc.pickuplearn.beans.QuestionBean;

/**
 * 作者： Jonsen
 * 时间: 2017/1/10 15:58
 * 联系方式：chenbin252@163.com
 */

public interface IWenDaModel {
    /**
     *
     * @param search 检索字段
     * @param index 下标
     * @param from  来源
     * @param needmore 是否有加载更多
     * @param callBack 回调
     */
    void getDynamicDatas(String search, int index, String from, boolean needmore, QuestionBean bean, ImplWenDaModel.GetDynamicDatasCallBack callBack);
    void getAnsewData(QuestionBean bean,final ImplWenDaModel.GetAnswerDatasCallBack callBack);
}
