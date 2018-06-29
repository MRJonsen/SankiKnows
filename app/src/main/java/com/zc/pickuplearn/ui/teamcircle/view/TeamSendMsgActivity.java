package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.TeamMessageBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class TeamSendMsgActivity extends BaseActivity {

    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.et_mark)
    EditText etMark;
    @BindView(R.id.cb_need_back)
    CheckBox cbNeedBack;
    @BindView(R.id.tv_msg_num)
    TextView tv_msg_num;
    @BindView(R.id.tv_mark_num)
    TextView tv_mark_num;
    private UserBean userInfo;
    private static final String Tag = "TeamSendMsgActivity";
    private TeamCircleBean teamCircleBean;
    AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    public static void starTeamSendMsgActivity(Context context,TeamCircleBean teamCircleBean) {
        Intent intent = new Intent(context, TeamSendMsgActivity.class);
        intent.putExtra(Tag,teamCircleBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_send_msg;
    }

    @Override
    public void initView() {
        initHead();
        initTextLimitLister();
    }

    private void initTextLimitLister() {
        etMark.addTextChangedListener(new TextWatcher() {//对输入框进行监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int length = s.length();
                tv_mark_num.setText(length + "/1000");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                tv_mark_num.setText(length + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etMsg.addTextChangedListener(new TextWatcher() {//对输入框进行监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int length = s.length();
                tv_msg_num.setText(length + "/50");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                tv_msg_num.setText(length + "/50");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initHead() {
        hvHead.setTitle("团队通知");
        hvHead.setRightText("发送通知");
        hvHead.setMyOnClickListener(new HeadView.myOnClickListener() {
            @Override
            public void rightButtonClick() {
                showAlert();
            }
        });
    }

    private void showAlert() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog(this).builder();
            alertDialog
                    .setTitle("提示").setMsg("是否确认发送?")
                    .setPositiveButton("发送", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           sendMsg();
                        }
                    }).setNegativeButton("返回", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        alertDialog.show();
    }

    @Override
    protected void initData() {
        userInfo = DataUtils.getUserInfo();
        teamCircleBean = (TeamCircleBean) getIntent().getSerializableExtra(Tag);
    }

    /**
     * 获取通知内容
     *
     * @return
     */
    public String getMsg() {
        return etMsg.getText().toString();
    }

    /**
     * 获取补充内容
     *
     * @return
     */
    public String getMark() {
        return etMark.getText().toString();
    }

    /**
     * 获取是否需要回执 1回执需要 0 不需要
     *
     * @return
     */
    public String getNeedBack() {
        return cbNeedBack.isChecked() ? "1" : "0";
    }

    /**
     * 发送通知
     */
    public void sendMsg() {
        String msg = getMsg();
        if (TextUtils.isEmpty(msg)) {
            showToast("请输入通知内容！");
            return;
        }
        String mark = getMark();
        String needBack = getNeedBack();
        final TeamMessageBean teamMessageBean = new TeamMessageBean();
        teamMessageBean.setMESSAGE(msg);
        teamMessageBean.setISRECEIPT(needBack);
        teamMessageBean.setREMARK(mark);
        teamMessageBean.setTEAMID(teamCircleBean.getSEQKEY());
        showProgressDialog();
        //1.系统的通知写入
        API.sendTeamMessage(teamMessageBean,"1" ,new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                showToast("操作成功！");
                disshowProgressDialog();
                EventBus.getDefault().post(TeamMessageManageActivity.TEAM_MSG_MANAGE_REFRESH);//通知团队消息列表界面更新
                close();
            }

            @Override
            public void onFailure() {
                showToast("操作失败！稍后再试");
                disshowProgressDialog();
            }
        });
        //2.极光通知发送
        API.sendTeamMessage(teamMessageBean,"2" ,new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceLable(false);
            progressDialog.setMsg("正在发送通知");
        }
        progressDialog.showProgressDialog();
    }

    private void disshowProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dissMissProgressDialog();
        }
    }
}
