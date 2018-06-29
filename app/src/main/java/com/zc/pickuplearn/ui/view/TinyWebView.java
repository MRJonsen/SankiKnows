package com.zc.pickuplearn.ui.view;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.utils.LogUtils;

/**
 * 类描述：一个让其支持App内部显示资源、支持JavaScript、支持显示进度条的WebView
 * Created by lzy on 2016/9/22.
 */

public class TinyWebView extends WebView implements NestedScrollingChild {
    private ProgressBar progressbar;
    OnUrlChangeListener mUrlChangeListener;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private NestedScrollingChildHelper mChildHelper;
    WebCall webCall;

    public TinyWebView(Context context) {
        super(context);
        init();
    }

    public TinyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TinyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgressBar(context);
        openJavaScript();
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebClient());
        init();

    }

    private void init() {
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    private void initProgressBar(Context context) {
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setBackgroundColor(getResources().getColor(R.color.gray));
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(context, 3), 0, 0));
        //改变progressbar默认进度条的颜色（深红色）为Color.GREEN
        progressbar.setProgressDrawable(new ClipDrawable(new ColorDrawable(getResources().getColor(R.color.actionsheet_blue)), Gravity.LEFT, ClipDrawable.HORIZONTAL));
        addView(progressbar);
    }

    /**
     * 方法描述：启用支持javascript
     */
    private void openJavaScript() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setAllowFileAccess(true);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 方法描述：根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 类描述：显示WebView加载的进度情况
     */
    public class WebChromeClient extends android.webkit.WebChromeClient {

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            if (webCall != null)
                webCall.fileChose(uploadMsg);
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // For Android > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            openFileChooser(uploadMsg, acceptType);
        }

        // For Android > 5.0
        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            if (webCall != null)
                webCall.fileChose5(filePathCallback);
            return true;
        }


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);

                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    public class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mUrlChangeListener != null) {
                mUrlChangeListener.onUrlChange(url);
            }
        }
    }

    public interface OnUrlChangeListener {
        void onUrlChange(String url);
    }


    /**
     * 链接变化监听
     *
     * @param listener
     */
    public void setUrlChangeListener(OnUrlChangeListener listener) {
        mUrlChangeListener = listener;
    }

    /**
     * 链接变化监听
     *
     * @param webCall
     */
    public void setFileCallListener(WebCall webCall) {
        this.webCall = webCall;
    }


//    float x1 = 0;
//    float x2 = 0;
//    float y1 = 0;
//    float y2 = 0;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        LogUtils.e("按下"+event.getAction());
//        setNestedScrollingEnabled(false);
//        //继承了Activity的onTouchEvent方法，直接监听点击事件
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            //当手指按下的时候
//            x1 = event.getX();
//            y1 = event.getY();
//        }
//        if (event.getAction()==MotionEvent.ACTION_MOVE){
//            LogUtils.e("移动");
//        }
//        if(event.getAction() == MotionEvent.ACTION_UP) {
//            //当手指离开的时候
//            x2 = event.getX();
//            y2 = event.getY();
//            if(y1 - y2 > 50) {
//                LogUtils.e("向上滑");
//            } else if(y2 - y1 > 50) {
//                LogUtils.e("向下滑");
//            } else if(x1 - x2 > 50) {
//                LogUtils.e("向左滑");
//            } else if(x2 - x1 > 50) {
//                LogUtils.e("向右滑");
//            }
//        }
//        return super.onTouchEvent(event);
//    }


    private float downx;
    private float downy;
    private MotionEvent b;
    private int mLastMotionY;
    private int mLastMotionX;
    private int mNestedYOffset;
    private int mNestedXOffset;
    private boolean mChange;

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        boolean result = false;
//
//        MotionEvent trackedEvent = MotionEvent.obtain(event);
//
//        final int action = MotionEventCompat.getActionMasked(event);
//
//        if (action == MotionEvent.ACTION_DOWN) {
//            mNestedYOffset = 0;
//            mNestedXOffset = 0;
//        }
//
//        int y = (int) event.getY();
//        int x = (int) event.getX();
//
//        event.offsetLocation(mNestedXOffset, mNestedYOffset);
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mLastMotionY = y;
//                mLastMotionX = x;
//                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
//                result = super.onTouchEvent(event);
//                mChange = false;
//                downx = event.getX();
//                downy = event.getY();
//                b = MotionEvent.obtain(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                LogUtils.e("移动");
//                int deltaY = mLastMotionY - y;
//                int deltaX = mLastMotionX - x;
//                if (dispatchNestedPreScroll(deltaX, deltaY, mScrollConsumed, mScrollOffset)) {
//                    deltaY -= mScrollConsumed[1];
//                    deltaX -= mScrollConsumed[0];
//                    trackedEvent.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
//                    mNestedYOffset += mScrollOffset[1];
//                    mNestedXOffset += mScrollOffset[0];
//                }
//                int oldY = getScrollY();
//                int oldX = getScrollX();
//                mLastMotionY = y - mScrollOffset[1];
//                mLastMotionX = x - mScrollOffset[0];
//                int newScrollY = Math.max(0, oldY + deltaY);
//                int newScrollX = Math.max(0, oldX + deltaX);
//                deltaY -= newScrollY - oldY;
//                deltaX -= newScrollX - oldX;
//                if (dispatchNestedScroll(newScrollX-deltaY, newScrollY - deltaY, deltaX, deltaY, mScrollOffset)) {
//                    mLastMotionY -= mScrollOffset[1];
//                    mLastMotionX -= mScrollOffset[0];
//                    trackedEvent.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
//                    mNestedYOffset += mScrollOffset[1];
//                    mNestedXOffset += mScrollOffset[0];
//                }
//                if (mScrollConsumed[0]==0&&mScrollOffset[0]==0){
//
//                }else {
//
//                }
//                if (mScrollConsumed[1] == 0 && mScrollOffset[1] == 0) {
//                    if (mChange) {
//                        mChange = false;
//                        trackedEvent.setAction(MotionEvent.ACTION_DOWN);
//                        super.onTouchEvent(trackedEvent);
//                    } else {
//                        result = super.onTouchEvent(trackedEvent);
//                    }
//                    trackedEvent.recycle();
//                } else {
//                    if (!mChange) {
//                        mChange = true;
//                        super.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0, 0, 0));
//                    }
//
//                }
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                stopNestedScroll();
//                result = super.onTouchEvent(event);
//                break;
//        }
//        return result;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = false;

        MotionEvent trackedEvent = MotionEvent.obtain(event);

        final int action = MotionEventCompat.getActionMasked(event);

        if (action == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }

        int y = (int) event.getY();

        event.offsetLocation(0, mNestedYOffset);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                result = super.onTouchEvent(event);
                mChange = false;
                downx = event.getX();
                downy = event.getY();
                b = MotionEvent.obtain(event);
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.e("移动");
                int deltaY = mLastMotionY - y;
                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1];
                    trackedEvent.offsetLocation(0, mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }
                int oldY = getScrollY();
                mLastMotionY = y - mScrollOffset[1];
                int newScrollY = Math.max(0, oldY + deltaY);
                deltaY -= newScrollY - oldY;
                if (dispatchNestedScroll(0, newScrollY - deltaY, 0, deltaY, mScrollOffset)) {
                    mLastMotionY -= mScrollOffset[1];
                    trackedEvent.offsetLocation(0, mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }
                if (mScrollConsumed[1] == 0 && mScrollOffset[1] == 0) {
                    if (mChange) {
                        mChange = false;
                        trackedEvent.setAction(MotionEvent.ACTION_DOWN);
                        super.onTouchEvent(trackedEvent);
                    } else {
                        result = super.onTouchEvent(trackedEvent);
                    }
                    trackedEvent.recycle();
                } else {
                    if (!mChange) {
                        mChange = true;
                        super.onTouchEvent(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0, 0, 0));
                    }

                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll();
                result = super.onTouchEvent(event);
                break;
        }
        return result;
    }

    // Nested Scroll implements
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                                        int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {

        LogUtils.e("dispatchNestedFling", velocityX + "," + velocityY);
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }


    /**
     * web 请求文件回掉接口
     */
    public interface WebCall {
        void fileChose(ValueCallback<Uri> uploadMsg);

        void fileChose5(ValueCallback<Uri[]> uploadMsg);
    }
}
