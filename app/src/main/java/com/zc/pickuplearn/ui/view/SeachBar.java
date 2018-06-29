package com.zc.pickuplearn.ui.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.utils.KeyBoardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;

/**
 * 搜索栏
 * 作者： Jonsen
 * 时间: 2017/3/10 10:39
 * 联系方式：chenbin252@163.com
 */

public class SeachBar extends LinearLayout {
    @BindView(R.id.et_search)
    ClearEditText mEdit;
    @BindView(R.id.bt_search)
    Button mBtSearch;
    private SearchButtonOnClickListener mListener;
    private Context mContext;
    public SeachBar(Context context) {
        super(context);
        mContext = context;
        initView();
    }


    public SeachBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public SeachBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.search_bar, this);
        ButterKnife.bind(view);
    }

    /**
     * 初始化搜索条儿
     */
    @OnTextChanged(value = R.id.et_search, callback = AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            if (!mBtSearch.isShown()) {
                mBtSearch.setVisibility(View.VISIBLE);
            }
        } else {
            mBtSearch.setVisibility(View.GONE);
        }
    }

    /**
     * 设置提示语
     * @param hint
     */
    public void setSearchEditTextHint(String hint){
        if (mEdit!=null){
            mEdit.setHint(hint);
        }
    }

    public EditText getEditText(){
        return mEdit;
    }
    /**
     * 设置搜索按钮监听
     * @param listener
     */
    public void setSearchButtonOnClickListener(SearchButtonOnClickListener listener) {
        mListener = listener;
    }

    /**
     * 获取搜索文字
     * @return
     */
    public String getSearchText(){
        return mEdit.getText().toString().trim();
    }
    public interface SearchButtonOnClickListener {
        void onClick();
    }

    @OnClick({R.id.bt_search})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search:
                if (mListener != null) {
                    mListener.onClick();
                    mEdit.setText("");
                }
                KeyBoardUtils.closeKeybord(v,mContext);
                break;
        }
    }
}
