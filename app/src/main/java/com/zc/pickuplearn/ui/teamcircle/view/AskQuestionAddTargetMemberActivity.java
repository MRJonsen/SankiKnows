package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.AddMemeberEvent;
import com.zc.pickuplearn.ui.teamcircle.adapter.PickTargetContactAdapter;
import com.zc.pickuplearn.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 指定回答添加人员界面
 */
public class AskQuestionAddTargetMemberActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.list)
    ListView listView;
    @BindView(R.id.tv_save)
    TextView tv_checked;

    private final static String TAG = "AskQuestionAddTargetMemberActivity";
    private final static String TAG_TEAM = "AskQuestionAddTargetMemberActivity_team";
    private PickTargetContactAdapter contactAdapter;
    private List<UserBean> friendList = new ArrayList<>();
    private List<UserBean> chooseUser = new ArrayList<>();
    private UserBean userInfo;

    @Override
    public int setLayout() {
        return R.layout.activity_ask_question_add_target_member;
    }

    /**
     * 开启本页面的方法
     *
     * @param context
     */
    public static void startAskQuestionAddTargetMemberActivity(Context context, TeamCircleBean teamCircleBean) {
        Intent intent = new Intent(context, AskQuestionAddTargetMemberActivity.class);
//        intent.putExtra(TAG, userBeen);
        intent.putExtra(TAG_TEAM, teamCircleBean);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        userInfo = DataUtils.getUserInfo();
        initListView();
    }


    /**
     * 处理查询数据 插入队列
     */
    private void doData(List<UserBean> userbeen) {
        ArrayList<UserBean> objects = new ArrayList<>();
        for (UserBean user1 : userbeen) {
            for (UserBean user : friendList) {
                if (user.getUSERCODE().equals(user1.getUSERCODE())) {
                    objects.add(user1);
                }
            }
        }
        userbeen.removeAll(objects);
        friendList.addAll(userbeen);
        contactAdapter.notifyDataSetChanged();
    }

    private void initListView() {
//        friendList = (ArrayList<UserBean>) getIntent().getSerializableExtra(TAG);
        TeamCircleBean teamCircleBean = (TeamCircleBean) getIntent().getSerializableExtra(TAG_TEAM);
        API.EditTeamGetMemberInfo(teamCircleBean, new CommonCallBack<List<UserBean>>() {//获取团队人员列表
            @Override
            public void onSuccess(List<UserBean> userBeanList) {
                if (userBeanList != null && userBeanList.size() > 0) {
                    ArrayList<UserBean> userBeen = new ArrayList<>();
                    for (int i = 0; i < userBeanList.size(); i++) {
                        if (userInfo.getUSERCODE().equals(userBeanList.get(i).getUSERCODE())) {
                            userBeen.add(userBeanList.get(i));
                        }
                    }
                    userBeanList.removeAll(userBeen);
                    friendList.addAll(userBeanList);
                    refreshList(friendList);
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void initData() {

    }

    /**
     * 判断是否包含
     *
     * @param userBean 人员
     * @return 存在true
     */
    private boolean isContainUser(List<UserBean> list, UserBean userBean) {
        boolean isContain = false;
        if (list != null && userBean != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUSERCODE().equals(userBean.getUSERCODE())) {
                    isContain = true;
                }
            }
        }
        return isContain;
    }

    private void setChooseNumber(List<UserBean> list) {
        if (list == null || list.size() == 0) {
            tv_checked.setText("确定");
        } else {
            tv_checked.setText("确定(" + list.size() + ")");
        }
    }


    private void refreshList(List<UserBean> users) {
        LogUtils.e("设置adptet" + users.size());
        contactAdapter = new PickTargetContactAdapter(this, users);
        contactAdapter.setOnPickContactItemClick(new PickTargetContactAdapter.OnPickContactItemClickListener() {
            @Override
            public void OnClick() {
                if (contactAdapter.getChooseUser() != null) {
                    chooseUser.clear();
                    chooseUser.addAll(contactAdapter.getChooseUser());
                    LogUtils.e("选中的ch" + chooseUser.size());
                    setChooseNumber(chooseUser);
                }
            }
        });
        listView.setAdapter(contactAdapter);
    }

    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                save();
                break;
        }
    }

    /**
     * 确认选择的members
     */
    private void save() {
        AddMemeberEvent addMemeberEvent = new AddMemeberEvent();
        addMemeberEvent.setUserBeen(chooseUser);
        EventBus.getDefault().post(addMemeberEvent);
        finish();
    }
}
