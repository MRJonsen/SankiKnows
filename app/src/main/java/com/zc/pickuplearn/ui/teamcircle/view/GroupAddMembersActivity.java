package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.AddMemeberEvent;
import com.zc.pickuplearn.ui.teamcircle.adapter.PickContactAdapter;
import com.zc.pickuplearn.ui.view.SeachBar;
import com.zc.pickuplearn.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建团队添加人员
 */
public class GroupAddMembersActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.sb_search_view)
    SeachBar sb_search;
    @BindView(R.id.list)
    ListView listView;
    @BindView(R.id.tv_save)
    TextView tv_checked;

    private final static String TAG = "GroupAddMembersActivity";
    private final static String TAG_OPERATOR = "GroupAddMembersActivity_operator";
    private PickContactAdapter contactAdapter;
    private List<UserBean> friendList;
    private List<UserBean> chooseUser = new ArrayList<>();
    private UserBean operator;

    @Override
    public int setLayout() {
        return R.layout.activity_group_add_member;
    }

    /**
     * 开启本页面的方法
     *
     * @param context
     * @param userBeen
     */
    public static void startAddmemberAcitivity(Context context, ArrayList<UserBean> userBeen,UserBean operator) {
        Intent intent = new Intent(context, GroupAddMembersActivity.class);
        intent.putExtra(TAG, userBeen);
        intent.putExtra(TAG_OPERATOR, operator);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        initSearchBar();
        initListView();
    }

    /**
     * 初始化搜索条目
     */
    private void initSearchBar() {
        sb_search.setSearchEditTextHint("请输入账号或者姓名进行检索");
//        sb_search.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        sb_search.setSearchButtonOnClickListener(new SeachBar.SearchButtonOnClickListener() {
            @Override
            public void onClick() {
                showProgress();
                API.getUserInfo(sb_search.getSearchText(), new CommonCallBack<List<UserBean>>() {
                    @Override
                    public void onSuccess(List<UserBean> userBeen) {
                        doData(userBeen);
                        hideProgress();
                    }

                    @Override
                    public void onFailure() {
                        hideProgress();
                    }
                });
            }
        });
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
        if (!(userbeen.size()>0)){
            showToast("该账号不存在");
        }
        friendList.addAll(1,userbeen);
        contactAdapter.notifyDataSetChanged();
    }

    private void initListView() {
        friendList = (ArrayList<UserBean>) getIntent().getSerializableExtra(TAG);
        operator = (UserBean) getIntent().getSerializableExtra(TAG_OPERATOR);
        refreshList(friendList);
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
        contactAdapter = new PickContactAdapter(GroupAddMembersActivity.this, users,operator);
        contactAdapter.setOnPickContactItemClick(new PickContactAdapter.OnPickContactItemClickListener() {
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
