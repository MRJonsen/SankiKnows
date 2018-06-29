package com.zc.pickuplearn.ui.login.presenter;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.application.MyAppliction;
import com.zc.pickuplearn.beans.LoginBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.ui.login.model.LoginModelImpl;
import com.zc.pickuplearn.ui.login.view.ILoginView;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.SPUtils;
import com.zc.pickuplearn.utils.ThreadUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * 作者： Jonsen
 * 时间: 2016/12/7 13:26
 * 联系方式：chenbin252@163.com
 */

public class LoginPresenterImpl implements ILoginPresenter {
    private ILoginView mLoginView;
    private LoginModelImpl loginModel;

    public LoginPresenterImpl(ILoginView loginView) {
        mLoginView = loginView;
        loginModel = new LoginModelImpl();
    }

    @Override
    public void doLogin() {
        //1.登录操作 强制修改密码 成功保存账号密码到本地 获取用户信息
        //2.极光登录

        final String account = mLoginView.getAccount();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showToast(UIUtils.getContext(), "请输入工号 !");
            return;
        }
        final String psw = mLoginView.getPsw();
        if (TextUtils.isEmpty(psw)) {
            ToastUtils.showToast(UIUtils.getContext(), "请输入密码!");
            return;
        }
        mLoginView.showProgressDialog();
        loginModel.doLogin(account, psw, new LoginModelImpl.LoginCallBack() {
            @Override
            public void onSuccess(String responese) {
                String datasString = ResultStringCommonUtils.getDatasString(responese, TableName.UUM_USER);
                try {
                    List<LoginBean> userBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<LoginBean>>() {
                    });
                    if ("0".equals(userBeen.get(0).getACCOUNTSTATUS())) {
                        mLoginView.enterChangPsw();
                    } else {
                        SPUtils.put(UIUtils.getContext(), SPUtils.USER_ACCOUNT, account);
                        SPUtils.put(UIUtils.getContext(), SPUtils.USRE_PSW, psw);
                        getUserInfo();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                mLoginView.disShowProgressDialog();
            }
        });
    }

    @Override
    public void getUserInfo() {
        mLoginView.showProgressDialog();
        final String account = mLoginView.getAccount();
        loginModel.getUserInfo(account, new LoginModelImpl.GetUserInfoCallBack() {
            @Override
            public void onSuccess(final List<UserBean> data) {
                ThreadUtils.runOnSubThread(new Runnable() {//极光登录
                    @Override
                    public void run() {
                        LogUtils.e("---------------->极光登录开始了————————————————————————————————");
                        JMessageClient.login(data.get(0).getUSERCODE(), "1234", new BasicCallback() {
                            @Override
                            public void gotResult(final int status, final String desc) {
                                if (status == 0) {
                                    LogUtils.e("LoginController", "status = " + status + desc);
                                    mLoginView.disShowProgressDialog();
//                                    //进入主页
                                    mLoginView.enterMain(data.get(0));
                                    MyAppliction.setAlias(data.get(0).getUSERACCOUNT());//推送设置别名
                                } else {
                                    LogUtils.e("LoginController", "status = " + status + desc);
                                    mLoginView.disShowProgressDialog();
                                    //进入主页
                                    mLoginView.enterMain(data.get(0));
                                    MyAppliction.setAlias(data.get(0).getUSERACCOUNT());//推送设置别名
                                }
                            }
                        });
                    }
                });

            }

            @Override
            public void onFailure() {
                mLoginView.disShowProgressDialog();
            }
        });
    }
}
