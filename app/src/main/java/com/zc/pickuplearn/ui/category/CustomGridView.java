package com.zc.pickuplearn.ui.category;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义GridView
 * 
 */
public class CustomGridView extends GridView {

	public CustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
