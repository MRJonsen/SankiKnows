package com.zc.pickuplearn.utils;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.beans.QuestionTypeBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据持久化处理类
 * 作者： Jonsen
 * 时间: 2016/12/8 13:03
 * 联系方式：chenbin252@163.com
 */

public class DataUtils {
    public static final String USER_INFO = "userinfo";
    public static List<QuestionTypeBean> list = new ArrayList<>();//问题分类

    public static void setUserInfo(final UserBean userInfo) {
        SPUtils.setObject(UIUtils.getContext(), USER_INFO, userInfo);
    }

    public static UserBean getUserInfo() {
        return SPUtils.getObject(UIUtils.getContext(), USER_INFO, UserBean.class);
    }

    /**
     * 获取问题分类列表
     */
    public static List<QuestionTypeBean> GetQuestionTypeList() {
        if (list != null && list.size() > 0) {
            return list;
        }
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "ISENABLED=1";
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_QUESTION_TYPE);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_QUESTION_TYPE, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_QUESTION_TYPE);
                    try {
                        List<QuestionTypeBean> qustionTypeBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<QuestionTypeBean>>() {
                        });
                        if (qustionTypeBeen != null && qustionTypeBeen.size() > 0) {
                                list.clear();
                                list.addAll(qustionTypeBeen);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
