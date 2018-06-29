package com.zc.pickuplearn.ui.classiccase.model;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;
import com.zc.pickuplearn.utils.LogUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： Jonsen
 * 时间: 2016/12/14 15:36
 * 联系方式：chenbin252@163.com
 */

public class ClassicCaseListModelImpl implements IClassicCaseListModel {

    @Override
    public void getClassicCaseData(QusetionTypeBean typeBean, ProfessionalCircleBean circleBean, String TYPE_NOW, int index, final GetClassicCaseDatasCallBack callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        if (circleBean != null) {
            if (!TextUtils.isEmpty(circleBean.getPROCIRCLECODE()))
                whereSql = "PROCIRCLECODE like '%" + circleBean.getPROCIRCLECODE() + "%'";
        }
        if (typeBean != null) {
            String code = typeBean.getCODE();
            if (!TextUtils.isEmpty(code))
                whereSql = "QUESTIONTYPECODE like '%" + code + "%'";
        }
        parametersMap.put("CLASSNAME", UrlMethod.CLASSIC_CASE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        if (TYPE_NOW.equals(UrlMethod.TYPE_HOT)) {
            parametersMap.put("ORDERSQL", "CLICKCOUNT desc");
        } else {
            parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        }
        parametersMap.put("MASTERTABLE", TableName.V_KLB_CLASSICCASE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        if (typeBean!=null&&"-1".equals(typeBean.getPARENTID())){
            commandMap.put("mobileapp", "true");
            commandMap.put("actionflag", "SEARCH_PARENT_TYPE");
            parametersMap.put("WHERESQL", "");
            parametersMap.put("PARENTID", "-1");
            parametersMap.put("TYPECODE", typeBean.getCODE());
        }else {
            commandMap.put("mobileapp", "true");
            commandMap.put("actionflag", "select");
        }
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_CLASSICCASE_INFO, datas, parametersMap,commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e(response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_CLASSICCASE_INFO);
                    try {
                        List<ClassicCaseBean> data = JsonUtils.parseJson2Object(datasString, new TypeToken<List<ClassicCaseBean>>() {
                        });
                        if (data.size() > 0) {
                            callBack.onSuccess(data);
                        } else {
                            callBack.onFailure();
                        }
                    } catch (Exception e) {
                        callBack.onFailure();
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

    }

    public interface GetClassicCaseDatasCallBack {
        void onSuccess(List<ClassicCaseBean> strings);

        void onFailure();
    }
}
