package com.zc.pickuplearn.http;

import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.zc.pickuplearn.beans.SourceBean;
import com.zc.pickuplearn.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回结果统一处理类 针对公司后台的数据格式统一处理 解析到datas层
 * 作者： Jonsen
 * 时间: 2016/11/29 15:52
 * 联系方式：chenbin252@163.com
 */

public class ResultStringCommonUtils {
    /**
     * 获取到datas一层的数据
     *
     * @param json
     * @param tablename
     * @return
     */
    public static String getDatasString(String json, String tablename) {
        String dataString = "";
        try {
            JsonElement datas = JsonUtils.decoElementJSONObject(json, "DATAS");
            JsonElement uum_user = JsonUtils.decoElementJSONObject(datas.toString(), tablename);
            JsonElement datas1 = JsonUtils.decoElementJSONObject(uum_user.toString(), "datas");
            dataString = datas1.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataString;
    }

    public static boolean getRet(String msg) {
        if (msg == null || msg.equals("") || msg.equals(" ") || msg.equals("null") || msg.equals("操作成功！") || msg.contains("28ma")) {//// TODO: 2017/3/9 暂时处理我的回答里面出现错误
            return true;
        } else {
            return false;
        }
    }

    //修改密码时候用到的
    public static boolean getDataRet(String msg) {
        if (msg == null || msg.equals("") || msg.equals(" ") || msg.equals("操作成功！")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 处理后台 资源url 分割 ,2016-11-21\\201f7931-5a37-4209-a4fb-8cba6d61f7c2.jpg,2016-11-21\\54c28013-ebf8-4356-b842-569c660b6115.jpg,2016-11-21\\b20d7d50-90ad-4f9a-a417-3060b00b6abf.jpg,2016-11-21\\5ce3e38f-d192-4a61-86c0-3df50514ea80.jpg,2016-11-21\\8ab9edb9-0ed1-4903-806c-099823d4c5f5.jpg,2016-11-21\\c82f6023-4056-494c-b80f-87763dcf777b.jpg,2016-11-21\\1df17090-13bc-4256-846d-73b217920528.jpg,2016-11-21\\963c1830-137a-4628-b403-aa0fbc0449fd.jpg
     */
    public static List<SourceBean> doSplitUrls(String sourceurl, String sourcename) {
        ArrayList<SourceBean> sourceList = new ArrayList<SourceBean>();
        if (!TextUtils.isEmpty(sourceurl)
                && !TextUtils.isEmpty(sourcename)) {
            String[] urls = sourceurl.split(",");
            String[] names = sourcename.split(",");
            for (int j = 1; j < urls.length; j++) {
                SourceBean sourceBean = new SourceBean();
                sourceBean.url = urls[j];
                sourceBean.name = names[j - 1];
                sourceList.add(sourceBean);
            }
        }
        return sourceList;
    }

    /**
     * 处理后台 资源url 分割 ,2016-11-21\\201f7931-5a37-4209-a4fb-8cba6d61f7c2.jpg,2016-11-21\\54c28013-ebf8-4356-b842-569c660b6115.jpg,2016-11-21\\b20d7d50-90ad-4f9a-a417-3060b00b6abf.jpg,2016-11-21\\5ce3e38f-d192-4a61-86c0-3df50514ea80.jpg,2016-11-21\\8ab9edb9-0ed1-4903-806c-099823d4c5f5.jpg,2016-11-21\\c82f6023-4056-494c-b80f-87763dcf777b.jpg,2016-11-21\\1df17090-13bc-4256-846d-73b217920528.jpg,2016-11-21\\963c1830-137a-4628-b403-aa0fbc0449fd.jpg
     */
    public static List<String> doSplitUrls(String sourceurl) {
        ArrayList<String> sourceList = new ArrayList<String>();
        if (!TextUtils.isEmpty(sourceurl)) {
            String[] urls = sourceurl.split(",");
            for (int j = 1; j < urls.length; j++) {
                if (!TextUtils.isEmpty(urls[j]))
                    sourceList.add(urls[j]);
            }
        }
        return sourceList;
    }


    public static String replaceString(String string, String sub, String replace) {
        String substring = string;
        if (!TextUtils.isEmpty(string)) {
            if (string.contains(sub)) {
                substring = string.replaceAll(sub, replace);
            }
        }
        return substring;
    }


    /**
     * 传入url路径子节点 返回一个完整的url
     *
     * @param subUrl
     * @return
     */
    public static String subUrlToWholeUrl(String subUrl) {
        String urlString = subUrl + "";
        if (!TextUtils.isEmpty(urlString)) {
            if (urlString.contains(",")) {
                urlString = urlString.replace(",", "");
            }
            if (urlString.contains("\\")) {
                urlString = urlString.replace("\\", "/");
            }
        }
        return HttpContacts.FILEURL + urlString;
    }

    public static String circleUrlString(String subUrl) {
        String urlString = subUrl + "";
        if (!TextUtils.isEmpty(urlString)) {
            if (urlString.contains(",")) {
                urlString = urlString.replace(",", "");
            }
            if (urlString.contains("\\")) {
                urlString = urlString.replace("\\", "/");
            }
            if (urlString.startsWith("/")) {
                urlString = urlString.replaceFirst("/", "");
            }
        }
        LogUtils.e("图片路径", HttpContacts.HOST + urlString);
        return HttpContacts.HOST + urlString;
    }

    /**
     * 处理后台 资源url 分割 ,2016-11-21\\201f7931-5a37-4209-a4fb-8cba6d61f7c2.jpg,2016-11-21\\54c28013-ebf8-4356-b842-569c660b6115.jpg,2016-11-21\\b20d7d50-90ad-4f9a-a417-3060b00b6abf.jpg,2016-11-21\\5ce3e38f-d192-4a61-86c0-3df50514ea80.jpg,2016-11-21\\8ab9edb9-0ed1-4903-806c-099823d4c5f5.jpg,2016-11-21\\c82f6023-4056-494c-b80f-87763dcf777b.jpg,2016-11-21\\1df17090-13bc-4256-846d-73b217920528.jpg,2016-11-21\\963c1830-137a-4628-b403-aa0fbc0449fd.jpg
     * 返回完整的链接
     */
    public static List<String> splitUrls(String sourceurl) {
        ArrayList<String> sourceList = new ArrayList<String>();
        if (!TextUtils.isEmpty(sourceurl)) {
            String[] urls = sourceurl.split(",");
            for (String url : urls) {
                if (!TextUtils.isEmpty(url))
                    sourceList.add(subUrlToWholeUrl(url));
            }

        }
        return sourceList;
    }

    /**
     * 处理后台 资源url 分割 ,2016-11-21\\201f7931-5a37-4209-a4fb-8cba6d61f7c2.jpg,2016-11-21\\54c28013-ebf8-4356-b842-569c660b6115.jpg,2016-11-21\\b20d7d50-90ad-4f9a-a417-3060b00b6abf.jpg,2016-11-21\\5ce3e38f-d192-4a61-86c0-3df50514ea80.jpg,2016-11-21\\8ab9edb9-0ed1-4903-806c-099823d4c5f5.jpg,2016-11-21\\c82f6023-4056-494c-b80f-87763dcf777b.jpg,2016-11-21\\1df17090-13bc-4256-846d-73b217920528.jpg,2016-11-21\\963c1830-137a-4628-b403-aa0fbc0449fd.jpg
     */
    public static List<SourceBean> splitUrls(String sourceurl, String sourcename) {
        ArrayList<SourceBean> sourceList = new ArrayList<>();
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> urlList = new ArrayList<>();
        if (!TextUtils.isEmpty(sourceurl) && !TextUtils.isEmpty(sourcename)) {
            String[] urls = sourceurl.split(",");
            String[] names = sourcename.split(",");
            for (String url : urls) {
                if (!TextUtils.isEmpty(url)) {
                    urlList.add(url);
                }
            }
            for (String name : names) {
                if (!TextUtils.isEmpty(name)) {
                    nameList.add(name);
                }
            }
            for (int i = 0; i < nameList.size(); i++) {
                SourceBean sourceBean = new SourceBean();
                sourceBean.url = urlList.get(i);
                sourceBean.name = nameList.get(i);
                sourceList.add(sourceBean);
            }
        }
        return sourceList;
    }
}
