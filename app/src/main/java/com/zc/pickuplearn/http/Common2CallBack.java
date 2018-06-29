package com.zc.pickuplearn.http;

/**
 * 作者： Jonsen
 * 时间: 2017/3/12 20:32
 * 联系方式：chenbin252@163.com
 */

public interface Common2CallBack<T,T1> {
    void onSuccess(T t,T1 t1);
    void onFailure();
}
