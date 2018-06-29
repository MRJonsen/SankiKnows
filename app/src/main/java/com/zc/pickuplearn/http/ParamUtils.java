package com.zc.pickuplearn.http;

import com.zc.pickuplearn.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 网络请求参数处理类
 * 作者： Jonsen
 * 时间: 2016/11/9 11:50
 * 联系方式：chenbin252@163.com
 */
public class ParamUtils {
    private ParamUtils() {
    }

    /**
     * @param tablename 表名
     * @param datas     数据域
     * @param params    参数
     */
    public static JSONObject params2JsonObject(final String tablename, final Map<String, String> datas,
                                               final Map<String, String> params) {

        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> mDatas = new HashMap<String, String>();
        mDatas.putAll(datas);
        dataMap.put(tablename, mDatas);
        dataList.add(dataMap);
        // 第一条记录结束
        // 参数区域赋值
        if (params.isEmpty() || !params.containsKey(UrlMethod.CLASSNAME)) {
            parametersMap.put(UrlMethod.CLASSNAME, UrlMethod.APP_BIZ_OPERATION);
        }
        parametersMap.putAll(params);
        // command赋值区域
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        JSONObject jsonObject = null;
        try {
            // 参数封装
            jsonObject = ParamUtils.getJsonObject(dataList,
                    parametersMap, commandMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    /**
     * @param tablename 表名
     * @param datas     数据域
     * @param params    参数
     *
     */
    public static JSONObject params2JsonObject4(final String tablename, final Map<String, String> datas,
                                               final Map<String, String> params,Map<String,String> command) {

        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> mDatas = new HashMap<String, String>();
        mDatas.putAll(datas);
        dataMap.put(tablename, mDatas);
        dataList.add(dataMap);
        // 第一条记录结束
        // 参数区域赋值
        if (params.isEmpty() || !params.containsKey(UrlMethod.CLASSNAME)) {
            parametersMap.put(UrlMethod.CLASSNAME, UrlMethod.APP_BIZ_OPERATION);
        }
        parametersMap.putAll(params);
        // command赋值区域
        commandMap.putAll(command);
        JSONObject jsonObject = null;
        try {
            // 参数封装
            jsonObject = ParamUtils.getJsonObject(dataList,
                    parametersMap, commandMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    /**
     * 登录的jsonobject
     */
    public static JSONObject paramLogin(final String tablename, final Map<String, String> datas,
                                               final Map<String, String> params) {

        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> mDatas = new HashMap<String, String>();
        mDatas.putAll(datas);
        dataMap.put(tablename, mDatas);
        dataList.add(dataMap);
        // 第一条记录结束
        // 参数区域赋值
//        if (params.isEmpty() || !params.containsKey(UrlMethod.CLASSNAME)) {
//            parametersMap.put(UrlMethod.CLASSNAME, UrlMethod.APP_BIZ_OPERATION);
//        }
        parametersMap.putAll(params);
        // command赋值区域
        commandMap.put("mobileapp", "true");
//        commandMap.put("actionflag", "select");
        JSONObject jsonObject = null;
        try {
            // 参数封装
            jsonObject = ParamUtils.getJsonObject(dataList,
                    parametersMap, commandMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 有从表的情况
     *
     * @param tablename 表名
     * @param datas     数据域
     * @param params    参数
     */
    public static JSONObject params2JsonObject2(final String tablename, final Map<String, String> datas,
                                                final Map<String, String> params, Map<String, Map<String, String>> datas2) {

        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> mDatas = new HashMap<String, String>();
        mDatas.putAll(datas);
        dataMap.put(tablename, mDatas);
        dataList.add(dataMap);
        dataList.add(datas2);
        // 第一条记录结束
        // 参数区域赋值
        if (params.isEmpty() || !params.containsKey(UrlMethod.CLASSNAME)) {
            parametersMap.put(UrlMethod.CLASSNAME, UrlMethod.APP_BIZ_OPERATION);
        }
        parametersMap.putAll(params);
        // command赋值区域
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        JSONObject jsonObject = null;
        try {
            // 参数封装
            jsonObject = ParamUtils.getJsonObject(dataList,
                    parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 有从表的情况 参数域
     *
     * @param tablename 表名
     * @param datas     数据域
     * @param params    参数
     */
    public static JSONObject params2JsonObject3(final String tablename, final Map<String, String> datas,
                                                final Map<String, String> params, List<Map<String, Map<String, String>>> datas2) {
        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> mDatas = new HashMap<String, String>();
        mDatas.putAll(datas);
        dataMap.put(tablename, mDatas);
        dataList.add(dataMap);
        if (datas2 != null) {
            for (int i = 0; i < datas2.size(); i++) {
                dataList.add(datas2.get(i));
            }
        }
        // 第一条记录结束
        // 参数区域赋值
        if (params.isEmpty() || !params.containsKey(UrlMethod.CLASSNAME)) {
            parametersMap.put(UrlMethod.CLASSNAME, UrlMethod.APP_BIZ_OPERATION);
        }
        parametersMap.putAll(params);
        // command赋值区域
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        JSONObject jsonObject = null;
        try {
            // 参数封装
            jsonObject = ParamUtils.getJsonObject(dataList,
                    parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 有从表的情况 数据域
     *
     * @param datas  数据域
     * @param params 参数
     */
    public static JSONObject params2JsonObject4(List<Map<String, Map<String, String>>> datas,
                                                final Map<String, String> params) {

        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        dataList.addAll(datas);
        // 第一条记录结束
        // 参数区域赋值
        if (params.isEmpty() || !params.containsKey(UrlMethod.CLASSNAME)) {
            parametersMap.put(UrlMethod.CLASSNAME, UrlMethod.APP_BIZ_OPERATION);
        }
        parametersMap.putAll(params);
        // command赋值区域
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        JSONObject jsonObject = null;
        try {
            // 参数封装
            jsonObject = ParamUtils.getJsonObject(dataList,
                    parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    /**
     * 有从表的情况 数据域
     *
     * @param datas  数据域
     * @param params 参数
     */
    public static JSONObject params2JsonObject5(List<Map<String, Map<String, String>>> datas,
                                                final Map<String, String> params,Map<String,String> command) {

        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        dataList.addAll(datas);
        // 第一条记录结束
        // 参数区域赋值
        if (params.isEmpty() || !params.containsKey(UrlMethod.CLASSNAME)) {
            parametersMap.put(UrlMethod.CLASSNAME, UrlMethod.APP_BIZ_OPERATION);
        }
        parametersMap.putAll(params);
        // command赋值区域
        commandMap.putAll(command);
        JSONObject jsonObject = null;
        try {
            // 参数封装
            jsonObject = ParamUtils.getJsonObject(dataList,
                    parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



    /**
     * 参数转换成jsonobject
     *
     * @param dataList
     * @param parametersMap
     * @param commandMap
     * @return
     * @throws Exception
     */
    public static JSONObject getJsonObject(ArrayList<Map<String, Map<String, String>>> dataList, Map<String, String> parametersMap, Map<String, String> commandMap) throws Exception {
        // data数据区域
        JSONArray DATAS = new JSONArray();
        Iterator<Map<String, Map<String, String>>> iterator = dataList.iterator();
        while (iterator.hasNext()) {
            Map<String, Map<String, String>> dataMap = (Map<String, Map<String, String>>) iterator.next();
            for (String keyObject : dataMap.keySet()) {
                JSONObject DATAStemp = new JSONObject();
                Map<String, String> mapObject = dataMap.get(keyObject);
                JSONArray fields = new JSONArray();
                for (String key : mapObject.keySet()) {
                    JSONObject tempJson = new JSONObject();
                    tempJson.put("name", key);
                    tempJson.put("value", mapObject.get(key));
                    fields.put(tempJson);
                }
                DATAStemp.put("name", keyObject);//表名
                DATAStemp.put("fields", fields);
                DATAS.put(DATAStemp);
            }
        }
        // 参数区域
        JSONObject PARAMETERS = new JSONObject();
        JSONArray PARA = new JSONArray();
        // METHOD 默认有：search，addSave，updateSave，delete 其它可以自由定制自己个性化方法
        for (String key : parametersMap.keySet()) {
            JSONObject tempJson = new JSONObject();
            tempJson.put("name", key);
            tempJson.put("value", parametersMap.get(key));
            PARA.put(tempJson);
        }
        PARAMETERS.put("PARA", PARA);
        // commad区域
        JSONObject COMMAND = new JSONObject();
        for (String key : commandMap.keySet()) {
            COMMAND.put(key, commandMap.get(key));
        }
        JSONObject NCI = new JSONObject();
        NCI.put("DATAS", DATAS);
        NCI.put("PARAMETERS", PARAMETERS);
        NCI.put("COMMAND", COMMAND);

        JSONObject jsonObjectIn = new JSONObject();
        jsonObjectIn.put("NCI", NCI);
        return jsonObjectIn;
    }
}
