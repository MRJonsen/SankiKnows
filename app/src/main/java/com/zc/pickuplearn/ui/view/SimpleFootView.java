package com.zc.pickuplearn.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zc.pickuplearn.R;

/**
 * Created by chenbin on 2017/8/15.
 */
public class SimpleFootView extends RelativeLayout {

    public SimpleFootView(Context context) {
        super(context);
        init(context);
    }

    public SimpleFootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleFootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.sample_footer, this);
    }
}