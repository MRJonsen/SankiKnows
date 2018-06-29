package com.zc.pickuplearn.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.zc.pickuplearn.R;

/**
 * @author bin E-mail: chenbin252@163.COM
 * @version 创建时间：2016-11-2 上午10:18:35
 * @Describe
 */
public class ExpandableTextView extends TextView {
	private static final int DEFAULT_TRIM_LENGTH = 80;
	private static final String ELLIPSIS = "更多...";

	private CharSequence originalText;
	private CharSequence trimmedText;
	private BufferType bufferType;
	private boolean trim = true;
	private int trimLength;

	public ExpandableTextView(Context context) {
		this(context, null);
	}

	public ExpandableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.ExpandableTextView);
		this.trimLength = typedArray.getInt(
				R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH);
		typedArray.recycle();

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setOnclick();
			}
		});
	}

	/**
	 * 此方法用于打开和收起文字
	 */
	public void setOnclick() {
		trim = !trim;
		setText();
		requestFocusFromTouch();
	}

	private void setText() {
		super.setText(getDisplayableText(), bufferType);
	}

	private CharSequence getDisplayableText() {
		return trim ? trimmedText : originalText;
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		originalText = text;
		trimmedText = getTrimmedText(text);
		bufferType = type;
		setText();
	}

	private CharSequence getTrimmedText(CharSequence text) {
		if (originalText != null && originalText.length() > trimLength) {
			return new SpannableStringBuilder(originalText, 0, trimLength + 1)
					.append(ELLIPSIS);
		} else {
			return originalText;
		}
	}

	public CharSequence getOriginalText() {
		return originalText;
	}

	public void setTrimLength(int trimLength) {
		this.trimLength = trimLength;
		trimmedText = getTrimmedText(originalText);
		setText();
	}

	public int getTrimLength() {
		return trimLength;
	}
}
