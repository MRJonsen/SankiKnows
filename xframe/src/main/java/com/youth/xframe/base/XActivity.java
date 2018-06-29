package com.youth.xframe.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.youth.xframe.common.XActivityStack;
import com.youth.xframe.utils.permission.XPermission;


public abstract class XActivity extends AppCompatActivity implements ICallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XActivityStack.getInstance().addActivity(this);
        initVariables();
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
//        initView();
//        initData(savedInstanceState);
    }


    /**
     * 初始化传入参数
     */
    public void initVariables() {

    }

    /**
     * Android M 全局权限申请回调
     *
     * @param requestCode  请求码
     * @param permissions  请求权限
     * @param grantResults 请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        XPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XActivityStack.getInstance().finishActivity(this);
    }
}
