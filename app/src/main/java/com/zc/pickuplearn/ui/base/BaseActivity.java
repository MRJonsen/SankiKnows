package com.zc.pickuplearn.ui.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.application.MyAppliction;
import com.zc.pickuplearn.event.FinishEvent;
import com.zc.pickuplearn.ui.im.chatting.utils.DialogCreator;
import com.zc.pickuplearn.ui.im.chatting.utils.FileHelper;
import com.zc.pickuplearn.ui.im.chatting.utils.SharePreferenceManager;
import com.zc.pickuplearn.ui.login.view.LoginActivity;
import com.zc.pickuplearn.ui.main.widget.MainActivity;
import com.zc.pickuplearn.utils.DataUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.UserInfo;

import static cn.jpush.im.android.api.JMessageClient.logout;

public abstract class BaseActivity extends BaseCommonActivity {
    private UserInfo myInfo;
    private Dialog dialog;
    protected int mWidth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){//配合saveinstance方法解决fragement加载空白
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStackImmediate();
        }
        //订阅接收消息,子类只要重写onEvent就能收到
        JMessageClient.registerEventReceiver(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
    }

    //    private Unbinder bind;
//    private ProgressDialog progressDialog;
//    private Context mContext;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(setLayout());
//        bind = ButterKnife.bind(this);
//        initView();
//        initData();
//        mContext = this;
//    }
//
//    /**
//     * @return 传入布局id
//     */
//    public abstract int setLayout();
//
//    /**
//     * 初始化activity数据
//     */
//    protected abstract void initView();
//
//    /**
//     * 初始化activity数据
//     */
//    protected abstract void initData();
//
//    public void  showToast(String msg){
//        ToastUtils.showToastCenter(this,msg);
//    }
//
//    public void closeActivity(){
//        finish();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        bind.unbind();
//    }
//
//    public void showProgress(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (progressDialog==null){
//                    progressDialog = new ProgressDialog(mContext);
//                }
//                progressDialog.showProgressDialog();
//            }
//        });
//    }
//    public void hideProgress(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (progressDialog!=null){
//                    progressDialog.dissMissProgressDialog();
//                }
//            }
//        });
//    }
//    public void close(){
//        finish();
//    }

    /**
     * 接收登录状态相关事件:登出事件,修改密码事件及被删除事件
     * @param event 登录状态相关事件
     */
    public void onEventMainThread(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();
        myInfo = event.getMyInfo();
        if (null != myInfo) {
            String path;
            File avatar = myInfo.getAvatarFile();
            if (avatar != null && avatar.exists()) {
                path = avatar.getAbsolutePath();
            } else {
                path = FileHelper.getUserAvatarPath(myInfo.getUserName());
            }
            SharePreferenceManager.setCachedUsername(myInfo.getUserName());
            SharePreferenceManager.setCachedAvatarPath(path);
            logout();
        }
        switch (reason) {
            case user_password_change:
                String title = mContext.getString(R.string.change_password);
                String msg = mContext.getString(R.string.change_password_message);
                dialog = DialogCreator.createBaseCustomDialog(mContext, title, msg, onClickListener);
                break;
            case user_logout:
                title = mContext.getString(R.string.user_logout_dialog_title);
                msg = mContext.getString(R.string.user_logout_dialog_message);
                dialog = DialogCreator.createBaseCustomDialog(mContext, title, msg, onClickListener);
                break;
            case user_deleted:
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(BaseActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        BaseActivity.this.finish();
                    }
                };
                title = mContext.getString(R.string.user_logout_dialog_title);
                msg = mContext.getString(R.string.user_delete_hint_message);
                dialog = DialogCreator.createBaseCustomDialog(mContext, title, msg, listener);
                break;
        }
        dialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
            clearData();//清除用户信息
            Intent intent = new Intent();
            if (null != myInfo) {
                intent.setClass(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                BaseActivity.this.finish();
            } else {
                intent.setClass(BaseActivity.this, LoginActivity.class);
                startActivity(intent);
                BaseActivity.this.finish();
            }
        }
    };
    public void clearData(){
        DataUtils.setUserInfo(null);//清除用户信息
        MyAppliction.setAlias("");//推送取消设备的别名
        EventBus.getDefault().post(new FinishEvent());
        EventBus.getDefault().post(MainActivity.LOGOUT);//发消息 让主页finish掉
    }
    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        if (dialog != null){
            dialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

//        @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
////        super.onSaveInstanceState(outState, outPersistentState);
//    }
}
