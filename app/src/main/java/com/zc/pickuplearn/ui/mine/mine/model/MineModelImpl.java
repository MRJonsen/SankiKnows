package com.zc.pickuplearn.ui.mine.mine.model;

import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/16 13:20
 * 联系方式：chenbin252@163.com
 */

public class MineModelImpl implements IMineModel {
    @Override
    public void uploadImage(List<String> list,final UploadHeadImageCallBack callBack) {
        HashMap<String, String> datas = new HashMap<String, String>();
        datas.put("SEQKEY", DataUtils.getUserInfo().getSEQKEY());
        datas.put("FILEURL", "");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("CLASSNAME", UrlMethod.URSER_INFO);
        params.put("limit", UrlMethod.PAZE_SIZE + "");
        params.put("METHOD", UrlMethod.UPDATASAVE);
        params.put("MASTERFIELD", "SEQKEY");
        params.put("WHERESQL", "");
        params.put("ORDERSQL", "");
        params.put("MENUAPP", "EMARK_APP");
        params.put("MASTERTABLE", TableName.KLB_USER_BASE_INFO);
        params.put("start", "0");
        params.put("DETAILTABLE", "");
        params.put("DETAILFIELD", "");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_USER_BASE_INFO, datas, params);
            HttpUtils.doPostFile(HttpContacts.UI_URL, jsonObject, list, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    callBack.onSuccess();
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

    public interface UploadHeadImageCallBack{
        void onSuccess();
        void onFailure();
    }
}
