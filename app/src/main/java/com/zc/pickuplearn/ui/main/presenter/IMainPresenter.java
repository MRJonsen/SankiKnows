package com.zc.pickuplearn.ui.main.presenter;

/**
 * 作者： Jonsen
 * 时间: 2016/12/6 14:46
 * 联系方式：chenbin252@163.com
 */

public interface IMainPresenter {
    void changeFragment(int index);
    void setNewMessageNum(int tab_index, int number);
    void setNewMessage(int tab_index,boolean hasNewMessage);
}
