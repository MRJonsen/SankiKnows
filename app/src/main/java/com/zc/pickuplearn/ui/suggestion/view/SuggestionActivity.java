package com.zc.pickuplearn.ui.suggestion.view;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 意见反馈页面
 */
public class SuggestionActivity extends BaseActivity {
    private static final String TAG = "SuggestionActivity";

    public static void startSuggestionActivity(Context context, UserBean userBean) {
        Intent intent = new Intent(context, SuggestionActivity.class);
        intent.putExtra(TAG, userBean);
        context.startActivity(intent);
    }

    @BindView(R.id.hv_headbar)
    HeadView mHeadView;

    @BindView(R.id.et_sugguest_input)
    EditText met_suggest;
    @BindView(R.id.tv_sugguest_hint)
    TextView tvsugguesthint;
    private UserBean mUserbean;

    @Override
    public int setLayout() {
        return R.layout.activity_suggetion;
    }

    @Override
    public void initView() {
        mHeadView.setTitle(getResources().getString(R.string.setting_suggest));
        mHeadView.setRightText(getResources().getString(R.string.commit));
        mHeadView.setMyOnClickListener(new HeadView.myOnClickListener() {

            @Override
            public void rightButtonClick() {
                String suggest = met_suggest.getText().toString();
                if (!TextUtils.isEmpty(suggest)) {
                    commitSuggest(suggest);
                } else {
                    ToastUtils.showToast(SuggestionActivity.this, "请输入建议!");
                }
            }
        });
        met_suggest.addTextChangedListener(new TextWatcher() {//对输入框进行监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int length = s.length();
                int size = 200 - length;
                tvsugguesthint.setText("还可以输入" + size + "字");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                int size = 200 - length;
                tvsugguesthint.setText("还可以输入" + size + "字");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        mUserbean = getParams();
    }

    /**
     * 回传给服务器
     *
     * @param suggest
     */
    protected void commitSuggest(String suggest) {
        // 数据区域
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("OPINION", suggest);
        datas.put("SEQKEY", "");
        datas.put("USERCODE", mUserbean.getUSERCODE());
        // 参数区域
        Map<String, String> params = new HashMap<String, String>();
        params.put("MASTERTABLE", TableName.KLB_SETUP_RECORD);
        params.put("MENUAPP", "EMARK_APP");
        params.put("ORDERSQL", "");
        params.put("WHERESQL", "");
        params.put("start", "0");
        params.put("METHOD", "addSave");
        params.put("MASTERFIELD", "USERCODE");
        params.put("DETAILFIELD", "");
        params.put("DETAILTABLE", "");
        params.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_SETUP_RECORD, datas, params);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    ToastUtils.showToast(SuggestionActivity.this, "提交成功！");
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    ToastUtils.showToast(SuggestionActivity.this, "操作失败！请稍后再试");
                }

            });

        } catch (Exception e) {
            ToastUtils.showToast(SuggestionActivity.this, "操作失败！请稍后再试");
        }
    }

    public UserBean getParams() {
        return (UserBean) getIntent().getSerializableExtra(TAG);
    }
}
