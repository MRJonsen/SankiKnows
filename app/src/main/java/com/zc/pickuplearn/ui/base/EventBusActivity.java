package com.zc.pickuplearn.ui.base;

import android.os.Bundle;

import com.zc.pickuplearn.event.FinishEvent;
import com.zc.pickuplearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 这侧
 * 作者： Jonsen
 * 时间: 2017/1/4 9:58
 * 联系方式：chenbin252@163.com
 */

public abstract class EventBusActivity extends BaseActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(setLayout());
//        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
//        initView();
//        initData();
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

//    /**
//     * @return 传入布局id
//     */
//    public abstract int setLayout();
//
//    /**
//     * 初始化activity数据
//     */
//    public abstract void initView();
//
//    /**
//     * 初始化activity数据
//     */
//    protected abstract void initData();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册eventbus
    }

    @Subscribe
    public void onEventMainThread(FinishEvent event) {
        if (event != null) {
            finish();
        }
    }

    public void  showToast(String msg){
        ToastUtils.showToastCenter(this,msg);
    }
}
