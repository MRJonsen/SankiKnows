package com.zc.pickuplearn.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者： Jonsen
 * 时间: 2016/11/30 11:15
 * 联系方式：chenbin252@163.com
 */

public abstract class EventBusBaseFragment extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
