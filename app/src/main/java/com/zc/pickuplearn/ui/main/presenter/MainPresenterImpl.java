package com.zc.pickuplearn.ui.main.presenter;

import com.zc.pickuplearn.ui.main.view.IMainView;

/**
 * 作者： Jonsen
 * 时间: 2016/12/6 14:48
 * 联系方式：chenbin252@163.com
 */

public class MainPresenterImpl implements IMainPresenter {
    private IMainView mView;
    public MainPresenterImpl(IMainView view) {
        mView = view;
    }

    @Override
    public void changeFragment(int index) {
        mView.changeFragment(index);
    }

    @Override
    public void setNewMessageNum(int tab_index, int number) {
        mView.setMessageNumber(tab_index,number);
    }

    @Override
    public void setNewMessage(int tab_index,boolean hasNewMessage) {
        mView.setMessagePoint(tab_index,hasNewMessage);
    }
}
