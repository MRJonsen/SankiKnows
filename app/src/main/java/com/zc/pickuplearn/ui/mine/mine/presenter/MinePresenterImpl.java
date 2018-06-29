package com.zc.pickuplearn.ui.mine.mine.presenter;

import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.ui.login.model.LoginModelImpl;
import com.zc.pickuplearn.ui.mine.mine.model.MineModelImpl;
import com.zc.pickuplearn.ui.mine.mine.view.IMineView;
import com.zc.pickuplearn.utils.ThreadUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;

import java.io.File;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * 作者： Jonsen
 * 时间: 2017/1/16 14:57
 * 联系方式：chenbin252@163.com
 */

public class MinePresenterImpl implements IMinePresenter {
    IMineView mView;
    MineModelImpl model;

    public MinePresenterImpl(IMineView view) {
        mView = view;
        model = new MineModelImpl();
    }

    @Override
    public void uploadimage(final List<String> paths) {
        model.uploadImage(paths, new MineModelImpl.UploadHeadImageCallBack() {
            @Override
            public void onSuccess() {
                new LoginModelImpl().getUserInfo(DataUtils.getUserInfo().getUSERACCOUNT(), new LoginModelImpl.GetUserInfoCallBack() {
                    @Override
                    public void onSuccess(List<UserBean> data) {
                        mView.setInfo();
                        //极光上传头像
                        ThreadUtils.runOnSubThread(new Runnable() {
                            @Override
                            public void run() {
                                JMessageClient.updateUserAvatar(new File(paths.get(0)), new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }

            @Override
            public void onFailure() {
                ToastUtils.showToast(UIUtils.getContext(), "上传失败，稍后再试");
            }
        });
    }
}
