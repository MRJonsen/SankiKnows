package com.zc.pickuplearn.ui.group.widget.prodetail.model;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.beans.CircleRankingBean;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
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
 * 时间: 2017/1/4 16:29
 * 联系方式：chenbin252@163.com
 */

public class ProCircleDetailModelImpl implements IProCircleDetailModel {

    @Override
    public void getCircleDetailData(ProfessionalCircleBean bean, final ProCircleDetailCallBack callBack) {

        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("PROCIRCLECODE", bean.getPROCIRCLECODE());
        datas.put("ANSWERTAKESUM", "");
        datas.put("ANSWERSUM", "");
        datas.put("QUESTIONSUM", "");
        datas.put("NICKNAME", "");
        datas.put("SUMPOINTS", "");

        String whereSQL = "PROCIRCLECODE = '" + bean.getPROCIRCLECODE() + "'";
        // 第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", UrlMethod.PROCIRCLE_USERPOS);
        parametersMap.put("METHOD", "search");// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSQL);
        parametersMap.put("ORDERSQL", "SUMPOINTS desc");
        // parametersMap.put("MASTERTABLE", "KLB_PROCIRCLE_INFO");
        parametersMap.put("MASTERTABLE", TableName.KLB_PROCIRCLE_USER_POS);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject = null;
        jsonObject = ParamUtils.params2JsonObject(TableName.KLB_PROCIRCLE_USER_POS, datas, parametersMap);
        HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
            @Override
            public void onSuccess(String response) {
                String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_PROCIRCLE_USER_POS);
                try {
                    List<CircleRankingBean> circleRankingBean = JsonUtils.parseJson2Object(datasString, new TypeToken<List<CircleRankingBean>>() {
                    });
                    int i = 1;
                    for (CircleRankingBean bean :
                            circleRankingBean) {
                        bean.setPOSITION(i++ + "");
                    }
                    CircleRankingBean circleRankingBean1 = new CircleRankingBean();
                    circleRankingBean1.setPOSITION("排名");
                    circleRankingBean1.setNICKNAME("昵称");
                    circleRankingBean1.setANSWERSUM("回答数");
                    circleRankingBean1.setANSWERTAKESUM("采纳数");
                    circleRankingBean1.setQUESTIONSUM("提问数");
                    circleRankingBean1.setANSWERTAKESUM("采纳数");
                    circleRankingBean1.setSUMPOINTS("贡献值");
                    circleRankingBean.add(0,circleRankingBean1);//添加第一行数据
                    callBack.onSuccess(circleRankingBean);
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
    }

    @Override
    public void joinCircle(ProfessionalCircleBean bean, final ProCircleJoinCallBack callBack) {
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("USERCODE", DataUtils.getUserInfo().getUSERCODE());
        datas.put("USERNAME", DataUtils.getUserInfo().getUSERNAME());
        // datas.put("SYSCREATEDATE", today);
        datas.put("PROCIRCLECODE", bean.getPROCIRCLECODE());
        datas.put("STATUS", "1");
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "USERCODE='"+userCode+"'";
        parametersMap.put("CLASSNAME",
                "com.nci.app.operation.business.AppBizOperation");
        if (bean.getISJOIN()) {
            parametersMap.put("METHOD", "delete");// updateSave,delete,search
            String sqlString = "USERCODE = '" + DataUtils.getUserInfo().getUSERCODE()
                    + "' and PROCIRCLECODE = '"
                    +  bean.getPROCIRCLECODE() + "'";
            parametersMap.put("WHERESQL", sqlString);

        } else {
            parametersMap.put("METHOD", "addSave");// updateSave,delete,search
            parametersMap.put("WHERESQL", "");
        }
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_PROCIRCLE_REL_USER);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject = null;
        jsonObject = ParamUtils.params2JsonObject(TableName.KLB_PROCIRCLE_REL_USER, datas, parametersMap);
        HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

            @Override
            public void onSuccess(String response) {
                String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_PROCIRCLE_USER_POS);
                LogUtils.e("加入圈子",datasString);
                callBack.onSuccess();
            }

            @Override
            public void onFailure(String error) {
                callBack.onFailure();
            }
        });
    }

    /**
     * 圈子排名回调
     */
    public interface ProCircleDetailCallBack{
        void onSuccess(List<CircleRankingBean> rankingBeanList);
        void onFailure();
    }

    /**
     * 加入圈子和退出圈子的回调
     */
    public interface ProCircleJoinCallBack{
        void onSuccess();
        void onFailure();
    }

}
