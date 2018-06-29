package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.album.AlbumFile;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.UrlType;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.AddMemeberEvent;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.CreatTeamCircleTypeEvent;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.QuitTeamEvent;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.TranferTeamEvent;
import com.zc.pickuplearn.ui.teamcircle.model.UserType;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamInfoGridAdapter;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamListAdpter;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;
import com.zc.pickuplearn.ui.view.ExpandGridView;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.ImageSelectUtil;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.ViewClickUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 团队信息修改
 */
public class TeamInfoEditActivity extends EventBusActivity {

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
    @BindView(R.id.hv_head)
    HeadView hvHead;
    @BindView(R.id.egv_team_member_list)
    ExpandGridView gridview;
    @BindView(R.id.ll_holder)
    LinearLayout mHolder; //团长底部权限布局
    @BindView(R.id.bt_quit_team)
    Button mBtQuitTeam;//退出团队Bin
    @BindView(R.id.ll_team_manager)
    LinearLayout ll_manager;
    private TeamInfoGridAdapter adapter;
    private QusetionTypeBean typeBean;
    private List<String> iconlist = new ArrayList<>();//添加的团队头像
    private ArrayList<UserBean> userBeen = new ArrayList<>();//添加的人员
    private static String TAG = "TeamInfoEditActivity";
    private TeamCircleBean teamCircleBean;
    private UserBean userInfo;
    private boolean IS_OWNER = false; //是不是团队拥有者 默认false
    private ProgressDialog progressDialog;
    public static void startTeamEditInfoActivity(Context context, TeamCircleBean teamCircleBean) {
        Intent intent = new Intent(context, TeamInfoEditActivity.class);
        intent.putExtra(TAG, teamCircleBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_creat;
    }

    @Override
    public void initView() {
        teamCircleBean = (TeamCircleBean) getIntent().getSerializableExtra(TAG);
        userInfo = DataUtils.getUserInfo();
        initTopBar();
        etTeamIntroduce.setText(teamCircleBean.getTEAMCIRCLEREMARK());
        etTeamProclaim.setText(teamCircleBean.getTEAMMANIFESTO());
        etTeamName.setText(teamCircleBean.getTEAMCIRCLENAME());
        tvTeamType.setText(teamCircleBean.getTEAMCIRCLECODENAME());
        typeBean = new QusetionTypeBean();
        typeBean.setCODE(teamCircleBean.getTEAMCIRCLECODE());
        typeBean.setNAME(teamCircleBean.getTEAMCIRCLECODENAME());
        if (!TextUtils.isEmpty(teamCircleBean.getFILEURL())) {
            ImageLoaderUtil.displayBitmapImageResource(this, ivTeamIcon, teamCircleBean.getFILEURL(), false);
        }
    }

    /**
     * 初始化topbar
     */
    private void initTopBar() {
        hvHead.setTitle("团队信息");
        if (teamCircleBean != null && userInfo != null) {
            if (userInfo.getUSERCODE().equals(teamCircleBean.getSYSCREATORID())) {
                IS_OWNER = true;
                userInfo.setUSERTYPE(UserType.holder);
            }
        }
        managerOperat();
    }

    private void managerOperat() {
        if (!(UserType.holder.equals(userInfo.getUSERTYPE()) || UserType.manager.equals(userInfo.getUSERTYPE()))) {
            //不是管理员
            etTeamIntroduce.setEnabled(false);
            etTeamProclaim.setEnabled(false);
            etTeamName.setEnabled(false);
            tvTeamType.setClickable(false);
            ivTeamIcon.setClickable(false);
        } else {
            etTeamIntroduce.setEnabled(true);
            etTeamProclaim.setEnabled(true);
            etTeamName.setEnabled(true);
            tvTeamType.setClickable(true);
            ivTeamIcon.setClickable(true);
            ll_manager.setVisibility(View.VISIBLE);
        }
        hvHead.setRightBtVisable(UserType.holder.equals(userInfo.getUSERTYPE()) || UserType.manager.equals(userInfo.getUSERTYPE()));
        initBottomButton(teamCircleBean);
        hvHead.setRightText("确认修改");
        hvHead.setMyOnClickListener(new HeadView.myOnClickListener() {
            @Override
            public void rightButtonClick() {
                if (ViewClickUtil.isFastClick()){
                    LogUtils.e("一次点击");
                    createTeam();
                }
            }
        });
    }

    /**
     * 修改班组信息
     */
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
        boolean isOwner = false;
        for (int i = 0; i < userBeen.size(); i++) {
            if (userBeen.get(i).getUSERCODE().equals(userInfo.getUSERCODE())) {
                String usertype = userBeen.get(i).getUSERTYPE();
                if (UserType.holder.equals(usertype)||UserType.manager.equals(usertype)) {
                    isOwner = true;
                    break;
                }
            }
        }
        if (!isOwner) {
            ToastUtils.showToast(TeamInfoEditActivity.this, "操作失败！无权限");
            return;
        }
        if (userBeen==null||userBeen.size()==0){
            ToastUtils.showToast(TeamInfoEditActivity.this, "请稍后再试");
            return;
        }
        teamCircleBean.setTEAMMANIFESTO(claim);
        teamCircleBean.setTEAMCIRCLENAME(name);
        teamCircleBean.setTEAMCIRCLEREMARK(introduce);
        teamCircleBean.setTEAMCIRCLECODENAME(typeBean.getNAME());
        teamCircleBean.setTEAMCIRCLECODE(typeBean.getCODE());
        showProgressDialog();
        API.EditTeamCircleInfo(teamCircleBean, userBeen, iconlist, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                ToastUtils.showToastCenter(TeamInfoEditActivity.this, s);
                finish();
                disshowProgressDialog();
            }

            @Override
            public void onFailure() {
                disshowProgressDialog();
                ToastUtils.showToastCenter(TeamInfoEditActivity.this, "操作失败！请稍后再试");
            }
        });
    }

    @Override
    protected void initData() {
        showMembers();
    }

    @OnClick({R.id.tv_team_type, R.id.iv_team_icon, R.id.bt_disband_team, R.id.bt_transfer_team, R.id.bt_quit_team,R.id.rb_team_info,R.id.rb_practice_manager,R.id.rb_test_manager})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_team_type:
                ScrollGridActivity.startScrollGridActivity(this, TypeGreenDaoEvent.TeamCreate);
                break;
            case R.id.iv_team_icon:
                ImageSelectUtil.choicePicture(TeamInfoEditActivity.this, 1, null, new ImageSelectUtil.ImageSelectCallBack() {
                    @Override
                    public void onSuccess(ArrayList<AlbumFile> albumfiles, List<String> imgPaths) {
                        iconlist.clear();
                        iconlist.add(imgPaths.get(0));
                        ImageLoaderUtil.displayBitmapImageResource(TeamInfoEditActivity.this, ivTeamIcon, imgPaths.get(0), true);
                    }
                });
//                Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO, 1, getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.bt_disband_team:
                disbanTeam();
                break;
            case R.id.bt_transfer_team:
                //转移团队
                TransferTeamActivity.starTransferTeamActivity(this, teamCircleBean, userBeen);
                break;
            case R.id.bt_quit_team:
                quiteTeam();
                break;
            case R.id.rb_team_info:
                TeamMessageManageActivity.startTeamMessageManageActivity(this,teamCircleBean);
                break;
            case R.id.rb_practice_manager:
                API.getTestLibraryUrl(teamCircleBean, UrlType.DailyDeploy, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        WebViewActivity.skip(TeamInfoEditActivity.this,s,"练习配置",true);
                    }

                    @Override
                    public void onFailure() {
                        showToast("操作失败！");
                    }
                });
                break;
            case R.id.rb_test_manager:
                API.getTestLibraryUrl(teamCircleBean, UrlType.CheckDeploy, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        WebViewActivity.skip(TeamInfoEditActivity.this,s,"考试管理",false);
                    }

                    @Override
                    public void onFailure() {
                        showToast("操作失败！");
                    }
                });
                break;
        }
    }

    /**
     * 解散团队
     */
    private void disbanTeam() {
        AlertDialog alertDialog = new AlertDialog(this).builder();
        alertDialog
                .setTitle("提示").setMsg("团队一旦解散，该团队数据信息会被清除，确认解散团队?")
                .setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        API.disbanTeam(teamCircleBean, new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                EventBus.getDefault().post(new QuitTeamEvent());//发出退出团队的通知
                                ToastUtils.showToast(TeamInfoEditActivity.this, "解散团队成功");
                                finish();
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                }).setNegativeButton("否", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        alertDialog.show();
    }

    public void quiteTeam() {
        AlertDialog alertDialog = new AlertDialog(this).builder();
        alertDialog
                .setTitle("提示").setMsg("是否要退出该团队?")
                .setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //主动退出团队
                        UserBean userInfo = DataUtils.getUserInfo();
                        UserBean mUserbean = userInfo;
                        if (userBeen.size() > 0) {
                            for (UserBean u :
                                    userBeen) {
                                if (userInfo.getUSERCODE().equals(u.getUSERCODE())) {
                                    mUserbean = u;
                                }
                            }
                        }
                        API.quiteTeam(teamCircleBean, mUserbean, new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                EventBus.getDefault().post(new QuitTeamEvent());//发出退出团队的通知
                                ToastUtils.showToast(TeamInfoEditActivity.this, "退出成功");
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                ToastUtils.showToast(TeamInfoEditActivity.this, "操作失败");
                            }
                        });
                    }
                }).setNegativeButton("否", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        alertDialog.show();
    }

    /**
     * 初始化底部权限按钮布局
     */
    private void initBottomButton(TeamCircleBean teamCircleBean) {
        if (UserType.holder.equals(userInfo.getUSERTYPE())) {
            mHolder.setVisibility(View.VISIBLE);
            mBtQuitTeam.setVisibility(View.GONE);
        } else {
            mHolder.setVisibility(View.GONE);
            mBtQuitTeam.setVisibility(View.VISIBLE);
        }
        if (TeamListAdpter.Type.join.equals(teamCircleBean.getSTATUS())) {
            mHolder.setVisibility(View.GONE);
            mBtQuitTeam.setVisibility(View.GONE);
        }
    }

    // 显示群成员头像昵称的gridview
    private void showMembers() {
//        adapter = new GridAdapter(this, members, hxid.equals(group.getOwner()) ? true : false, groupId);
        adapter = new TeamInfoGridAdapter(this, userBeen, userInfo, teamCircleBean);
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
        API.EditTeamGetMemberInfo(teamCircleBean, new CommonCallBack<List<UserBean>>() {
            @Override
            public void onSuccess(List<UserBean> userBeanList) {
                if (userBeanList != null && userBeanList.size() > 0) {
                    userBeen.addAll(userBeanList);
                    getUserType(userBeanList);
                    initTopBar();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure() {

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

    /**
     * 接收转移团队成功的时候 关闭当前页面
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(TranferTeamEvent event) {
        if (event != null) {
            finish();
        }
    }

    @Subscribe
    public void onEventMainThread(AddMemeberEvent event) {
        if (event != null) {
            List<UserBean> userBeenlist = event.getUserBeen();
            if (userBeenlist != null && userBeenlist.size() > 0) {
                userBeen.clear();
                userBeen.addAll(userBeenlist);
                getUserType(userBeenlist);
                initTopBar();
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void getUserType(List<UserBean> userBeenlist) {
        for (UserBean user :
                userBeenlist) {
            if (userInfo.getUSERCODE().equals(user.getUSERCODE())) {
                userInfo.setUSERTYPE(user.getUSERTYPE());
            }
        }
    }

    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceLable(false);
            progressDialog.setMsg("信息更新中...");
        }
        progressDialog.showProgressDialog();
    }

    private void disshowProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dissMissProgressDialog();
        }
    }
}
