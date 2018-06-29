package com.zc.pickuplearn.ui.mine.changenickname.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class ChangeNickNameActivity extends BaseActivity {
    private static final String TAG = "ChangeNickNameActivity";

    public static void startChangeNickNameActivity(Context context, UserBean userBean) {
        Intent intent = new Intent(context, ChangeNickNameActivity.class);
        intent.putExtra(TAG, userBean);
        context.startActivity(intent);
    }

    @BindView(R.id.hv_headbar)
    HeadView mHeadView;
    @BindView(R.id.et_name)
    EditText mEt_name;
    private UserBean mUserbean;

    @Override
    public int setLayout() {
        return R.layout.activity_change_nick_name;
    }

    public void initView() {
        mHeadView.setTitle(getResources().getString(R.string.change_nickname));
        mHeadView.setRightText(getResources().getString(R.string.save));
        mHeadView.setMyOnClickListener(new HeadView.myOnClickListener() {
            @Override
            public void rightButtonClick() {
                String name = mEt_name.getText().toString();
                changeNickName(name);
            }
        });
    }

    protected void changeNickName(final String name) {
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showToast(this, "请输入昵称");
            return;
        }
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("NICKNAME", name);
        datas.put("SEQKEY", mUserbean.getSEQKEY());
        Map<String, String> params = new HashMap<String, String>();
        params.put("MASTERTABLE", TableName.KLB_USER_BASE_INFO);
        params.put("CLASSNAME", UrlMethod.USER_INFO);
        params.put("MENUAPP", "EMARK_APP");
        params.put("ORDERSQL", "");
        params.put("WHERESQL", "");
        params.put("start", "0");
        params.put("METHOD", "updateSave");
        params.put("MASTERFIELD", "SEQKEY");
        params.put("DETAILFIELD", "");
        params.put("limit", "20");
        params.put("DETAILTABLE", "");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_USER_BASE_INFO, datas, params);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("修改昵称",response);
                    mUserbean.setNICKNAME(name);
                    DataUtils.setUserInfo(mUserbean);
                    ToastUtils.showToast(ChangeNickNameActivity.this,
                            "修改成功！");
                    setNickNameHint();
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    ToastUtils.showToast(ChangeNickNameActivity.this, "操作失败！请稍后再试");
                }

            });

        } catch (Exception e) {
            ToastUtils.showToast(ChangeNickNameActivity.this, "操作失败！请稍后再试");
        }


    }

    public void initData() {
        mUserbean = getParams();
        setNickNameHint();
    }

    /**
     * edittext 设置提示文本
     */
    private void setNickNameHint() {
        if (mUserbean != null) {
            mEt_name.setHint(TextUtils.isEmpty(mUserbean.getNICKNAME()) ? mUserbean.getUSERNAME() : mUserbean.getNICKNAME());
        } else {
            mEt_name.setHint("请输入昵称");
        }
    }

    public UserBean getParams() {
        return (UserBean) getIntent().getSerializableExtra(TAG);
    }
}
