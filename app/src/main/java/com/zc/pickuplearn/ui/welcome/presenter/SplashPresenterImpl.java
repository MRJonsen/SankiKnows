package com.zc.pickuplearn.ui.welcome.presenter;

import android.text.TextUtils;

import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.ui.login.model.LoginModelImpl;
import com.zc.pickuplearn.ui.welcome.view.ISplashView;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.NetUtils;
import com.zc.pickuplearn.utils.SPUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;

/**
 * 作者： Jonsen
 * 时间: 2016/12/8 13:36
 */

public class SplashPresenterImpl implements ISplashPresenter {
    private ISplashView mSplashView;

    public SplashPresenterImpl(ISplashView SplashView) {
        mSplashView = SplashView;
    }

    @Override
    public void goView() {
        if (NetUtils.hasNetwork(UIUtils.getContext())) {
            //1.持久化分类数据
//            DataUtils.GetQuestionTypeList();//初始化问题分类
            //2.登录操作重新获取cookie
            String account = (String) SPUtils.get(UIUtils.getContext(), SPUtils.USER_ACCOUNT, "");
            String psw = (String) SPUtils.get(UIUtils.getContext(), SPUtils.USRE_PSW, "");
            final UserBean userInfo = DataUtils.getUserInfo();
            if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(psw) && userInfo != null) {
                LoginModelImpl loginModel = new LoginModelImpl();
                loginModel.doLogin(account, psw, new LoginModelImpl.LoginCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        //1.持久化分类数据
                        DataUtils.GetQuestionTypeList();//初始化问题分类
                        mSplashView.enterMain(userInfo);
                    }

                    @Override
                    public void onFailure() {
                        mSplashView.enterLogin();
                    }
                });
            } else {
                mSplashView.enterLogin();
            }
        } else {
            mSplashView.enterLogin();
        }
    }
}
