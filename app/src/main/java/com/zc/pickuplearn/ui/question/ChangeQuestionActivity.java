package com.zc.pickuplearn.ui.question;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.hhl.library.FlowTagLayout;
import com.hhl.library.OnTagSelectListener;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.QuestionTypeBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.event.QuestionStatusChangeEvent;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.ImageDownLoadCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.ui.askquestion.widget.TagAdapter;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.im.chatting.utils.SharePreferenceManager;
import com.zc.pickuplearn.ui.login.model.LoginModelImpl;
import com.zc.pickuplearn.ui.question.adapter.GVAdapter;
import com.zc.pickuplearn.ui.type.view.PopScrollGridActivity;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.KeyboardLayout;
import com.zc.pickuplearn.ui.view.UISwitchButton;
import com.zc.pickuplearn.ui.view.dialog.ActionSheetDialog;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.ImageSelectUtil;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提问 修改问题页面
 */
public class ChangeQuestionActivity extends EventBusActivity {

    @BindView(R.id.activity_ask_question)
    KeyboardLayout rootView;
    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.hv_head)
    HeadView header;
    @BindView(R.id.et_question)
    EditText mEdittext;
    @BindView(R.id.bottom_content)
    FrameLayout bottomContent;//底部内容
    @BindView(R.id.bottom_content_pic)
    LinearLayout bottom_content_pic;
    @BindView(R.id.bottom_content_name)
    LinearLayout bottom_content_name;
    @BindView(R.id.bottom_content_score)
    LinearLayout bottom_content_score;
    @BindView(R.id.uisb_name)
    UISwitchButton switchButton_is_showname;
    @BindView(R.id.tv_person_score)
    TextView tvScore;//匿名个人分数
    @BindView(R.id.tv_score_score)
    TextView tvSocreScore;//悬赏个人分数
    @BindView(R.id.score_flow_layout)
    FlowTagLayout scoreflow;//积分选择流布局
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.rl_function)
    RelativeLayout rl_function;//底部功能条
    @BindView(R.id.tv_sugguest_hint)
    TextView tvsugguesthint;
//    @BindView(R.id.ask_score_icon_normal)
//    TextView mScore_change;//分数变化
    private static final String TAG = ChangeQuestionActivity.class.getSimpleName();
    private static final String TAG_FUNCTION_TYPE = ChangeQuestionActivity.class.getSimpleName() + "_TYPE";
    private String title;//标题
    private String title_right;//标题右边按钮
    private String str_commit_warning;//提交数据时候提示语

    private static final int IMG_LIMIT = 3;//添加图片数量
    private List<String> list = new ArrayList<>();
    private ActionSheetDialog builder = null;// 头像选择弹框
    private GVAdapter imageAdapter;//图片编辑区域的适配器
    private QuestionBean questionbean;
    private AlertDialog alertDialog;
    int mKeyboardHeight = 700; // 键盘默认高度
    private boolean NEED_SHOW_NAME = true; //记录是否匿名的变量 true不匿名
    private TagAdapter<String> mScoreAdapter;//悬赏积分的流布局适配器
    private String SCORE = "";//记录悬赏的积分数
    private QuestionBean tagQuestionBean;
    private AlertDialog dialog;
    private FunctionType functionType;//功能类型
    private boolean KEY_VISIABLE = false;//记录键盘不可见
    private int total_point = 0;//总的个人积分
    private int award_point = 0;//奖励分变化记录
    private int init_award_bonus = 0;//初始化奖励分
    private int init_anonymity_bonus = 0;//初始化匿名分数


    public static void startChangeQuestionActivity(Context context, QuestionBean questionBean, FunctionType functionType) {
        Intent intent = new Intent(context, ChangeQuestionActivity.class);
        intent.putExtra(TAG, questionBean);
        intent.putExtra(TAG_FUNCTION_TYPE, functionType);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_change_qustion;
    }

    @Override
    public void initView() {
        initParam();
        initHeader();
//        initQuestionEditText();
        initScoreLayout();
        initBottomLayoutParams();
    }

    @Override
    protected void initData() {
        initAlbumView();
        initBottomListener();
        getPersonScore();
    }

//    private void initQuestionEditText() {
//        mEdittext.addTextChangedListener(new TextWatcher() {//对输入框进行监听
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                int length = s.length();
//                tvsugguesthint.setText(length + "/200");
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                int length = s.length();
//                tvsugguesthint.setText(length + "/200");
//                if (length>=200)
//                showToast("最多输入200个字！");
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }


    /**
     * 获取初始化数据
     */
    public void initParam() {
        questionbean = (QuestionBean) getIntent().getSerializableExtra(TAG);
        functionType = (FunctionType) getIntent().getSerializableExtra(TAG_FUNCTION_TYPE);
        switch (functionType) {
            case QUESTION_ASK:
                title = "描述你的问题";
                title_right = "发送";
                str_commit_warning = "确定提交这个问题吗？";
                break;
            case QUESTION_EDIT:
                title = "修改你的问题";
                title_right = "提交";
                str_commit_warning = "确定要修改问题？";
                break;
        }
    }
    //===================================顶部功能=====================================

    /**
     * 初始化顶部布局
     */
    private void initHeader() {
        header.setLeftBackClickListener(new HeadView.LeftbackclickListener() {
            @Override
            public void onLeftBackClick() {
                if (!TextUtils.isEmpty(mEdittext.getText().toString().trim())) {
                    new AlertDialog(ChangeQuestionActivity.this).builder()
                            .setTitle("提示").setMsg("现在退出编辑，你输入的内容将不会被保存")
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    close();
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                } else {
                    close();
                }
            }
        });
        header.setMyOnClickListener(new HeadView.myOnClickListener() {
            @Override
            public void rightButtonClick() {
                commitDataWarningDialog();
            }
        });
        header.setTitle(title);
        header.setRightText(title_right);
    }

    /**
     * 获取个人积分
     */
    private void getPersonScore() {
        UserBean userInfo = DataUtils.getUserInfo();
        //获取个人积分
        new LoginModelImpl().getUserInfo(userInfo.getUSERACCOUNT(), new LoginModelImpl.GetUserInfoCallBack() {

            @Override
            public void onSuccess(List<UserBean> data) {
                UserBean userBean = data.get(0);
                if (userBean != null) {
                    String sumpoints = userBean.getSUMPOINTS();
                    if (TextUtils.isEmpty(sumpoints)) {
                        sumpoints = "0";
                    } else {
                        total_point = Integer.parseInt(sumpoints);
                    }
                    tvScore.setText(sumpoints);
                    tvSocreScore.setText(sumpoints);
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

//======================底部功能=========================

    /**
     * 底部整体布局监听事件初始化
     */
    public void initBottomListener() {
        //底部功能条目的点击事件
        rl_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(bottomContent, ChangeQuestionActivity.this);//关闭键盘
                bottomContent.setVisibility(View.GONE);
            }
        });
        //监听键盘弹出收回
        rootView.setKeyboardListener(new KeyboardLayout.KeyboardLayoutListener() {
            @Override
            public void onKeyboardStateChanged(boolean isActive, int keyboardHeight) {
                KEY_VISIABLE = isActive;
                if (isActive) {
                    bottomContent.setVisibility(View.GONE);
                }
            }
        });
        initNameFunction();
    }

    private void initBottomLayoutParams() {
        int softKeyboardHeight = SharePreferenceManager.getCachedKeyboardHeight();
        if (softKeyboardHeight > 0) {
            mKeyboardHeight = softKeyboardHeight;
        }
        ViewGroup.LayoutParams layoutParams = bottomContent.getLayoutParams();
        layoutParams.height = mKeyboardHeight;
        bottomContent.setLayoutParams(layoutParams);
    }

    /**
     * 展示底部三个隐藏的布局 0 匿名 1悬赏 2 图片
     * window 选择第三个功能时候是否要显示弹窗
     */
    public void switchFunction(int index, boolean window) {
        if (bottomContent.getVisibility() == View.GONE) {
            bottomContent.setVisibility(View.VISIBLE);//显示底部
        } else if (bottomContent.getVisibility() == View.VISIBLE && index != 2) {
            switch (index) {
                case 0:
                    if (bottom_content_name.getVisibility() == View.VISIBLE)
                        bottomContent.setVisibility(View.GONE);
                    break;
                case 1:
                    if (bottom_content_score.getVisibility() == View.VISIBLE)
                        bottomContent.setVisibility(View.GONE);
                    break;
                case 2:
                    if (bottom_content_pic.getVisibility() == View.VISIBLE)
                        bottomContent.setVisibility(View.GONE);
                    break;
            }
        }
        if (KEY_VISIABLE) {
            KeyBoardUtils.closeKeybord(bottomContent, ChangeQuestionActivity.this);//关闭键盘
        }
        bottom_content_pic.setVisibility(View.GONE);
        bottom_content_name.setVisibility(View.GONE);
        bottom_content_score.setVisibility(View.GONE);
        switch (index) {
            case 0:
                bottom_content_name.setVisibility(View.VISIBLE);
                break;
            case 1:
                bottom_content_score.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (window) {
                    showPickPhotoDialog();
                }
                bottom_content_pic.setVisibility(View.VISIBLE);
                break;
        }
    }
    //=======================匿名========================


    /**
     * 匿名布局
     */
    public void initNameFunction() {
        switchButton_is_showname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NEED_SHOW_NAME = !isChecked;
                if (!NEED_SHOW_NAME) {
                    if (calScore(10, 1)) {
                        switchButton_is_showname.setChecked(false);
                        showToast("积分不足");
                        NEED_SHOW_NAME = true;
                    }
                }
                LogUtils.e(NEED_SHOW_NAME + "匿名" + isChecked);
            }
        });

        if (questionbean != null) {
            String isanonymity = questionbean.getISANONYMITY();
            init_anonymity_bonus = "1".equals(isanonymity) ? 10 : 0;//给匿名分赋值 1为匿名 0 不匿名
            switchButton_is_showname.setChecked("1".equals(isanonymity));
            NEED_SHOW_NAME = !("1").equals(isanonymity);
        } else {
            switchButton_is_showname.setChecked(false);//匿名按钮 默认关闭
        }
    }

    //==================================积分===================================

    /**
     * 计算积分
     *
     * @param score
     * @param function 1。计算匿名  2。计算
     * @return
     */
    private boolean calScore(int score, int function) {
        boolean is_enough = false;
        LogUtils.e("total_point" + total_point + "init_anonymity_bonus" + init_anonymity_bonus + "init_award_bonus" + init_award_bonus + "award_point" + award_point + "score" + score);
        switch (function) {
            case 1:
                if ((total_point + init_anonymity_bonus + init_award_bonus - award_point - score) < 0) {//计算积分
                    showToast("积分不足");
                    return true;
                }
                break;
            case 2:
                if ((total_point + init_anonymity_bonus + init_award_bonus - score - (switchButton_is_showname.isChecked() ? 10 : 0)) < 0) {//计算积分
                    showToast("积分不足");
                    return true;
                }
                break;
        }
        return is_enough;
    }

    /**
     * c初始化积分布局
     */
    private void initScoreLayout() {
        //尺寸
        mScoreAdapter = new TagAdapter<>(this);
        scoreflow.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        scoreflow.setAdapter(mScoreAdapter);
        scoreflow.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                    }
                    if (calScore(Integer.parseInt(sb.toString()), 2)) {//计算积分
                        parent.clearAllOption();
                    } else {
                        SCORE = sb.toString();
                        if (sb.toString().equals("0")) {
                            SCORE = "";
                        }
                        setScore(SCORE);
                    }
                } else {
                    setScore("");
                }
            }
        });
        tvTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopScrollGridActivity.startScrollGridActivity(ChangeQuestionActivity.this, TypeGreenDaoEvent.ChangeQuestion);
            }
        });
        initScoreData();
        if (questionbean != null) {
            mEdittext.setText(questionbean.getQUESTIONEXPLAIN());
            List<QuestionTypeBean> tip = questionbean.getTip();
            if (tip != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < tip.size(); i++) {
                    if (i == 0) {
                        stringBuilder.append(tip.get(i).getQUESTIONTYPENAME());
                    } else {
                        stringBuilder.append("," + tip.get(i).getQUESTIONTYPENAME());
                    }
                }
                String tag = stringBuilder.toString();
                if (!TextUtils.isEmpty(tag))
                    tvTag.setText(tag);
            }
            SCORE = questionbean.getBONUSPOINTS();
            setScore(SCORE);
            if (!TextUtils.isEmpty(SCORE)) {
                init_award_bonus = Integer.parseInt(SCORE);
            }
        }
    }

    private void setScore(String score) {
//        if (mScore_change != null) {
//            if (TextUtils.isEmpty(score)) {
//                award_point = 0;
//            } else {
//                award_point = Integer.parseInt(score);
//            }
//            mScore_change.setText(score);
//        }
    }

    /**
     * 初始化 积分布局流布局的 分数数据
     */
    private void initScoreData() {
        List<String> dataSource = new ArrayList<>();
        dataSource.add("0");
        dataSource.add("5");
        dataSource.add("10");
        dataSource.add("20");
        dataSource.add("30");
        dataSource.add("50");
        dataSource.add("70");
        dataSource.add("100");
        mScoreAdapter.onlyAddAll(dataSource);
    }


    @OnClick({R.id.ask_anomynous, R.id.ask_score_icon_normal, R.id.ask_photo_normal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ask_anomynous:
                switchFunction(0, false);
                break;
            case R.id.ask_score_icon_normal:
                switchFunction(1, false);
                break;
            case R.id.ask_photo_normal:
                switchFunction(2, true);
                break;
        }
    }
    //=======================================================================

    //=============================图片处理==========================================================

    /**
     * 初始化图片选择布局
     */
    private void initAlbumView() {
        imageAdapter = new GVAdapter(this, list);
        gridView.setAdapter(imageAdapter);
        initImageData();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).equals(GVAdapter.IMG_ADD_TAG)) {
                    if (imageAdapter.getData().size() < IMG_LIMIT) {
                        showPickPhotoDialog();
                    } else
                        ToastUtils.showToast(ChangeQuestionActivity.this, "最多只能选择" + (IMG_LIMIT) + "张照片！");
                }
            }
        });
    }

    /**
     * 初始化图片选择数据
     */
    private void initImageData() {
        List<String> strings = ResultStringCommonUtils.doSplitUrls(questionbean.getFILEURL());
        for (String url : strings) {
            String imgUrl = ResultStringCommonUtils.subUrlToWholeUrl(url);

            ImageLoaderUtil.downLoadImg(imgUrl, new ImageDownLoadCallBack() {
                @Override
                public void onDownLoadSuccess(File file) {

                    imageAdapter.addData(file.getAbsolutePath());
                }

                @Override
                public void onDownLoadFailed() {

                }
            });
        }
        if (strings != null && strings.size() > 0) {
            switchFunction(2, false);
        }
    }

    /**
     * 图片选择弹框
     */
    public void showPickPhotoDialog() {
        if (imageAdapter.getData().size() > IMG_LIMIT - 1) {
            ToastUtils.showToast(ChangeQuestionActivity.this, "最多只能选择" + (IMG_LIMIT) + "张照片！");
            return;
        }
        if (builder == null) {
            builder = new ActionSheetDialog(this).builder();
            builder.setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            ImageSelectUtil.takePhoto(ChangeQuestionActivity.this, new ImageSelectUtil.ImageSelectCallBack() {
                                @Override
                                public void onSuccess(ArrayList<AlbumFile> albumfiles, List<String> imgPaths) {
                                    if (imageAdapter.getData().size() < 3)
                                        imageAdapter.addData(imgPaths.get(0));
                                }
                            });
                        }
                    })
                    .addSheetItem("从相册中选取", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    ImageSelectUtil.choicePicture(ChangeQuestionActivity.this, IMG_LIMIT, imageAdapter.getData(), new Action<ArrayList<AlbumFile>>() {
                                        @Override
                                        public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                                            ArrayList<String> urls = new ArrayList<>();
                                            for (AlbumFile file :
                                                    result) {
                                                urls.add(file.getPath());
                                            }
                                            imageAdapter.addData(urls);
                                        }

//                                        @Override
//                                        public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
//                                            ArrayList<String> urls = new ArrayList<>();
//                                            for (AlbumFile file :
//                                                    result) {
//                                                urls.add(file.getPath());
//                                            }
//                                            imageAdapter.addData(urls);
//                                        }
//
//                                        @Override
//                                        public void onAlbumCancel(int requestCode) {
//
//                                        }
                                    });
                                }
                            });
        }
        builder.show();

    }
    //==============================================================================================

    //===================================提交数据相关=================================================

    /**
     * 提交数据提醒框
     */
    private void commitDataWarningDialog() {
        String question = mEdittext.getText().toString().trim();
        if (question.length() < 6) {
            showDataAlertDialog("请输入不少于6个字的问题描述");
            return;
        }
//        else if (question.length() > 200) {
//            showDataAlertDialog("请输入不多于200字的问题描述");
//            return;
//        }
        if (alertDialog == null) {
            alertDialog = new AlertDialog(this).builder();
            alertDialog.setCancelable(false);
            alertDialog
                    .setTitle("提示").setMsg(str_commit_warning)
                    .setPositiveButton("提交", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (functionType) {
                                case QUESTION_ASK:
                                    netQuestionAsk();
                                    break;
                                case QUESTION_EDIT:
                                    netQuestionEditData();
                                    break;
                            }

                        }
                    }).setNegativeButton("我再想想", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        alertDialog.show();
    }


    /**
     * 提交数据时候题时光
     *
     * @param msg
     */
    public void showDataAlertDialog(String msg) {
        if (dialog == null) {
            dialog = new AlertDialog(this).builder();
            dialog.setCancelable(false);
            dialog.setPositiveButton(XFrame.getString(R.string.sure), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        dialog.setMsg(msg);
        dialog.show();

    }


    /**
     * 提交问题修改数据
     */
    public void netQuestionEditData() {
        String question = mEdittext.getText().toString().trim();
        ArrayList<String> fils = new ArrayList<>();
        for (String url :
                imageAdapter.getData()) {
            LogUtils.e("提交问，图片", url);
        }
        fils.addAll(imageAdapter.getData());
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        if (fils.size() != 0) {
            datas.put("FILEURL", "");
        }
        datas.put("SYSCREATEDATE", DateUtils.dataFormatNow("yyyy-MM-dd HH:mm:ss"));
        datas.put("QUESTIONUSERNAME", userInfo.getUSERNAME());
        datas.put("SEQKEY", questionbean == null ? "" : questionbean.getSEQKEY());
        datas.put("QUESTIONUSERCODE", userInfo.getUSERCODE());
        datas.put("QUESTIONEXPLAIN", question);
        datas.put("QUESTIONTYPECODE", tagQuestionBean == null ? questionbean.getQUESTIONTYPECODE() : tagQuestionBean.getQUESTIONTYPECODE());
        datas.put("ISANONYMITY", NEED_SHOW_NAME ? "0" : "1");//0 不匿名  1 匿名
        datas.put("BONUSPOINTS", SCORE);
        datas.put("ISSOLVE", questionbean == null ? "0" : questionbean.getISSOLVE());
        datas.put("QUESTIONLEVEL", "1");
        datas.put("IMPORTLEVEL", "1");
        datas.put("ISEXPERTANSWER", "0");
        datas.put("ISLOSE", "0");
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.QUESTION_POINTS);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.UPDATASAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", "KLB_QUESTION_INFO");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject = null;
        showProgress();
        try {
            jsonObject = ParamUtils.params2JsonObject("KLB_QUESTION_INFO", datas, parametersMap);
            LogUtils.e("修改问题" + jsonObject.toString());
            HttpUtils.doPostFile(HttpContacts.UI_URL, jsonObject, fils, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e(response);
                    processQuestionEditBackData(response);
                }

                @Override
                public void onFailure(String error) {
                    ToastUtils.showToast(ChangeQuestionActivity.this, "服务器正忙！");
                    LogUtils.e("提问失败" + error);
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    hideProgress();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(ChangeQuestionActivity.this, "服务器正忙！");
        }
    }

    /**
     * 处理 修改提问成功返回数据
     *
     * @param response
     */
    private void processQuestionEditBackData(String response) {
        try {
            JsonElement datas = JsonUtils.decoElementJSONObject(response, "EXCEPTIONDATA");
            if (!TextUtils.isEmpty(datas.toString())) {
                JSONArray jsonArray = new JSONArray(datas.toString());
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String reason = jsonObject1.getString("reason");
                    ToastUtils.showToastCenter(ChangeQuestionActivity.this, reason);
                } else {
                    JsonElement datas1 = JsonUtils.decoElementJSONObject(response, "COMMAND");
                    String target = JsonUtils.decoElementASString(datas1.toString(), "target");
                    if (!TextUtils.isEmpty(target) && !"null".equals(target)) {
                        showToast(target);
                    }

                    EventBus.getDefault().post(new QuestionChangeEvent());
                    EventBus.getDefault().post(new QuestionStatusChangeEvent());
                    finish();
                }
            } else {
                JsonElement datas1 = JsonUtils.decoElementJSONObject(response, "COMMAND");
                String target = JsonUtils.decoElementASString(datas1.toString(), "target");
                if (!TextUtils.isEmpty(target) && !"null".equals(target)) {
                    showToast(target);
                }
                finish();
            }
        } catch (Exception e) {
            ToastUtils.showToastCenter(ChangeQuestionActivity.this, "操作失败！");
            e.printStackTrace();
        }
    }


    /**
     * 提问
     */
    public void netQuestionAsk() {
        String question = mEdittext.getText().toString().trim();
        ArrayList<String> fils = new ArrayList<>();
        fils.addAll(imageAdapter.getData());
        UserBean userInfo = DataUtils.getUserInfo();
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        // 第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("SYSCREATEDATE", DateUtils.dataFormatNow("yyyy-MM-dd HH:mm:ss"));
        datas.put("QUESTIONUSERNAME", userInfo.getUSERNAME());
        datas.put("QUESTIONUSERCODE", userInfo.getUSERCODE());
        datas.put("QUESTIONEXPLAIN", question);
        datas.put("QUESTIONTYPECODE", questionbean == null ? "" : questionbean.getQUESTIONTYPECODE());
        datas.put("ISANONYMITY", NEED_SHOW_NAME ? "0" : "1");//0 不匿名  1 匿名
        datas.put("BONUSPOINTS", SCORE);
        datas.put("QUESTIONTYPECODE", tagQuestionBean == null ? "" : tagQuestionBean.getQUESTIONTYPECODE());
        datas.put("ISSOLVE", "0");
        datas.put("QUESTIONLEVEL", "1");
        datas.put("IMPORTLEVEL", "1");
        datas.put("ISEXPERTANSWER", "0");
        datas.put("ISLOSE", "0");
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.QUESTION_POINTS);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", UrlMethod.ADDSAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", "KLB_QUESTION_INFO");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject = null;
        showProgress();
        try {
            jsonObject = ParamUtils.params2JsonObject("KLB_QUESTION_INFO", datas, parametersMap);
            LogUtils.e("提问发送数据", jsonObject.toString());
            if (fils.isEmpty()) {
                HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                processAskQuestionBackData(response);
                            }

                            @Override
                            public void onFailure(String error) {
                                ToastUtils.showToast(ChangeQuestionActivity.this, "服务器正忙！");
                                LogUtils.e("提问失败" + error);
                            }

                            @Override
                            public void onAfter(int id) {
                                super.onAfter(id);
                                hideProgress();
                            }
                        }
                );
            } else {
                HttpUtils.doPostFile(HttpContacts.UI_URL, jsonObject, fils, new MyStringCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        processAskQuestionBackData(response);
                    }

                    @Override
                    public void onFailure(String error) {
                        ToastUtils.showToast(ChangeQuestionActivity.this, "操作失败！");
                        LogUtils.e("提问失败" + error);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        hideProgress();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(ChangeQuestionActivity.this, "服务器正忙！");
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (!TextUtils.isEmpty(mEdittext.getText().toString().trim())) {
            new AlertDialog(ChangeQuestionActivity.this).builder()
                    .setTitle("提示").setMsg("现在退出编辑，你输入的内容将不会被保存")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            close();
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else {
            close();
        }
    }

    /**
     * 处理 提问成功返回数据
     *
     * @param response
     */
    private void processAskQuestionBackData(String response) {
        EventBus.getDefault().post(new QuestionStatusChangeEvent());
        try {
            JsonElement datas = JsonUtils.decoElementJSONObject(response, "EXCEPTIONDATA");
            if (!TextUtils.isEmpty(datas.toString())) {
                JSONArray jsonArray = new JSONArray(datas.toString());
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String reason = jsonObject1.getString("reason");
                    ToastUtils.showToastCenter(ChangeQuestionActivity.this, reason);
                } else {
                    JsonElement datas1 = JsonUtils.decoElementJSONObject(response, "COMMAND");
                    String target = JsonUtils.decoElementASString(datas1.toString(), "target");
                    showToast(target);
                    finish();
                }
            } else {
                JsonElement datas1 = JsonUtils.decoElementJSONObject(response, "COMMAND");
                String target = JsonUtils.decoElementASString(datas1.toString(), "target");
                showToast(target);
                finish();
            }
        } catch (Exception e) {
            ToastUtils.showToastCenter(ChangeQuestionActivity.this, "服务器正忙！");
            e.printStackTrace();
        }
    }
    //==============================================================================================


    @Subscribe
    public void onEventMainThread(ChangeQustionEvent event) {
        //接收分类页面的点选消息
        if (event != null) {
            tagQuestionBean = event.getQuestionBean();
            tvTag.setText(tagQuestionBean.getQUESTIONTYPENAME());
        }
    }

}
