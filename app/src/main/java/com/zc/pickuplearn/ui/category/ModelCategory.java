package com.zc.pickuplearn.ui.category;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.utils.LogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： Jonsen
 * 时间: 2017/2/24 9:38
 * 联系方式：chenbin252@163.com
 */

public class ModelCategory {
    public static final String TAG = "QuestionClassification";

    /**
     * 获取分类树数据
     */
    public void getQuestionTypeData(final GetCatogoryDataCallBack callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.PROCIRCLE_POSITION);
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", "V_KLB_QUESTION_TYPE");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject("V_KLB_QUESTION_TYPE", datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL,
                    jsonObject, new MyStringCallBack() {
                        @Override
                        public void onSuccess(String response) {
                            String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_QUESTION_TYPE);
                            LogUtils.e("分类树", datasString);
                            try {
                                List<QusetionTypeBean> qustionTypeBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<QusetionTypeBean>>() {
                                });
                                if (qustionTypeBeen != null && qustionTypeBeen.size() > 0) {
//                                    SPUtils.put(UIUtils.getContext(), TAG,
//                                            response);// 访问数据成功保存到本地
                                    callBack.onSuccess(qustionTypeBeen);
                                } else {
                                    callBack.onFail();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                callBack.onFail();
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            callBack.onFail();
                        }
                    });
        } catch (Exception e) {
            callBack.onFail();
        }
    }

    public interface GetCatogoryDataCallBack {
        void onSuccess(List<QusetionTypeBean> qustionTypeBeen);

        void onFail();
    }

    /**
     * 解析分类json串
     *
     * @param response
     */
    public List<QusetionTypeBean> processTypeStringData(String response) {
        List<QusetionTypeBean> list = new ArrayList<>();
        try {
            String datasString = ResultStringCommonUtils.getDatasString(response, "V_KLB_QUESTION_TYPE");
            list = JsonUtils.parseJson2Object(datasString,
                    new TypeToken<List<QusetionTypeBean>>() {
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
