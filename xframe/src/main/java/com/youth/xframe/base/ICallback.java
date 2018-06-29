package com.youth.xframe.base;

import android.os.Bundle;


public interface ICallback {
    /**
     * 返回布局文件id
     *
     * @return
     */
    int getLayoutId();

    /**
     * 初始化布局文件
     */
    void initView();

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(Bundle savedInstanceState);

}
