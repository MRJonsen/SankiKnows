package com.zc.pickuplearn.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者： Jonsen
 * 时间: 2016/11/30 11:15
 * 联系方式：chenbin252@163.com
 */

public abstract class BaseFragment extends Fragment {
    protected boolean isVisible;
    private View view;
    private Context mContext;
    private Unbinder bind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        view = inflater.inflate(getLayoutId(), container, false);
//        bind = ButterKnife.bind(this, view);
//        mContext = getContext();
//        initView();
        if (view == null) {
            mContext = getContext();
            view = inflater.inflate(getLayoutId(), container, false);
            bind = ButterKnife.bind(this, view);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
//                parent.removeView(view);
                parent.removeAllViewsInLayout();
            }
        }
        return view;
    }

    /**
     * 获取根视图
     */
    public View getRootView() {
        return view;
    }


    /**
     * 在这里实现Fragment数据的懒加载. 需要与viewPage配合使用 否则需要手动设置
     *
     * @param isVisibleToUser 是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {

    }

    protected void onInvisible() {
    }

    /**
     * 获取布局id
     */
    public abstract int getLayoutId();

    /**
     * 初始化布局
     */
    public abstract void initView();

    public Context getmCtx() {
        return mContext;
    }

    @Override
    public void onDestroy() {
//        //注销消息接收
//        JMessageClient.unRegisterEventReceiver(this);
//        if (dialog != null) {
//            dialog.dismiss();
//        }
        super.onDestroy();
        bind.unbind();
    }


//    protected int mWidth;
//    private Dialog dialog;
//
//    private UserInfo myInfo;
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //订阅接收消息,子类只要重写onEvent就能收到消息
//        JMessageClient.registerEventReceiver(this);
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        mWidth = dm.widthPixels;
//    }
//
//    private View.OnClickListener onClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            dialog.dismiss();
//            clearData();
//            Intent intent = new Intent();
//            if (null != myInfo) {
//                intent.setClass(getActivity(), LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                getActivity().finish();
//            } else {
//                Log.d(TAG, "user info is null! Jump to Login activity");
//                intent.setClass(getActivity(), LoginActivity.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        }
//    };
//    /**
//     * 接收登录状态相关事件:登出事件,修改密码事件及被删除事件
//     * @param event 登录状态相关事件
//     */
//    @Subscribe
//    public void onEventMainThread(LoginStateChangeEvent event) {
//        LoginStateChangeEvent.Reason reason = event.getReason();
//        myInfo = event.getMyInfo();
//        if (null != myInfo) {
//            String path;
//            File avatar = myInfo.getAvatarFile();
//            if (avatar != null && avatar.exists()) {
//                path = avatar.getAbsolutePath();
//            } else {
//                path = FileHelper.getUserAvatarPath(myInfo.getUserName());
//            }
//            Log.i(TAG, "userName " + myInfo.getUserName());
//            SharePreferenceManager.setCachedUsername(myInfo.getUserName());
//            SharePreferenceManager.setCachedAvatarPath(path);
//            JMessageClient.logout();
//        }
//        switch (reason) {
//            case user_password_change:
//                String title = mContext.getString(R.string.change_password);
//                String msg = mContext.getString(R.string.change_password_message);
//                dialog = DialogCreator.createBaseCustomDialog(mContext, title, msg, onClickListener);
//                break;
//            case user_logout:
//                title = mContext.getString(R.string.user_logout_dialog_title);
//                msg = mContext.getString(R.string.user_logout_dialog_message);
//                dialog = DialogCreator.createBaseCustomDialog(mContext, title, msg, onClickListener);
//                break;
//            case user_deleted:
//                View.OnClickListener listener = new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        Intent intent = new Intent();
//                        intent.setClass(mContext, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        BaseFragment.this.getActivity().finish();
//                    }
//                };
//                title = mContext.getString(R.string.user_logout_dialog_title);
//                msg = mContext.getString(R.string.user_delete_hint_message);
//                dialog = DialogCreator.createBaseCustomDialog(mContext, title, msg, listener);
//                break;
//        }
//        dialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
//        dialog.show();
//    }
//    public void clearData(){
//        DataUtils.setUserInfo(null);//清除用户信息
//        MyAppliction.setAlias("");//推送取消设备的别名
//        EventBus.getDefault().post(new FinishEvent());
//        EventBus.getDefault().post(MainActivity.LOGOUT);//发消息 让主页finish掉
//    }
}
