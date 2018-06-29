package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.album.AlbumFile;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.AddMemeberEvent;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.CreatTeamCircleTypeEvent;
import com.zc.pickuplearn.ui.teamcircle.model.UserType;
import com.zc.pickuplearn.ui.teamcircle.adapter.GridAdapter;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;
import com.zc.pickuplearn.ui.view.ExpandGridView;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.ImageSelectUtil;
import com.zc.pickuplearn.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamCreateActivity extends EventBusActivity {
    @BindView(R.id.iv_team_icon)
    ImageView ivTeamIcon;
    @BindView(R.id.et_team_name)
    EditText etTeamName;
    @BindView(R.id.tv_team_type)
    TextView tvTeamType;
    @BindView(R.id.et_team_proclaim)
    EditText etTeamProclaim;
    @BindView(R.id.et_team_introduce)
    EditText etTeamIntroduce;
    @BindView(R.id.activity_team_creat)
    LinearLayout activityTeamCreat;
    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.egv_team_member_list)
    ExpandGridView gridview;
    private GridAdapter adapter;
    private QusetionTypeBean typeBean;
    private List<String> iconlist = new ArrayList<>();//添加的团队头像
    private ArrayList<UserBean> userBeen = new ArrayList<>();//添加的人员
    private UserBean operator;
    private ProgressDialog progressDialog;

    public static void startTeamCreateActivity(Context context) {
        Intent intent = new Intent(context, TeamCreateActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_creat;
    }

    @Override
    public void initView() {
        initTopBar();
    }


    /**
     * 初始化topbar
     */
    private void initTopBar() {
        hvHead.setTitle("创建团队");
        hvHead.setRightText("确认创建");
        hvHead.setMyOnClickListener(new HeadView.myOnClickListener() {
            @Override
            public void rightButtonClick() {
                createTeam();
            }
        });
    }

    private void createTeam() {
        String name = etTeamName.getText().toString().trim();
        String claim = etTeamProclaim.getText().toString().trim();
        String introduce = etTeamIntroduce.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showToast(XFrame.getString(R.string.warning_input_team_name));
            return;
        } else if (TextUtils.isEmpty(claim)) {
            showToast(XFrame.getString(R.string.warning_input_team_declaration));
            return;

        } else if (TextUtils.isEmpty(introduce)) {
            showToast(XFrame.getString(R.string.warning_input_team_introduction));
            return;

        } else if (typeBean == null) {
            showToast(XFrame.getString(R.string.warning_input_team_type));
            return;
        }
        if (userBeen == null || userBeen.size() == 0) {
            showToast("请稍后再试");
            return;
        }
        TeamCircleBean teamCircleBean = new TeamCircleBean();
        teamCircleBean.setTEAMMANIFESTO(claim);
        teamCircleBean.setTEAMCIRCLENAME(name);
        teamCircleBean.setTEAMCIRCLEREMARK(introduce);
        teamCircleBean.setTEAMCIRCLECODENAME(typeBean.getNAME());
        teamCircleBean.setTEAMCIRCLECODE(typeBean.getCODE());
        showProgressDialog();
        API.CreateTeamCircle(teamCircleBean, userBeen, iconlist, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                ToastUtils.showToastCenter(TeamCreateActivity.this, s);
                disshowProgressDialog();
                finish();
            }

            @Override
            public void onFailure() {
                disshowProgressDialog();
                ToastUtils.showToastCenter(TeamCreateActivity.this, "操作失败！请稍后再试");
            }
        });
    }

    @Override
    protected void initData() {
        operator = DataUtils.getUserInfo();
        operator.setUSERTYPE(UserType.holder);
        userBeen.add(operator);
        showMembers();
    }

    @OnClick({R.id.tv_team_type, R.id.iv_team_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_team_type:
                ScrollGridActivity.startScrollGridActivity(this, TypeGreenDaoEvent.TeamCreate);
                break;
            case R.id.iv_team_icon:
                ImageSelectUtil.choicePicture(TeamCreateActivity.this, 1, null, new ImageSelectUtil.ImageSelectCallBack() {
                    @Override
                    public void onSuccess(ArrayList<AlbumFile> albumfiles, List<String> imgPaths) {
                        iconlist.clear();
                        iconlist.add(imgPaths.get(0));
                        ImageLoaderUtil.displayBitmapImageResource(TeamCreateActivity.this, ivTeamIcon, imgPaths.get(0), true);
                    }
                });
                break;
        }
    }

    // 显示群成员头像昵称的gridview
    private void showMembers() {
//        adapter = new GridAdapter(this, members, hxid.equals(group.getOwner()) ? true : false, groupId);
        adapter = new GridAdapter(this, userBeen, true, "1", operator);
        gridview.setAdapter(adapter);
        // 设置OnTouchListener,为了让群主方便地推出删除模》
        gridview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (adapter.isInDeleteMode) {
                            adapter.isInDeleteMode = false;
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }



    @Subscribe
    public void onEventMainThread(CreatTeamCircleTypeEvent event) {
        if (event != null) {
            typeBean = event.getQuestionBean();
            tvTeamType.setText(typeBean.getNAME());
        }
    }

    @Subscribe
    public void onEventMainThread(AddMemeberEvent event) {
        if (event != null) {
            List<UserBean> userBeenlist = event.getUserBeen();
            if (userBeenlist != null && userBeenlist.size() > 0) {
                userBeen.clear();
                userBeen.addAll(userBeenlist);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceLable(false);
            progressDialog.setMsg("创建中...");
        }
        progressDialog.showProgressDialog();
    }

    private void disshowProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dissMissProgressDialog();
        }
    }
}
