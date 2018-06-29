package com.zc.pickuplearn.ui.dynamic.widget.answerqustion.model;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.QuestionTypeBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view.QuestionDetailActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.widget.QuestionDetailType;
import com.zc.pickuplearn.utils.LogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： Jonsen
 * 时间: 2017/1/13 14:11
 * 联系方式：chenbin252@163.com
 */

public class ImplQuestionDetailModel implements QuestionDetailModel {
    @Override
    public void getDynamicDatas(QuestionBean bean, final GetDynamicDatasCallBack callBack) {

        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "SEQKEY ='" + bean.getSEQKEY() + "'";
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
            callBack.onFailure();
        }
    }

    @Override
    public void getAnsewListData(QuestionBean bean, final GetAnswerDatasCallBack callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<String, String>();
        //第一条记录结束
        // 参数区域赋值
        String whereSqlString = "QUESTIONID='" + bean.getSEQKEY() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.ANSWER_GOOD);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");//SYSCREATEDATE ISPASS old
        parametersMap.put("ORDERSQL", "ISPASS desc , GOODNUMS desc, QACOUNT desc,SYSCREATEDATE asc");//new
        parametersMap.put("MASTERTABLE", TableName.V_KLB_ANSWER_INFO);
        parametersMap.put("DETAILTABLE", "");
        if (QuestionDetailActivity.comfrom != null) {
            if (!TextUtils.isEmpty(bean.getINFOCOUNT()) && Integer.valueOf(bean.getINFOCOUNT()) > 0) {
                if (QuestionDetailActivity.comfrom == QuestionDetailType.MyAnswer)
                    parametersMap.put("OPERATETYPE", "1");
                if (QuestionDetailActivity.comfrom == QuestionDetailType.MyQuestion)
                    parametersMap.put("OPERATETYPE", "2");
            } else {
                parametersMap.put("OPERATETYPE", "");
            }
        } else {
            parametersMap.put("OPERATETYPE", "");
        }
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_ANSWER_INFO, datas, parametersMap);
//            LogUtils.e("刷新消息提示的条数"+jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("回答列表数据" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_ANSWER_INFO);
                    try {
                        List<AnswerBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<AnswerBean>>() {
                        });
//                        if (questionBeen.size() > 0) {
                            callBack.onSuccess(questionBeen);
//                        } else {
//                            callBack.onFailure();
//                        }
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

    /**
     * 点赞
     */
    @Override
    public void goodAnswer(AnswerBean answerBean, final GoodAnserCallback callback) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("ANSWERID", answerBean.getSEQKEY());
        datas.put("USERCODE", userInfo.getUSERCODE());
        datas.put("QUESTIONID", answerBean.getQUESTIONID());
        datas.put("USERNAME", TextUtils.isEmpty(userInfo.getNICKNAME()) ? userInfo.getUSERNAME() : userInfo.getNICKNAME());
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        parametersMap.put("METHOD", "addSave");// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", "KLB_ANSWER_GOOD");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject("KLB_ANSWER_GOOD", datas, parametersMap);
            LogUtils.e("点赞发送",jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("点赞成功" + response);
                    callback.onSuccess();
                }

                @Override
                public void onFailure(String error) {
                    callback.onFail();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail();
        }
    }

    /**
     * 采纳
     */
    @Override
    public void takeAnswer(AnswerBean answerBean, final TakeAnswerCallback callback) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("ISPASS", "1");
        datas.put("QUESTIONID", answerBean.getQUESTIONID());
        datas.put("SEQKEY", answerBean.getSEQKEY());
        // 从表数据
        Map<String, Map<String, String>> dataMap1 = new HashMap<String, Map<String, String>>();
        Map<String, String> datas1 = new HashMap<String, String>();
        datas1.put("ISSOLVE", "1");

//        dataMap1.put("KLB_ANSWER_INFO",datas);
        dataMap1.put("KLB_QUESTION_INFO", datas1);
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "SEQKEY ='"+answerId+"'";
        parametersMap.put("CLASSNAME",
                "com.nci.klb.app.answer.AnswerTake");
        parametersMap.put("METHOD", "updateSave");// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");// whereSql
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", "KLB_ANSWER_INFO");
        parametersMap.put("DETAILTABLE", "KLB_QUESTION_INFO");
        parametersMap.put("MASTERFIELD", "QUESTIONID");
        parametersMap.put("DETAILFIELD", "SEQKEY");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject2("KLB_ANSWER_INFO", datas, parametersMap, dataMap1);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    callback.onSuccess();
                }

                @Override
                public void onFailure(String error) {
                    callback.onFail();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail();
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

    public interface TakeAnswerCallback {
        void onSuccess();

        void onFail();
    }

    /**
     * 点赞回调
     */
    public interface GoodAnserCallback {
        void onSuccess();

        void onFail();
    }
}
