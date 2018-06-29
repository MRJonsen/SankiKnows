package com.zc.pickuplearn.http;

/**
 * 作者： Jonsen
 * 时间: 2017/3/12 20:32
 * 联系方式：chenbin252@163.com
 */

public interface CommonCallBack<T> {
    void onSuccess(T t);
    void onFailure();
}
