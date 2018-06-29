package com.zc.pickuplearn.http;

import android.os.Environment;

/**
 * 通讯地址类
 * 时间: 2016/11/9 11:20
 */
public class HttpContacts {
//    public static String HOST = "http://117.149.2.229:1630/emark_learn/";//外网测试地址(新)
    public static String HOST = "http://114.55.55.205:8080/emarkspg_nbsjzd/";//云
//    public static String HOST = "http://192.168.10.181:8080/emarkspg_sx/";//jsf
//    public static String HOST = "http://192.168.10.155:8899/emarkspg_sx/";//gk
//    public static String HOST = "http://192.168.1.189:8080/emark_learn/";//
//    public static String HOST = "http://192.168.10.189:8080/emark_learn/";//
//    public static String HOST = "http://192.168.1.190:8080/emark_learn/";//

    public static String LOGIN_URL = HOST + "do/app/login";//登录操作
    public static String LOGOUT_URL = HOST + "do/app/logout";//登出操作
    public static String UI_URL = HOST + "do/app/uiaction";//通用操作
    public static String VERSION = HOST + "do/app/version";//检测更新
    //apk图片下载地址
    public static String FILEURL = HOST + "file/";
    //文件下载路径
    public static final String absolutePath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/";
    //文件下载路径
    public static final String absolutePath2 = absolutePath;

}
