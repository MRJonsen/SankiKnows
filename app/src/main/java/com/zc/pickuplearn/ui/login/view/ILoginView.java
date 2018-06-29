package com.zc.pickuplearn.ui.login.view;

import com.zc.pickuplearn.beans.UserBean;

/**
 * 作者： Jonsen
 * 时间: 2016/12/7 13:17
 * 联系方式：chenbin252@163.com
 */

public interface ILoginView {
    /**
     * 显示进度框
     */
    void showProgressDialog();

    /**
     * 隐藏进度框
     */
    void disShowProgressDialog();
    String getAccount();
    String getPsw();
    void enterMain(UserBean userBean);
    void enterChangPsw();
}
