package com.zc.pickuplearn.ui.home.presenter;

import com.zc.pickuplearn.beans.ClassicCaseBean;

/**
 * homefragment业务接口
 * 作者： Jonsen
 * 时间: 2016/12/5 15:34
 * 联系方式：chenbin252@163.com
 */

public interface HomePresenter {
    /**
     * 加载经典案例数据
     */
    void loadClassicCaseDatas();

    /**
     * 加载问答数据
     */
    void loadDynamicDatas();

    void enterClassicCaseDetail(ClassicCaseBean classicCaseBean);
    void enterClassicCaseList();
}
