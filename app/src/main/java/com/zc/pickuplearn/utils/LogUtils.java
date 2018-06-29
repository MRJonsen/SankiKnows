package com.zc.pickuplearn.utils;

/**
 * 日志相关管理类
 * Created by cb on 2015/11/25.
 */


import android.util.Log;

import com.youth.xframe.utils.log.XLog;
import com.zc.pickuplearn.BuildConfig;

public class LogUtils {

	private static final String TAG = "-------------------->"; // 默认的Tag
	public static boolean isDebug = BuildConfig.DEBUG;// 是否需要打印bug，可以在application的onCreate函数里面初始化
//	public static boolean isDebug = false;// 是否需要打印bug，可以在application的onCreate函数里面初始化

	private LogUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (isDebug){
			Log.i(TAG, msg);
		}
	}

	public static void d(String msg) {
		if (isDebug){			
			Log.d(TAG, msg);
		}
	}

	public static void e(String msg) {
		if (isDebug){
			Log.e(TAG, msg);
		}
	}

	public static void v(String msg) {
		if (isDebug) {
			Log.v(TAG, msg);
		}
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if (isDebug){			
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isDebug){			
			Log.d(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isDebug){			
			Log.e(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (isDebug){			
			Log.v(tag, msg);
		}
	}
	public static void json(String msg) {
		if (isDebug){
			XLog.json(msg);
		}
	}
}
