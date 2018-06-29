package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamMessageBean;
import com.zc.pickuplearn.beans.TeamMsgStatsUserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamMsgHandleActivity extends BaseActivity {


    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.et_msg)
    EditText etMsg;
    @BindView(R.id.et_mark)
    EditText etMark;
    @BindView(R.id.tv_pusher)
    TextView tvPusher;
    @BindView(R.id.tv_push_time)
    TextView tvPushTime;
    @BindView(R.id.bt_handle)
    Button btHandle;

    private static final String Tag = "TeamMsgHandleActivity";
    private static final String PRAM_2 = "PRAM_2";
    private TeamMessageBean messageBean;
    private String sqlkey;

    public static void startTeamMsgHandleActivity(Context context, TeamMessageBean messageBean,String sqlkey) {
        Intent intent = new Intent(context, TeamMsgHandleActivity.class);
        intent.putExtra(Tag, messageBean);
        intent.putExtra(PRAM_2, sqlkey);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_msg_handle;
    }

    @Override
    public void initView() {
        hvHead.setTitle("消息");
    }

    @Override
    protected void initData() {
        messageBean = (TeamMessageBean) getIntent().getSerializableExtra(Tag);
        sqlkey = getIntent().getStringExtra(PRAM_2);
        setMsgView(messageBean);
        if ("1".equals(messageBean.getISRECEIPT())) {
            API.getPersonTeamMsgStatus(messageBean, new CommonCallBack<List<TeamMsgStatsUserBean>>() {
                @Override
                public void onSuccess(List<TeamMsgStatsUserBean> personMsgBeen) {
                   setHandleButton(3);
                }

                @Override
                public void onFailure() {
                    setHandleButton(1);
                }
            });
        }else if ("4".equals(messageBean.getISRECEIPT())){
            setHandleButton(4);
        }else if ("0".equals(messageBean.getISRECEIPT())){
            setHandleButton(2);
        }
    }

    /**
     * 设置按钮的状态
     *
     * @param type
     */
    private void setHandleButton(int type) {
        switch (type) {
            case 1:
                btHandle.setText("点击回执");
                btHandle.setEnabled(true);
                btHandle.setVisibility(View.VISIBLE);
                break;
            case 2:
                btHandle.setText("不需回执");
                btHandle.setEnabled(false);
                btHandle.setVisibility(View.INVISIBLE);
                break;
            case 3:
                btHandle.setText("已回执");
                btHandle.setEnabled(false);
                btHandle.setVisibility(View.VISIBLE);
                break;
            case 4:
                btHandle.setText("已撤回");
                btHandle.setEnabled(false);
                btHandle.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @OnClick(R.id.bt_handle)
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_handle:
                API.setPersonTeamMsghandle(sqlkey, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String teamMsgStatsUserBeen) {
                        showToast("操作成功！");
                        setHandleButton(3);
                    }

                    @Override
                    public void onFailure() {
                    }
                });
                break;
        }
    }
    private void setMsgView(TeamMessageBean messageBean) {
        if (messageBean != null) {
            tvPusher.setText(messageBean.getUSERNAME());
            tvPushTime.setText(DateUtils.dataFormatNow("yyyy-MM-dd HH:mm:ss", messageBean.getSYSCREATEDATE()));
            etMsg.setText(messageBean.getMESSAGE());
            etMark.setText(messageBean.getREMARK());
        }
    }

}
