package com.zc.pickuplearn.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.TinyWebView;
import com.zc.pickuplearn.utils.LogUtils;

import butterknife.BindView;


public class WebViewFragment extends BaseFragment{
    private HeadView webviewTitle;
    @BindView(R.id.progress_webview)
    TinyWebView progressWebview;
    private String webViewUrl;
    private View view;
    private String title;
    private boolean need_title_bar;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_web_view;
    }

    @Override
    public void initView() {
        getData();
        initViews();
        loadData();
    }

    /**
     * 方法描述：接收数据
     */
    private void getData() {
        Bundle arguments = getArguments();
        title = arguments.getString("webview_title");
        webViewUrl = arguments.getString("webview_url");
        need_title_bar = arguments.getBoolean("need_title_bar");
    }

    /**
     * 方法描述：初始化WebView
     */
    private void initViews() {
        view =getRootView();
//        progressWebview = (TinyWebView) view.findViewById(R.id.progress_webview);
        webviewTitle = (HeadView) view.findViewById(R.id.hv_headbar);
        webviewTitle.setTitle(getResources().getString(R.string.tab_tag_school));
        webviewTitle.setLeftBackClickListener(new HeadView.LeftbackclickListener() {
            @Override
            public void onLeftBackClick() {
                webBack();
            }
        });
        if (!need_title_bar){
            webviewTitle.setVisibility(View.GONE);
        }
//        progressWebview.getSettings().setJavaScriptEnabled(true);
        //web资源
        progressWebview.loadUrl(webViewUrl);
    }

    public void webBack() {
        if (progressWebview.canGoBack()) {
            progressWebview.goBack();//返回上一页面
        }else {
            ((Activity)getContext()).finish();
        }
    }

    /**
     * 方法描述：加载数据
     */
    private void loadData() {
        if (!TextUtils.isEmpty(title))
            webviewTitle.setTitle(title);
            webviewTitle.setLeftBtnVisable(true);
        if (TextUtils.isEmpty(webViewUrl))
            progressWebview.loadUrl(webViewUrl);
    }

    /**
     * 方法描述：改写物理按键——返回的逻辑
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LogUtils.e("webview 接收到了");
            if (progressWebview.canGoBack()) {
                progressWebview.goBack();//返回上一页面
                return true;
            }
        }
        return false;
    }

    public WebView getWebView(){
        return progressWebview;
    }
    public void reload(String url){
        webViewUrl = url;
        progressWebview.loadUrl(url);
    }
    /**
     * 方法描述：跳转至本fragment
     *
     * @param webviewUrl   WebView的url
     * @param webviewTitle WebView页面的标题
     */
    public static WebViewFragment newInstance(String webviewUrl, String webviewTitle,boolean needBar) {
        Bundle bundle = new Bundle();
        WebViewFragment instance = new WebViewFragment();
        bundle.putString("webview_url", webviewUrl);
        bundle.putString("webview_title", webviewTitle);
        bundle.putBoolean("need_title_bar",needBar);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressWebview!=null){
            if (progressWebview!=null){
                CookieSyncManager.createInstance(getmCtx());
                CookieManager.getInstance().removeAllCookie();
                progressWebview.clearCache(true);
                progressWebview.destroy(); //解决结束webview 页面 视频 和 音频还在播放的问题
            }
        }
    }
}
