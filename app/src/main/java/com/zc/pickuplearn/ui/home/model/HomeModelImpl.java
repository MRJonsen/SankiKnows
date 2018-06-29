package com.zc.pickuplearn.ui.home.model;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.QuestionTypeBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.utils.LogUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： Jonsen
 * 时间: 2016/12/12 16:31
 * 联系方式：chenbin252@163.com
 */

public class HomeModelImpl implements IHomeModel {

    @Override
    public void getClassicCaseDatas(ProfessionalCircleBean mbean, final GetClassicCaseDatasCallBack callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        String procirclecode = "";
        if (mbean != null) {
            if (!TextUtils.isEmpty(mbean.getPROCIRCLECODE())) {
                whereSql = "PROCIRCLECODE like '%" + mbean.getPROCIRCLECODE() + "%'";
                procirclecode = mbean.getPROCIRCLECODE();
            }
        }
        parametersMap.put("CLASSNAME", UrlMethod.CLASSIC_CASE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("PROCIRCLECODE",procirclecode);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("MASTERTABLE", TableName.V_KLB_CLASSICCASE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "3");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "classicCaseSearch");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_CLASSICCASE_INFO, datas, parametersMap,commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("金典案咧"+response);
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

    @Override
    public void getDynamicDatas(final String search, final GetDynamicDatasCallBack callBack) {

        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "ISSOLVE=0 and QUESTIONLEVEL = 1 and ISEXPERTANSWER = 0 and QUESTIONEXPLAIN like '%"
                + search + "%'";
        parametersMap.put("CLASSNAME", UrlMethod.QUESTION_POINTS);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("MASTERTABLE", TableName.V_KLB_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "5");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_QUESTION_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_QUESTION_INFO);
                    try {
                        List<QuestionBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<QuestionBean>>() {
                        });
                        if (questionBeen.size() > 0) {
                            List<QuestionTypeBean> qustionTypeBeen = DataUtils.GetQuestionTypeList();
                            if (qustionTypeBeen.size() > 0) {
                                for (QuestionBean bean : questionBeen) {
                                    for (QuestionTypeBean typebean : qustionTypeBeen) {
                                        if (bean.getQUESTIONTYPECODE().equals(typebean.getQUESTIONTYPECODE())) {
                                            bean.setQUESTIONTYPENAME(typebean.getQUESTIONTYPENAME());
                                        }
                                    }
                                }
                            }
                            callBack.onSuccess(questionBeen);
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
                    callBack.onFailure();
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

    public interface GetDynamicDatasCallBack {
        void onSuccess(List<QuestionBean> dynamic_data);

        void onFailure();
    }
}
