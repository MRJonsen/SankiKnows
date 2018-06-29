package com.zc.pickuplearn.ui.main.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.MsgBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.beans.VersionBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.event.MyMsgEvent;
import com.zc.pickuplearn.utils.DeviceInfo;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.askquestion.view.AskQuestionActivity;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.group.view.GroupFragment;
import com.zc.pickuplearn.ui.home_new.view.HomeNewFragment;
import com.zc.pickuplearn.ui.main.presenter.MainPresenterImpl;
import com.zc.pickuplearn.ui.main.view.IMainView;
import com.zc.pickuplearn.ui.mine.mine.view.MineFragment;
import com.zc.pickuplearn.ui.teamcircle.view.TeamCircleFragment;
import com.zc.pickuplearn.ui.update.DownLoadUtils;
import com.zc.pickuplearn.ui.update.DownloadApk;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.UIUtils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
//import me.majiajie.pagerbottomtabstrip.Controller;
//import me.majiajie.pagerbottomtabstrip.PagerBottomTabLayout;
//import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;

public class MainActivity extends EventBusActivity implements IMainView {
    public static final String LOGOUT = "MainActivity_finish";
    private static final int REQUEST_READ_PHONE_STATE = 200;

    public static void startActivityMain(Activity context, UserBean userBean) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(DataUtils.USER_INFO, userBean);
        context.startActivity(intent);
    }

//    @BindView(R.id.tab)
//    PagerBottomTabLayout bottomTabLayout;
//    @BindView(R.id.bt_fa)
    ImageButton mBtCenter;
    private String[] TabTag = {"home", "group", "school", "mine"};
    private long firstTime = 0;//双击退出记录时间
    private FragmentManager fragmentManager;
    private BaseFragment current_fragment;
//    private Controller build;//底部导航栏
    private MainPresenterImpl mainPresenter;
    private int selected = 0;//标记底部导航栏选中的索引

    @Override
    public void initView() {
        fragmentManager = getSupportFragmentManager();
//        initTab();
        mainPresenter = new MainPresenterImpl(this);
//        HttpUtils.checkVersion(new HttpUtils.UpdateCallback() {
//            @Override
//            public void onSuccess(VersionBean bean) {
//                if ("1".equals(bean.getTYPE())) {
//                    showUpdateDialog(bean);
//                } else {
//                    upDateAPK(bean);
//                }
//            }
//        });//检测版本更新
        //检查读取设备信息权限 动态处理
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            DeviceInfo.searchIMEI();//回传设备信息
        }
    }

    //初始化底部导航栏
//    private void initTab() {
//        build = bottomTabLayout.builder()
//                .setDefaultColor(getResources().getColor(R.color.color_tab_unselected))
//                .addTabItem(R.mipmap.icon_home_unpress, getResources().getString(R.string.tab_tag_home), getResources().getColor(R.color.color_tab_selected))
//                .addTabItem(R.mipmap.icon_circle_unpress, getResources().getString(R.string.tab_tag_group), getResources().getColor(R.color.color_tab_selected))
//                .addTabItem(R.mipmap.icon_wenda_unpress, getResources().getString(R.string.tab_tag_dynamic), getResources().getColor(R.color.color_tab_selected))
//                .addTabItem(R.mipmap.home_team_icon, getResources().getString(R.string.tab_tag_team), getResources().getColor(R.color.color_tab_selected))
//                .addTabItem(R.mipmap.icon_home_mine, getResources().getString(R.string.tab_tag_mine), getResources().getColor(R.color.color_tab_selected))
//                .build();
//        build.addTabItemClickListener(new OnTabItemSelectListener() {
//            @Override
//            public void onSelected(int index, Object tag) {
//                if (index == 2) { //此处处理中间FloatActionButton 的事件
//                    build.setSelect(selected);
//                    onCenterClick();
//                    return;
//                }
//                selected = index;
//                if (index > 1) {
//                    mainPresenter.changeFragment(index - 1);
//                } else {
//                    mainPresenter.changeFragment(index);
//                }
//            }
//
//            @Override
//            public void onRepeatClick(int index, Object tag) {
//
//            }
//        });
//        mBtCenter.setOnClickListener(new View.OnClickListener() {//底部按钮监听
//            @Override
//            public void onClick(View v) {
//                onCenterClick();
//            }
//        });
//    }

    /**
     * 中间按钮点击
     */
    private void onCenterClick() {
//        QuestionClassification.startQuestionClassfication(this,QuestionClassification.TIWEN);//提问
        AskQuestionActivity.startAskQuestionActivity(this, null);
    }

    @Override
    public void changeFragment(int index) {
        BaseFragment fragment = FragmentFactory.createFragment(index);
        if (current_fragment != null) {
            if (!fragment.isAdded()) {
                fragmentManager.beginTransaction().hide(current_fragment).add(R.id.fl_content, fragment, TabTag[index]).commit();
            } else {
                fragmentManager.beginTransaction().hide(current_fragment).show(fragment).commit();
            }
        } else {
            fragmentManager.beginTransaction().add(R.id.fl_content, fragment, TabTag[index]).commit();
        }
        current_fragment = fragment;
        getMsgInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMsgInfo();
    }

    public void getMsgInfo() {
        //查询消息条数 并且设置给我的界面
        API.getUserInfoAndMsg(new CommonCallBack<MsgBean>() {
            @Override
            public void onSuccess(MsgBean msgBean) {
                if (msgBean != null) {
                    if (!TextUtils.isEmpty(msgBean.getTEAMINFOCOUNT())) {
                        if (Integer.valueOf(msgBean.getTEAMINFOCOUNT()) > 0) {
                            setMessageNumber(3, Integer.valueOf(msgBean.getTEAMINFOCOUNT()));//给团队 设置消息提示
                        } else {
                            setMessageNumber(3, 0);
                        }
                    } else {
                        setMessageNumber(3, 0);
                    }
                    if (!TextUtils.isEmpty(msgBean.getINFOCOUNT())) {
                        if (Integer.valueOf(msgBean.getINFOCOUNT()) > 0) {
                            setMessageNumber(4, Integer.valueOf(msgBean.getINFOCOUNT()));//给我的 设置消息提示
                        } else {
                            setMessageNumber(4, 0);
                        }
                    } else {
                        setMessageNumber(4, 0);
                    }
                    if (current_fragment instanceof HomeNewFragment) {
                        HomeNewFragment current_fragment = (HomeNewFragment) MainActivity.this.current_fragment;
                        current_fragment.setMyNewMsg(msgBean.getPERINFOCOUNT());
                    } else if (current_fragment instanceof MineFragment) {
                        MineFragment current_fragment = (MineFragment) MainActivity.this.current_fragment;
                        current_fragment.setMyAnswerMsg(msgBean.getANSWERCOUNT());
                        current_fragment.setMyQuestionMsg(msgBean.getQUESTIONCOUNT());
                        current_fragment.setMyNewMsg(msgBean.getPERINFOCOUNT());
                    } else if (current_fragment instanceof GroupFragment) {
                        GroupFragment current_fragment = (GroupFragment) MainActivity.this.current_fragment;
                        current_fragment.setMyNewMsg(msgBean.getPERINFOCOUNT());
                    } else if (current_fragment instanceof TeamCircleFragment) {
                        TeamCircleFragment current_fragment = (TeamCircleFragment) MainActivity.this.current_fragment;
                        current_fragment.setMyNewMsg(msgBean.getPERINFOCOUNT());

                    }
                } else {
                    setMessageNumber(4, 0);//给我的 设置消息提示 取消
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void initData() {
//        build.setSelect(TabTag[selected]);//默认选中第一个Tab
    }


    @Override
    public int setLayout() {
        return R.layout.activity_main;
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
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                if (current_fragment instanceof WebViewFragment && ((WebViewFragment) current_fragment).onKeyDown(keyCode, event)) {
//                    return true;//处理学堂页面回退
//                } else {
//                    long secondTime = System.currentTimeMillis();
//                    if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于2秒，则不退出
//                        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                        firstTime = secondTime;// 更新firstTime
//                        return true;
//                    } else { // 两次按键小于2秒时，退出应用
//                        System.exit(0);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void setMessageNumber(int tab_index, int number) {
//        if (build != null) {
//            build.setMessageNumber(getResources().getStringArray(R.array.tab_tag)[tab_index], number);
//        }
    }

    @Override
    public void setMessagePoint(int tab_index, boolean hasNewMessage) {
//        if (build != null) {
//            build.setDisplayOval(getResources().getStringArray(R.array.tab_tag)[tab_index], hasNewMessage);
//        }
    }

    @Override
    public MainPresenterImpl getMainPresenterImpl() {
        return mainPresenter;
    }

    /**
     * 接收退出登录按钮发来的消息
     *
     * @param msg
     */
    @Subscribe
    public void onEventMainThread(String msg) {
        if (LOGOUT.equals(msg)) {
            FragmentFactory.hashMap.clear();
            finish();
        }
    }
    @Subscribe
    public void onEventMainThread(MyMsgEvent event){
        if (event!=null){
            getMsgInfo();
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

    public void upDateAPK(VersionBean bean) {
        //更新
        if (DownLoadUtils.getInstance(getApplicationContext()).canDownload()) {
            DownloadApk.downloadApk(getApplicationContext(), bean.getFILEURL2(), UIUtils.getString(R.string.app_name) + "更新", "Hobbees");
        } else {
            DownLoadUtils.getInstance(getApplicationContext()).skipToDownloadManager();
        }

    }

    /**
     * 获取权限结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    DeviceInfo.searchIMEI();//回传设备信息
                }
                break;

            default:
                break;
        }
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
}
