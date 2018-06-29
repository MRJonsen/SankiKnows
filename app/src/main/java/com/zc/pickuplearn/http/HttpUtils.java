package com.zc.pickuplearn.http;


import android.content.Context;

import com.zc.pickuplearn.utils.PictureUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.CookieStore;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * 网络访问封装类
 * 作者： Jonsen
 * 时间: 2016/11/9 9:08
 * 联系方式：chenbin252@163.com
 */
public class HttpUtils {

    private static CookieStore cookieStore;

    /**
     * 在application中 初始化okhttp配置
     *
     * @param context 上下文环境
     */
    public static void initOkhttpConfig(Context context) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);//可访问所有https网站
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(context));
        cookieStore = cookieJar.getCookieStore();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)//持久化cookie 包括session
//                .addNetworkInterceptor(new LogInterceptor("网络",true))//日志拦截
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private static HttpUtils mInstance;

    private HttpUtils() {
    }

    /**
     * 获取单例
     */
    private static HttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 清除cookies
     */
    public static void clearCookies() {
        if (cookieStore != null) {
            cookieStore.removeAll();
        }
    }

    /**
     * post请求方法
     *
     * @param url        请求地址
     * @param jsonObject 请求体
     * @param callback   回调
     * @param <T>        泛型
     */
    public static <T> void doPost(String url, JSONObject jsonObject, Callback<T> callback) {
        HttpUtils.getInstance().post(url, jsonObject, callback);

    }

    /**
     * post请求方法
     *
     * @param url      请求地址
     * @param files    请求体
     * @param callback 回调
     * @param <T>      泛型
     */
    public static <T> void doPostFile(String url, JSONObject jsonobject, List<String> files, Callback<T> callback) {
        if (files == null || files.isEmpty()) {
            doPost(url, jsonobject, callback);
        } else {
            HttpUtils.getInstance().postFile(url, jsonobject, files, callback);
        }
    }

    /**
     * 下载文件方法
     *
     * @param url      请求地址
     * @param callBack 回调
     */
    public static RequestCall doDownFile(String url, FileCallBack callBack) {
        return HttpUtils.getInstance().downFile(url, callBack);
    }


    /**
     * post请求方法
     *
     * @param url      请求地址
     * @param object   请求体
     * @param callback 回调
     * @param <T>      泛型
     */
    private <T> RequestCall post(String url, JSONObject object, final Callback<T> callback) {
        RequestCall build = OkHttpUtils
                .postString()
                .url(url)
                .content(object.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build();
        build.execute(callback);
        return build;
    }

    private <T> void postFile(String url, JSONObject json, List<String> files, Callback<T> callback) {
        PostFormBuilder post = OkHttpUtils
                .post();

        int i = 0;
        ArrayList<String> strings = new ArrayList<>();
        for (String path : files) {
            try {
                String s = PictureUtil.bitmapToPath(path);
                strings.add(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String path : strings) {
            i++;
            File file = new File(path);
            post.addFile(i + "", path.substring(path.lastIndexOf("/")), file);
        }

        post
                .url(url)
                .addParams("JSONBODY", json.toString())
                .build()
                .execute(callback);
    }

    private RequestCall downFile(String url, FileCallBack callBack) {
        RequestCall build = OkHttpUtils
                .get()
                .url(url)
                .build();
        build.execute(callBack);
        return build;
    }

    /**
     * 一般的post请求
     *
     * @param url      链接
     * @param params   参数
     * @param callback 回调
     * @param <T>      泛型
     */
    public static <T> void doCommenPost(String url, Map<String, String> params, Callback<T> callback) {
        OkHttpUtils
                .post()
                .url(url)
                .params(params)
                .build()
                .execute(callback);
    }


}
