package com.zc.pickuplearn.ui.login.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.youth.xframe.common.XActivityStack;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.login.presenter.LoginPresenterImpl;
import com.zc.pickuplearn.ui.main_pick_up_learn.PULearnMainActivity;
import com.zc.pickuplearn.ui.mine.changepsw.ChangePswPopActivity;
import com.zc.pickuplearn.ui.view.ClearEditText;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.utils.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements ILoginView {


    /**
     * 打开登录页面的方法
     *
     * @param context 上下文
     */
    public static void startLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        XActivityStack.getInstance().finishOthersActivity(LoginActivity.class);//关闭除其他页面
        HttpUtils.clearCookies();//清除所有cookie
    }

    @BindView(R.id.et_jn)
    ClearEditText mAccount;//账号框
    @BindView(R.id.et_pwd)
    ClearEditText mPWD; //密码框
    @BindView(R.id.bt_login)
    Button mLogin; //登录按钮
    private ProgressDialog dialog; //进度框
    private LoginPresenterImpl loginPresenter;

    @Override
    public int setLayout() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        loginPresenter = new LoginPresenterImpl(this);
        mAccount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    mPWD.requestFocus();
                    return true;
                }
                return false;
            }
        });
        mPWD.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getId() == R.id.et_pwd && EditorInfo.IME_ACTION_GO == actionId) {
                    loginPresenter.doLogin();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    protected void initData() {
        String account = (String) SPUtils.get(this, SPUtils.USER_ACCOUNT, "");
        String psw = (String) SPUtils.get(this, SPUtils.USRE_PSW, "");
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(psw)) {
            mAccount.setText(account);
            mPWD.setText(psw);
        }
    }

    @Override
    public void showProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog == null) {
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setCanceLable(false);
                    dialog.setMsg("登录中...");
                }
                dialog.showProgressDialog();

            }
        });
    }

    @Override
    public void disShowProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.dissMissProgressDialog();
                }
            }
        });
    }

    @Override
    public String getAccount() {
        return mAccount.getText().toString().trim();
    }

    @Override
    public String getPsw() {
        return mPWD.getText().toString().trim();
    }

    @Override
    public void enterMain(UserBean userBean) {
        PULearnMainActivity.startActivityMain(this, userBean);
    }

    @Override
    public void enterChangPsw() {
        startActivity(new Intent(this, ChangePswPopActivity.class));
    }

    @OnClick(R.id.bt_login)
    public void onClick(View view) {
        loginPresenter.doLogin();
    }

}
