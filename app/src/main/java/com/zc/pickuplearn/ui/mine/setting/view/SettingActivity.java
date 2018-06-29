package com.zc.pickuplearn.ui.mine.setting.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.pickuplearn.ui.file_view.FileDoer;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.application.MyAppliction;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.beans.VersionBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.callback.UpdateCallback;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.mine.changenickname.view.ChangeNickNameActivity;
import com.zc.pickuplearn.ui.mine.changepsw.ChangePswActivity;
import com.zc.pickuplearn.ui.login.model.LoginModelImpl;
import com.zc.pickuplearn.ui.login.view.LoginActivity;
import com.zc.pickuplearn.ui.main.widget.MainActivity;
import com.zc.pickuplearn.ui.suggestion.view.SuggestionActivity;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.SystemUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.ViewClickUtil;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import okhttp3.Call;
import okhttp3.Request;

public class SettingActivity extends BaseActivity {

    private RequestCall requestCall;

    public static void startSettingActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.hv_headbar)
    HeadView mHeadView;

    @BindView(R.id.ll_nickname)
    LinearLayout mll_nicknameLayout;

    @BindView(R.id.ll_level)
    LinearLayout mll_level;

    @BindView(R.id.ll_cache)
    LinearLayout mll_cache;

    @BindView(R.id.ll_sugguest)
    LinearLayout mll_sugguest;

    @BindView(R.id.ll_about)
    LinearLayout mll_about;

    @BindView(R.id.ll_logout)
    LinearLayout mll_logout;

    @BindView(R.id.tv_level)
    TextView mtv_level;

    @BindView(R.id.tv_nickname)
    TextView mtv_nickname;

    @BindView(R.id.ll_changepsw)
    LinearLayout mll_changepsw;

    @BindView(R.id.tv_score)
    TextView mtv_score;

    @BindView(R.id.tv_user)
    TextView tv_user;

    @BindView(R.id.tv_version)
    TextView tv_version;
    @Override
    protected void onResume() {
        super.onResume();
        setNickName();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        mHeadView.setTitle(getResources().getString(R.string.my_setting));// 设置标题
        initVersion();
    }

    private void initVersion() {
        tv_version.setText(SystemUtils.getApkVersionName(this));
    }

    @Override
    protected void initData() {
        mtv_level.setText(DataUtils.getUserInfo().getLEVELNAME());
        getPersonData();
    }

    private void getPersonData() {
        //获取个人积分
        new LoginModelImpl().getUserInfo(DataUtils.getUserInfo().getUSERACCOUNT(), new LoginModelImpl.GetUserInfoCallBack() {

            @Override
            public void onSuccess(List<UserBean> data) {
                UserBean userBean = data.get(0);
                if (userBean != null) {
                    String sumpoints = userBean.getSUMPOINTS();
                    if (TextUtils.isEmpty(sumpoints)){
                        sumpoints = "0";
                    }
                    mtv_score.setText(sumpoints);
                    mtv_level.setText(userBean.getLEVELNAME());
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @OnClick({R.id.ll_nickname, R.id.ll_level, R.id.ll_cache,
            R.id.ll_sugguest, R.id.ll_about, R.id.ll_logout, R.id.ll_changepsw,R.id.ll_check_version})
    public void onBarClick(View view) {
        switch (view.getId()) {
            case R.id.ll_nickname:
                ChangeNickNameActivity.startChangeNickNameActivity(this, DataUtils.getUserInfo());
                break;
            case R.id.ll_level:
                ToastUtils.showToastCenter(this,
                        getResources().getString(R.string.function_no_open));
                break;
            case R.id.ll_cache:
                showCleanCacheDialog();
                break;
            case R.id.ll_sugguest:
                SuggestionActivity.startSuggestionActivity(this, DataUtils.getUserInfo());
                break;
            case R.id.ll_about:
                ToastUtils.showToastCenter(this,
                        getResources().getString(R.string.function_no_open));

                break;
            case R.id.ll_logout:
                exit();
                break;
            case R.id.ll_changepsw:
                changepsw();
                break;
            case R.id.ll_check_version:
                if (ViewClickUtil.isFastClick()){
                    checkVersion();
                }
                break;

        }
    }

    private void checkVersion() {
        API.checkVersion(new UpdateCallback() {
            @Override
            public void onSuccess(final VersionBean bean) {
                if ("1".equals(bean.getTYPE())) {
                    new AlertDialog(SettingActivity.this).builder()
                            .setTitle("更新提示").setMsg("发现新版本，是否立即更新？").setCancelable(false)
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    upDateAPK(bean);
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
//                else {
//                    upDateAPK(bean);
//                }
            }

            @Override
            public void noNew() {
                ToastUtils.showToast(SettingActivity.this,"已经是最新版本！");
            }
        });
    }

    private void upDateAPK(VersionBean bean) {
        String childUrl = bean.getFILEURL2();
        int lastIndexOf = childUrl.lastIndexOf("/");
        final String filename = childUrl.substring(lastIndexOf + 1);
        String url = childUrl;
        LogUtils.e(url);
        File file = new File(HttpContacts.absolutePath + filename);
        boolean IS_DOWN = file.exists();// 判断文件是否下载存在了
        if (IS_DOWN) {
            file.delete();
        }
        requestCall = HttpUtils.doDownFile(url, new FileCallBack(HttpContacts.absolutePath2, filename) {

            @Override
            public void onBefore(final Request request, int id) {
                super.onBefore(request, id);
                showUpLoadProgressDialog();

            }


            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                ToastUtils.showToastCenter(SettingActivity.this, "下载失败！请稍后再试");
                progressDialog.dismiss();
                requestCall=null;
            }

            @Override
            public void onResponse(File response, int id) {
                FileDoer.getInstance().openDocument(SettingActivity.this, response.getAbsolutePath());
                progressDialog.dismiss();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                LogUtils.e("progress", progress + "");
                LogUtils.e("total", total + "");
                progressDialog.setProgress((int) (progress * 100));
            }
        });
    }

    ProgressDialog progressDialog ;
    public void showUpLoadProgressDialog(){
        if (progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }
        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
                if (requestCall!=null){
                    requestCall.cancel();
                }
            }
        });
        progressDialog.show();
    }

    /**
     * 跳转修改密码页面
     */
    private void changepsw() {
        Intent intent = new Intent(SettingActivity.this, ChangePswActivity.class);
        startActivity(intent);
    }

    private void showCleanCacheDialog() {
        new AlertDialog(this).builder()
                .setTitle("提示").setMsg("是否清除缓存？")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageLoaderUtil.clearMemory(SettingActivity.this);
//                        DataCleanManager.cleanExternalCache(UIUtils.getContext());
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    /**
     * 设置name
     */
    private void setNickName() {
        UserBean userInfo = DataUtils.getUserInfo();
        if (userInfo != null) {
            mtv_nickname.setText(TextUtils.isEmpty(userInfo.getNICKNAME()) ? userInfo.getUSERNAME() : userInfo.getNICKNAME());
            tv_user.setText(userInfo.getUSERACCOUNT());
        }
    }

    /**
     * 退出登录
     */
    public void exit() {
        DataUtils.setUserInfo(null);//清除用户信息
        MyAppliction.setAlias("");//推送取消设备的别名
        startActivity(new Intent(this, LoginActivity.class));
        JMessageClient.logout();
        HttpUtils.clearCookies();
        EventBus.getDefault().post(MainActivity.LOGOUT);//发消息 让主页finish掉
        finish();
    }
}
