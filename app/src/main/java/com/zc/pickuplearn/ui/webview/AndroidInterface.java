package com.zc.pickuplearn.ui.webview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.just.library.AgentWeb;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.ui.district_manager.CourseCollectActivity;
import com.zc.pickuplearn.ui.district_manager.QuestionBankActivity;
import com.zc.pickuplearn.ui.question_and_answer.DynamicActivity;
import com.zc.pickuplearn.ui.question_and_answer.DynamicType;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.SystemUtils;

/**
 * Created on 2017/5/14.
 *  js调用原声接口
 */

public class AndroidInterface {
    private Handler deliver = new Handler(Looper.getMainLooper());
    private AgentWeb agent;
    private Context context;

    public AndroidInterface(AgentWeb agent, Context context) {
        this.agent = agent;
        this.context = context;
    }



    @JavascriptInterface
    public void callAndroid(final String msg) {


        deliver.post(new Runnable() {
            @Override
            public void run() {
                Log.i("Info", "main Thread:" + Thread.currentThread());
                Toast.makeText(context.getApplicationContext(), "" + msg, Toast.LENGTH_LONG).show();
            }
        });


        Log.i("Info", "Thread:" + Thread.currentThread());

    }

    @JavascriptInterface
    public void jump(String msg) {
        LogUtils.e(msg);
    }

    @JavascriptInterface
    public void close(final String msg) {
        deliver.post(new Runnable() {
            @Override
            public void run() {
                ((AppCompatActivity)context).finish();
            }
        });


        Log.i("Info", "Thread:" + Thread.currentThread());

    }
    @JavascriptInterface
    public void getNetwork(){
        deliver.post(new Runnable() {
            @Override
            public void run() {
                String is_wifi = SystemUtils.isWifi(context)?"wifi":"";
//                agent.getJsEntraceAccess().quickCallJs("javascript:showMessageFromWKWebViewResult("+is_wifi+")");
                agent.getJsEntraceAccess().quickCallJs("showMessageFromWKWebViewResult",is_wifi);
            }
        });

    }

    @JavascriptInterface
    public void openCourseCollectActivity() {
        CourseCollectActivity.open(context);
    }
    @JavascriptInterface
    public void openQuestionBankActivity() {
        QuestionBankActivity.open(context);
    }
    @JavascriptInterface
    public void openDynamicActivity() {
        DynamicActivity.open(context, DynamicType.CourseWareHome);
    }
}
