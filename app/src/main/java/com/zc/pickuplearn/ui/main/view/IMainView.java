package com.zc.pickuplearn.ui.main.view;

import com.zc.pickuplearn.ui.main.presenter.MainPresenterImpl;

/**
 * 主页fragment界面操作
 * 作者： Jonsen
 * 时间: 2016/12/5 10:21
 * 联系方式：chenbin252@163.com
 */

public interface IMainView {

    /**
     * 切换fragment
     * @param index 索引
     */
    void changeFragment (int index);
    /**
     * 底部消息条数提示
     * @param tab_index 底部导航栏索引
     * @param number    消息条数,0的时候不显示
     */
    void setMessageNumber(int tab_index,int number);


    /**
     * 底部导航栏消息红点
     * @param tab_index tab 索引
     * @param hasNewMessage 是否有新消息
     */
    void setMessagePoint(int tab_index,boolean hasNewMessage);
    MainPresenterImpl getMainPresenterImpl();
}
