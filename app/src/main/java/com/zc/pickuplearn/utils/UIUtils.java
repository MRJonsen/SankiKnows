package com.zc.pickuplearn.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.zc.pickuplearn.application.MyAppliction;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.WebViewActivity;

/**
 * 作者： Jonsen
 * 时间: 2016/11/29 14:32
 * 联系方式：chenbin252@163.com
 */

public class UIUtils {
    /**提供获取上下环境方法*/
    public static Context getContext() {
        return MyAppliction.getContext();
    }
    /**
     * @return	全局的hander对象
     */
    public static Handler getHandler(){
        return MyAppliction.getHandler();
    }

    /**
     * @return	返回主线程方法
     */
    public static Thread getMainThread(){
        return MyAppliction.getMainThread();
    }
    /** xml--->view*/
    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    // 获取资源文件夹
    public static Resources getResources() {
        return getContext().getResources();
    }

    // 获取string操作
    public static String getString(int stringId) {
        return getResources().getString(stringId);
    }

    // 获取stringArray数组
    public static String[] getStringArray(int stringArrayId) {
        return getResources().getStringArray(stringArrayId);
    }

    // 获取drawable
    public static Drawable getDrawable(int drawableId) {
        return getResources().getDrawable(drawableId);
    }

    // dip--->px
    public static int dip2px(int dip) {
        float d = getResources().getDisplayMetrics().density;
        return (int) (dip * d + 0.5);
    }

    // px---->dip
    public static int px2dip(int px) {
        float d = getResources().getDisplayMetrics().density;
        return (int) (px / d + 0.5);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px
     */
    public static int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static ColorStateList getColorStateList(int mTabTextColorResId) {
        // 根据颜色选择器id,获取颜色选择器对象的方法
        return getResources().getColorStateList(mTabTextColorResId);
    }
    /**
     * @return	返回主线程id方法
     */
    public static int getMainThreadId(){
        return MyAppliction.getMainThreadId();
    }
    /**
     * @param runnable	将任务保证在主线程中运行的方法
     */
    public static void runInMainThread(Runnable runnable){
        //获取调用此方法所在的线程
        if(android.os.Process.myTid() == getMainThreadId()){
            //如果上诉的runnable就是在主线程中要去执行的任务,则直接运行即可
            runnable.run();
        }else{
            //如果上诉的runnable运行在子线程中,将其传递到主线程中去做执行
            getHandler().post(runnable);
        }
    }

    /**
     * 打开功能链接
     * @param mContext
     * @param functionName
     */
    public static void startActionUrl(final Context mContext, String functionName) {
        API.getFunctionUrl(functionName, new CommonCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                LogUtils.e(s);
                WebViewActivity.skip(mContext, s, "", false);
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
