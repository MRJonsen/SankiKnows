package com.zc.pickuplearn.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;



/**
 * 屏蔽viewpager滑动事件
 * Created by chenbin on 2017/9/4.
 */

public class NoScrollViewPager extends me.majiajie.pagerbottomtabstrip.LazyViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}
