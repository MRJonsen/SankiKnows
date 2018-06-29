package com.zc.pickuplearn.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zc.pickuplearn.R;

/**
 * 作者： Jonsen
 * 时间: 2016/12/14 10:55
 * 联系方式：chenbin252@163.com
 */

public class RecyclerViewGridDivider extends RecyclerView.ItemDecoration {
    private int margin;

    public RecyclerViewGridDivider(Context context) {
        margin = context.getResources().getDimensionPixelSize(R.dimen.classic_case_margin);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }
}

