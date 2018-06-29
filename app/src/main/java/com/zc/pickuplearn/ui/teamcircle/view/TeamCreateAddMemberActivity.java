package com.zc.pickuplearn.ui.teamcircle.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.view.SeachBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamCreateAddMemberActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.sb_search_view)
    SeachBar sbSearchView;
    @BindView(R.id.rc_content)
    RecyclerView rcContent;
    List<UserBean> addListBean = new ArrayList<>();
    @Override
    public int setLayout() {
        return R.layout.activity_team_create_add_member;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.tv_save:
                break;
        }
    }
}
