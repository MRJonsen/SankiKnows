package com.zc.pickuplearn.ui.group.widget.professional.model;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.beans.ProfessionalCircleRealUserBean;
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
 * 时间: 2017/1/4 10:19
 * 联系方式：chenbin252@163.com
 */

public class ProfessionModelImpl implements IProfessionModel {
    @Override
    public void getProfessionalCircleData(final AllProCircleCallBack callBack) {

        Map<String, String> datas = new HashMap<String, String>();
        Map<String, String> parametersMap = new HashMap<String, String>();
        parametersMap.put("CLASSNAME", UrlMethod.PROCIRCLE_POSITION);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "PROCIRCLEPOINTS desc ");
        parametersMap.put("MASTERTABLE", TableName.KLB_PROCIRCLE_POSITION);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_PROCIRCLE_POSITION, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_PROCIRCLE_POSITION);
                    LogUtils.e("圈子排名",datasString);
                    try {
                        List<ProfessionalCircleBean> professionalCircleBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<ProfessionalCircleBean>>() {
                        });
                        if (professionalCircleBeen!=null&&professionalCircleBeen.size()>0){
                            //圈子排名初始化
                            int i = 0;
                            for (ProfessionalCircleBean bean:
                                    professionalCircleBeen) {
                                i++;
                                bean.setPOSITION(i+"");
                            }
                            callBack.onSuccess(professionalCircleBeen);
                        }else {
                            callBack.onFail();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("圈子排名",error);
                    callBack.onFail();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getMyProfessionalCircleData(final MyProCircleCallBack callBack) {
// 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        String whereSql = "USERCODE='" + DataUtils.getUserInfo().getUSERCODE() + "'";
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_PROCIRCLE_REL_USER);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "200");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_PROCIRCLE_REL_USER, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_PROCIRCLE_REL_USER);
                    try {
                        List<ProfessionalCircleRealUserBean> professionalCircleRealUserBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<ProfessionalCircleRealUserBean>>() {
                        });
                        if (professionalCircleRealUserBeen!=null&&professionalCircleRealUserBeen.size()>0){
                            callBack.onSuccess(professionalCircleRealUserBeen);
                        }else {
                            callBack.onFail();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callBack.onFail();
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

    public interface AllProCircleCallBack {
        void onSuccess(List<ProfessionalCircleBean> professionalCircleBeen);

        void onFail();

    }

    public interface MyProCircleCallBack {
        void onSuccess(List<ProfessionalCircleRealUserBean> professionalCircleRealUserBeen);

        void onFail();
    }
}
