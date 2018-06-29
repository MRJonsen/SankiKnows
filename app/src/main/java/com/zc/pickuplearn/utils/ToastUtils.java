package com.zc.pickuplearn.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 作者： Jonsen
 * 时间: 2016/11/29 14:29
 * 联系方式：chenbin252@163.com
 */

public class ToastUtils {
    private static Toast mToast = null;
    private static Toast mToastIncenter = null;

    public static void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                mToast.show();
            }
        });
    }

    // 在屏幕中间的toast
    public static void showToastCenter(Context context, String text) {
        if (mToastIncenter == null) {
            mToastIncenter = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mToastIncenter.setGravity(Gravity.CENTER, 0, 0);
            mToastIncenter.setDuration(Toast.LENGTH_SHORT);
        } else {
            mToastIncenter.setText(text);
        }
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                mToastIncenter.show();
            }
        });
    }

    /**
     * 显示吐司信息（较长时间）
     *
     * @param context
     * @param text
     */
    public static void displayTextLong(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示吐司信息（较短时间）
     *
     * @param context
     * @param text
     */
    public static void displayTextShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示吐司信息交给handler处理（较长时间）
     *
     * @param context
     * @param text
     * @param handler
     */
    public static void displayTextLong2Handler(final Context context,
                                               final String text, Handler handler) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.displayTextLong(context, text);
            }
        });
    }

    /**
     * 显示吐司信息交给handler处理（较短时间）
     *
     * @param context
     * @param text
     * @param handler
     */
    public static void displayTextShort2Handler(final Context context,
                                                final String text, Handler handler) {

        handler.post(new Runnable() {

            @Override
            public void run() {
                ToastUtils.displayTextShort(context, text);
            }
        });
    }
}
