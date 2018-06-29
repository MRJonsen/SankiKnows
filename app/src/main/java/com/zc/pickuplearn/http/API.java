package com.zc.pickuplearn.http;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.jpush.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.application.MyAppliction;
import com.zc.pickuplearn.beans.AbilityBankBean;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.beans.CourseWareCommentBean;
import com.zc.pickuplearn.beans.CourseWareHomeBean;
import com.zc.pickuplearn.beans.FunctionBean;
import com.zc.pickuplearn.beans.IndustryStandardBean;
import com.zc.pickuplearn.beans.InfoBean;
import com.zc.pickuplearn.beans.MsgBean;
import com.zc.pickuplearn.beans.PersonMsgBean;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.beans.Professor;
import com.zc.pickuplearn.beans.QuestionBankItem2Bean;
import com.zc.pickuplearn.beans.QuestionBankItemBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.QuestionTypeBean;
import com.zc.pickuplearn.beans.SkillBean;
import com.zc.pickuplearn.beans.SkillMoudleItemBean;
import com.zc.pickuplearn.beans.SkillMoudleLearnBean;
import com.zc.pickuplearn.beans.SystemMsgBean;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.TeamMessageBean;
import com.zc.pickuplearn.beans.TeamMsgStatsUserBean;
import com.zc.pickuplearn.beans.TeamRankPersonBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.beans.VersionBean;
import com.zc.pickuplearn.http.callback.UpdateCallback;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;
import com.zc.pickuplearn.ui.district_manager.CourseDetailActivity;
import com.zc.pickuplearn.ui.question_and_answer.DynamicType;
import com.zc.pickuplearn.ui.question_and_answer.TypeQuestion;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamDynamicBean;
import com.zc.pickuplearn.ui.teamcircle.model.UserType;
import com.zc.pickuplearn.ui.teamcircle.view.TeamCompareDetailFragment;
import com.zc.pickuplearn.ui.teamcircle.view.TeamListActivity;
import com.zc.pickuplearn.ui.update.DownloadApk;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.StringUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zc.pickuplearn.ui.teamcircle.view.TeamCompareDetailFragment.Type.QUESTION;

/**
 * 通用请求
 */

public class API {

    static API instance;

    public static API getInstance() {
        if (instance == null) {
            synchronized (API.class) {
                if (instance == null) {
                    instance = new API();
                }
            }
        }
        return instance;
    }
    //=======================================================================

    /**
     * 检测版本更新
     *
     * @param callback 回调
     */
    public static void checkVersion(final UpdateCallback callback) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("MOBILETYPE", "");
        datas.put("VERSIONCODE", "");
        datas.put("VERSIONNAME", "");
        datas.put("TYPE", "");
        datas.put("FILEURL2", "");
        String whereSql = "MOBILETYPE='4'";
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("MASTERTABLE", "SNOW_APP_VERSION");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject("SNOW_APP_VERSION", datas, parametersMap);
            HttpUtils.doPost(HttpContacts.VERSION, jsonObject, new MyStringCallBack() {


                @Override
                public void onSuccess(String response) {
                    LogUtils.e("更新", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, "SNOW_APP_VERSION");
                    try {
                        List<VersionBean> versionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<VersionBean>>() {
                        });
                        if (versionBeen.size() > 0) {
                            String apkVersionCode = DownloadApk.getApkVersionCode(UIUtils.getContext());
                            if (Integer.valueOf(apkVersionCode) < versionBeen.get(0).getVERSIONCODE()) {
                                callback.onSuccess(versionBeen.get(0));
                            } else {
                                callback.noNew();
                            }
                        }
                        LogUtils.e(versionBeen.get(0).getFILEURL2());
                    } catch (Exception e) {
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
    //=================================问答相关==========================================================


    //===========================================================================================

    public static void loginOut(String account, String psw, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("USERACCOUNT", account);
        datas.put("LOGINPWD", psw);
        String whereSql = "";
        parametersMap.put("NOSUITUNIT", "true");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.paramLogin(TableName.UUM_USER, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.LOGOUT_URL, jsonObject, new LoginStringCallBack() {
                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                }

                @Override
                public void onSuccess(String response) {
                    callBack.onSuccess(response);
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            e.printStackTrace();
        }
    }

    /**
     * 查询个人信息  提示消息
     */
    public static void getUserInfoAndMsg(final CommonCallBack<MsgBean> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("INFOCOUNT", "");
        datas.put("PERINFOCOUNT", "");
        datas.put("QUESTIONCOUNT", "");
        datas.put("TEAMINFOCOUNT", "");
        datas.put("ANSWERCOUNT", "");
        String whereSql = "USERCODE ='" + DataUtils.getUserInfo().getUSERCODE() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.USERINFOMANAGE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_USER_BASE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "userbaseinfo");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_USER_BASE_INFO, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_USER_BASE_INFO);
                    try {
                        List<MsgBean> msgBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<MsgBean>>() {
                        });
                        callBack.onSuccess(msgBeen.get(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                }

                @Override
                public void loginTimeOut(boolean isLogin) {
//                    super.loginTimeOut(isLogin);
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            e.printStackTrace();
        }
    }

    /**
     * 我回答的问题
     */
    public static void getMyAnswerQuestionList(int index, String search, final CommonCallBack<List<QuestionBean>> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "ISLOSE=0 and USERCODE ='" + userInfo.getUSERCODE() + "'";
        if (!TextUtils.isEmpty(search)) {
            whereSql = whereSql + " and QUESTIONEXPLAIN like '%" + search + "%'";
        }
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.My_Answer);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_QUESTION_INFO_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + index);
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_QUESTION_INFO_LIST, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("我的回答" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_QUESTION_INFO_LIST);
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
//                                            bean.setQUESTIONTYPENAME(typebean.getQUESTIONTYPENAME());
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
            callBack.onFailure();
            e.printStackTrace();
        }
    }

    /**
     * 获取班组圈列表
     */
    public static void getTeamCircleList(final CommonCallBack<List<TeamCircleBean>> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区
        Map<String, String> datas = new HashMap<>();
        String whereSql = "";
        if (!TextUtils.isEmpty(TeamListActivity.SearchText)) {
            whereSql = "TEAMCIRCLENAME like '%" + TeamListActivity.SearchText + "%'";
            TeamListActivity.SearchText = "";
        }
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_INFO);
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
//        parametersMap.put("ORDERSQL", "teammembers desc,SYSCREATEDATE desc");
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", TeamListActivity.mCurrentCounter + "");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_TEAMCIRCLE_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("班组圈list", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_TEAMCIRCLE_INFO);
                    try {
                        List<TeamCircleBean> teamCircleBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamCircleBean>>() {
                        });
                        callBack.onSuccess(teamCircleBeen);
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
            callBack.onFailure();
        }
    }

    /**
     * 获取我加入的班组圈
     */
    public static void getMyJoinTeamList(final CommonCallBack<List<TeamCircleBean>> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区
        Map<String, String> datas = new HashMap<>();
        String whereSql = "USERCODE = '" + DataUtils.getUserInfo().getUSERCODE() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.TEAMUSERNAME);
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_USER_TEAMNAME);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAMCIRCLE_USER_TEAMNAME, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("我加入的团队", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_TEAMCIRCLE_USER_TEAMNAME);
                    try {
                        List<TeamCircleBean> teamCircleBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamCircleBean>>() {
                        });
                        LogUtils.e("--->", teamCircleBeen.size() + "");
                        callBack.onSuccess(teamCircleBeen);
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
            callBack.onFailure();
        }
    }

    /**
     * 获取单个我加入的班组圈信息
     */
    public static void getAnMyJoinTeamList(TeamDynamicBean teamDynamicBean, final CommonCallBack<List<TeamCircleBean>> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区
        Map<String, String> datas = new HashMap<>();
        String whereSql = "USERCODE = '" + DataUtils.getUserInfo().getUSERCODE() + "' and TEAMCODE = '" + teamDynamicBean.getTEAMCIRCLECODE() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.TEAMUSERNAME);
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_USER_TEAMNAME);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAMCIRCLE_USER_TEAMNAME, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
//                    LogUtils.e("动态获取圈子" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_TEAMCIRCLE_USER_TEAMNAME);
                    try {
                        List<TeamCircleBean> teamCircleBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamCircleBean>>() {
                        });
                        callBack.onSuccess(teamCircleBeen);
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
            callBack.onFailure();
        }
    }

    /**
     * 创建班组圈
     */
    public static void CreateTeamCircle(TeamCircleBean teamcirclebean, List<UserBean> userBeen, List<String> icon, final CommonCallBack<String> callBack) {
        String operaterUserCode = DataUtils.getUserInfo().getUSERCODE();//操作者
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("TEAMCIRCLENAME", teamcirclebean.getTEAMCIRCLENAME());
        datas.put("TEAMCIRCLECODENAME", teamcirclebean.getTEAMCIRCLECODENAME());
        datas.put("TEAMCIRCLECODE", teamcirclebean.getTEAMCIRCLECODE());
        datas.put("TEAMMANIFESTO", teamcirclebean.getTEAMMANIFESTO());//宣言
        datas.put("TEAMCIRCLEREMARK", teamcirclebean.getTEAMCIRCLEREMARK());
        List<Map<String, Map<String, String>>> list = new ArrayList<>();
        if (userBeen != null && userBeen.size() > 0) {
            for (UserBean userBean : userBeen) {
                // 从表数据
                Map<String, Map<String, String>> dataMap1 = new HashMap<String, Map<String, String>>();
                Map<String, String> datas1 = new HashMap<String, String>();
                if (userBean.getUSERCODE().equals(operaterUserCode)) {
                    datas1.put("USERCODE", userBean.getUSERCODE());
                    datas1.put("USERTYPE", UserType.holder);
                } else {
                    datas1.put("USERCODE", userBean.getUSERCODE());
                    datas1.put("USERTYPE", UserType.member);
                }
                dataMap1.put(TableName.KLB_TEAMCIRCLE_REL_USER, datas1);
                list.add(dataMap1);
            }
        }
//        datas.put("SEQKEY",);//修改时候
//        parametersMap.put("METHOD", UrlMethod.ADDSAVE);//改成updata


        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_TEAMCIRCLE_MANAGER);
        parametersMap.put("METHOD", UrlMethod.ADDSAVE);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_INFO);
        parametersMap.put("DETAILTABLE", TableName.KLB_TEAMCIRCLE_REL_USER);
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "TEAMCIRCLECODE");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject3(TableName.KLB_TEAMCIRCLE_INFO, datas, parametersMap, list);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPostFile(HttpContacts.UI_URL, jsonObject, icon, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("创建团队", response);
                    callBack.onSuccess("创建成功");
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

    /**
     * 修改班组圈信息
     */
    public static void EditTeamCircleInfo(TeamCircleBean teamcirclebean, List<UserBean> userBeen, List<String> icon, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        if (icon != null && icon.size() != 0) {
            datas.put("FILEURL", "");//有图片加此参数 修改头像
        }
        datas.put("TEAMCIRCLENAME", teamcirclebean.getTEAMCIRCLENAME());
        datas.put("TEAMCIRCLECODENAME", teamcirclebean.getTEAMCIRCLECODENAME());
        datas.put("TEAMCIRCLECODE", teamcirclebean.getTEAMCIRCLECODE());
        datas.put("TEAMMANIFESTO", teamcirclebean.getTEAMMANIFESTO());//宣言
        datas.put("TEAMCIRCLEREMARK", teamcirclebean.getTEAMCIRCLEREMARK());
        datas.put("JMESSAGEGROUPID", teamcirclebean.getJMESSAGEGROUPID());
        List<Map<String, Map<String, String>>> list = new ArrayList<>();
        if (userBeen != null && userBeen.size() > 0) {
            for (UserBean userBean : userBeen) {
                // 从表数据
                Map<String, Map<String, String>> dataMap1 = new HashMap<String, Map<String, String>>();
                Map<String, String> datas1 = new HashMap<String, String>();
//                if (userBean.getUSERCODE().equals(operaterUserCode)) {
//                    datas1.put("USERCODE", userBean.getUSERCODE());
//                    datas1.put("USERTYPE", UserType.holder);
//                } else {
                if (TextUtils.isEmpty(userBean.getUSERTYPE())) {
                    datas1.put("USERCODE", userBean.getUSERCODE());
                    datas1.put("USERTYPE", UserType.member);
                } else {
                    datas1.put("USERCODE", userBean.getUSERCODE());
                    datas1.put("USERTYPE", userBean.getUSERTYPE());
                }
//                }
                dataMap1.put(TableName.KLB_TEAMCIRCLE_REL_USER, datas1);
                list.add(dataMap1);
            }
        }
        datas.put("SEQKEY", teamcirclebean.getSEQKEY());//修改时候
//        LogUtils.e("SEQKEY" + teamcirclebean.getSEQKEY());
//        parametersMap.put("METHOD", UrlMethod.UPDATASAVE);//改成updata

        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_TEAMCIRCLE_MANAGER);
        parametersMap.put("METHOD", "GroupUserMan");
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_INFO);
        parametersMap.put("DETAILTABLE", TableName.KLB_TEAMCIRCLE_REL_USER);
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "TEAMCIRCLECODE");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject3(TableName.KLB_TEAMCIRCLE_INFO, datas, parametersMap, list);
            HttpUtils.doPostFile(HttpContacts.UI_URL, jsonObject, icon, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
//                    LogUtils.e(response);
                    callBack.onSuccess("修改成功");
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("修改成功" + error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 获取班组圈成员列表
     */
    public static void EditTeamGetMemberInfo(TeamCircleBean teamcirclebean, final CommonCallBack<List<UserBean>> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区
        Map<String, String> datas = new HashMap<>();
        datas.put("TEAMCIRCLECODE", teamcirclebean.getSEQKEY());
        datas.put("USERCODE", "");
        datas.put("USERNAME", "");
        datas.put("USERTYPE", "");
        datas.put("FILEURL", "");
        datas.put("SEQKEY", "");
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_USER_INFO);
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "USERTYPE DESC");
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_REL_USER);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_TEAMCIRCLE_REL_USER, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("班组圈成员", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_TEAMCIRCLE_REL_USER);
                    try {
                        List<UserBean> userBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<UserBean>>() {
                        });
                        callBack.onSuccess(userBeen);
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
//            callBack.onFailure();
        }
    }

    /**
     * 获取个人信息
     *
     * @param account
     * @param callBack
     */
    public static void getUserInfo(String account, final CommonCallBack<List<UserBean>> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("USERACCOUNT", "");
        datas.put("PASSWORD", "");
        datas.put("USERNAME", "");
        datas.put("USERCODE", "");
        datas.put("SEQKEY", "");
        datas.put("EXPERTTYPE", "");
        datas.put("EXPERTFRADE", "");
        datas.put("SUMPOINTS", "");
        datas.put("EXPERTSTATUS", "");
        datas.put("NICKNAME", "");
        datas.put("FILEURL", "");
        datas.put("LEVELNAME", "");
        String whereSql = "(USERCODE like'%" + account + "%' or USERNAME like '%" + account + "%')";
        parametersMap.put("CLASSNAME", UrlMethod.USER_INFO);
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.V_KLB_USER_BASE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_USER_BASE_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_USER_BASE_INFO);
                    try {
                        List<UserBean> userBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<UserBean>>() {
                        });
                        callBack.onSuccess(userBeen);
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
            callBack.onFailure();
        }
    }

    /**
     * 团队问题搜索
     */
    public static void teamQustionSearch(String searchtext, int index, TeamCircleBean teamcirclebean, final CommonCallBack<List<QuestionBean>> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "TEAMCIRCLECODE ='" + teamcirclebean.getSEQKEY() + "' and ( targetusercode is null or targetusercode like '%" + userInfo.getUSERCODE() + "%' ) and QUESTIONEXPLAIN like '%" + searchtext + "%'";
//        LogUtils.e("sousuo",whereSql);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_QUESTION);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + index);
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO);
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
//                                        // TODO: 2017/2/20  处理标签数据
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
//                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 班组圈你问我答
     */
    public static void teamQuestionAction(int index, TeamCircleBean teamcirclebean, final CommonCallBack<List<QuestionBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "ISSOLVE='0' and TEAMCIRCLECODE ='" + teamcirclebean.getSEQKEY() + "' and TARGETUSERCODE is NULL";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_QUESTION);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + index);
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO);
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
//                                        // TODO: 2017/2/20  处理标签数据
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
     * 班组圈已解决问题
     */
    public static void teamQuestionSolve(int index, TeamCircleBean teamcirclebean, final CommonCallBack<List<QuestionBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "ISSOLVE='1' and TEAMCIRCLECODE ='" + teamcirclebean.getSEQKEY() + "'";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_QUESTION);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + index);
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO);
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
//                                        // TODO: 2017/2/20  处理标签数据
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
     * 班组圈挑战
     */
    public static void teamQuestionChallenge(int index, TeamCircleBean teamcirclebean, final CommonCallBack<List<QuestionBean>> callBack) {
        String usercode = DataUtils.getUserInfo().getUSERCODE();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "ISSOLVE='0' and TEAMCIRCLECODE ='" + teamcirclebean.getSEQKEY() + "'and TARGETUSERCODE like '%" + usercode + "%'";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_QUESTION);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + index);
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
//                    LogUtils.e("班组圈挑战" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO);
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
     * 班组圈我的问题
     */
    public static void teamQuestionMyQuestion(int index, TeamCircleBean teamcirclebean, final CommonCallBack<List<QuestionBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "TEAMCIRCLECODE ='" + teamcirclebean.getSEQKEY() + "' and QUESTIONUSERCODE ='" + DataUtils.getUserInfo().getUSERCODE() + "'";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_QUESTION);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + index);
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
//                    LogUtils.e("我的问题" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_TEAMCIRCLE_QUESTION_INFO);
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
            callBack.onFailure();
            e.printStackTrace();
        }
    }

    /**
     * 班组圈问题答案列表
     */
    public static void getTeamAnswerList(QuestionBean questionBean, final CommonCallBack<List<AnswerBean>> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区
        String operatetype;
        if (userInfo.getUSERCODE().equals(questionBean.getQUESTIONUSERCODE())) {
            operatetype = "2";
        } else {
            operatetype = "1";
        }
        Map<String, String> datas = new HashMap<>();
        String whereSql = "QUESTIONID = '" + questionBean.getSEQKEY() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_ANSWERGOOD);
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "ISPASS desc,GOODNUMS desc,QACOUNT desc,SYSCREATEDATE asc");
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_ANSWER_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        parametersMap.put("OPERATETYPE", operatetype);
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_TEAMCIRCLE_ANSWER_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("回答列表", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_TEAMCIRCLE_ANSWER_INFO);
                    try {
                        List<AnswerBean> teamCircleBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<AnswerBean>>() {
                        });
                        callBack.onSuccess(teamCircleBeen);
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
            callBack.onFailure();
        }
    }

    /**
     * 班组圈提问方法
     */
    public static void teamAskQuestionMyQuestion(QuestionBean questionbean, List<UserBean> userBeen, boolean isEdit, List<String> files, final CommonCallBack<String> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> parametersMap = new HashMap<>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("SYSCREATEDATE", DateUtils.dataFormatNow("yyyy-MM-dd HH:mm:ss"));
        datas.put("SEQKEY", questionbean.getSEQKEY());
        datas.put("QUESTIONEXPLAIN", questionbean.getQUESTIONEXPLAIN());
        datas.put("QUESTIONTYPECODE", questionbean.getQUESTIONTYPECODE());
        if (!isEdit) {
//            LogUtils.e("插入");
            datas.put("QUESTIONUSERNAME", userInfo.getUSERNAME());
            datas.put("QUESTIONUSERCODE", userInfo.getUSERCODE());
            datas.put("BONUSPOINTS", questionbean.getBONUSPOINTS());
            datas.put("ISSOLVE", "0");
            datas.put("QUESTIONLEVEL", "1");
            datas.put("ISANONYMITY", "0");//0 不匿名  1 匿名
            datas.put("IMPORTLEVEL", "1");
            datas.put("ISLOSE", "0");
        } else {
            if (files != null && files.size() > 0) {
                datas.put("FILEURL", "");
            }
        }
        if (userBeen != null && userBeen.size() > 0) {
            String target = "";
            String name = "";
            for (int i = 0; i < userBeen.size(); i++) {
                if (i == 0) {
                    target = target + userBeen.get(i).getUSERCODE();
                    name = name + userBeen.get(i).getUSERNAME();
                } else {
                    target = target + "," + userBeen.get(i).getUSERCODE();
                    name = name + "," + userBeen.get(i).getUSERNAME();
                }
            }
            datas.put("TARGETUSERCODE", target);//指定人员回答
            datas.put("TARGETUSERNAME", name);//指定人员回答名称
        } else {
            datas.put("TARGETUSERCODE", questionbean.getTARGETUSERCODE());//指定人员回答
            datas.put("TARGETUSERNAME", questionbean.getTARGETUSERNAME());//指定人员回答
        }

        datas.put("ISEXPERTANSWER", "0");
        datas.put("TEAMCIRCLECODE", questionbean.getTEAMCIRCLECODE());
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_QUESTION);
        parametersMap.put("METHOD", isEdit ? UrlMethod.UPDATASAVE : UrlMethod.ADDSAVE);//修改或者新提问
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAMCIRCLE_QUESTION_INFO, datas, parametersMap);
//            LogUtils.e("提问指定", jsonObject.toString());
            HttpUtils.doPostFile(HttpContacts.UI_URL, jsonObject, files, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
//                    LogUtils.e("提问指定", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_TEAMCIRCLE_QUESTION_INFO);
                    try {
                        callBack.onSuccess(response);
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
            callBack.onFailure();
        }

    }

    /**
     * 班组圈点赞方法
     */
    public static void teamAnserGood(QuestionBean questionBean, AnswerBean answerBean, final CommonCallBack<String> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区
        Map<String, String> datas = new HashMap<>();
        datas.put("ANSWERID", answerBean.getSEQKEY());
        datas.put("USERCODE", userInfo.getUSERCODE());
        datas.put("QUESTIONID", questionBean.getSEQKEY());
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.APP_BIZ_OPERATION);
        parametersMap.put("METHOD", UrlMethod.ADDSAVE);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "ISPASS desc,GOODNUMS desc,QACOUNT desc,SYSCREATEDATE asc");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_ANSWER_GOOD);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAMCIRCLE_ANSWER_GOOD, datas, parametersMap);
            LogUtils.e("点赞" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
//                    LogUtils.e("点赞" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_TEAMCIRCLE_ANSWER_GOOD);
                    try {
                        callBack.onSuccess("已点赞");
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
            callBack.onFailure();
        }
    }

    /**
     * 班组圈采纳方法
     */
    public static void teamTakeAnswer(AnswerBean answerBean, final CommonCallBack<String> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区
        Map<String, String> datas = new HashMap<>();
        datas.put("SEQKEY", answerBean.getSEQKEY());
        datas.put("QUESTIONID", answerBean.getQUESTIONID());
        datas.put("ISPASS", "1");
        datas.put("ANSWERUSERCODE", answerBean.getANSWERUSERCODE());
        Map<String, String> datas1 = new HashMap<>();
        datas1.put("ISSOLVE", "1");
        HashMap<String, Map<String, String>> stringMapHashMap = new HashMap<>();
        stringMapHashMap.put(TableName.KLB_TEAMCIRCLE_ANSWER_INFO, datas);
        stringMapHashMap.put(TableName.KLB_TEAMCIRCLE_QUESTION_INFO, datas1);
        ArrayList<Map<String, Map<String, String>>> objects = new ArrayList<>();
        objects.add(stringMapHashMap);
//        LogUtils.e("采纳list"+stringMapHashMap.size());
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_ANSWER_TAKE);
        parametersMap.put("METHOD", UrlMethod.UPDATASAVE);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_ANSWER_INFO);
        parametersMap.put("DETAILTABLE", TableName.KLB_TEAMCIRCLE_QUESTION_INFO);
        parametersMap.put("MASTERFIELD", "QUESTIONID");
        parametersMap.put("DETAILFIELD", "SEQKEY");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(objects, parametersMap);
            LogUtils.e("采纳传入" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
//                    LogUtils.e("采纳" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_TEAMCIRCLE_ANSWER_INFO);
                    try {
                        callBack.onSuccess("已采纳");
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
            callBack.onFailure();
        }
    }

    /**
     * 班组圈回答方法
     */
    public static void teamAnswerQuestion(AnswerBean answerBean, final CommonCallBack<String> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区
        Map<String, String> datas = new HashMap<>();
        datas.put("SEQKEY", answerBean.getSEQKEY());
        datas.put("QUESTIONID", answerBean.getQUESTIONID());
        datas.put("ISPASS", "1");
        datas.put("ANSWERUSERCODE", answerBean.getANSWERUSERCODE());
        Map<String, String> datas1 = new HashMap<>();
        datas1.put("ISSOLVE", "1");
        HashMap<String, Map<String, String>> stringMapHashMap = new HashMap<>();
        stringMapHashMap.put(TableName.KLB_TEAMCIRCLE_ANSWER_INFO, datas);
        stringMapHashMap.put(TableName.KLB_TEAMCIRCLE_QUESTION_INFO, datas1);
        ArrayList<Map<String, Map<String, String>>> objects = new ArrayList<>();
        objects.add(stringMapHashMap);
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_ANSWER_TAKE);
        parametersMap.put("METHOD", UrlMethod.ADDSAVE);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "ISPASS desc,GOODNUMS desc,QACOUNT desc,SYSCREATEDATE asc");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_ANSWER_INFO);
        parametersMap.put("DETAILTABLE", TableName.KLB_TEAMCIRCLE_QUESTION_INFO);
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(objects, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
//                    LogUtils.e("采纳" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_TEAMCIRCLE_ANSWER_INFO);
                    try {
                        callBack.onSuccess("已点赞");
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
            callBack.onFailure();
        }
    }

    /**
     * 获取一条答案
     *
     * @param bean
     * @param callBack
     */
    public static void getTeamAnAnserData(QuestionBean bean, final CommonCallBack<List<AnswerBean>> callBack) {
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
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_ANSWERGOOD);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_ANSWER_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "1");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_TEAMCIRCLE_ANSWER_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
//                    LogUtils.e("团队班组圈 获取单条回答" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_TEAMCIRCLE_ANSWER_INFO);
                    try {
                        List<AnswerBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<AnswerBean>>() {
                        });
                        callBack.onSuccess(questionBeen);
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
            callBack.onFailure();
        }
    }

    /**
     * 申请加入班组圈
     */
    public static void applyJoinTeam(TeamCircleBean teamcirclebean, final CommonCallBack<String> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();//操作者
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("USERCODE", userInfo.getUSERCODE());
        datas.put("USERTYPE", UserType.preliminarymember);
        datas.put("TEAMCIRCLECODE", teamcirclebean.getSEQKEY());
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.APP_BIZ_OPERATION);
        parametersMap.put("METHOD", UrlMethod.ADDSAVE);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_REL_USER);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAMCIRCLE_REL_USER, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("申请加入班组圈" + response);
                    callBack.onSuccess(response);
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

    /**
     * 退出班组圈
     */
    public static void quiteTeam(TeamCircleBean teamcirclebean, UserBean userInfo, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
//        datas.put("USERCODE", userInfo.getUSERCODE());
//        datas.put("TEAMCIRCLECODE", teamcirclebean.getSEQKEY());
        datas.put("seqkey", userInfo.getSEQKEY());
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_TEAMCIRCLE_MANAGER);
        parametersMap.put("METHOD", "GroupUserQuit");
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_REL_USER);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        parametersMap.put("GROUPID", teamcirclebean.getJMESSAGEGROUPID());
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAMCIRCLE_REL_USER, datas, parametersMap);
            LogUtils.e("退出班组圈儿传入" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("退出班组圈儿" + response);
                    callBack.onSuccess(response);
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

    /**
     * 修改班组成员状态  审核通过 升级管理员
     */
    public static void agreeJoinTeam(TeamCircleBean teamcirclebean, String usertype, boolean domanager, UserBean userInfo, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("USERCODE", userInfo.getUSERCODE());
        datas.put("USERTYPE", usertype);
        datas.put("USERNAME", userInfo.getUSERNAME());
        datas.put("seqkey", userInfo.getSEQKEY());
        datas.put("TEAMCIRCLECODE", teamcirclebean.getSEQKEY());
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_TEAMCIRCLE_MANAGER);
        parametersMap.put("METHOD", "groupUserCheck");
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_REL_USER);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        parametersMap.put("GROUPID", teamcirclebean.getJMESSAGEGROUPID());

        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        if (domanager) {
            if (UserType.manager.equals(usertype)) {
                commandMap.put("actionflag", "addManager");
            } else if (UserType.member.equals(usertype)) {
                commandMap.put("actionflag", "removerManager");
            }
        } else {
            commandMap.put("actionflag", "select");
        }
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_TEAMCIRCLE_REL_USER, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("同意加入班组圈" + response);
                    callBack.onSuccess(response);
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

    /**
     * 转移团队
     *
     * @param teamcirclebean 团队信息
     * @param userInfo       新团长
     * @param userBeanList   成员列表
     * @param callBack       回调
     */
//    public static void TranferTeam(TeamCircleBean teamcirclebean, UserBean userInfo, List<UserBean> userBeanList, final CommonCallBack<String> callBack) {
//        UserBean userInfo1 = DataUtils.getUserInfo();//操作人的信息
//        Map<String, String> parametersMap = new HashMap<>();
//        // 数据区域
//        Map<String, String> datas = new HashMap<>();
//        datas.put("SEQKEY", teamcirclebean.getSEQKEY());
//        datas.put("SYSCREATORID", userInfo.getUSERCODE());
//        datas.put("JMESSAGEGROUPID", teamcirclebean.getJMESSAGEGROUPID());
//        String whereSql = "";
//        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_TEAMCIRCLE_MANAGER);
//        parametersMap.put("METHOD", "GroupUserTran");
//        parametersMap.put("MENUAPP", "EMARK_APP");
//        parametersMap.put("WHERESQL", whereSql);
//        parametersMap.put("ORDERSQL", "");
//        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_INFO);
//        parametersMap.put("DETAILTABLE", TableName.KLB_PROCIRCLE_REL_USER);
//        parametersMap.put("MASTERFIELD", "SEQKEY");
//        parametersMap.put("DETAILFIELD", "TEAMCIRCLECODE");
//        parametersMap.put("start", "0");
//        parametersMap.put("limit", "-1");
//        if (userBeanList != null && userBeanList.size() > 0) {
//            JSONArray fields = new JSONArray();
//            for (UserBean userBean : userBeanList) {
//                JSONObject tempJson = new JSONObject();
//                try {
//                    if (userBean.getUSERCODE().equals(userInfo.getUSERCODE())) {
//                        tempJson.put("USERCODE", userBean.getUSERCODE());
//                        tempJson.put("USERTYPE", UserType.holder);
//                    } else {
//                        tempJson.put("USERCODE", userBean.getUSERCODE());
//                        if (userBean.getUSERCODE().equals(userInfo1.getUSERCODE())) {
//                            tempJson.put("USERTYPE", UserType.member);
//                        } else {
//                            tempJson.put("USERTYPE", TextUtils.isEmpty(userBean.getUSERTYPE()) ? UserType.member : userBean.getUSERTYPE());
//                        }
//                    }
//                    fields.put(tempJson);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            parametersMap.put("USERCODELIST", fields.toString());
//        }
//        JSONObject jsonObject;
//        try {
//            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAMCIRCLE_INFO, datas, parametersMap);
//            LogUtils.e("转移团队传入" + jsonObject.toString());
//            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
//
//                @Override
//                public void onSuccess(String response) {
//                    LogUtils.e("转移团队" + response);
//                    callBack.onSuccess(response);
//                }
//
//                @Override
//                public void onFailure(String error) {
//                    callBack.onFailure();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//            callBack.onFailure();
//        }
//    }
    public static void TranferTeam(TeamCircleBean teamcirclebean, UserBean userInfo, List<UserBean> userBeanList, final CommonCallBack<String> callBack) {
        UserBean userInfo1 = DataUtils.getUserInfo();//操作人的信息
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("SEQKEY", teamcirclebean.getSEQKEY());
        datas.put("SYSCREATORID", userInfo.getUSERCODE());
        datas.put("JMESSAGEGROUPID", teamcirclebean.getJMESSAGEGROUPID());
        List<Map<String, Map<String, String>>> list = new ArrayList<>();
        if (userBeanList != null && userBeanList.size() > 0) {
            for (UserBean userBean : userBeanList) {
                // 从表数据
                Map<String, Map<String, String>> dataMap1 = new HashMap<String, Map<String, String>>();
                Map<String, String> datas1 = new HashMap<String, String>();
                if (userBean.getUSERCODE().equals(userInfo.getUSERCODE())) {
                    datas1.put("USERCODE", userBean.getUSERCODE());
                    datas1.put("USERTYPE", UserType.holder);
                    datas1.put("USERNAME", userBean.getUSERNAME());
                } else {
                    datas1.put("USERCODE", userBean.getUSERCODE());
                    datas1.put("USERNAME", userBean.getUSERNAME());
                    if (userBean.getUSERCODE().equals(userInfo1.getUSERCODE())) {
                        datas1.put("USERTYPE", UserType.member);
                    } else {
                        datas1.put("USERTYPE", TextUtils.isEmpty(userBean.getUSERTYPE()) ? UserType.member : userBean.getUSERTYPE());
                    }
                }
                dataMap1.put(TableName.KLB_TEAMCIRCLE_REL_USER, datas1);
                list.add(dataMap1);
            }
        }
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_TEAMCIRCLE_MANAGER);
        parametersMap.put("METHOD", "GroupUserTran");
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_INFO);
        parametersMap.put("DETAILTABLE", TableName.KLB_TEAMCIRCLE_REL_USER);
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "TEAMCIRCLECODE");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        if (userBeanList != null && userBeanList.size() > 0) {
            JSONArray fields = new JSONArray();
            for (UserBean userBean : userBeanList) {
                JSONObject tempJson = new JSONObject();
                try {
                    if (userBean.getUSERCODE().equals(userInfo.getUSERCODE())) {
                        tempJson.put("USERCODE", userBean.getUSERCODE());
                        tempJson.put("USERTYPE", UserType.holder);
                        tempJson.put("USERNAME", userBean.getUSERNAME());
                    } else {
                        tempJson.put("USERCODE", userBean.getUSERCODE());
                        tempJson.put("USERNAME", userBean.getUSERNAME());
                        if (userBean.getUSERCODE().equals(userInfo1.getUSERCODE())) {
                            tempJson.put("USERTYPE", UserType.member);
                        } else {
                            tempJson.put("USERTYPE", TextUtils.isEmpty(userBean.getUSERTYPE()) ? UserType.member : userBean.getUSERTYPE());
                        }
                    }
                    fields.put(tempJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            parametersMap.put("USERCODELIST", fields.toString());
        }
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject3(TableName.KLB_TEAMCIRCLE_INFO, datas, parametersMap, list);
            LogUtils.e("转移团队传入" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("转移团队" + response);
                    callBack.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("转移团队error" + error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 解散团队
     */
    public static void disbanTeam(TeamCircleBean teamcirclebean, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("SEQKEY", teamcirclebean.getSEQKEY());
        datas.put("JMESSAGEGROUPID", teamcirclebean.getJMESSAGEGROUPID());
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_TEAMCIRCLE_MANAGER);
        parametersMap.put("METHOD", UrlMethod.DELETE);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAMCIRCLE_INFO, datas, parametersMap);
            LogUtils.e("解散团队传入" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("解散团队" + response);
                    callBack.onSuccess(response);
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

    /**
     * 班组圈动态
     */
    public static void getTeamDynamic(List<TeamCircleBean> teamCircleBeen, final CommonCallBack<List<TeamDynamicBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        String range = "";
        //加入圈子才去查询
        if (teamCircleBeen != null && teamCircleBeen.size() > 0) {
            for (int i = 0; i < teamCircleBeen.size(); i++) {
                if (i == 0) {
                    range = range + teamCircleBeen.get(i).getTEAMCODE();
                } else {
                    range = range + "," + teamCircleBeen.get(i).getTEAMCODE();
                }
            }
        } else {
            return;
        }
        whereSqlString = "TEAMCIRCLECODE in (" + range + ")";
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_TEAMCIRCLE_DYNAMIC);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_DYNAMIC_INFO_TEAM);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "30");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_DYNAMIC_INFO_TEAM, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("获取团队动态" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_DYNAMIC_INFO_TEAM);
                    try {
                        List<TeamDynamicBean> teamDynamicbeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamDynamicBean>>() {
                        });
                        callBack.onSuccess(teamDynamicbeen);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callBack.onFailure();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("获取团队动态error" + error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 获取专家信息
     *
     * @param professionalCircleBean
     * @param start                  起始索引
     * @param limit                  分页数量
     * @param callBack
     */
    public static void getProfessorList(ProfessionalCircleBean professionalCircleBean, int start, String limit, final CommonCallBack<List<Professor>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        if (!TextUtils.isEmpty(professionalCircleBean.getPROCIRCLECODE())) {
            whereSqlString = "PROCIRCLECODE ='" + professionalCircleBean.getPROCIRCLECODE() + "'";
        }
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.APP_BIZ_OPERATION);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.KLB_EXPERT_DETAIL);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", start + "");
        parametersMap.put("limit", TextUtils.isEmpty(limit) ? "20" : limit);
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_EXPERT_DETAIL, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("专家信息" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_EXPERT_DETAIL);
                    try {
                        List<Professor> teamDynamicbeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<Professor>>() {
                        });
                        callBack.onSuccess(teamDynamicbeen);
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
            callBack.onFailure();
        }
    }

    /**
     * 专家问题查询
     */
    public static void professorQustionSearch(String searchtext, int index, Professor professor, final CommonCallBack<List<QuestionBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where expertusercode like '%33002726%';
        String whereSql = "expertusercode like '%" + professor.getUSERCODE() + "%'";
        if (!TextUtils.isEmpty(searchtext)) {
            whereSql = whereSql + "and QUESTIONEXPLAIN like '%" + searchtext + "%' ";
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
                    LogUtils.e("专家回答", response);
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
//                                        // TODO: 2017/2/20  处理标签数据
                                        if (bean.getQUESTIONTYPECODE().equals(typebean.getQUESTIONTYPECODE())) {
//                                            bean.setQUESTIONTYPENAME(typebean.getQUESTIONTYPENAME());
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

    /**
     * 比一比
     */
    public static void getTeamCompare(TeamCompareDetailFragment.Type from, int index, String period, TeamCircleBean teamCircleBean, final CommonCallBack<List<TeamRankPersonBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("TEAMCIRCLECODE", teamCircleBean.getSEQKEY());
        datas.put("USERNAME", "");
        datas.put("FILEURL", "");
        datas.put("ANSWERSUM", "");
        datas.put("QUESTIONSUM", "");

        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where expertusercode like '%33002726%';
        String whereSql = "";
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("CLASSNAME", UrlMethod.TEAMCIRCLE_RANK);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("YEARMONTH", period);
        parametersMap.put("TEAMCIRCLECODE", teamCircleBean.getSEQKEY());
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_MEMBERRANK);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        if (QUESTION.equals(from)) {
            commandMap.put("actionflag", "TEAMCIRCLE_QUESTIONRANK");
        } else if (TeamCompareDetailFragment.Type.ANSWER.equals(from)) {
            commandMap.put("actionflag", "TEAMCIRCLE_ANSWERRANK");
        }
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_TEAMCIRCLE_MEMBERRANK, datas, parametersMap, commandMap);
            LogUtils.e("比一比", jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("比一比", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_TEAMCIRCLE_MEMBERRANK);
                    try {
                        List<TeamRankPersonBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamRankPersonBean>>() {
                        });
                        callBack.onSuccess(questionBeen);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("比一比error", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 练一练 考试  考试配置 练习配置
     */
    public static void getTestLibraryUrl(TeamCircleBean teamCircleBean, String type, final CommonCallBack<String> callBack) {
        Map<String, String> datas = new HashMap<String, String>();

        datas.put("SEQKEY", teamCircleBean.getSEQKEY());
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.TEXAM_CASE_TEAM);
        //ExamTeam 团队测试 DailyPractice 每日一练  CheckDeploy 考试配置 CheckDeploy 每日一练
        parametersMap.put("METHOD", type);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_TEAMCIRCLE_INFO, datas, parametersMap);
            LogUtils.e("每日一练", jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("每日一练", response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        callBack.onSuccess(target);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("每日一练", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 行业规范
     *
     * @param limit                  条数
     * @param index                  查询索引
     * @param type                   查询类型 最热 最新
     * @param professionalCircleBean
     * @param callBack               回调
     */
    public static void getIndustryStandard(QusetionTypeBean mType, String limit, int index, String type, ProfessionalCircleBean professionalCircleBean, final CommonCallBack<List<IndustryStandardBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where expertusercode like '%33002726%';
        String whereSql = "PROCIRCLECODE like '%" + professionalCircleBean.getPROCIRCLECODE() + "%'";
        if (mType != null) {
            String code = mType.getCODE();
            if (!TextUtils.isEmpty(code))
                whereSql = "QUESTIONTYPECODE like '%" + code + "%'";
        }
        if (UrlMethod.TYPE_HOT.equals(type)) {
            parametersMap.put("ORDERSQL", "CLICKCOUNT desc");
        } else {
            parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        }
        parametersMap.put("CLASSNAME", UrlMethod.STANDARD_INFO);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_STANDARD_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", TextUtils.isEmpty(limit) ? "" + UrlMethod.PAZE_SIZE : limit);
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "PROCIRCLE_STANDARD_INFO");
        if (mType != null && "-1".equals(mType.getPARENTID())) {
            parametersMap.put("WHERESQL", "");
            parametersMap.put("PARENTID", "-1");
            parametersMap.put("TYPECODE", mType.getCODE());
        }
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_STANDARD_INFO, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("行业规范", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_STANDARD_INFO);
                    try {
                        List<IndustryStandardBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<IndustryStandardBean>>() {
                        });
                        callBack.onSuccess(questionBeen);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("行业规范error", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getIndustryStandardByName(int index, String search, final CommonCallBack<List<IndustryStandardBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where expertusercode like '%33002726%';
        String whereSql = "STANDARDNAME like '%" + search + "%'";
        parametersMap.put("ORDERSQL", "CLICKCOUNT desc");
        parametersMap.put("CLASSNAME", UrlMethod.STANDARD_INFO);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_STANDARD_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "PROCIRCLE_STANDARD_INFO");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_STANDARD_INFO, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("行业规范搜索", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_STANDARD_INFO);
                    try {
                        List<IndustryStandardBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<IndustryStandardBean>>() {
                        });
                        callBack.onSuccess(questionBeen);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("行业规范搜索error", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 行业规范
     */
    public static void clickIndustryStandard(IndustryStandardBean bean, final CommonCallBack<String> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("SEQKEY", bean.getSEQKEY());
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();

//        select * from klb_question_info where expertusercode like '%33002726%';
        String whereSql = "";
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("CLASSNAME", UrlMethod.STANDARD_CLICKCOUNT);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_STANDARD_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_STANDARD_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("行业规范点击", response);
                    callBack.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("行业规范点击error", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 经典案列点击量增加
     */
    public static void clickClassicCase(String id, final CommonCallBack<String> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("SEQKEY", id);
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        // String whereSql =
        // "ISLOSE=0 and ISEXPERTANSWER = 2 and QUESTIONEXPLAIN like '%"
        // + searchStr + "%'";
        parametersMap.put("CLASSNAME", UrlMethod.CLICK_COUNT);
        parametersMap.put("METHOD", "search");// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("MASTERTABLE", TableName.KLB_CLASSICCASE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_CLASSICCASE_INFO, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("经典案列点击", response);
                    callBack.onSuccess(response);
                }

                @Override
                public void loginTimeOut(boolean isLogin) {
//                    super.loginTimeOut(isLogin);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("经典案例点击", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 获取团队通知列表
     */
    public static void getTeamMessage(int index, TeamCircleBean teamCircleBean, final CommonCallBack<List<TeamMessageBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
//        datas.put("TEAMID",teamCircleBean.getSEQKEY());
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "TEAMID = '" + teamCircleBean.getSEQKEY() + "'";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.TEAMMESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.KLB_MESSAGE_SEND);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "messageList");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_MESSAGE_SEND, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("团队通知" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_MESSAGE_SEND);
                    try {
                        List<TeamMessageBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamMessageBean>>() {
                        });
                        if (questionBeen != null && questionBeen.size() > 0) {
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
                    LogUtils.e("团队消息列表error", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 发送团队通知
     */
    public static void sendTeamMessage(TeamMessageBean teamMessageBean, String functionCode, final CommonCallBack<String> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        datas.put("TEAMID", teamMessageBean.getTEAMID());
        datas.put("MESSAGE", teamMessageBean.getMESSAGE());
        datas.put("REMARK", teamMessageBean.getREMARK());
        datas.put("URERIF", teamMessageBean.getUSERID());
        datas.put("ISRECEIPT", teamMessageBean.getISRECEIPT());
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "";
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("CLASSNAME", UrlMethod.TEAMMESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.ADDSAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.KLB_MESSAGE_SEND);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        switch (functionCode) {
            case "1":
                commandMap.put("actionflag", "sendmessage");//1.系统发送 2，极光发送
                break;
            case "2":
                commandMap.put("actionflag", "sendTeamMes");//1.系统发送 2，极光发送
                break;
        }
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_MESSAGE_SEND, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("团队发送消息" + response);
//                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_MESSAGE_SEND);
//                    try {
//                        List<TeamMessageBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamMessageBean>>() {
//                        });
                    callBack.onSuccess(response);
//                    } catch (Exception e) {
//                        callBack.onFailure();
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("团队发送消息列表error", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 删除团队通知
     */
    public static void deletTeamMessage(boolean isClear, TeamMessageBean teamMessageBean, final CommonCallBack<String> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        datas.put("SEQKEY", teamMessageBean.getSEQKEY());
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "";
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("CLASSNAME", UrlMethod.TEAMMESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.DELETE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.KLB_MESSAGE_SEND);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "messageDelete");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_MESSAGE_SEND, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("团队删除消息" + response);
//                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_MESSAGE_SEND);
//                    try {
//                        List<TeamMessageBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamMessageBean>>() {
//                        });
                    callBack.onSuccess(response);
//                    } catch (Exception e) {
//                        callBack.onFailure();
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("团队删除消息error", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 团队通知未回执
     */
    public static void getTeamMessageNoResponse(int index, TeamMessageBean teamMessageBean, final CommonCallBack<List<TeamMsgStatsUserBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "sendid = '" + teamMessageBean.getSEQKEY() + "'";
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("CLASSNAME", UrlMethod.TEAMMESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_MESSAGE_REC_N);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "messageDetail");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_MESSAGE_REC_N, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("团队消息明细" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_MESSAGE_REC_N);
                    try {
                        List<TeamMsgStatsUserBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamMsgStatsUserBean>>() {
                        });
                        callBack.onSuccess(questionBeen);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("团队消息列表明细", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 团队通知回执
     */
    public static void getTeamMessageResponse(int index, TeamMessageBean teamMessageBean, final CommonCallBack<List<TeamMsgStatsUserBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "sendid='" + teamMessageBean.getSEQKEY() + "'";
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("CLASSNAME", UrlMethod.TEAMMESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_MESSAGE_REC_Y);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "messageDetail");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_MESSAGE_REC_Y, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("团队消息明细" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_MESSAGE_REC_Y);
                    try {
                        List<TeamMsgStatsUserBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamMsgStatsUserBean>>() {
                        });
                        callBack.onSuccess(questionBeen);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("团队消息列表明细", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 个人消息列表
     */
    public static void getPersonMessage(int index, final CommonCallBack<List<PersonMsgBean>> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "USERCODE='" + userInfo.getUSERCODE() + "'";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.PERSON_MESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_DYNAMIC_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "messageList");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_DYNAMIC_INFO, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("个人消息明细" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_DYNAMIC_INFO);
                    try {
                        List<PersonMsgBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<PersonMsgBean>>() {
                        });
                        callBack.onSuccess(questionBeen);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("个人消息列表明细", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 个人消息删除
     */
    public static void deletPersonMessage(boolean isClear, PersonMsgBean personMsgBean, final CommonCallBack<String> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        if (!isClear) {
            datas.put("SEQKEY", personMsgBean.getSEQKEY());
        }
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "USERCODE='" + userInfo.getUSERCODE() + "'";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.PERSON_MESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.UPDATASAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.KLB_DYNAMIC_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        if (isClear) {
            commandMap.put("actionflag", "messageDeleteAll");
        } else {
            commandMap.put("actionflag", "messageDelete");
        }
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_DYNAMIC_INFO, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("个人删除" + response);
                    callBack.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("个人消息删除失败", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 个人消息删除
     */
    public static void deletSomePersonMessage(List<PersonMsgBean> personMsgBean, final CommonCallBack<String> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        String seqkey = "";
        for (int i = 0; i < personMsgBean.size(); i++) {
            if (i == 0)
                seqkey = personMsgBean.get(i).getSEQKEY();
            seqkey += "," + personMsgBean.get(i).getSEQKEY();
        }
        datas.put("SEQKEY", seqkey);
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "USERCODE='" + userInfo.getUSERCODE() + "'";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.PERSON_MESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.UPDATASAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.KLB_DYNAMIC_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "messageDelete");

        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_DYNAMIC_INFO, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("个人删除" + response);
                    callBack.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("个人消息删除失败", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 获取个人团队消息回执状态
     */
    public static void getPersonTeamMsgStatus(TeamMessageBean messageBean, final CommonCallBack<List<TeamMsgStatsUserBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "SENDID='" + messageBean.getSEQKEY() + "' and USERID = '" + messageBean.getUSERID() + "'";
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("CLASSNAME", UrlMethod.TEAMMESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_MESSAGE_REC_Y);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "messageDetail");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_MESSAGE_REC_Y, datas, parametersMap, commandMap);
            LogUtils.e("查询个人回执状态发送", jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("查询个人回执状态" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_MESSAGE_REC_Y);
                    try {
                        List<TeamMsgStatsUserBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<TeamMsgStatsUserBean>>() {
                        });
                        if (questionBeen.size() > 0) {
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
                    LogUtils.e("查询个人回执状态", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 处理个人团队消息回执状态
     */
    public static void setPersonTeamMsghandle(String sqlkey, final CommonCallBack<String> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        datas.put("SEQKEY", sqlkey);
        datas.put("MESFLAG", "3");
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "";
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("CLASSNAME", UrlMethod.TEAMMESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.UPDATASAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.KLB_MESSAGE_REC);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "messageUpdateSave");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_MESSAGE_REC, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("回执" + response);
                    callBack.onSuccess(response);
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

    /**
     * 处理个人团队消息读取状态
     */
    public static void setPersonMsgReadStatus(boolean isAllRead, PersonMsgBean personMsgBean, final CommonCallBack<String> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        if (personMsgBean != null) {
            datas.put("SEQKEY", personMsgBean.getSEQKEY());
        }
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "";
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("CLASSNAME", UrlMethod.PERSON_MESSAGE_MANAGE);
        parametersMap.put("METHOD", isAllRead ? UrlMethod.UPDATASAVE : UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.KLB_DYNAMIC_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        if (personMsgBean != null) {
            commandMap.put("actionflag", "2".equals(personMsgBean.getMESTYPE()) ? "questionDetail" : "messageDetail");
        }
        if (isAllRead) {
            commandMap.put("actionflag", "updateReadFlag");
        }
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_DYNAMIC_INFO, datas, parametersMap, commandMap);
            LogUtils.e("团队消息读取状态发送", jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("团队消息读取状态" + response);
                    callBack.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("团队消息读取状态" + error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 系统消息获取
     */
    public static void getSystemMessage(int index, final CommonCallBack<List<SystemMsgBean>> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> datas = new HashMap<String, String>();
        //记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.PERSON_MESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.KLB_SYSTEM_NOTICE);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "messageSystem");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_SYSTEM_NOTICE, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("系统消息明细" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_SYSTEM_NOTICE);
                    try {
                        List<SystemMsgBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<SystemMsgBean>>() {
                        });
                        callBack.onSuccess(questionBeen);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("系统消息明细", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 个人问题删除
     */
    public static void setQuestionLose(QuestionBean questionbean, final CommonCallBack<String> callBack) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("SEQKEY", questionbean.getSEQKEY());
        datas.put("ISLOSE", "1");
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.QUESTION_POINTS);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.UPDATASAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", "KLB_QUESTION_INFO");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject("KLB_QUESTION_INFO", datas, parametersMap);
            LogUtils.e("修改问题" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e(response);
                    callBack.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                    LogUtils.e("提问失败" + error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    public static void getAnQuestion(QuestionBean questionbean, final CommonCallBack<List<QuestionBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "ISLOSE='0' and ISSOLVE='0' and QUESTIONLEVEL='1' and ISEXPERTANSWER='0' and SEQKEY = '" + questionbean.getSEQKEY() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.QUESTION_POINTS);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_QUESTION_INFO, datas, parametersMap);
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
     * 个人答案删除
     */
    public static void deletAnswer(QuestionBean questionbean, AnswerBean answerBean, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("QUESTIONID", questionbean.getSEQKEY());
        datas.put("ANSWERUSERCODE", answerBean.getANSWERUSERCODE());
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.My_Answer);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.DELETE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_ANSWER_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "DELETE_MY_ANSWER");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_ANSWER_INFO, datas, parametersMap, commandMap);
            LogUtils.e("删除答案" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e(response);
                    callBack.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                    LogUtils.e("删除答案失败" + error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    /**
     * 系统消息点击
     */
    public static void systemMessageClick(SystemMsgBean systemMsgBean, final CommonCallBack<String> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("SEQKEY", systemMsgBean.getSEQKEY());
        //记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where TEAMID like '%33002726%';
        String whereSql = "";
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("CLASSNAME", UrlMethod.PERSON_MESSAGE_MANAGE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.KLB_SYSTEM_NOTICE);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "-1");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "systemNotice");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_SYSTEM_NOTICE, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("系统消息点击", response);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("系统消息点击错误", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
        }
    }

    /**
     * 获取功能链接
     */
    public static void getFunctionUrl3(String function, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.EXAMFUNCTION);
        parametersMap.put("METHOD", "ToHtml");
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", function);
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_TEAMCIRCLE_INFO, datas, parametersMap, commandMap);
            LogUtils.e("getfunctionurl3" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("getFunctionUrl3", response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        callBack.onSuccess(target);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            e.printStackTrace();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    /**
     * 个人消息 web类型功能
     * 获取功能链接  methon "ToHtmlPath", "";
     */
    public static void getFunctionUrl4(String function, String suburl, String mestype, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.EXAMFUNCTION);
        parametersMap.put("METHOD", function);
        parametersMap.put("URLPATH", suburl);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("MESTYPE", mestype);
        parametersMap.put("start", "0");
        parametersMap.put("limit", "1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", function);
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_TEAMCIRCLE_INFO, datas, parametersMap, commandMap);
            LogUtils.e("getfunctionurl4" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("getFunctionUrl4", response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        callBack.onSuccess(target);
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

    /**
     * 获取功能链接
     */
    public static void getFunctionUrl2(String function, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.EXAMCASETEAM);//com.nci.klb.app.exam.examCaseTeam
        parametersMap.put("METHOD", function);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.V_KLB_TEAMCIRCLE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_TEAMCIRCLE_INFO, datas, parametersMap, commandMap);
            LogUtils.e("getfunctionurl2" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("getFunctionUrl2", response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        callBack.onSuccess(target);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    /**
     * 推荐我的自定义菜单
     */
    public static void getFunctionList(final CommonCallBack<List<FunctionBean>> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.FUNCTION_LISTMANAGE);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.V_KLB_FUNCTION_USER_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "functionList");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_FUNCTION_USER_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e(response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_FUNCTION_USER_LIST);
                    try {
                        List<FunctionBean> functionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<FunctionBean>>() {
                        });
                        callBack.onSuccess(functionBeen);
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

    /**
     * 首页消息
     */
    public static void getMessageList(int index, final CommonCallBack<List<InfoBean>> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.MESSAGE_LIST);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_DYNAMIC_HOME_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + index);
        parametersMap.put("limit", "10");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "messageList");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_DYNAMIC_HOME_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e(response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_DYNAMIC_HOME_INFO);
                    try {
                        List<InfoBean> functionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<InfoBean>>() {
                        });
                        callBack.onSuccess(functionBeen);
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

    /**
     * 功能列表更多
     */
    public static void getMoreFunctionList(final CommonCallBack<List<FunctionBean>> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.FUNCTION_LISTMANAGE);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "funselect");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_FUNCTION_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e(response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_FUNCTION_LIST);
                    try {
                        List<FunctionBean> functionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<FunctionBean>>() {
                        });
                        callBack.onSuccess(functionBeen);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    /**
     * 功能编辑保存
     */
    public static void editFunctionEditSave(List<FunctionBean> functionBeanList, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        List<Map<String, Map<String, String>>> datasMap = new ArrayList<>();
        if (functionBeanList != null && functionBeanList.size() > 0) {
            int i = 1;
            for (FunctionBean functionBean : functionBeanList) {
                // 从表数据
                Map<String, Map<String, String>> dataMap1 = new HashMap<String, Map<String, String>>();
                Map<String, String> datas1 = new HashMap<String, String>();
                datas1.put("SORT", i + "");
                datas1.put("seqkey", functionBean.getSEQKEY());
                datas1.put("FUNCTIONCODE", functionBean.getFUNCTIONCODE());
                dataMap1.put(TableName.KLB_FUNCTION_USER_LIST, datas1);
                datasMap.add(dataMap1);
                i++;
            }
        }
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.FUNCTION_LISTMANAGE);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.UPDATASAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_USER_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "functionSave");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject5(datasMap, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    callBack.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    /**
     * 能力题库
     */
    public static void getQuestionBank(final CommonCallBack<AbilityBankBean> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.QUESTION_BANK);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "questionBank");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_FUNCTION_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("能力题库" + response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        AbilityBankBean abilityBankBean = JsonUtils.parseJson2Object(target, new TypeToken<AbilityBankBean>() {
                        });
                        callBack.onSuccess(abilityBankBean);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    /**
     * 能力题库里面的点击
     * 点击章节练习 METHOD： chapterPractice
     * 点击随机练习 METHOD： randomPractice
     * 点击章节练习 METHOD：  typechapterPractice(能力种类)
     * 点击模拟考试 METHOD： mockExam
     * 点击难题攻克 METHOD： problemTake
     * 点击每日一练 METHOD： dailyPractice
     * 点击我的错题 METHOD： errorSubject
     * 点击我的收藏 METHOD： myCollect
     * 点击练习记录 METHOD： exerciseRecord
     * 点击我的笔记 METHOD： myNotes
     * 实操规范 METHOD： PracticalNorm
     */
    public static void getQuestionBankDetail(String methon, String module_id, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.QUESTION_BANK);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", methon);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("module_id", module_id);
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "questionBank");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_FUNCTION_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("能力题库" + response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        callBack.onSuccess(target);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }


    public void getQuestionBankChapterPractice(String abilitytype, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.QUESTION_BANK);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", "typechapterPractice");// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("abilitytype", abilitytype);
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "questionBank");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_FUNCTION_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("能力类型章节练习" + response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        callBack.onSuccess(target);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    public static void getQuestionBankPracticalNorm(String methon, String codename, String module_code, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.QUESTION_BANK);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", methon);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put(codename, module_code);
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "questionBank");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_FUNCTION_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("能力题库" + response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        callBack.onSuccess(target);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    /**
     * 能力学堂 CourseHome   员工发展 DevelopNavigation  我的履历 myRecord  我的培训 myTrain 我的发展积分 developIntegral 我的获奖及荣誉 myAwards
     */
    public static void getFunctionUrl(String functionName, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME", UrlMethod.ZctHttpClient);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", functionName);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_FUNCTION_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("getFunctionUrl", response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        callBack.onSuccess(target);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    /**
     * 修改首页信息的状态
     */
    public static void setInfoIgnore(InfoBean infoBean, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();

        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        datas.put("SEQKEY", infoBean.getSEQKEY());
        datas.put("MES_STATE", "0");
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME", UrlMethod.MESSAGE_LIST);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.UPDATASAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_DYNAMIC_HOME_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "updateUserSave");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_DYNAMIC_HOME_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("修改消息状态" + response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        callBack.onSuccess(target);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }


    /**
     * 获取问题列表
     * from 进入的场景 1。首页 2课件首页 3课件明细
     */
    public static void getQuestionList(DynamicType from, UserBean userBean, QuestionBean questionBean, TypeQuestion typeQuestion, int starIndex, int limit, final CommonCallBack<List<QuestionBean>> callBack) {

        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        switch (typeQuestion) {
            case RECOMMEND:
                parametersMap.put("ORDERSQL", "SYSCREATEDATE,ANSWERSUM desc");
                whereSql = "ISLOSE='0' and ISSOLVE='0' and QUESTIONLEVEL = '1'";
                break;
            case NEWLY:
                parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
                whereSql = "ISLOSE='0' and ISSOLVE='0' and QUESTIONLEVEL= '1'";
                break;
            case REWARD:
                whereSql = "ISLOSE = '0' and BONUSPOINTS IS NOT NULL and ISSOLVE = 0 and BONUSPOINTS != 0 ";
                parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
                break;
            case PERSONANSER:
                if (userBean == null) {
                    callBack.onFailure();
                    return;
                }
                whereSql = "ANSWERUSERCODE = '" + userBean.getUSERCODE() + "'";
//                whereSql = "ISANSWER = '1' and QUESTIONLEVEL = '1'";
                parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
                break;
            case PERSONQUESTION:
                if (userBean == null) {
                    callBack.onFailure();
                    return;
                }
                whereSql = "ISLOSE=0 and QUESTIONUSERCODE ='" + userBean.getUSERCODE() + "'";
                parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
                break;
            case SEARCH:
                if (questionBean == null) {
                    callBack.onFailure();
                    return;
                }
                whereSql = "(questionexplain like '%" + questionBean.getQUESTIONEXPLAIN() + "%' or (select klb_question_type.questiontypename from klb_question_type where klb_question_type.seqkey in (v_klb_question_info.questiontypecode)) like '%" + questionBean.getQUESTIONEXPLAIN() + "%')";
                if (!TextUtils.isEmpty(questionBean.getQUESTIONTYPECODE())) {
                    whereSql = "ISLOSE='0' and QUESTIONLEVEL='1' and QUESTIONTYPECODE like'%" + questionBean.getQUESTIONTYPECODE() + "%'";
                }
                parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
                break;
        }
        parametersMap.put("CLASSNAME", UrlMethod.QUESTION_POINTS);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + starIndex);
        parametersMap.put("limit", "" + limit);
        HashMap<String, String> commandMap = new HashMap<>();
        if (questionBean != null && "-1".equals(questionBean.getPARENTID())) {
            commandMap.put("mobileapp", "true");
            commandMap.put("actionflag", "SEARCH_PARENT_TYPE");
            parametersMap.put("CLASSNAME", "com.nci.klb.app.question.QuestionSearch");
            parametersMap.put("WHERESQL", "");
            parametersMap.put("PARENTID", "-1");
            parametersMap.put("TYPECODE", questionBean.getQUESTIONTYPECODE());
        } else {
            commandMap.put("mobileapp", "true");
            commandMap.put("actionflag", "select");
            switch (from) {
//                case Home:
//                    commandMap.put("actionflag", "select");
//                    break;
                case CourseWareHome:
                    if (typeQuestion == TypeQuestion.RECOMMEND) {
                        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
                        commandMap.put("actionflag", "qusetionpostype");
                    }
                    break;
                case CourseWareDetail:
                    if (typeQuestion == TypeQuestion.RECOMMEND) {
                        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
                        parametersMap.put("abilitycode", CourseDetailActivity.bean.getABILITYCODE());
                        commandMap.put("actionflag", "qusetionAbility");
                    }
                    break;
            }
        }
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_QUESTION_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("问题列表" + response);
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
//                                            bean.setQUESTIONTYPENAME(typebean.getQUESTIONTYPENAME());
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
                    LogUtils.e("问题查询error" + error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一条问题
     */
    public static void getAnQuestionBean(QuestionBean questionBean, final CommonCallBack<List<QuestionBean>> callBack) {
        Map<String, String> datas = new HashMap<>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<>();
        String whereSql = "SEQKEY = '" + questionBean.getSEQKEY() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.QUESTION_POINTS);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_QUESTION_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_QUESTION_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("单条问题" + response);
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
     * 问题点赞
     */
    public static void goodQuestion(QuestionBean questionBean, final CommonCallBack<String> callback) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("QUESTIONID", questionBean.getSEQKEY());
        datas.put("USERCODE", userInfo.getUSERCODE());
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        parametersMap.put("CLASSNAME", UrlMethod.ANSWER_GOOD);
        parametersMap.put("METHOD", "addSave");// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_ANSWER_GOOD);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "questionGood");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_ANSWER_GOOD, datas, parametersMap, commandMap);
            LogUtils.e("问题点赞", jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("问题点赞成功" + response);
                    callback.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    callback.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailure();
        }
    }

    public static void getAnswerList(QuestionBean bean, int mCurrent, String orderType, final CommonCallBack<List<AnswerBean>> callBack) {
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
        parametersMap.put("orderstr", orderType);
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//new
        parametersMap.put("MASTERTABLE", TableName.V_KLB_ANSWER_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + mCurrent);
        parametersMap.put("limit", "20");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "answerOrder");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_ANSWER_INFO, datas, parametersMap, commandMap);
            LogUtils.json(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
//                    LogUtils.e("回答列表数据" + response);
                    LogUtils.json(response);
                    String dataString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_ANSWER_INFO);
                    try {
                        List<AnswerBean> questionBeen = JsonUtils.parseJson2Object(dataString, new TypeToken<List<AnswerBean>>() {
                        });
                        callBack.onSuccess(questionBeen);
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
            callBack.onFailure();
        }
    }

    /**
     * 回答点赞
     */
    public static void goodAnswer(AnswerBean answerBean, final CommonCallBack<String> callback) {
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("ANSWERID", answerBean.getSEQKEY());
        datas.put("USERCODE", userInfo.getUSERCODE());
        datas.put("QUESTIONID", answerBean.getQUESTIONID());
//        datas.put("USERNAME", TextUtils.isEmpty(userInfo.getNICKNAME()) ? userInfo.getUSERNAME() : userInfo.getNICKNAME());
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        parametersMap.put("CLASSNAME", UrlMethod.ANSWER_GOOD);
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
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "answerGood");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject("KLB_ANSWER_GOOD", datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("回答点赞" + response);
                    callback.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    callback.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailure();
        }
    }

    /**
     * 采纳
     */
    public static void takeAnswer(AnswerBean answerBean, final CommonCallBack<String> callback) {
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
                    callback.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    callback.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailure();
        }
    }

    /**
     * 获取经典案例列表
     */
    public static void getClassicCaseList(int index, String actionFlag, final CommonCallBack<List<ClassicCaseBean>> callBack) {
        // 第一条记录结束
        Map<String, String> datas = new HashMap<String, String>();
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.CLASSIC_CASE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.V_KLB_CLASSICCASE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", actionFlag);
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_CLASSICCASE_INFO, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("经典案例", response);
                    String dataString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_CLASSICCASE_INFO);
                    try {
                        List<ClassicCaseBean> data = JsonUtils.parseJson2Object(dataString, new TypeToken<List<ClassicCaseBean>>() {
                        });
                        callBack.onSuccess(data);
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

    /**
     * 获取经典案例列表
     */
    public static void getClassicCaseList(int index, int orderType, String keyword, String typecode, final CommonCallBack<List<ClassicCaseBean>> callBack) {
        // 第一条记录结束
        Map<String, String> datas = new HashMap<String, String>();
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        parametersMap.put("orderstr", orderType + "");//orderstr：1最新，2最热
        switch (orderType) {
            case 1:
                parametersMap.put("orderstr", orderType + "");//orderstr：1最新，2最热
                break;
            case 2:
                parametersMap.put("orderstr", orderType + "");//orderstr：1最新，2最热
                break;
            case 3:
                whereSql = "QUESTIONTYPECODE like '%" + typecode + "%'";
                break;
            case 4:
                whereSql = "CASENAME like '%" + typecode + "%'";
                break;
        }
        parametersMap.put("CLASSNAME", UrlMethod.CLASSIC_CASE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.V_KLB_CLASSICCASE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "searchOrder");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_CLASSICCASE_INFO, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("经典案例", response);
                    String dataString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_CLASSICCASE_INFO);
                    try {
                        List<ClassicCaseBean> data = JsonUtils.parseJson2Object(dataString, new TypeToken<List<ClassicCaseBean>>() {
                        });
                        callBack.onSuccess(data);
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

    /**
     * 行业规范
     */
    public static void getIndustryStandard(int index, int type, String keyword, String typecode, final CommonCallBack<List<IndustryStandardBean>> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
//        select * from klb_question_info where expertusercode like '%33002726%';
        String whereSql = "";
        switch (type) {
            case 1:
                parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
                break;
            case 2:
                parametersMap.put("ORDERSQL", "CLICKCOUNT desc");
                break;
            case 3:
                whereSql = "PROCIRCLECODE like '%" + typecode + "%'";
                break;
            case 4:
                whereSql = "STANDARDNAME like '%" + keyword + "%'";
                break;
        }
        parametersMap.put("CLASSNAME", UrlMethod.STANDARD_INFO);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("MASTERTABLE", TableName.V_KLB_STANDARD_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "PROCIRCLE_STANDARD_INFO");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_STANDARD_INFO, datas, parametersMap, commandMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("行业规范", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_STANDARD_INFO);
                    try {
                        List<IndustryStandardBean> questionBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<IndustryStandardBean>>() {
                        });
                        callBack.onSuccess(questionBeen);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("行业规范error", error);
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
            e.printStackTrace();
        }
    }

    /**
     * 获取专家信息列表
     */
    public static void getExpertList(int start, int orderType, String keyword, String typecode, final CommonCallBack<List<Professor>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        switch (orderType) {
            case 1:
                parametersMap.put("orderstr", orderType + "");//  orderstr：1评级，2解疑数
                break;
            case 2:
                parametersMap.put("orderstr", orderType + "");//  orderstr：1评级，2解疑数
                break;
            case 3:
                parametersMap.put("procirclecode", typecode + "");
                break;
            case 4:
                whereSqlString = "USERNAME like '%" + keyword + "%'";
                break;
        }
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.EXPERT_MANAGE);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_EXPERT_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", start + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "searchOrder");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_EXPERT_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_EXPERT_INFO);
                    try {
                        List<Professor> teamDynamicbeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<Professor>>() {
                        });
                        callBack.onSuccess(teamDynamicbeen);
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
            callBack.onFailure();
        }
    }

    /**
     * 获取台区经理首页课件列表
     */
    public void getCourseWareHomePage(final int start, String orderType, String abilitytype, final CommonCallBack<List<CourseWareHomeBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        if (!TextUtils.isEmpty(abilitytype)) {
            whereSqlString = "abilitytype = '" + abilitytype + "'";
        }
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_ABILITY_TYPE);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("orderstr", orderType);
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", start + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "courseList");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_ABILITY_TYPE, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_ABILITY_TYPE);
                    try {
                        List<CourseWareHomeBean> courseWareHomeBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<CourseWareHomeBean>>() {
                        });
                        callBack.onSuccess(courseWareHomeBeen);
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
            callBack.onFailure();
        }
    }


    //台区经理推荐课件
    public void getCourseWareRecommend(int start, String orderType, String abilitytype, final CommonCallBack<List<CourseWareBean.DatasBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";//ABILITYTYPE
        if (!TextUtils.isEmpty(abilitytype)) {
            whereSqlString = "ABILITYTYPE = '" + abilitytype + "'";
        }
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_COURSEWARE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("orderstr", orderType);
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", start + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "courseRecommend");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_COURSEWARE_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_COURSEWARE_INFO);
                    try {
                        List<CourseWareBean.DatasBean> datasBeanList = JsonUtils.parseJson2Object(datasString, new TypeToken<List<CourseWareBean.DatasBean>>() {
                        });
                        callBack.onSuccess(datasBeanList);
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
            callBack.onFailure();
        }
    }

    //台区经理推荐课件
    public void searchCourseWare(int start, String search, final CommonCallBack<List<CourseWareBean.DatasBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_COURSEWARE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("courseware", search);
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", start + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "courseSearch");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_COURSEWARE_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_COURSEWARE_INFO);
                    try {
                        List<CourseWareBean.DatasBean> datasBeanList = JsonUtils.parseJson2Object(datasString, new TypeToken<List<CourseWareBean.DatasBean>>() {
                        });
                        callBack.onSuccess(datasBeanList);
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
            callBack.onFailure();
        }
    }

    //台区经理我收藏的课件列表
    public static void getCourseWareCollect(int start, final CommonCallBack<List<CourseWareBean.DatasBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();

        String whereSqlString = "";
        //第一条记录结束
        // 参数区域赋值

        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
        parametersMap.put("usercode", DataUtils.getUserInfo().getUSERCODE());
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_COURSEWARE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", start + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "coursecollect");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_COURSEWARE_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_COURSEWARE_INFO);
                    try {
                        List<CourseWareBean.DatasBean> datasBeanList = JsonUtils.parseJson2Object(datasString, new TypeToken<List<CourseWareBean.DatasBean>>() {
                        });
                        callBack.onSuccess(datasBeanList);
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
            callBack.onFailure();
        }
    }

    //台区经理首页更多课件
    public void getMoreCourseWare(int start, String orderType, String abilitytype, final CommonCallBack<List<CourseWareBean.DatasBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_COURSEWARE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("orderstr", orderType);
        parametersMap.put("abilitytype", abilitytype);
        parametersMap.put("start", start + "");
        parametersMap.put("limit", "10");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "courseAbilitytype");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_COURSEWARE_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_COURSEWARE_INFO);
                    try {
                        List<CourseWareBean.DatasBean> datalist = JsonUtils.parseJson2Object(datasString, new TypeToken<List<CourseWareBean.DatasBean>>() {
                        });
                        callBack.onSuccess(datalist);
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
            callBack.onFailure();
        }
    }


    //能力种类相关课件
    public void getRelatCourseWare(int start, SkillBean.DataBean bean, final CommonCallBack<List<CourseWareBean.DatasBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_COURSEWARE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        if (bean != null) {
            parametersMap.put("abilitycode", bean.getABILITYCODE());
//            parametersMap.put("abilitytype", bean.getABILITYTYPE());
        }
        parametersMap.put("start", start + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "courseAbility");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_COURSEWARE_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_COURSEWARE_INFO);
                    try {
                        List<CourseWareBean.DatasBean> datalist = JsonUtils.parseJson2Object(datasString, new TypeToken<List<CourseWareBean.DatasBean>>() {
                        });
                        callBack.onSuccess(datalist);
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
            callBack.onFailure();
        }
    }

    //能力项相关课件
    public void getRelatCourseWare(int start, CourseWareBean.DatasBean bean, final CommonCallBack<List<CourseWareBean.DatasBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_COURSEWARE_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("courseid", bean.getSEQKEY());
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        if (bean != null) {
//            parametersMap.put("abilitytype", bean.getABILITYTYPE());
            parametersMap.put("abilitycode", bean.getABILITYCODE());
        }
        parametersMap.put("start", start + "");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "courseAbility");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_COURSEWARE_INFO, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_COURSEWARE_INFO);
                    try {
                        List<CourseWareBean.DatasBean> datalist = JsonUtils.parseJson2Object(datasString, new TypeToken<List<CourseWareBean.DatasBean>>() {
                        });
                        callBack.onSuccess(datalist);
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
            callBack.onFailure();
        }
    }

    //收藏课件
    public void collectCourseWare(CourseWareBean.DatasBean bean, final CommonCallBack<String> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        datas.put("COURSEID", bean.getSEQKEY());
        datas.put("USERCODE", DataUtils.getUserInfo().getUSERCODE());
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.APP_BIZ_OPERATION);
        parametersMap.put("METHOD", "1".equals(bean.getISCOLLECT()) ? UrlMethod.DELETE : UrlMethod.ADDSAVE);//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.klb_courseware_collect);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.klb_courseware_collect, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.klb_courseware_collect);
                    LogUtils.e(response);
                    try {
                        callBack.onSuccess(response);
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
            callBack.onFailure();
        }
    }

    //课件点击
    public void courseWareClick(CourseWareBean.DatasBean bean, final CommonCallBack<String> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
        parametersMap.put("METHOD", UrlMethod.UPDATASAVE);//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_ABILITY_TYPE);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("courseid", bean.getSEQKEY());
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "clickcount");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_ABILITY_TYPE, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_ABILITY_TYPE);
                    LogUtils.e("点击量" + response);
                    try {
                        callBack.onSuccess(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callBack.onFailure();
                    }
                }

                @Override
                public void loginTimeOut(boolean isLogin) {
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


    //技能清单
    public void getSkillBill(int type, int start, final CommonCallBack<List<SkillBean.DataBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.KlbAbilityManage);
//        parametersMap.put("METHOD", UrlMethod.SEARCH);//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.klb_courseware_collect);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + start);
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        switch (type) {
            case 1:
                parametersMap.put("METHOD", "abilityListy");
                commandMap.put("actionflag", "abilityListy");//pass
                break;
            case 2:
                parametersMap.put("METHOD", "abilityListn");
                commandMap.put("actionflag", "abilityListn");//no pass
                break;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.klb_courseware_collect, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        SkillBean skillBean = JsonUtils.parseJson2Object(target, new TypeToken<SkillBean>() {
                        });
                        List<SkillBean.DataBean> data = skillBean.getData();
                        callBack.onSuccess(data);
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
            callBack.onFailure();
        }
    }

    /**
     * 题库练习
     */
    public void practiceQuestionBank(final Common2CallBack<List<QuestionBankItemBean.DataBean>, List<QuestionBankItem2Bean>> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.QUESTION_BANK);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "questionBankScene");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_FUNCTION_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("能力题库" + response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        QuestionBankItemBean dataBeen = JsonUtils.parseJson2Object(target, new TypeToken<QuestionBankItemBean>() {
                        });
                        String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_FUNCTION_LIST);
                        List<QuestionBankItem2Bean> datasBeanList = JsonUtils.parseJson2Object(datasString, new TypeToken<List<QuestionBankItem2Bean>>() {
                        });
                        callBack.onSuccess(dataBeen.getData(), datasBeanList);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    /**
     * 课件评论
     */
    public void courseComment(CourseWareBean.DatasBean bean, String comment, final CommonCallBack<String> callBack) {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        datas.put("USERCODE", DataUtils.getUserInfo().getUSERCODE());
        datas.put("COURSEID", bean.getSEQKEY());
        datas.put("CONTENTEXPLAIN", comment);
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        // String whereSql = "CASECODE='" + caseBean.getId() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
        parametersMap.put("METHOD", "addSave");// updateSave,delete,search,addSave
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.klb_courseware_content);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "contentcount");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.klb_courseware_content, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "EXCEPTIONDATA");
                        if (!TextUtils.isEmpty(datas.toString())) {
                            JSONArray jsonArray = new JSONArray(datas.toString());
                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String reason = jsonObject1.getString("reason");
                                ToastUtils.showToastCenter(MyAppliction.getContext(), reason);
                                callBack.onFailure();
                            } else {
                                callBack.onSuccess(response);
                            }
                        } else {
                            callBack.onSuccess(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callBack.onFailure();
                    }
//                    callBack.onSuccess(response);
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                }
            });
        } catch (Exception e) {
            callBack.onFailure();
        }


    }

    //课件评论数据
    public void courseWareCommentData(CourseWareBean.DatasBean bean, int index, final CommonCallBack<List<CourseWareCommentBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();

        String whereSqlString = "";
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.CoursewareManage);
        parametersMap.put("METHOD", UrlMethod.SEARCH);//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.V_KLB_COURSEWARE_CONTENT);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("courseid", bean.getSEQKEY());
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "" + index);
        parametersMap.put("limit", UrlMethod.PAZE_SIZE + "");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "coursecontent");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.V_KLB_COURSEWARE_CONTENT, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_COURSEWARE_CONTENT);
                    LogUtils.e("评论" + datasString);
                    try {
                        List<CourseWareCommentBean> datalist = JsonUtils.parseJson2Object(datasString, new TypeToken<List<CourseWareCommentBean>>() {
                        });
                        callBack.onSuccess(datalist);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callBack.onFailure();
                    }
                }

                @Override
                public void loginTimeOut(boolean isLogin) {
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


    /**
     * 能力模块学习
     */
    public void getSkillMoudleLearn(String functioncode, final CommonCallBack<List<SkillMoudleLearnBean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        datas.put("functioncode", functioncode);
        String whereSqlString = "";

        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", "com.nci.klb.app.exam.SkillStudyMode");
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("orderstr", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "SkillStudyList");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_FUNCTION_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {

                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        JsonElement datas2 = JsonUtils.decoElementJSONObject(target, "DATAS");
                        if (datas2 == null) {
                            datas2 = JsonUtils.decoElementJSONObject(target, "data");
                        }
                        List<SkillMoudleLearnBean> courseWareHomeBeen = JsonUtils.parseJson2Object(datas2.toString(), new TypeToken<List<SkillMoudleLearnBean>>() {
                        });
                        LogUtils.e("能力课件", datas2.toString());
                        callBack.onSuccess(courseWareHomeBeen);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
//                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_FUNCTION_USER_LIST);
//                    try {
//                        List<CourseWareHomeBean> courseWareHomeBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<CourseWareHomeBean>>() {
//                        });
//                        callBack.onSuccess(courseWareHomeBeen);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        callBack.onFailure();
//                    }
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

    /**
     * 能力模块数据
     */
    public void getSkillMoudleData(SkillMoudleLearnBean skillMoudleLearnBean, final Common2CallBack<List<SkillMoudleItemBean>, List<QuestionBankItem2Bean>> callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, String> datas = new HashMap<>();
        String whereSqlString = "";

        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.QuestionBankQuery);
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("ABILITYTYPECODE", skillMoudleLearnBean.getABILITYTYPECODE());
        parametersMap.put("MODTYPE", skillMoudleLearnBean.getMODTYPE());
        parametersMap.put("WHERESQL", whereSqlString);
        parametersMap.put("ORDERSQL", "");//SYSCREATEDATE ISPASS
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("orderstr", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
//        commandMap.put("actionflag", "questionBank");
        commandMap.put("actionflag", "questionList");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_FUNCTION_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {

                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        JsonElement itemList = JsonUtils.decoElementJSONObject(target, "itemsList");
                        String scoreList = JsonUtils.decoElementASString(target, "scoreList");
                        JsonElement datas1 = JsonUtils.decoElementJSONObject(scoreList, "data");
                        List<SkillMoudleItemBean> courseWareHomeBeen = JsonUtils.parseJson2Object(datas1.toString(), new TypeToken<List<SkillMoudleItemBean>>() {
                        });


//                        String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_FUNCTION_LIST);
                        List<QuestionBankItem2Bean> datasBeanList = JsonUtils.parseJson2Object(itemList.toString(), new TypeToken<List<QuestionBankItem2Bean>>() {
                        });
                        callBack.onSuccess(courseWareHomeBeen, datasBeanList);
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

    public static void getQuestionBankQuery(String methon, String module_id, String abilitycode, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                "com.nci.klb.app.exam.QuestionBankQuery");
        parametersMap.put("METHOD", methon);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("module_id", module_id);
        parametersMap.put("abilitytypecode", abilitycode);

        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_FUNCTION_LIST);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "questionBank");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_FUNCTION_LIST, datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("能力模块" + response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                        String target = JsonUtils.decoElementASString(datas.toString(), "target");
                        callBack.onSuccess(target);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }

    public static void postStudyProgress(HashMap<String, String> data, final CommonCallBack<String> callBack) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.putAll(data);

        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                "com.nci.klb.app.exam.CourseOperateInfo");
        parametersMap.put("METHOD", "addSave");// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", "KLB_COURSE_OPERATE_INFO");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "courseSave");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject4("KLB_COURSE_OPERATE_INFO", datas, parametersMap, commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("记录学习进度" + response);
                    try {

                        callBack.onSuccess(response);
                    } catch (Exception e) {
                        callBack.onFailure();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    callBack.onFailure();
                    ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callBack.onFailure();
            ToastUtils.showToast(UIUtils.getContext(), "服务器正忙！");
        }
    }
}

