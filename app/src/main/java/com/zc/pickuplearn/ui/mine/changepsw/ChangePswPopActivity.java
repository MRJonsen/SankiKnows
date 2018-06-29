package com.zc.pickuplearn.ui.mine.changepsw;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 密码修改 （弹框）
 */
public class ChangePswPopActivity extends BaseActivity {
    @BindView(R.id.hv_headbar)
    HeadView mHeadView;
    @BindView(R.id.dialog_change_pwd_oldPwd)
    EditText oldPsw;
    @BindView(R.id.dialog_change_pwd_newPsw1)
    EditText newPsw1;
    @BindView(R.id.dialog_change_pwd_newPsw2)
    EditText newPsw2;
    ProgressDialog loading;

    @Override
    public int setLayout() {
        return R.layout.activity_change_psw_pop;
    }

    @Override
    public void initView() {
        mHeadView.setTitle("首次登录请修改密码");
        mHeadView.setLeftBtnVisable(false);
        loading = new ProgressDialog(this);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.dialog_change_pwd_ok, R.id.dialog_change_pwd_cancel})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.dialog_change_pwd_ok:
                ChangePswAction();
                break;
            case R.id.dialog_change_pwd_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    private void ChangePswAction() {
        final String pswOld = oldPsw.getText().toString();
        final String pswNew1 = newPsw1.getText().toString();
        final String pswNew2 = newPsw2.getText().toString();
        if (pswOld.equals("")) {
            Toast.makeText(ChangePswPopActivity.this, "旧密码不能为空！",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (pswNew1.equals("") || pswNew1.length() < 6) {
                Toast.makeText(ChangePswPopActivity.this, "新密码不能少于六位",
                        Toast.LENGTH_SHORT).show();
            } else if (pswNew1.equals(pswNew2)) {
                loading.showProgressDialog();
                changePsw(pswOld, pswNew1, pswNew2);
            } else {
                Toast.makeText(ChangePswPopActivity.this, "两次密码不一致！",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changePsw(final String oldPsw, final String newPsw1, final String newPsw2) {
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("OLDPASSWORD", oldPsw);
        datas.put("NEWPASSWORD", newPsw1);
        datas.put("NEWPASSWORD1", newPsw2);
        // 第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", "com.nci.epm.biz.hr.EpmHr");
        parametersMap.put("METHOD", "saveSelfPwd");// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        // parametersMap.put("ORDERSQL", "DISPLAYORDER");
        parametersMap.put("MASTERTABLE", TableName.EPM_USER_PWD);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.EPM_USER_PWD, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new StringCallback() {

                @Override
                public void onError(Call call, Exception e, int id) {
                    e.printStackTrace();
                    ToastUtils.showToast(UIUtils.getContext(), "连接服务器失败！");
                }

                @Override
                public void onResponse(String response, int id) {
                    String reason = "";
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response);
                        String retMsg = obj.getString("RETMSG");
                        reason = retMsg;
                        Boolean flag = ResultStringCommonUtils.getDataRet(retMsg);
                        LogUtils.e("修改密码", flag + "");
                        if (flag) {
                            JSONObject object1 = obj.getJSONObject("DATAS");
                            JSONObject object2 = object1
                                    .getJSONObject("EPM_USER_PWD");
                            Toast.makeText(ChangePswPopActivity.this, "密码修改成功！",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            JSONArray jsonArray = obj.getJSONArray("EXCEPTIONDATA");

                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                                reason = jsonObject2.getString("reason");
                            }

                            Toast.makeText(ChangePswPopActivity.this, reason,
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            ToastUtils.showToast(ChangePswPopActivity.this, "操作失败！请稍后再试");
        } finally {
            loading.dissMissProgressDialog();
        }


    }
}