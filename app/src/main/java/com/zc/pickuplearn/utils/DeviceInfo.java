package com.zc.pickuplearn.utils;

import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 更新设备信息 回传
 * 作者： Jonsen
 * 时间: 2017/3/7 10:27
 * 联系方式：chenbin252@163.com
 */

public class DeviceInfo {
    //不允许new
    private DeviceInfo() {
    }

    private static String today = DateUtils.dataFormatNow("yyyy-MM-dd");
    private static String mobileSeq;
    private static String loginTimes;

    public static void searchIMEI() {
        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("IMEI", SystemUtils.getIMEI(UIUtils.getContext()));
        datas.put("SEQKEY", "");
        datas.put("LOGINTIMES", "");
        datas.put("SYSCREATORID", DataUtils.getUserInfo().getUSERCODE());
        datas.put("PSNNAME", "");
        dataMap.put("SNOW_MOBILE_DEVICE", datas);
        dataList.add(dataMap);
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", "com.nci.app.operation.business.AppBizOperation");
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");//ORGCODE IN (#TEAMORGCODES#) and persontype like 'B%' AND ISASSESS='true'
        parametersMap.put("WHERESQL", "");

        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", "SNOW_MOBILE_DEVICE");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");

        // command赋值区域
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject("SNOW_MOBILE_DEVICE", datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {


                @Override
                public void onSuccess(String response) {
                    LogUtils.e("回传设备信息获取" ,response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject object1 = object.getJSONObject("DATAS");
                        JSONObject obj = object1.getJSONObject("SNOW_MOBILE_DEVICE");
                        int total = obj.getInt("totalCount");
                        if (total > 0) {
                            JSONArray arr = obj.getJSONArray("datas");
                            JSONObject obj1 = arr.getJSONObject(0);
                            mobileSeq = obj1.getString("SEQKEY");
                            loginTimes = obj1.getString("LOGINTIMES");
                            if (loginTimes.equals("")) {
                                loginTimes = "1";
                            }
                        }
                        insertSysInfo(total);
                    } catch (JSONException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e(error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertSysInfo(int total) {
        // TODO 自动生成的方法存根
        if (total == 0) {
            setMobileInfo();
        } else {
            updateMobileInfo();
        }
    }

    private static void updateMobileInfo() {
        // TODO 自动生成的方法存根
        int totalTimes = Integer.valueOf(loginTimes) + 1;
        final String totalTime = String.valueOf(totalTimes);
        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("SEQKEY", mobileSeq);
        datas.put("LOGINTIMES", totalTime);
//				datas.put("BRAND", SystemUtils.getBrand());//手机品牌
        datas.put("IMEI", SystemUtils.getIMEI(UIUtils.getContext()));
//				datas.put("IP", SystemUtils.getLocalIpAddress());
//				datas.put("MAC", SystemUtils.getMacAddress(MainTabActivity.this));
        datas.put("PHONEMODEL", SystemUtils.getModel());//手机型号
        datas.put("SYS", SystemUtils.getOS());//操作系统型号
        datas.put("SYSVERSION", SystemUtils.getApkVersionName(UIUtils.getContext()));
        datas.put("SYSVERSIONCODE", SystemUtils.getApkVersionCode(UIUtils.getContext()));
//        if (null != SystemUtils.getTelePhoneNumber(UIUtils.getContext())) {
//            datas.put("TELNUMBER", SystemUtils.getTelePhoneNumber(UIUtils.getContext()));
//        }
        datas.put("USERACCOUNT", DataUtils.getUserInfo().getUSERACCOUNT());
        datas.put("PSNNAME", DataUtils.getUserInfo().getUSERNAME());
        datas.put("PSNNUM", DataUtils.getUserInfo().getUSERCODE());
        datas.put("ISENABLE", "true");
        datas.put("LATESTUSE", today);
        datas.put("SYSUPDATEDATE", today);
        dataMap.put("SNOW_MOBILE_DEVICE", datas);
        dataList.add(dataMap);
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", "com.nci.app.operation.business.AppBizOperation");
        parametersMap.put("METHOD", "updateSave");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");//ORGCODE IN (#TEAMORGCODES#) and persontype like 'B%' AND ISASSESS='true'
        parametersMap.put("WHERESQL", "");

        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", "SNOW_MOBILE_DEVICE");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");

        // command赋值区域
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject("SNOW_MOBILE_DEVICE", datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {


                @Override
                public void onSuccess(String response) {
                    LogUtils.e("回传设备信息更新" ,response);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e(error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void setMobileInfo() {
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("BRAND", SystemUtils.getBrand());//手机品牌
        datas.put("IMEI", SystemUtils.getIMEI(UIUtils.getContext()));
        datas.put("IP", SystemUtils.getLocalIpAddress());
        datas.put("MAC", SystemUtils.getMacAddress(UIUtils.getContext()));
        datas.put("PHONEMODEL", SystemUtils.getModel());//手机型号
        datas.put("SYS", SystemUtils.getOS());//操作系统型号
        datas.put("SYSVERSION", SystemUtils.getApkVersionName(UIUtils.getContext()));
        datas.put("SYSVERSIONCODE", SystemUtils.getApkVersionCode(UIUtils.getContext()));
        if (null != SystemUtils.getTelePhoneNumber(UIUtils.getContext())) {
            datas.put("TELNUMBER", SystemUtils.getTelePhoneNumber(UIUtils.getContext()));
        }
        datas.put("USERACCOUNT", DataUtils.getUserInfo().getUSERACCOUNT());
        datas.put("PSNNAME", DataUtils.getUserInfo().getUSERNAME());
        datas.put("PSNNUM", DataUtils.getUserInfo().getUSERCODE());
        datas.put("LOGINTIMES", "1");
        datas.put("ISENABLE", "true");
        datas.put("FIRSTUSE", today);
        datas.put("LATESTUSE", today);
        datas.put("SYSCREATEDATE", today);
        dataMap.put("SNOW_MOBILE_DEVICE", datas);
        dataList.add(dataMap);
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", "com.nci.app.operation.business.AppBizOperation");
        parametersMap.put("METHOD", "addSave");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");//ORGCODE IN (#TEAMORGCODES#) and persontype like 'B%' AND ISASSESS='true'
        parametersMap.put("WHERESQL", "");

        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", "SNOW_MOBILE_DEVICE");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");

        // command赋值区域
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject("SNOW_MOBILE_DEVICE", datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {


                @Override
                public void onSuccess(String response) {
                    LogUtils.e("回传设备信息新加" ,response);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e(error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}