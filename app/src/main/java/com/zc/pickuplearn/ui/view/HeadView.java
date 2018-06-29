package com.zc.pickuplearn.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.utils.KeyBoardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * created by bin on 2016.9.27
 */
public class HeadView extends LinearLayout {
    @BindView(R.id.iv_left)
    ImageButton mLeft;
    @BindView(R.id.tv_top)
    TextView mTop;
    @BindView(R.id.bt_right)
    Button mRight;
    @BindView(R.id.ibt_right)
    ImageButton mIRight;
    @BindView(R.id.ibt_collection)
    ImageButton mIBsave;// 收藏按钮
    @BindView(R.id.rl_background)
    RelativeLayout rl_background;
    private LeftbackclickListener mLeftBackListener;
    private myOnClickListener mListener;
    private myLeftIBMClickListener mListener2;
    private mySaveIBClicklistener mListener3;

    public HeadView(Context context) {
        super(context);
        initView(context);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @SuppressLint("NewApi")
    public HeadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(final Context context) {
        // LayoutInflater.from(context).inflate(R.layout.my_head_item, this);
        View view = View.inflate(getContext(), R.layout.my_head_item, this);
        ButterKnife.bind(view);
        // mLeft = (ImageButton) view.findViewById(R.id.iv_left);
        // mTop = (TextView) view.findViewById(R.id.tv_top);
        // mRight = (Button) view.findViewById(R.id.bt_right);
        // mIRight = (ImageButton) view.findViewById(R.id.ibt_right);
        // mIRight.setOnClickListener(this);
//		mLeft.setOnClickListener(this);
//		mRight.setOnClickListener(this);
//		mIBsave.setOnClickListener(this);
    }


    @OnClick({R.id.ibt_collection, R.id.iv_left, R.id.ibt_right, R.id.bt_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                if (mLeftBackListener==null){
                    KeyBoardUtils.closeKeybord(getRootView(), getContext());
                    ((Activity) getContext()).finish();
                }else {
                    mLeftBackListener.onLeftBackClick();
                }
                break;
            case R.id.bt_right:
                if (mListener != null) {
                    KeyBoardUtils.closeKeybord(getRootView(), getContext());
                    mListener.rightButtonClick();
                }
                break;
            case R.id.ibt_right:
                if (mListener2 != null) {
                    mListener2.rightIButtonClick();
                }
                break;
            case R.id.ibt_collection:
                if (mListener3 != null) {
                    mListener3.saveButtonClick();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置左边返回按钮可见
     *
     * @param visable
     */
    public void setLeftBtnVisable(boolean visable) {
        mLeft.setVisibility(visable ? View.VISIBLE : View.INVISIBLE);
    }

    public void setRigthIMBVisable() {
        mIRight.setVisibility(View.VISIBLE);
        mRight.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置收藏按钮是否  收藏了
     */
    public void setRightSave(boolean save) {
        mIBsave.setVisibility(View.VISIBLE);
        if (save) {
            mIBsave.setBackgroundResource(R.mipmap.courseinfo_star_pressed);
        } else {
            mIBsave.setBackgroundResource(R.mipmap.courseinfo_star);
        }
    }

    public void setTitle(String title) {
        if (mTop != null)
            mTop.setText(title);
    }

    public void setRightText(String right) {
        if (mRight != null) {
            mRight.setText(right);
        }
    }

    public void setRightBtVisable(boolean visable){
        if (mRight != null) {
            mRight.setVisibility(visable?VISIBLE:GONE);
        }
    }
    /**
     * 设置右边button儿图片 和点击
     *
     * @param able
     * @param drawable
     */
    public void setRigthIMBSrcAndAble(boolean able, int drawable) {
        mIRight.setClickable(able);
        mIRight.setBackgroundResource(drawable);
    }

    public void setLeftBackClickListener(LeftbackclickListener listener){
        mLeftBackListener = listener;
    }
    public void setMyOnClickListener(myOnClickListener listener) {
        mListener = listener;
    }

    public void setRightIBMOnClickListener(myLeftIBMClickListener listener) {
        mListener2 = listener;
    }

    public void setRightSaveOnClickListener(mySaveIBClicklistener listener) {
        mListener3 = listener;
    }

    public interface myOnClickListener {
        void rightButtonClick();
    }

    public interface myLeftIBMClickListener {
        void rightIButtonClick();

    }

    public void setBarBg(int color){
        rl_background.setBackgroundColor(color);
    }
    public interface mySaveIBClicklistener {
        void saveButtonClick();

    }
    public interface LeftbackclickListener{
        void onLeftBackClick();
    }
}
