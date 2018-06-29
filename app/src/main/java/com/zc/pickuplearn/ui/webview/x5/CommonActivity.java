package com.zc.pickuplearn.ui.webview.x5;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.zc.pickuplearn.R;


/**
 * Created by cenxiaozhong on 2017/5/23.
 */

public class CommonActivity extends AppCompatActivity {


    private FrameLayout mFrameLayout;
    public static final String TYPE_KEY = "type_key";
    public static final String URL = "URL";
    private FragmentManager mFragmentManager;



    public static void open(Context context,String url){
        Intent intent = new Intent(context, CommonActivity.class);
        intent.putExtra(TYPE_KEY,5);
        intent.putExtra(URL,url);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        mFrameLayout = (FrameLayout) this.findViewById(R.id.container_framelayout);


        int key = getIntent().getIntExtra(TYPE_KEY, -1);
        String stringExtra = getIntent().getStringExtra(URL);
        mFragmentManager = this.getSupportFragmentManager();
        openFragment(key,stringExtra);
    }


    private AgentWebFragment mAgentWebFragment;

    private void openFragment(int key,String url) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = null;


        switch (key) {

            /*Fragment 使用AgenWebt*/
            case 0:
                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
                mBundle.putString(AgentWebFragment.URL_KEY, "http://www.vip.com");
                break;
            case 1:
                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
                mBundle.putString(AgentWebFragment.URL_KEY, "https://h5.m.jd.com/active/download/download.html?channel=jd-msy1");
                break;
            case 2:
                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
                mBundle.putString(AgentWebFragment.URL_KEY, "file:///android_asset/upload_file/uploadfile.html");
                break;
            case 3:
                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
                mBundle.putString(AgentWebFragment.URL_KEY, "file:///android_asset/upload_file/jsuploadfile.html");
                break;
            case 4:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = JsAgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "file:///android_asset/js_interaction/hello.html");

                break;

            case 5:
                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "http://114.55.55.205:8080/emarkspg_nbsjzd/file/2017-08-09/b3bc6784-39b6-4a40-ac93-5313908c4097.mp4");
                mBundle.putString(AgentWebFragment.URL_KEY, url);
//                mBundle.putString(AgentWebFragment.URL_KEY, "http://m.youku.com/video/id_XODEzMjU1MTI4.html");
//                mBundle.putString(AgentWebFragment.URL_KEY, "http://ow365.cn/?i=14153&furl=http://114.55.55.205:8080/emarkspg_nbsjzd/file/2017-11-03/7c6d774d-b793-4382-b989-bb946e13368a.pdf");
                break;
            case 6:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = CustomIndicatorFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "http://www.taobao.com");
                break;
            case 7:
//                ft.add(R.id.container_framelayout, mAgentWebFragment = CustomSettingsFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
//                mBundle.putString(AgentWebFragment.URL_KEY, "http://www.wandoujia.com/apps");
                break;

            case 8:
                ft.add(R.id.container_framelayout, mAgentWebFragment = AgentWebFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
                mBundle.putString(AgentWebFragment.URL_KEY, "file:///android_asset/sms/sms.html");
                break;
            /* 自定义 WebView */
            case 9:
                /*ft.add(R.id.container_framelayout, mAgentWebFragment = CustomWebViewFragment.getInstance(mBundle = new Bundle()), AgentWebFragment.class.getName());
                mBundle.putString(AgentWebFragment.URL_KEY, "");*/
                break;

        }
        ft.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mAgentWebFragment.onActivityResult(requestCode, resultCode, data);
        Log.i("Info", "activity result");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        AgentWebFragment mAgentWebFragment = this.mAgentWebFragment;
        if (mAgentWebFragment != null) {
            FragmentKeyDown mFragmentKeyDown = (FragmentKeyDown) mAgentWebFragment;
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
