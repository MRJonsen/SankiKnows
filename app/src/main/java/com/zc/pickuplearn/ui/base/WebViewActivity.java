package com.zc.pickuplearn.ui.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.ui.district_manager.CourseCollectActivity;
import com.zc.pickuplearn.ui.district_manager.CourseDetailActivity;
import com.zc.pickuplearn.ui.district_manager.QuestionBankActivity;
import com.zc.pickuplearn.ui.question_and_answer.DynamicActivity;
import com.zc.pickuplearn.ui.question_and_answer.DynamicType;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.TinyWebView;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.StatusBarUtil;
import com.zc.pickuplearn.utils.SystemUtils;

import java.io.File;
import java.util.HashMap;

/**
 * 类描述：WebView加载网络资源
 */
public class WebViewActivity extends AppCompatActivity {
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private HeadView webviewTitle;
    private TinyWebView progressWebview;
    private String title;
    private String webViewUrl;
    private String htmlcontent;
    private boolean needBar = true;
    private boolean need_back = false;
    private HashMap<String, String> param;


    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view);
        View root = findViewById(R.id.root);
        //处理状态栏及间距
        StatusBarUtil.darkMode(this);
        StatusBarUtil.setPaddingSmart(this, root);
        getData();
        initViews();
        loadData();
        setWebviewCallFile();
    }

    private void setWebviewCallFile() {
        if (progressWebview != null) {
            progressWebview.setFileCallListener(new TinyWebView.WebCall() {
                @Override
                public void fileChose(ValueCallback<Uri> uploadMsg) {
                    openFileChooserImpl(uploadMsg);
                }

                @Override
                public void fileChose5(ValueCallback<Uri[]> uploadMsg) {
                    openFileChooserImplForAndroid5(uploadMsg);
                }
            });
        }
    }


    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "图片选择"),
                FILECHOOSER_RESULTCODE);
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");

        startActivityForResult(chooserIntent,
                FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    /**
     * web请求资源文件
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null
                    : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    /**
     * 方法描述：接收数据
     */
    private void getData() {
        webViewUrl = getIntent().getStringExtra("webview_url");
        title = getIntent().getStringExtra("webview_title");
        needBar = getIntent().getBooleanExtra("need_bar", true);
        need_back = getIntent().getBooleanExtra("need_back", false);
        htmlcontent = getIntent().getStringExtra("html_content");
        param = (HashMap<String, String>) getIntent().getSerializableExtra("param");

    }

    /**
     * 方法描述：初始化WebView
     */
    @SuppressLint("AddJavascriptInterface")
    private void initViews() {
        progressWebview = (TinyWebView) findViewById(R.id.progress_webview);
        setCacheDir();
        webviewTitle = (HeadView) findViewById(R.id.hv_headbar);
        webviewTitle.setLeftBackClickListener(new HeadView.LeftbackclickListener() {
            @Override
            public void onLeftBackClick() {
                if (progressWebview.canGoBack()) {
                    progressWebview.goBack();//返回上一页面
                } else {
                    progressWebview.loadUrl("javascript:saveExamCase1()");
                    finish();
                }
            }
        });
        if (!needBar) {
            webviewTitle.setVisibility(View.GONE);
        }
        //web资源
        if (!TextUtils.isEmpty(webViewUrl)) {
            if (param != null && param.size() > 0) {
                LogUtils.e("qingqiutouwangye");
                progressWebview.loadUrl(webViewUrl, param);
            } else {

                progressWebview.loadUrl(webViewUrl);
            }
        }
        if (!TextUtils.isEmpty(htmlcontent)) {
            progressWebview.getSettings().setTextSize(WebSettings.TextSize.LARGER);
            progressWebview.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
        }
        progressWebview.setUrlChangeListener(new TinyWebView.OnUrlChangeListener() {
            @Override
            public void onUrlChange(String url) {
                LogUtils.e("url变化" + url);
                if (url.contains("javasscriptss:back")) {
                    finish();
                }
            }
        });
        progressWebview.addJavascriptInterface(new ActionFromJs(), "ActionFromJs");
    }

    private void setCacheDir() {
        progressWebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置 缓存模式
        // 开启 DOM storage API 功能
        progressWebview.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        progressWebview.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
//      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        //设置数据库缓存路径
        progressWebview.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        progressWebview.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        progressWebview.getSettings().setAppCacheEnabled(true);
    }

    public void clearWebViewCache() {

        //清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

    LeftbackclickListener mLeftBackListener;

    public interface LeftbackclickListener {
        void onLeftBackClick();
    }

    public void setLeftBackClickListener(LeftbackclickListener listener) {
        mLeftBackListener = listener;
    }

    /**
     * 方法描述：加载数据
     */
    private void loadData() {
        if (!TextUtils.isEmpty(title))
            webviewTitle.setTitle(title);

        if (TextUtils.isEmpty(webViewUrl))
            progressWebview.loadUrl(webViewUrl);
    }

    /**
     * 方法描述：改写物理按键——返回的逻辑
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (progressWebview.canGoBack()) {
                String url1 = progressWebview.getUrl();
                if (url1.contains("isback=2")) {
                    finish();
                    return true;
                }
                progressWebview.goBack();//返回上一页面
                return true;
            } else {
                if (need_back) {
                    //此处处理能力题库返回时 保存方法
                    String url = progressWebview.getUrl();
                    if (url.contains("javasscriptss:back")) {
                        finish();
                        return true;
                    } else if (url.contains("isback=2")) {
                        finish();
                        return true;
                    } else {
                        progressWebview.loadUrl("javascript:Common.getSave()");
                        return false;
                    }
                } else {
                    if (progressWebview.canGoBack()) {
                        progressWebview.goBack();//返回上一页面
                        return true;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 方法描述：跳转至本Activity
     *
     * @param activity     发起跳转的Activity
     * @param webviewUrl   WebView的url
     * @param webviewTitle WebView页面的标题
     */
    public static void skip(Context activity, String webviewUrl, String webviewTitle, boolean needTitlebar) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("webview_url", webviewUrl);
        intent.putExtra("webview_title", webviewTitle);
        intent.putExtra("need_bar", needTitlebar);
        activity.startActivity(intent);
    }

    /**
     * 方法描述：跳转至本Activity
     *
     * @param activity     发起跳转的Activity
     * @param webviewUrl   WebView的url
     * @param webviewTitle WebView页面的标题
     */
    public static void skip(Context activity, String webviewUrl, String webviewTitle, boolean needTitlebar, boolean needback) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("webview_url", webviewUrl);
        intent.putExtra("webview_title", webviewTitle);
        intent.putExtra("need_bar", needTitlebar);
        intent.putExtra("need_back", needback);
        activity.startActivity(intent);
    }

    /**
     * 方法描述：跳转至本Activity
     *
     * @param activity     发起跳转的Activity
     * @param webviewUrl   WebView的url
     * @param webviewTitle WebView页面的标题
     */
    public static void skip(Context activity, String webviewUrl, String webviewTitle, boolean needTitlebar, HashMap<String, String> head) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("webview_url", webviewUrl);
        intent.putExtra("webview_title", webviewTitle);
        intent.putExtra("need_bar", needTitlebar);
        intent.putExtra("param", head);
        activity.startActivity(intent);
    }

    /**
     * 方法描述：跳转至本Activity
     *
     * @param activity     发起跳转的Activity
     * @param content      WebView的html片段
     * @param webviewTitle WebView页面的标题
     */
    public static void skip(Context activity, String content, String webviewTitle) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("webview_title", webviewTitle);
        intent.putExtra("html_content", content);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressWebview != null) {
            clearWebViewCache();
            CookieSyncManager.createInstance(this);
            CookieManager.getInstance().removeAllCookie();
            progressWebview.clearCache(true);
            progressWebview.destroy(); //解决结束webview 页面 视频 和 音频还在播放的问题
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String is_wifi = msg.arg1 == 1 ? "wifi" : "";
                    LogUtils.e("js调用", is_wifi + msg.arg1);
                    progressWebview.loadUrl("javascript:showMessageFromWKWebViewResult(" + is_wifi + ")");
                    break;
            }

        }
    };

    public class ActionFromJs {
        @JavascriptInterface
        public void close() {
            finish();
        }


        @JavascriptInterface
        public void jump(String msg) {
            LogUtils.e(msg);
            if (!TextUtils.isEmpty(msg)){
                try {
                    CourseWareBean.DatasBean datasBean = JsonUtils.parseJson2Object(msg, new TypeToken<CourseWareBean.DatasBean>() {
                    });
                    CourseDetailActivity.open(WebViewActivity.this, datasBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @JavascriptInterface
        public void getNetwork() {
            boolean wifi = SystemUtils.isWifi(WebViewActivity.this);
            int is_wifi = wifi ? 1 : 2;
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.arg1 = is_wifi;
                handler.sendMessage(message);
            }
        }

        @JavascriptInterface
        public void openCourseCollectActivity() {
            CourseCollectActivity.open(WebViewActivity.this);
        }
        @JavascriptInterface
        public void openQuestionBankActivity() {
            QuestionBankActivity.open(WebViewActivity.this);
        }
        @JavascriptInterface
        public void openDynamicActivity() {
            DynamicActivity.open(WebViewActivity.this, DynamicType.CourseWareHome);
        }
    }
}
