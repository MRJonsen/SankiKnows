package com.zc.pickuplearn.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by chenbin on 2017/11/11.
 */

public class MaxRecycleView extends RecyclerView {


    public MaxRecycleView(Context context) {
        super(context);
    }

    public MaxRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
