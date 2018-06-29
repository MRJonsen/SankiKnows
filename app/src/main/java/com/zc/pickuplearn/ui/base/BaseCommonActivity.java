package com.zc.pickuplearn.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.youth.xframe.base.XActivity;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.utils.StatusBarUtil;
import com.zc.pickuplearn.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseCommonActivity extends XActivity {

    private Unbinder bind;
    private ProgressDialog progressDialog;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ButterKnife.bind(this);
        mContext = this;
        initView();
        initData(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return setLayout();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initData();
    }

    /**
     * @return 传入布局id
     */
    public abstract int setLayout();


    /**
     * 初始化activity数据
     */
    protected abstract void initData();


    /**
     * 提示框
     * @param msg
     */
    public void showToast(String msg) {
        ToastUtils.showToastCenter(this, msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }


    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(mContext);
                }
                progressDialog.setCanceLable(false);
                progressDialog.showProgressDialog();
            }
        });
    }
    public void showProgress(final boolean cancel, final String warning) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setCanceLable(cancel);
                }
                progressDialog.setCanceLable(cancel);
                if (!TextUtils.isEmpty(warning)){
                    progressDialog.setMsg(warning);
                }
                progressDialog.showProgressDialog();
            }
        });
    }

    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dissMissProgressDialog();
                }
            }
        });
    }

    public void initStatusBar(View view){
        StatusBarUtil.darkMode(this);
        StatusBarUtil.setPaddingSmart(this,view);
    }
    public void close() {
        finish();
    }
}
