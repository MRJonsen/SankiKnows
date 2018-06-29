package com.zc.pickuplearn.ui.login.model;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.LoginStringCallBack;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.UIUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 作者： Jonsen
 * 时间: 2016/12/7 14:04
 * 联系方式：chenbin252@163.com
 */

public class LoginModelImpl implements ILoginModel {
    @Override
    public void getUserInfo(String account, final GetUserInfoCallBack callBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("USERACCOUNT", "");
        datas.put("PASSWORD", "");
        datas.put("USERNAME", "");
        datas.put("USERACCOUNT", account);
        datas.put("USERCODE", "");
        datas.put("SEQKEY", "");
        datas.put("EXPERTTYPE", "");
        datas.put("EXPERTFRADE", "");
        datas.put("SUMPOINTS", "");
        datas.put("EXPERTSTATUS", "");
        datas.put("NICKNAME", "");
        datas.put("FILEURL", "");
        datas.put("LEVELNAME", "");
        String whereSql = "";
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
            LogUtils.e("登陆获取个人信息", jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("登陆获取个人信息", response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_USER_BASE_INFO);
                    try {
                        final List<UserBean> userBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<UserBean>>() {
                        });
                        if (userBeen.size() > 0) {
                            DataUtils.setUserInfo(userBeen.get(0));//保存用户信息
//                            ThreadUtils.runOnSubThread(new Runnable() {//极光登录
//                                @Override
//                                public void run() {
//                                    JMessageClient.login(userBeen.get(0).getUSERCODE(), "1234", new BasicCallback() {
//                                        @Override
//                                        public void gotResult(final int status, final String desc) {
//                                            if (status == 0) {
//                                                //IM登录成功
//                                                callBack.onSuccess(userBeen);
//                                            } else {
//                                                LogUtils.e("LoginController", "status = " + status + desc);
//                                                callBack.onSuccess(userBeen);
//                                            }
//                                        }
//                                    });
//                                }
//                            });
                            callBack.onSuccess(userBeen);
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
            callBack.onFailure();
        }
    }

    @Override
    public void doLogin(String account, String psw, final LoginCallBack mLoginCallBack) {
        Map<String, String> parametersMap = new HashMap<>();
        // 数据区域
        Map<String, String> datas = new HashMap<>();
        datas.put("USERACCOUNT", account);
        datas.put("LOGINPWD", psw);
        datas.put("ACCOUNTSTATUS", "");
//        String whereSql = "";
        parametersMap.put("NOSUITUNIT", "true");
        parametersMap.put("appKey", UIUtils.getString(R.string.APP_KEY));
        parametersMap.put("masterSecret", UIUtils.getString(R.string.MASTER_SECRET));
        parametersMap.put("appId", "SX");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.paramLogin(TableName.UUM_USER, datas, parametersMap);
//            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.LOGIN_URL, jsonObject, new LoginStringCallBack() {
                @Override
                public void onFailure(String error) {
                    LogUtils.e("登录失败", error);
                    mLoginCallBack.onFailure();
                }

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("登录成功", response);
                    mLoginCallBack.onSuccess(response);
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    super.onError(call, e, id);
                    mLoginCallBack.onFailure();
                }
            });
        } catch (Exception e) {
            mLoginCallBack.onFailure();
            e.printStackTrace();
        }
    }

    public interface LoginCallBack {
        void onSuccess(String response);

        void onFailure();
    }

    public interface GetUserInfoCallBack {
        void onSuccess(List<UserBean> data);

        void onFailure();
    }
}
