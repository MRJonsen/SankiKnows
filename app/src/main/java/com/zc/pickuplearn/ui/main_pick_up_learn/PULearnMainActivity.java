package com.zc.pickuplearn.ui.main_pick_up_learn;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.youth.xframe.XFrame;
import com.youth.xframe.common.XActivityStack;
import com.youth.xframe.utils.permission.XPermission;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.MsgBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.beans.VersionBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.callback.UpdateCallback;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.file_view.FileDoer;
import com.zc.pickuplearn.ui.main_pick_up_learn.adapter.TabViewPageAdapter;
import com.zc.pickuplearn.ui.update.DownLoadUtils;
import com.zc.pickuplearn.ui.update.DownloadApk;
import com.zc.pickuplearn.ui.view.NoScrollViewPager;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.DeviceInfo;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;
import okhttp3.Call;
import okhttp3.Request;

import static com.zc.pickuplearn.ui.main.widget.MainActivity.LOGOUT;

public class PULearnMainActivity extends EventBusActivity {

    private FragmentManager supportFragmentManager;
//    private UserBean userBean;
    private long firstTime = 0;//双击退出记录时间
    private NavigationController navigationController;

    public static void startActivityMain(Activity context, UserBean userBean) {
        Intent intent = new Intent(context, PULearnMainActivity.class);
        intent.putExtra(DataUtils.USER_INFO, userBean);
        context.startActivity(intent);
        context.finish();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_pulearn_main;
    }

    @Override
    public void initView() {
        supportFragmentManager = getSupportFragmentManager();
//        userBean = (UserBean) getIntent().getSerializableExtra(DataUtils.USER_INFO);
        initTab();
        API.checkVersion(new UpdateCallback() {
            @Override
            public void onSuccess(VersionBean bean) {
                if ("1".equals(bean.getTYPE())) {
                    showUpdateDialog(bean);
                } else {
                    upDateAPK(bean);
                }
            }

            @Override
            public void noNew() {
            }
        });//检测版本更新

        //检查读取设备信息权限 动态处理
        XPermission.requestPermissions(this, 100, new String[]{Manifest.permission.READ_PHONE_STATE}, new XPermission.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                DeviceInfo.searchIMEI();//回传设备信息
            }

            @Override
            public void onPermissionDenied() {
                //给出友好提示，并且提示启动当前应用设置页面打开权限
                XPermission.showTipsDialog(PULearnMainActivity.this);
            }
        });

    }

    @Override
    protected void initData() {
    }


    //初始化底部导航栏
    private void initTab() {
        PageNavigationView tab = (PageNavigationView) findViewById(R.id.bottom_tab);
        navigationController = tab.material()
                .setDefaultColor(XFrame.getColor(R.color.color_tab_unselected))
                .addItem(R.mipmap.icon_home_unpress, getResources().getString(R.string.tab_tag_home), XFrame.getColor(R.color.color_tab_selected))
                .addItem(R.mipmap.team, getResources().getString(R.string.tab_tag_team), XFrame.getColor(R.color.color_tab_selected))
                .addItem(R.mipmap.home_msg, getResources().getString(R.string.tab_tag_msg), XFrame.getColor(R.color.color_tab_selected))
                .addItem(R.mipmap.my, getResources().getString(R.string.tab_tag_mine), XFrame.getColor(R.color.color_tab_selected))
                .build();
        final NoScrollViewPager viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabViewPageAdapter(supportFragmentManager, 4));
        navigationController.setupWithViewPager(viewPager);//配合viewpager使用
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                getMsgInfo();
            }

            @Override
            public void onRepeat(int index) {

            }
        });
    }


    /**
     * 双击退出
     *
     * @param keyCode 键值
     * @param event   触摸时间
     * @return exit(0)
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;// 更新firstTime
                    return true;
                } else { // 两次按键小于2秒时，退出应用
                    XActivityStack.getInstance().appExit();
                }
                break;
            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 接收退出登录按钮发来的消息
     */
    @Subscribe
    public void onEventMainThread(String msg) {
        if (LOGOUT.equals(msg)) {
            FragmentFactory.hashMap.clear();
            finish();
        }
    }

    //下列操作主要是做更新
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.注册下载广播接收器
        DownloadApk.registerBroadcast(this);
        //2.删除已存在的Apk
        DownloadApk.removeFile(this);
    }


    @Override
    protected void onDestroy() {
        DownloadApk.unregisterBroadcast(this);
        super.onDestroy();
    }

    private void showUpdateDialog(final VersionBean bean) {
        new AlertDialog(this).builder()
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

    private RequestCall requestCall;
    private void upDateAPK(VersionBean bean) {
        String childUrl = bean.getFILEURL2();
        int lastIndexOf = childUrl.lastIndexOf("/");
        final String filename = childUrl.substring(lastIndexOf + 1);
        String url = childUrl;
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
                ToastUtils.showToastCenter(PULearnMainActivity.this, "下载失败！请稍后再试");
                progressDialog.dismiss();
                requestCall=null;
            }

            @Override
            public void onResponse(File response, int id) {
                FileDoer.getInstance().openDocument(PULearnMainActivity.this, response.getAbsolutePath());
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
//    public void upDateAPK(VersionBean bean) {
//        //更新
//        if (DownLoadUtils.getInstance(getApplicationContext()).canDownload()) {
//            DownloadApk.downloadApk(getApplicationContext(), bean.getFILEURL2(), UIUtils.getString(R.string.app_name) + "更新", "Hobbees");
//        } else {
//            DownLoadUtils.getInstance(getApplicationContext()).skipToDownloadManager();
//        }
//
//    }

    public void getMsgInfo() {
        //查询消息条数 并且设置给我的界面
        API.getUserInfoAndMsg(new CommonCallBack<MsgBean>() {
            @Override
            public void onSuccess(MsgBean msgBean) {
                if (msgBean != null) {
//                    if (!TextUtils.isEmpty(msgBean.getTEAMINFOCOUNT())) {
//                        if (Integer.valueOf(msgBean.getTEAMINFOCOUNT()) > 0) {
//                            setMessageNumber(1, Integer.valueOf(msgBean.getTEAMINFOCOUNT()));//给团队 设置消息提示
//                        } else {
//                            setMessageNumber(1, 0);
//                        }
//                    } else {
//                        setMessageNumber(1, 0);
//                    }
//                    if (!TextUtils.isEmpty(msgBean.getINFOCOUNT())) {
//                        if (Integer.valueOf(msgBean.getINFOCOUNT()) > 0) {
//                            setMessageNumber(3, Integer.valueOf(msgBean.getINFOCOUNT()));//给我的 设置消息提示
//                        } else {
//                            setMessageNumber(3, 0);
//                        }
//                    } else {
//                        setMessageNumber(3, 0);
//                    }
                    int chatMsgCount = JMessageClient.getAllUnReadMsgCount();
                    if (!TextUtils.isEmpty(msgBean.getPERINFOCOUNT())) {
                        if (Integer.valueOf(msgBean.getPERINFOCOUNT()) > 0) {
                            setMessageNumber(2, Integer.valueOf(msgBean.getPERINFOCOUNT()) + chatMsgCount);//给我的 设置消息提示
                        } else {
                            setMessageNumber(2, chatMsgCount);
                        }
                    } else {
                        setMessageNumber(2, chatMsgCount);
                    }
                } else {
                    setMessageNumber(3, 0);
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void setMessageNumber(int tab_index, int number) {
        if (navigationController != null)
            navigationController.setHasMessage(tab_index, number > 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMsgInfo();
    }
}
