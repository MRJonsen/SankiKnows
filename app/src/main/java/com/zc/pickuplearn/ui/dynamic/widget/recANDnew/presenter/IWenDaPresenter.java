package com.zc.pickuplearn.ui.dynamic.widget.recANDnew.presenter;

import com.zc.pickuplearn.beans.QuestionBean;

/**
 * 作者： Jonsen
 * 时间: 2017/1/10 15:57
 * 联系方式：chenbin252@163.com
 */

public interface IWenDaPresenter {

    /**
     * 获取数据的逻辑
     * @param from 页面从哪个地方请求
     * @param needmore 是否有加载更多
     */
    void loadDynamicDatas(String from,boolean needmore,QuestionBean bean);
}
