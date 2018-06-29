package com.zc.pickuplearn.ui.dynamic.widget.recANDnew.model;

import android.text.TextUtils;

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
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view.WenDaFragment;
import com.zc.pickuplearn.utils.LogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： Jonsen
 * 时间: 2017/1/10 15:58
 * 联系方式：chenbin252@163.com
 */

public class ImplWenDaModel implements IWenDaModel {
    @Override
    public void getDynamicDatas(final String search, final int index, String from, boolean needmore, QuestionBean bean, final GetDynamicDatasCallBack callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
//        if (WenDaFragment.THE_NEW.equals(from)) {
//            parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
//            whereSql = "ISLOSE='0' and ISSOLVE='0' and QUESTIONLEVEL='1' and ISEXPERTANSWER='0' and QUESTIONEXPLAIN like '%" + search + "%'";
//            if (needmore) {
//                whereSql = "ISLOSE='0' and  ISSOLVE='0'and QUESTIONLEVEL = '1' and ISEXPERTANSWER = '0' and QUESTIONEXPLAIN like '%" + search + "%'";
//            }
//        } else if (WenDaFragment.THE_RECOMMENT.equals(from)) {
//            whereSql = "ISLOSE='0' and ISSOLVE='0' and QUESTIONLEVEL = '1' and ISEXPERTANSWER = '0' and QUESTIONEXPLAIN like '%" + search + "%'";
//            parametersMap.put("ORDERSQL", "SYSCREATEDATE,ANSWERSUM desc");
//            if (needmore) {
//                whereSql = "ISLOSE='0' and  ISSOLVE='0'and QUESTIONLEVEL = '1' and QUESTIONEXPLAIN like '%" + search + "%'";
//                parametersMap.put("ORDERSQL", "SYSCREATEDATE,ANSWERSUM desc");
//            }
//        }
//        if (bean != null) {//从分类过来的
//            whereSql = "ISLOSE='0' and QUESTIONLEVEL='1' and QUESTIONTYPECODE like'%" + bean.getQUESTIONTYPECODE() + "%'";
//            parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
//        }
//        if (!TextUtils.isEmpty(search)) {
//            whereSql = "(questionexplain like '%" + search + "%' or (select klb_question_type.questiontypename from klb_question_type where klb_question_type.seqkey in (v_klb_question_info.questiontypecode)) like '%" + search + "%')";
//            parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
//        }
//        if (WenDaFragment.THE_SCORE.equals(from)) {
//            whereSql = "ISLOSE = '0' and BONUSPOINTS IS NOT NULL and ISSOLVE = 0 and BONUSPOINTS != 0 ";
//            parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
//        }
        if (WenDaFragment.THE_NEW.equals(from)) {
            parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
            whereSql = "ISLOSE='0' and ISSOLVE='0' and QUESTIONLEVEL='1' and QUESTIONEXPLAIN like '%" + search + "%'";
            if (needmore) {
                whereSql = "ISLOSE='0' and  ISSOLVE='0'and QUESTIONLEVEL = '1' and QUESTIONEXPLAIN like '%" + search + "%'";
            }
        } else if (WenDaFragment.THE_RECOMMENT.equals(from)) {
            whereSql = "ISLOSE='0' and ISSOLVE='0' and QUESTIONLEVEL = '1'  and QUESTIONEXPLAIN like '%" + search + "%'";
            parametersMap.put("ORDERSQL", "SYSCREATEDATE,ANSWERSUM desc");
            if (needmore) {
                whereSql = "ISLOSE='0' and  ISSOLVE='0'and QUESTIONLEVEL = '1' and QUESTIONEXPLAIN like '%" + search + "%'";
                parametersMap.put("ORDERSQL", "SYSCREATEDATE,ANSWERSUM desc");
            }
        }
        if (bean != null) {//从分类过来的
            whereSql = "ISLOSE='0' and QUESTIONLEVEL='1' and QUESTIONTYPECODE like'%" + bean.getQUESTIONTYPECODE() + "%'";
            parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        }
        if (!TextUtils.isEmpty(search)) {
            whereSql = "(questionexplain like '%" + search + "%' or (select klb_question_type.questiontypename from klb_question_type where klb_question_type.seqkey in (v_klb_question_info.questiontypecode)) like '%" + search + "%')";
            parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        }
        if (WenDaFragment.THE_SCORE.equals(from)) {
            whereSql = "ISLOSE = '0' and BONUSPOINTS IS NOT NULL and ISSOLVE = 0 and BONUSPOINTS != 0 ";
            parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        }
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
        HashMap<String, String> commandMap = new HashMap<>();
        if (bean != null && "-1".equals(bean.getPARENTID())) {
            commandMap.put("mobileapp", "true");
            commandMap.put("actionflag", "SEARCH_PARENT_TYPE");
            parametersMap.put("CLASSNAME", "com.nci.klb.app.question.QuestionSearch");
            parametersMap.put("WHERESQL", "");
            parametersMap.put("PARENTID", "-1");
            parametersMap.put("TYPECODE", bean.getQUESTIONTYPECODE());
        } else {
            commandMap.put("mobileapp", "true");
            commandMap.put("actionflag", "select");
        }
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_QUESTION_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("首页问题" + response);
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

    /**
     * 获取一条答案
     *
     * @param bean
     * @param callBack
     */
    @Override
    public void getAnsewData(QuestionBean bean, final GetAnswerDatasCallBack callBack) {
        String usercode = DataUtils.getUserInfo().getUSERCODE();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<String, String>();
        //第一条记录结束
        // 参数区域赋值
        String whereSqlString = "QUESTIONID='" + bean.getSEQKEY() + "'" + "and ANSWERUSERCODE ='" + usercode + "'";
        parametersMap.put("CLASSNAME", UrlMethod.ANSWER_GOOD);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_ANSWER_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "1");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_ANSWER_INFO, datas, parametersMap);
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
            callBack.onFailure();
            e.printStackTrace();
        }
    }

    public interface GetAnswerDatasCallBack {
        void onSuccess(List<AnswerBean> dynamic_data);

        void onFailure();
    }

    public interface GetDynamicDatasCallBack {
        void onSuccess(List<QuestionBean> dynamic_data);

        void onFailure();
    }
}
