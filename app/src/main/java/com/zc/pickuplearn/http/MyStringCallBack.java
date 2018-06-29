package com.zc.pickuplearn.http;

import android.text.TextUtils;

import com.youth.xframe.XFrame;
import com.zc.pickuplearn.ui.login.view.LoginActivity;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author： Jonsen
 * Date: 2016/12/8 9:46
 */

public abstract class MyStringCallBack extends Callback<String> {


    /**
     * UI Thread
     *
     * @param request 请求
     */
    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        LogUtils.e("onBefore" + id, request.body().toString());
    }

    /**
     * thread
     *
     * @param response 响应
     * @param id       请求id
     * @return
     * @throws IOException
     */
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        return response.body().string();
    }


    @Override
    public void onResponse(String response, int id) {
        LogUtils.e("onResponse" + id, response);
        try {
            String retMsg = JsonUtils.decoElementASString(response, UrlMethod.RETMSG);
            boolean ret = ResultStringCommonUtils.getRet(retMsg);
            if (ret) {
                onSuccess(response);
            } else {
                onFailure(response);
            }
        } catch (Exception e) {
            if (!TextUtils.isEmpty(response) && response.contains("<!DOCTYPE html >")) {
                loginTimeOut(!TextUtils.isEmpty(response) && response.contains("<!DOCTYPE html >"));
            }
            onFailure("");
        }
    }


    @Override
    public void onError(Call call, Exception e, int id) {
        e.printStackTrace();
        if (e instanceof ConnectException) {
            ToastUtils.showToast(XFrame.getContext(), "连接异常!");
        } else if (e instanceof TimeoutException) {
            ToastUtils.showToast(XFrame.getContext(), "网络连接超时!");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showToast(XFrame.getContext(), "网络连接超时!");
        } else {
            ToastUtils.showToast(XFrame.getContext(), "连接服务器失败！");
        }
        onFailure("");
    }


    /**
     * UI Thread
     *
     * @param id
     */
    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        LogUtils.e("onAfter" + id);
    }


    public void loginTimeOut(boolean isLogin) {
        try{
            Response response = OkHttpUtils
                    .get()//
                    .url("")//
                    .tag(this)//
                    .build()//
                    .execute();
        }catch(Exception e){
            e.printStackTrace();
        }

        LoginActivity.startLogin(XFrame.getContext());
    }

    public abstract void onSuccess(String response);

    public abstract void onFailure(String error);
}
