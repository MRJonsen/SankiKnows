package com.zc.pickuplearn.ui.home.view;

import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.QuestionBean;

import java.util.List;

/**
 * 主页界面界面操作
 * 作者： Jonsen
 * 时间: 2016/12/5 13:38
 * 联系方式：chenbin252@163.com
 */

public interface IHomeView {

    /**
     *  刷新控件的结束
     */
    void disShowRefreshProgress();
    void addClassCaseData(List<ClassicCaseBean> list);
    void addDynamicData(List<QuestionBean> list);
    String getSearchText();
    /**
     * 设置签到天数
     * @param num
     */
    void setSignNum(String num);

    /**
     * 设置帮助人数
     * @param num
     */
    void setHelpNum(String num);

    /**
     * 隐藏签到布局
     */
    void closeSignLayout();

    /**
     * 跳转经典案咧列表页面
     */
    void enterClassicCaseListView();

    /**
     * 跳转经典案咧详情页面
     */
    void enterClassicCaseDetailView(ClassicCaseBean classicCaseBean);
    void clearSearchString();
}
