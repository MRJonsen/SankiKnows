package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.TranferTeamEvent;
import com.zc.pickuplearn.ui.teamcircle.adapter.TransTeamPickContanctGridAdapter;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 转移团队
 */
public class TransferTeamActivity extends BaseActivity {
    @BindView(R.id.list)
    ListView listView;
    @BindView(R.id.hv_head)
    HeadView mhead;
    private TransTeamPickContanctGridAdapter contactAdapter;
    private List<UserBean> mData = new ArrayList<>();
    private final static String TAG_LIST = "TransferTeamActivity_user_list";
    private final static String TAG_TEAMCIRCLE_BEAN = "TransferTeamActivity_team_circle_bean";
    private UserBean chooseMember;//选中转移的的人员
    private TeamCircleBean teamCircleBean;

    public static void starTransferTeamActivity(Context context, TeamCircleBean teamCircleBean,ArrayList<UserBean> userBeen) {
        Intent intent = new Intent(context, TransferTeamActivity.class);
        intent.putExtra(TAG_LIST, userBeen);
        intent.putExtra(TAG_TEAMCIRCLE_BEAN, teamCircleBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_transfer_team;
    }

    @Override
    public void initView() {
        initHead();
    }

    private void initHead() {
        if (mhead != null) {
            mhead.setRightText("升为团长");
            mhead.setMyOnClickListener(new HeadView.myOnClickListener() {
                @Override
                public void rightButtonClick() {
                    if (chooseMember != null) {
                        API.TranferTeam(teamCircleBean, chooseMember, mData, new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                ToastUtils.showToastCenter(TransferTeamActivity.this,"操作成功！");
                                EventBus.getDefault().post(new TranferTeamEvent());//发出退出团队的通知
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                ToastUtils.showToastCenter(TransferTeamActivity.this,"操作失败！");
                            }
                        });
                    } else {
                        ToastUtils.showToast(TransferTeamActivity.this, "请选择人员");
                    }
                }
            });
        }
    }

    @Override
    protected void initData() {
        List<UserBean> serializableExtra = (List<UserBean>) getIntent().getSerializableExtra(TAG_LIST);
        teamCircleBean = (TeamCircleBean) getIntent().getSerializableExtra(TAG_TEAMCIRCLE_BEAN);
        mData.addAll(serializableExtra);
        refreshList(mData);
    }

    private void refreshList(List<UserBean> users) {
        contactAdapter = new TransTeamPickContanctGridAdapter(TransferTeamActivity.this, users);
        contactAdapter.setOnPickContactItemClick(new TransTeamPickContanctGridAdapter.OnPickContactItemClickListener() {
            @Override
            public void OnClick() {
                chooseMember = contactAdapter.getChooseUser();//选中的人回传 赋值
            }
        });
        listView.setAdapter(contactAdapter);
    }
}
