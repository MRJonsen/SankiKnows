package com.zc.pickuplearn.ui.welcome.view;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.login.view.LoginActivity;
import com.zc.pickuplearn.ui.main_pick_up_learn.PULearnMainActivity;
import com.zc.pickuplearn.ui.welcome.presenter.SplashPresenterImpl;
import com.zc.pickuplearn.utils.LogUtils;

/**
 * 启动界面
 * 1.登录获取最新cookie
 * 2.跳转  未登录--》登录   登录--》主页
 */
public class SplashActivity extends BaseActivity implements ISplashView {

    private SplashPresenterImpl splashPresenter;

    @Override
    public int setLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        splashPresenter = new SplashPresenterImpl(this);
    }

    @Override
    protected void initData() {
        splashPresenter.goView();
    }

    @Override
    public void enterMain(final UserBean userBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PULearnMainActivity.startActivityMain(SplashActivity.this, userBean);
            }
        });
    }

    @Override
    public void enterLogin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginActivity.startLogin(SplashActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("SplashDestroy");
    }
}
