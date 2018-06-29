package com.zc.pickuplearn.ui.mine.mine.widget.myanswer.model;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.beans.AnswerBean;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： Jonsen
 * 时间: 2017/1/12 12:55
 * 联系方式：chenbin252@163.com
 */

public class ImplMyAnswerModel implements IMyAnswerModel {

    @Override
    public void getDynamicDatas(String search, int index, List<AnswerBean> list, final GetDynamicDatasCallBack callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        if (list == null) {
            whereSql = "SYSCREATORID ='" + DataUtils.getUserInfo().getUSERCODE() + "' and QUESTIONEXPLAIN like '%"
                    + search + "%'and ISLOSE = '0'";
        } else {
            String range = "";
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        range = "'" + list.get(i).getQUESTIONID() + "'";
                    } else {
                        if (i == list.size() - 1) {
                            range = range + "," + "'" + list.get(i).getQUESTIONID() + "'";
                        } else {
                            range = range + "," + "'" + list.get(i).getQUESTIONID() + "'";
                        }
                    }

                }
                whereSql = "SEQKEY in (" + range + ") and QUESTIONEXPLAIN like '%"
                        + search + "%'and ISLOSE ='0'";
            }
        }
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.QUESTION_POINTS);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + index);
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");

        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_QUESTION_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("问题列表"+response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_QUESTION_INFO);
                    try {
                        List<QuestionBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<QuestionBean>>() {
                        });
                        if (questionBeen.size() > 0) {
                            List<QuestionTypeBean> qustionTypeBeen = DataUtils.GetQuestionTypeList();
                            if (qustionTypeBeen.size() > 0) {
                                for (QuestionBean bean : questionBeen) {
                                    List<QuestionTypeBean> list = new ArrayList<QuestionTypeBean>();//问题标签集合
                                    for (QuestionTypeBean typebean : qustionTypeBeen) {
                                        if (bean.getQUESTIONTYPECODE().contains(typebean.getQUESTIONTYPECODE())) {
                                            boolean has = false;
                                            for (QuestionTypeBean typebean2 :
                                                    list) {
                                                if (typebean2.getQUESTIONTYPENAME().equals(typebean.getQUESTIONTYPENAME())) {
                                                    has = true;
                                                }
                                            }
                                            if (!has) {
                                                list.add(typebean);
                                            }
                                        }
                                        // TODO: 2017/2/20  处理标签数据
                                        if (bean.getQUESTIONTYPECODE().equals(typebean.getQUESTIONTYPECODE())) {
                                            bean.setQUESTIONTYPENAME(typebean.getQUESTIONTYPENAME());
                                        }
                                    }
                                    bean.setTip(list);
                                }
                            }
                            callBack.onSuccess(questionBeen);
                        } else {
                            callBack.onSuccess(questionBeen);
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

    @Override
    public void getAnswerData(final GetAnswerDatasCallBack callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<String, String>();
        //第一条记录结束
        // 参数区域赋值
        String whereSqlString = "ANSWERUSERCODE = '" +DataUtils.getUserInfo().getUSERCODE()+ "'";
        parametersMap.put("CLASSNAME", UrlMethod.ANSWER_GOOD);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "ISPASS desc");
        parametersMap.put("MASTERTABLE", TableName.V_KLB_ANSWER_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_ANSWER_INFO, datas, parametersMap);
            LogUtils.e("我的回答" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_ANSWER_INFO);
                    try {
                        List<AnswerBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<AnswerBean>>() {
                        });
                        if (questionBeen.size() > 0) {
                            callBack.onSuccess(questionBeen);
                        } else {
                            callBack.onFailure();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callBack.onFailure();
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

    public interface GetDynamicDatasCallBack {
        void onSuccess(List<QuestionBean> dynamic_data);

        void onFailure();
    }

    public interface GetAnswerDatasCallBack {
        void onSuccess(List<AnswerBean> dynamic_data);

        void onFailure();
    }
}
