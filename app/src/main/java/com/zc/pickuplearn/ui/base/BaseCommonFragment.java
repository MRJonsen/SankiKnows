package com.zc.pickuplearn.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youth.xframe.base.XFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者： Jonsen
 * 时间: 2016/11/30 11:15
 * 联系方式：chenbin252@163.com
 */

public abstract class BaseCommonFragment extends XFragment {
    protected boolean isVisible;
    private View view;
    private Context mContext;
    private Unbinder bind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        bind = ButterKnife.bind(this, view);
        mContext= getContext();
        initView();
        return view;
    }

    /**
     * 获取根视图
     */
    public View getRootView() {
        return view;
    }


    /**
     * 在这里实现Fragment数据的懒加载. 需要与viewPage配合使用 否则需要手动设置
     *
     * @param isVisibleToUser 是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {

    }

    protected void onInvisible() {
    }

    /**
     * 获取布局id
     */
    public abstract int getLayoutId();
    /**
     * 初始化布局
     */
    public abstract void initView();
    public Context getmCtx(){
        return mContext;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
