package com.zc.pickuplearn.ui.mine.mine.view;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zc.pickuplearn.ui.file_view.FileDoer;
import com.yanzhenjie.album.AlbumFile;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.event.MyMsgEvent;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.ui.login.model.LoginModelImpl;
import com.zc.pickuplearn.ui.mine.mine.presenter.MinePresenterImpl;
import com.zc.pickuplearn.ui.mine.mine.widget.AboutActivity;
import com.zc.pickuplearn.ui.mine.mine.widget.MineAnswerActivity;
import com.zc.pickuplearn.ui.mine.mine.widget.MyQuestionActivity;
import com.zc.pickuplearn.ui.mine.school.view.SchoolActivity;
import com.zc.pickuplearn.ui.mine.setting.view.SettingActivity;
import com.zc.pickuplearn.ui.msgbox.PersonalMsgActivity;
import com.zc.pickuplearn.ui.question_and_answer.TypeQuestion;
import com.zc.pickuplearn.ui.sign.view.SignInActivity;
import com.zc.pickuplearn.ui.suggestion.view.SuggestionActivity;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.ui.view.dialog.ActionSheetDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.ImageSelectUtil;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.StatusBarUtil;
import com.zc.pickuplearn.utils.SystemUtils;
import com.zc.pickuplearn.utils.UIUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.eventbus.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 我的模块
 * created by bin 2016/11/30 14:13
 */

public class MineFragment extends BaseFragment implements IMineView {


    @BindView(R.id.civ_icon)
    CircleImageView mCivIcon;
    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.tv_my_question_msg)
    TextView tvMyQuestionMsg;//我的问题消息红点
    @BindView(R.id.tv_my_answer_msg)
    TextView tvMyAnswerMsg;//我的回答消息红点
    @BindView(R.id.tv_mine_new_msg_num)
    TextView tvMyNewMsg;
    @BindView(R.id.tv_person_score)
    TextView tv_person_score;
    private ProgressDialog progressDialog;
    private ActionSheetDialog builder = null;// 头像选择弹框
    private MinePresenterImpl presenter;
    private boolean IS_FIRST_IN = true;
    ArrayList mAlbumFile = new ArrayList<AlbumFile>();

    @Override
    public void onResume() {
        super.onResume();
        setInfo();
        getPersonData();
        if (IS_FIRST_IN) {
            IS_FIRST_IN = false;
        } else {
            EventBus.getDefault().post(new MyMsgEvent());
//            MainActivity context = (MainActivity) getContext();
//            context.getMsgInfo();//查询消息
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        presenter = new MinePresenterImpl(this);
        setInfo();
        //处理状态栏及间距
        StatusBarUtil.darkMode((Activity) getmCtx());
        StatusBarUtil.setPaddingSmart(getmCtx(), getRootView());
    }

    @OnClick({R.id.ll_question, R.id.ll_answer, R.id.ll_circle, R.id.rl_mine_new_msg,
            R.id.ll_setting, R.id.civ_icon, R.id.tv_name, R.id.ll_save,
            R.id.ll_personinfo, R.id.ib_mine_sign, R.id.ib_mine_setting, R.id.ll_service,
            R.id.ll_sugguest, R.id.ll_school, R.id.ll_about, R.id.ll_operate,
            R.id.ll_personal_info, R.id.ll_my_answer, R.id.ll_my_question, R.id.ll_my_train,
            R.id.ll_my_develop_grade, R.id.ll_my_experience, R.id.ll_my_honor})
    public void chooseFunction(View view) {
        switch (view.getId()) {
            case R.id.ll_question:
                startActivity(new Intent(getContext(), MyQuestionActivity.class));
                break;
            case R.id.ll_my_question:
                MyQuestionActivity.open(getmCtx(), TypeQuestion.PERSONQUESTION,DataUtils.getUserInfo());
//                startActivity(new Intent(getContext(), MyQuestionActivity.class));
                break;

            case R.id.ll_my_answer:
                MyQuestionActivity.open(getmCtx(), TypeQuestion.PERSONANSER,DataUtils.getUserInfo());
//                MineAnswerActivity.startMineAnswerActivity(getContext());
                break;


            case R.id.ll_answer:
                MineAnswerActivity.startMineAnswerActivity(getContext());
                break;

            case R.id.ll_circle:
                WebViewActivity.skip(getmCtx(), "http://120.27.152.63:7788/ecm/ZUI/pages/dzd/practice.html" + "?userId=" + DataUtils.getUserInfo().getUSERCODE(), UIUtils.getString(R.string.my_practice), true);
//                MyCircleActivity.startMyCircleActivity(getContext());
                break;
            case R.id.ll_setting:
                SettingActivity.startSettingActivity(getContext());
                break;
            case R.id.civ_icon:
                showPickPhotoDialog();
                break;
            case R.id.tv_name:
//                startActivity(new Intent(mContext, MyChangeNickNameActivity.class));
                break;
            case R.id.ll_save:
//                startActivity(new Intent(mContext, MyCollectionActivity.class));
                break;
            case R.id.ib_mine_sign://签到
                SignInActivity.startSignInActivity(getmCtx());
                break;
            case R.id.ll_personinfo:
                SettingActivity.startSettingActivity(getContext());
                break;
            case R.id.ll_personal_info:
                SettingActivity.startSettingActivity(getContext());
                break;
            case R.id.ib_mine_setting:
                SettingActivity.startSettingActivity(getContext());
                break;
            case R.id.ll_service:
                SystemUtils.call(getmCtx(), "15867208300");
                break;
            case R.id.ll_sugguest: //建议
                SuggestionActivity.startSuggestionActivity(getmCtx(), DataUtils.getUserInfo());
                break;
            case R.id.ll_school: //学堂
                SchoolActivity.startSchoolActivity(getmCtx());
                break;
            case R.id.ll_about: //关于
                AboutActivity.startAbout(getmCtx());
                break;
            case R.id.ll_operate: //操作手册
                openOperateHandbook();
                break;
            case R.id.rl_mine_new_msg:
                PersonalMsgActivity.startPersonalMsgActivity(getmCtx());
                break;
            case R.id.ll_my_train://我的培训
                UIUtils.startActionUrl(getmCtx(), "myTrain");
                break;
            case R.id.ll_my_experience://我的履历
                UIUtils.startActionUrl(getmCtx(), "myRecord");
                break;
            case R.id.ll_my_honor://我的获奖及荣誉
                UIUtils.startActionUrl(getmCtx(), "myAwards");
                break;
            case R.id.ll_my_develop_grade://我的发展积分
                UIUtils.startActionUrl(getmCtx(), "developIntegral");
                break;
        }
    }

    private void openOperateHandbook() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getmCtx());
        }
        String childUrl = "file/about/operation.pdf";
        int lastIndexOf = childUrl.lastIndexOf("/");
        String filename = childUrl.substring(lastIndexOf + 1);
        progressDialog.showProgressDialog();
        String url = HttpContacts.HOST + childUrl;
        LogUtils.e("下载" + url);
        HttpUtils.doDownFile(url, new FileCallBack(HttpContacts.absolutePath2, filename) {
            @Override
            public void onError(Call call, Exception e, int id) {
                progressDialog.dissMissProgressDialog();
            }

            @Override
            public void onResponse(File response, int id) {
                progressDialog.dissMissProgressDialog();
                FileDoer.getInstance().openDocument(getmCtx(), response.getAbsolutePath());
            }
        });
    }


    @Override
    public void setName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mName.setText(name);
        }
    }

    @Override
    public void setHeadImage(String url, boolean islocal) {
        if (!TextUtils.isEmpty(url)) {
            ImageLoaderUtil.displayCircleView(getContext(), mCivIcon, url, islocal);
        }
    }

    @Override
    public void showPickPhotoDialog() {
        builder = new ActionSheetDialog(getContext()).builder();
        builder.setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        ImageSelectUtil.takePhoto(getmCtx(), new ImageSelectUtil.ImageSelectCallBack() {
                            @Override
                            public void onSuccess(ArrayList<AlbumFile> albumfiles, List<String> imgPaths) {
                                presenter.uploadimage(imgPaths);
                            }
                        });
                    }
                })
                .addSheetItem("从相册中选取", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                ImageSelectUtil.choicePicture(getmCtx(), 1, mAlbumFile, new ImageSelectUtil.ImageSelectCallBack() {
                                    @Override
                                    public void onSuccess(ArrayList<AlbumFile> albumfiles, List<String> imgPaths) {
                                        mAlbumFile = albumfiles;
                                        presenter.uploadimage(imgPaths);
                                    }
                                });
                            }
                        });
        builder.show();
    }


    @Override
    public void setInfo() {
        UserBean userInfo = DataUtils.getUserInfo();
        if (userInfo != null) {
            setHeadImage(userInfo.getFILEURL(), false);
            if (TextUtils.isEmpty(userInfo.getNICKNAME())) {
                setName(userInfo.getUSERNAME());
            } else {
                setName(userInfo.getNICKNAME());
            }
        }
    }

    /**
     * 设置消息提示
     *
     * @param msg
     */
    public void setMyNewMsg(String msg) {
        if (tvMyNewMsg != null) {
            int allUnReadMsgCount = JMessageClient.getAllUnReadMsgCount();
            LogUtils.e("会话消息" + allUnReadMsgCount);
            if (!TextUtils.isEmpty(msg)) {
                Integer integer = Integer.valueOf(msg);
                if (integer > 0) {
                    allUnReadMsgCount = allUnReadMsgCount + integer;
                }
            }
            if (allUnReadMsgCount > 0) {
                LogUtils.e("消息总数" + allUnReadMsgCount);
                tvMyNewMsg.setVisibility(View.VISIBLE);
                if (allUnReadMsgCount > 99) {
                    tvMyNewMsg.setText("99+");
                } else {
                    tvMyNewMsg.setText(allUnReadMsgCount + "");
                }
            } else {
                tvMyNewMsg.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置我的问题提升
     *
     * @param msg
     */
    public void setMyQuestionMsg(String msg) {
        LogUtils.e("设置我的提问条数" + msg);
        if (tvMyQuestionMsg != null && !TextUtils.isEmpty(msg)) {
            if (Integer.valueOf(msg) > 0) {
                tvMyQuestionMsg.setVisibility(View.VISIBLE);
                tvMyQuestionMsg.setText(msg);
            } else {
                tvMyQuestionMsg.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置我的回答消息
     *
     * @param msg
     */
    public void setMyAnswerMsg(String msg) {
        LogUtils.e("设置我的回答消息条数" + msg);
        if (tvMyAnswerMsg != null && !TextUtils.isEmpty(msg)) {
            if (Integer.valueOf(msg) > 0) {
                tvMyAnswerMsg.setVisibility(View.VISIBLE);
                tvMyAnswerMsg.setText(msg);
            } else {
                tvMyAnswerMsg.setVisibility(View.GONE);
            }
        }
    }

    private void getPersonData() {
        //获取个人积分
        new LoginModelImpl().getUserInfo(DataUtils.getUserInfo().getUSERACCOUNT(), new LoginModelImpl.GetUserInfoCallBack() {

            @Override
            public void onSuccess(List<UserBean> data) {
                UserBean userBean = data.get(0);
                if (userBean != null) {
                    String sumpoints = userBean.getSUMPOINTS();
                    if (TextUtils.isEmpty(sumpoints)) {
                        sumpoints = "0";
                    }
                    tv_person_score.setText(sumpoints + "积分");
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
