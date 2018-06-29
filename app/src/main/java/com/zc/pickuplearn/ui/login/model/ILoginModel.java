package com.zc.pickuplearn.ui.login.model;

/**
 * 作者： Jonsen
 * 时间: 2016/12/7 14:03
 * 联系方式：chenbin252@163.com
 */

public interface ILoginModel {
    void getUserInfo(String account, LoginModelImpl.GetUserInfoCallBack callBack);
    void doLogin(String account, String psw, LoginModelImpl.LoginCallBack loginCallBack);
}
