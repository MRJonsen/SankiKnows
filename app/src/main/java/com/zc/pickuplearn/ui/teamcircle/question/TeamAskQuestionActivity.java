package com.zc.pickuplearn.ui.teamcircle.question;

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
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageDownLoadCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.askquestion.widget.AskQuestionEvent;
import com.zc.pickuplearn.ui.askquestion.widget.TagAdapter;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.im.chatting.utils.SharePreferenceManager;
import com.zc.pickuplearn.ui.question.FunctionType;
import com.zc.pickuplearn.ui.question.adapter.GVAdapter;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.AddMemeberEvent;
import com.zc.pickuplearn.ui.teamcircle.view.AskQuestionAddTargetMemberActivity;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.KeyboardLayout;
import com.zc.pickuplearn.ui.view.UISwitchButton;
import com.zc.pickuplearn.ui.view.dialog.ActionSheetDialog;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.ImageSelectUtil;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamAskQuestionActivity extends EventBusActivity {


    @BindView(R.id.activity_ask_question)
    KeyboardLayout rootView;
    @BindView(R.id.tv_target)
    TextView tvTarget; //指定回答人员布局
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
    private static final String IMG_ADD_TAG = "ADD_IMAGE";
    private static final String TAG_QUESTION = "AskQuestionActivity_questionbean";
    private static final String TAG_TEAM_CIRCLE = "AskQuestionActivity_teamcircle";
    private static final String TAG_FUNCTION_TYPE = TeamAskQuestionActivity.class.getSimpleName() + "_TYPE";
    private static final int IMG_LIMIT = 3;//添加图片数量

    private List<String> list = new ArrayList<>();
    private ActionSheetDialog builder = null;// 头像选择弹框
    private GVAdapter imageAdapter;
    private QuestionBean questionbean;
    private AlertDialog alertDialog;
    int mKeyboardHeight = 700; // 输入法默认高度为400
    private boolean NEED_SHOW_NAME = true; //记录是否匿名的变量 true不匿名
    private TagAdapter<String> mScoreAdapter;//悬赏积分的流布局适配器
    private String SCORE = "";//记录悬赏的积分数
    private QuestionBean eventQuestionBean;
    private boolean IS_EDIT_QUESTION = false;//是不是修改问题 默认不是
    private String TARGET_ANSWER_CODE = "";
    private TeamCircleBean teamCircleBean;
    private ArrayList<UserBean> targetMembers = new ArrayList<>();//指定回答人员表
    private FunctionType functionType;//功能类型
    private String title;//标题
    private String title_right;//标题右边按钮
    private String str_commit_warning;//提交数据时候提示语
    private AlertDialog dialog;
    private boolean KEY_VISIABLE = false;//记录键盘不可见

    public static void startTeamAskQuestionActivity(Context context, QuestionBean questionBean, TeamCircleBean teamCircleBean, FunctionType functionType) {
        Intent intent = new Intent(context, TeamAskQuestionActivity.class);
        intent.putExtra(TAG_QUESTION, questionBean);
        intent.putExtra(TAG_TEAM_CIRCLE, teamCircleBean);
        intent.putExtra(TAG_FUNCTION_TYPE, functionType);
        context.startActivity(intent);
    }


    @Override
    public int setLayout() {
        return R.layout.activity_team_ask_question;
    }

    @Override
    public void initView() {
        initQuestionEditText();
        initParam();
        initHeader();
        initScoreLayout();
        initBottomLayoutParams();
    }

    public void initParam() {
        questionbean = (QuestionBean) getIntent().getSerializableExtra(TAG_QUESTION);
        functionType = (FunctionType) getIntent().getSerializableExtra(TAG_FUNCTION_TYPE);
        teamCircleBean = (TeamCircleBean) getIntent().getSerializableExtra(TAG_TEAM_CIRCLE);
        switch (functionType) {
            case TEAM_QUESTION_ASK:
                title = "描述你的问题";
                title_right = "发送";
                str_commit_warning = "确定提交这个问题吗？";
                break;
            case TEAM_QUESTION_EDIT:
                title = "修改你的问题";
                title_right = "提交";
                str_commit_warning = "确定要修改问题？";
                break;
        }
        if (questionbean != null) {
            initParams();
        }
    }


    private void initHeader() {
        header.setLeftBackClickListener(new HeadView.LeftbackclickListener() {
            @Override
            public void onLeftBackClick() {
                if (!TextUtils.isEmpty(mEdittext.getText().toString().trim())){
                    new AlertDialog(TeamAskQuestionActivity.this).builder()
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
                }else {
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
    private void initQuestionEditText() {
        mEdittext.addTextChangedListener(new TextWatcher() {//对输入框进行监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int length = s.length();
                tvsugguesthint.setText(length + "/200");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                tvsugguesthint.setText(length + "/200");
                if (length>=200)
                    showToast("最多输入200个字！");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    /**
     * 从修改问题过来初始化参数
     */
    private void initParams() {
        if (questionbean != null) {
            IS_EDIT_QUESTION = true;
            mEdittext.setText(questionbean.getQUESTIONEXPLAIN());
            tvTarget.setText(questionbean.getTARGETUSERNAME());
            List<QuestionTypeBean> tip = questionbean.getTip();
            if (tip != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < tip.size(); i++) {
                    if (i == 0) {
                        stringBuilder.append(tip.get(i).getQUESTIONTYPENAME());
                    } else {
                        stringBuilder.append("," + tip.get(i).getQUESTIONTYPENAME());
                    }
                    tvTag.setText(stringBuilder.toString());
                }
            }
            SCORE = questionbean.getBONUSPOINTS();
        }
    }

    @Override
    protected void initData() {
        initAlbumView();
        initBottomListener();
    }

    //=================底部====================

    /**
     * 底部整体布局监听事件初始化
     */
    public void initBottomListener() {
        //底部功能条目的点击事件
        rl_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(bottomContent, TeamAskQuestionActivity.this);//关闭键盘
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
     */
    public void switchFunction(int index,boolean window) {
        if (bottomContent.getVisibility() == View.GONE) {
            bottomContent.setVisibility(View.VISIBLE);//显示底部
        } else if (bottomContent.getVisibility() == View.VISIBLE&&index!=2) {
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
        if (KEY_VISIABLE){
            KeyBoardUtils.closeKeybord(bottomContent, this);//关闭键盘
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
                if (window){
                    showPickPhotoDialog();
                }
                bottom_content_pic.setVisibility(View.VISIBLE);
                break;
        }
    }

///=================匿名====================

    /**
     * 匿名
     */
    public void initNameFunction() {
        switchButton_is_showname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NEED_SHOW_NAME = !isChecked;
                LogUtils.e(NEED_SHOW_NAME + "匿名" + isChecked);
            }
        });
        switchButton_is_showname.setChecked(false);//匿名按钮 默认关闭
    }
///=================积分====================

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
                    SCORE = sb.toString();
                    if (sb.toString().equals("0")) {
                        SCORE = "";
                    }
                } else {
                    ToastUtils.showToast(TeamAskQuestionActivity.this, "没有选择分数");
                }
            }
        });
        tvTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollGridActivity.startScrollGridActivity(TeamAskQuestionActivity.this, TypeGreenDaoEvent.AskQuestion);
            }
        });
        initScoreData();
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

    @OnClick({R.id.ask_anomynous, R.id.ask_score_icon_normal, R.id.ask_photo_normal, R.id.tv_target})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ask_anomynous:
                switchFunction(0,false);
                break;
            case R.id.ask_score_icon_normal:
                switchFunction(1,false);
                break;
            case R.id.ask_photo_normal:
                switchFunction(2,true);
                break;
            case R.id.tv_target:
                AskQuestionAddTargetMemberActivity.startAskQuestionAddTargetMemberActivity(this, teamCircleBean);
                break;
        }
    }

    //========================图片======================================
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
                        ToastUtils.showToast(TeamAskQuestionActivity.this, "最多只能选择" + (IMG_LIMIT) + "张照片！");
                }
            }
        });
    }

    /**
     * 初始化图片选择数据
     */
    private void initImageData() {
        if (questionbean != null) {
            List<String> strings = ResultStringCommonUtils.doSplitUrls(questionbean.getFILEURL());
            for (String url :
                    strings) {
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
            if (strings!=null&&strings.size()>0){
                switchFunction(2,false);
            }
        }
    }

    /**
     * 图片选择弹框
     */
    public void showPickPhotoDialog() {
        if (imageAdapter.getData().size() > IMG_LIMIT - 1) {
            ToastUtils.showToast(TeamAskQuestionActivity.this, "最多只能选择" + (IMG_LIMIT) + "张照片！");
            return;
        }
        if (builder == null) {
            builder = new ActionSheetDialog(this).builder();
            builder.setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            ImageSelectUtil.takePhoto(TeamAskQuestionActivity.this, new ImageSelectUtil.ImageSelectCallBack() {
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
                                    ImageSelectUtil.choicePicture(TeamAskQuestionActivity.this, IMG_LIMIT, imageAdapter.getData(), new Action<ArrayList<AlbumFile>>() {
                                        @Override
                                        public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                                            ArrayList<String> urls = new ArrayList<>();
                                            for (AlbumFile file :
                                                    result) {
                                                urls.add(file.getPath());
                                            }
                                            imageAdapter.addData(urls);
                                        }
//
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

//======================提交数据================================
    public void commitQuestion() {
        String question = mEdittext.getText().toString().trim();
        ArrayList<String> fils = new ArrayList<>();
        fils.addAll(imageAdapter.getData());
        if (questionbean == null) {
            questionbean = new QuestionBean();
        }
        UserBean userInfo = DataUtils.getUserInfo();
        questionbean.setQUESTIONEXPLAIN(question);
        questionbean.setQUESTIONUSERNAME(userInfo.getUSERNAME());
        questionbean.setQUESTIONUSERCODE(userInfo.getUSERCODE());
        questionbean.setQUESTIONTYPECODE(eventQuestionBean == null ? questionbean.getQUESTIONTYPECODE() : eventQuestionBean.getQUESTIONTYPECODE());
        questionbean.setBONUSPOINTS(SCORE);
        questionbean.setTEAMCIRCLECODE(teamCircleBean.getSEQKEY());
        if (!TextUtils.isEmpty(TARGET_ANSWER_CODE)) {
            questionbean.setTARGETUSERCODE(TARGET_ANSWER_CODE);
        }
        // TODO: 2017/3/16 指定人员回答的usercode
        showProgress();
        API.teamAskQuestionMyQuestion(questionbean, targetMembers, IS_EDIT_QUESTION, fils, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                hideProgress();
                processAskBackData(s);
            }

            @Override

            public void onFailure() {
                hideProgress();
                ToastUtils.showToast(TeamAskQuestionActivity.this, "操作失败！");
            }
        });
    }

    /**
     * 处理 提问成功返回数据
     *
     * @param response
     */
    private void processAskBackData(String response) {
        LogUtils.e("提问成功" + response);
        try {
            JsonElement datas = JsonUtils.decoElementJSONObject(response, "EXCEPTIONDATA");
            if (!TextUtils.isEmpty(datas.toString())) {
                JSONArray jsonArray = new JSONArray(datas.toString());
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String reason = jsonObject1.getString("reason");
                    ToastUtils.showToastCenter(TeamAskQuestionActivity.this, reason);
                } else {
                    ToastUtils.showToastCenter(TeamAskQuestionActivity.this, "操作成功！");
                    finish();
                }
            } else {
                ToastUtils.showToastCenter(TeamAskQuestionActivity.this, "操作成功！");
                finish();
            }
        } catch (Exception e) {
            ToastUtils.showToastCenter(TeamAskQuestionActivity.this, "服务器正忙！");
            e.printStackTrace();
        }
    }


    /**
     * 提交提醒框
     */
    private void commitDataWarningDialog() {
        String question = mEdittext.getText().toString().trim();
        if (question.length() < 6) {
            showDataAlertDialog("请输入不少于6个字的问题描述");
            return;
        } else if (question.length() > 200) {
            showDataAlertDialog("请输入不多于200字的问题描述");
            return;
        }
        if (alertDialog == null) {
            alertDialog = new AlertDialog(this).builder();
            alertDialog
                    .setTitle("提示").setMsg(str_commit_warning)
                    .setPositiveButton("提交", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commitQuestion();
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


    //问题类型添加监听
    @Subscribe
    public void onEventMainThread(AskQuestionEvent event) {
        if (event != null) {
            eventQuestionBean = event.getQuestionBean();
            tvTag.setText(eventQuestionBean.getQUESTIONTYPENAME());
        }
    }

    //添加指定人员回答监听
    @Subscribe
    public void onEventMainThread(AddMemeberEvent event) {
        if (event != null) {
            List<UserBean> userBeenlist = event.getUserBeen();
            if (userBeenlist != null && userBeenlist.size() > 0) {
                targetMembers.clear();
                targetMembers.addAll(userBeenlist);
                String target = "";
                for (int i = 0; i < userBeenlist.size(); i++) {
                    if (i == 0) {
                        target = target + userBeenlist.get(i).getUSERNAME();
                    } else {
                        target = target + "," + userBeenlist.get(i).getUSERNAME();
                    }
                }
                tvTarget.setText(target);
            }
        }
    }
}