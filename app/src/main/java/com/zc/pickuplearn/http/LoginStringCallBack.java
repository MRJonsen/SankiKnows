package com.zc.pickuplearn.http;

import com.youth.xframe.XFrame;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;

/**
 * 作者： Jonsen
 * 时间: 2016/12/8 9:46
 * 联系方式：chenbin252@163.com
 */

public abstract class LoginStringCallBack extends MyStringCallBack {
    @Override
    public void onError(Call call, Exception e, int id) {
        e.printStackTrace();
        if (e instanceof ConnectException) {
            ToastUtils.showToast(XFrame.getContext(), "网络异常!");
        } else if (e instanceof TimeoutException) {
            ToastUtils.showToast(XFrame.getContext(), "网络连接超时!");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showToast(XFrame.getContext(), "网络连接超时!");
        } else {
            ToastUtils.showToast(XFrame.getContext(), "连接服务器失败!");
        }
        onFailure("");
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            LogUtils.e(response);
            String retMsg = JsonUtils.decoElementASString(response, UrlMethod.RETMSG);
            boolean ret = ResultStringCommonUtils.getRet(retMsg);
            if (ret) {
                onSuccess(response);
            } else {
                ToastUtils.showToast(UIUtils.getContext(), retMsg);
                onFailure(response);
            }
        } catch (Exception e) {
//            ToastUtils.showToast(UIUtils.getContext(),"请登录！");
            e.printStackTrace();
        }
    }

}
