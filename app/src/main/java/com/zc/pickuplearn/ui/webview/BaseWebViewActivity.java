package com.zc.pickuplearn.ui.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.utils.LogUtils;

public class BaseWebViewActivity extends AppCompatActivity {

    private boolean need_bar;

    public static void open(Context context, String url,boolean needbar){
        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra(URL,url);
        intent.putExtra(NEED_BAR,needbar);
        context.startActivity(intent);
    }

    public static final String TYPE_KEY = "type_key";
    public static final String URL = BaseWebViewActivity.class.getSimpleName()+"_URL";
    public static final String NEED_BAR = BaseWebViewActivity.class.getSimpleName()+"_BAR";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_web_view);
        String url = getIntent().getStringExtra(URL);
        LogUtils.e(url);
        need_bar = getIntent().getBooleanExtra(NEED_BAR,false);
//        int key = getIntent().getIntExtra(TYPE_KEY, -1);
        mFragmentManager = this.getSupportFragmentManager();
        openFragment(url);
    }


    private AgentWebFragment mAgentWebFragment;

    private void openFragment(String url) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;


//        switch (key) {
//
//            /*Fragment 使用AgenWeb*/
//            case 0:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "https://m.vip.com/?source=www&jump_https=1");
//                break;
//            /*下载文件*/
//            case 1:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "https://h5.m.jd.com/active/download/download.html?channel=jd-msy1");
//                break;
//            /*input标签上传文件*/
//            case 2:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "file:///android_asset/upload_file/uploadfile.html");
//                break;
//            /*Js上传文件*/
//            case 3:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "file:///android_asset/upload_file/jsuploadfile.html");
//                break;
//            /*Js*/
//            case 4:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = JsAgentWebFragment.getInstance(mBundle = new Bundle()), JsAgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "file:///android_asset/js_interaction/hello.html");
//                break;
//
//            /*优酷*/
//            case 5:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "http://m.youku.com/video/id_XODEzMjU1MTI4.html");
////                mBundle.putString(AgentWebFragment.URL_KEY, "http://vod.hzzcdz.com/67c74df6e267453dbbeddcd6bdc0f320/a9974c667bcc4d15962214f474554133-5287d2089db37e62345123a1be272f8b.mp4");
////                mBundle.putString(AgentWebFragment.URL_KEY, "https://v.qq.com/x/page/i0530nu6z1a.html");
//                break;
//            /*淘宝*/
//            case 6:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = CustomIndicatorFragment.getInstance(mBundle = new Bundle()), CustomIndicatorFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "https://m.taobao.com/?sprefer=sypc00");
//                break;
//            /*豌豆荚*/
//            case 7:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = CustomSettingsFragment.getInstance(mBundle = new Bundle()), CustomSettingsFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "http://www.wandoujia.com/apps");
//                break;
//
//            /*短信*/
//            case 8:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "file:///android_asset/sms/sms.html");
//                break;
//            /* 自定义 WebView */
//            case 9:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = CustomWebViewFragment.getInstance(mBundle = new Bundle()), CustomWebViewFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "");
//                break;
//            /*回弹效果*/
//            case 10:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = BounceWebFragment.getInstance(mBundle = new Bundle()), BounceWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "http://m.mogujie.com/?f=mgjlm&ptp=_qd._cps______3069826.152.1.0");
//                break;
//
//            /*JsBridge 演示*/
//            case 11:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = JsbridgeWebFragment.getInstance(mBundle = new Bundle()), JsbridgeWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "file:///android_asset/jsbridge/demo.html");
//                break;
//
//            /*SmartRefresh 下拉刷新*/
//            case 12:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = SmartRefreshWebFragment.getInstance(mBundle = new Bundle()), SmartRefreshWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "http://www.163.com/");
//                break;
//
//        }
        ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
        mBundle.putString(AgentWebFragment.URL_KEY,url);
        mBundle.putBoolean(AgentWebFragment.BAR_KEY,need_bar);
        ft.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //一定要保证 mAentWebFragemnt 回调
        mAgentWebFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AgentWebFragment mAgentWebFragment = this.mAgentWebFragment;
        if (mAgentWebFragment != null) {
            FragmentKeyDown mFragmentKeyDown = mAgentWebFragment;
            if (mFragmentKeyDown.onFragmentKeyDown(keyCode, event))
                return true;
            else
                return super.onKeyDown(keyCode, event);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
