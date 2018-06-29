package com.zc.pickuplearn.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lzy.ninegrid.NineGridView;
import com.tencent.smtt.sdk.QbSdk;
import com.youth.xframe.base.XApplication;
import com.zc.pickuplearn.BuildConfig;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.exception.ExceptionHandler;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.ui.im.chatting.utils.SharePreferenceManager;
import com.zc.pickuplearn.utils.LogUtils;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.im.android.api.JMessageClient;

import static android.content.ContentValues.TAG;

/**
 * 作者： Jonsen
 * 时间: 2016/11/8 21:42
 * 联系方式：chenbin252@163.com
 */
public class MyAppliction extends XApplication {

    private static final String JCHAT_CONFIGS = "JChat_configs";
    //全局的上下文环境
    private static Context mContext;
    // 全局的handler
    private static Handler mHandler;
    // 主线程
    private static Thread mMainThread;
    // 主线程id
    private static int mMainThreadId;
    private static final int MSG_SET_ALIAS = 1001;//别名请求码
    /**
     * 设置别名的回调
     */
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    LogUtils.e(TAG, logs);
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        HttpUtils.initOkhttpConfig(this);//初始化网络配置
        if (!BuildConfig.DEBUG) {
            ExceptionHandler.getInstance().initConfig(this);
//        CrashHandler.getInstance().init(this);//初始化全局异常配置
        }
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();//主線程id
        initJPushIM();
        NineGridView.setImageLoader(new GlideImageLoader());
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.e("-------------------->","内核加载完毕");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.e("-------------------->","内核加载完毕"+b);
            }
        });
    }

    /**
     * 初始化极光配置
     */
    private void initJPushIM() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SET_ALIAS:
                        JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                        break;
                }
                super.handleMessage(msg);
            }
        };

        /*-----------------------------极光初始化--------------------------------------*/
        JPushInterface.setDebugMode(LogUtils.isDebug);// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
        /*-------------------------------------------------------------------------------*/
           /*-----------------------------极光IM--------------------------------------*/
        JMessageClient.setDebugMode(LogUtils.isDebug);// 设置开启日志,发布时请关闭日志
        JMessageClient.init(this, true);
        //设置Notification的模式
        JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_NO_VIBRATE);//即时通讯 通知栏 有消息 有声音 无振动
        SharePreferenceManager.init(this, JCHAT_CONFIGS);
        /*-------------------------------------------------------------------------------*/
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 极光设置别名
     *
     * @param alias 别名(工号)
     */
    public static void setAlias(String alias) {
        //调用JPush API设置Alias
        LogUtils.e("设置别名" + alias);
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    /**
     * nineGrid Glide 加载
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context)
                    .load(url)
                    .fitCenter()
                    .crossFade()
                    .error(R.mipmap.default_img)
                    .placeholder(R.mipmap.default_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
