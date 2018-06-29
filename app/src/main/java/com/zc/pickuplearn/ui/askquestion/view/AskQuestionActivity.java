package com.zc.pickuplearn.ui.askquestion.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.hhl.library.FlowTagLayout;
import com.hhl.library.OnTagSelectListener;
import com.yanzhenjie.album.AlbumFile;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.QuestionTypeBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.ui.askquestion.widget.AskQuestionEvent;
import com.zc.pickuplearn.ui.askquestion.widget.TagAdapter;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.login.model.LoginModelImpl;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.KeyboardLayout;
import com.zc.pickuplearn.ui.view.UISwitchButton;
import com.zc.pickuplearn.ui.view.dialog.ActionSheetDialog;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.ImageSelectUtil;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

//提问页面（废弃）
public class AskQuestionActivity extends EventBusActivity {
    private static final String IMG_ADD_TAG = "ADD_IMAGE";
    private static final String TAG = "AskQuestionActivity";
    private static final int IMG_COUNT = 4;//添加图片数量
    private static final int ACTIVITY_REQUEST_SELECT_PHOTO = 99;//请求码
    private List<String> list;
    private ActionSheetDialog builder = null;// 头像选择弹框
    private GVAdapter adapter;
    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.hv_head)
    HeadView header;
    @BindView(R.id.et_question)
    EditText mEdittext;
    @BindView(R.id.bottom_content)
    KeyboardLayout bottomContent;//底部内容
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
    private QuestionBean questionbean;
    private AlertDialog alertDialog;
    int mKeyboardHeight = 400; // 输入法默认高度为400
    private boolean NEED_SHOW_NAME = true; //记录是否匿名的变量 true不匿名
    private TagAdapter<String> mScoreAdapter;//悬赏积分的流布局适配器
    private String SCORE = "";//记录悬赏的积分数
    private QuestionBean eventQuestionBean;
    ArrayList mAlbumFile = new ArrayList<AlbumFile>();

    public static void startAskQuestionActivity(Context context, QuestionBean questionBean) {
        Intent intent = new Intent(context, AskQuestionActivity.class);
        intent.putExtra(TAG, questionBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_ask_question;
    }

    @Override
    public void initView() {
        getParam();
        initHeader();
        initScoreLayout();
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
                    SCORE = sb.toString();
                    if (sb.toString().equals("0")) {
                        SCORE = "";
                    }
                } else {
                    ToastUtils.showToast(AskQuestionActivity.this, "没有选择分数");
                }
            }
        });
        tvTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollGridActivity.startScrollGridActivity(AskQuestionActivity.this, TypeGreenDaoEvent.AskQuestion);
            }
        });
        initScoreData();
    }

    private void initHeader() {
        header.setMyOnClickListener(new HeadView.myOnClickListener() {
            @Override
            public void rightButtonClick() {
                comittwarningDialog();
            }
        });
        header.setTitle("问题的描述");
        header.setRightText("发送");
    }

    /**
     * 从修改问题过来初始化参数
     */
    private void initParams() {
        if (questionbean != null) {
            header.setTitle("修改问题");
            header.setRightText("提交");
            List<QuestionTypeBean> tip = questionbean.getTip();
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
    }

    @Override
    protected void initData() {
        if (list == null) {
            list = new ArrayList<>();
            list.add(IMG_ADD_TAG);
        }
        adapter = new GVAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).equals(IMG_ADD_TAG)) {
                    if (list.size() < IMG_COUNT) {
                        showPickPhotoDialog();
                    } else
                        ToastUtils.showToast(AskQuestionActivity.this, "最多只能选择" + (IMG_COUNT - 1) + "张照片！");
                }
            }
        });
        refreshAdapter();
        initBottomListener();
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

    private void refreshAdapter() {
        if (list == null) {
            list = new ArrayList<>();
            list.add(IMG_ADD_TAG);
        }
        if (adapter == null) {
            adapter = new GVAdapter();
        }
// TODO: 2017/1/17 解决最后添加按钮背景问题 添加一张图片时候被第一张图背景覆盖
        gridView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    public void showPickPhotoDialog() {
        if (list.size() > IMG_COUNT) {
            ToastUtils.showToast(AskQuestionActivity.this, "最多只能选择" + (IMG_COUNT - 1) + "张照片！");
            return;
        }
        if (builder == null) {
            builder = new ActionSheetDialog(this).builder();
            builder.setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            ImageSelectUtil.takePhoto(AskQuestionActivity.this, new ImageSelectUtil.ImageSelectCallBack() {
                                @Override
                                public void onSuccess(ArrayList<AlbumFile> albumfiles, List<String> imgPaths) {
                                    if (list.size() < 4)
                                        list.add(list.lastIndexOf(IMG_ADD_TAG), imgPaths.get(0));
                                    refreshAdapter();
                                }
                            });
                        }
                    })
                    .addSheetItem("从相册中选取", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    ImageSelectUtil.choicePicture(AskQuestionActivity.this, 3, mAlbumFile, new ImageSelectUtil.ImageSelectCallBack() {
                                        @Override
                                        public void onSuccess(ArrayList<AlbumFile> albumfiles, List<String> imgPaths) {
                                            mAlbumFile = albumfiles;
                                            for (String path :
                                                    imgPaths) {
                                                if (list.size() < 4)
                                                    list.add(list.lastIndexOf(IMG_ADD_TAG), path);
                                            }
                                            refreshAdapter();
                                        }
                                    });
//                                    Album.startAlbum(AskQuestionActivity.this, ACTIVITY_REQUEST_SELECT_PHOTO, IMG_COUNT - list.size(), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));
                                }
                            });
        }
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
//                List<String> pathList = Album.parseResult(data);
//                for (String path :
//                        pathList) {
//                    list.add(list.lastIndexOf(IMG_ADD_TAG), path);
//                }
//                refreshAdapter();
            }
        }
    }

    @OnClick({R.id.ask_anomynous, R.id.ask_score_icon_normal, R.id.ask_photo_normal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ask_anomynous:
                showBottomView(0);
                break;
            case R.id.ask_score_icon_normal:
                showBottomView(1);
                break;
            case R.id.ask_photo_normal:
                showBottomView(2);
                showPickPhotoDialog();
                break;
        }
    }

    /**
     * 底部整体布局监听事件初始化
     */
    public void initBottomListener() {
        bottomContent.setKeyboardListener(new KeyboardLayout.KeyboardLayoutListener() {//监听键盘的
            @Override
            public void onKeyboardStateChanged(boolean isActive, int keyboardHeight) {
                LogUtils.e("KeyboardLayout",keyboardHeight+","+isActive);
                if (isActive) { // 输入法打开
                    if (mKeyboardHeight != keyboardHeight) { // 键盘发生改变时才设置emojiView的高度，因为会触发onGlobalLayoutChanged，导致onKeyboardStateChanged再次被调用
                        mKeyboardHeight = keyboardHeight;
                        //// TODO: 2017/2/16  暂时不处理自动调整底部布局高度问题默认400px
//                        initBottomLayoutParams(); // 每次输入法弹起时，设置布局的高度为键盘的高度，以便下次弹出时刚好等于键盘高度
//                        showBottomView(0);//默认显示第一个
                    }
                } else {
//                    bottom_content_pic.setVisibility(View.GONE);
//                    bottom_content_name.setVisibility(View.GONE);
//                    bottom_content_score.setVisibility(View.GONE);
                }
            }
        });
        switchButton_is_showname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                NEED_SHOW_NAME = !isChecked;
                LogUtils.e(NEED_SHOW_NAME + "匿名" + isChecked);
            }
        });
        switchButton_is_showname.setChecked(false);//匿名按钮 默认关闭
    }

    private void initBottomLayoutParams() {
        ViewGroup.LayoutParams layoutParams = bottomContent.getLayoutParams();
        layoutParams.height = mKeyboardHeight;
        bottomContent.setLayoutParams(layoutParams);
    }

    /**
     * 展示底部三个隐藏的布局 0 匿名 1悬赏 2 图片
     */
    public void showBottomView(int index) {
//        KeyBoardUtils.closeKeybord(bottomContent, this);//关闭键盘
        if (bottomContent.getVisibility() == View.GONE) {
            bottomContent.setVisibility(View.VISIBLE);//显示底部
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
                bottom_content_pic.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void commitQuestion() {
        String question = mEdittext.getText().toString().trim();
        if (TextUtils.isEmpty(question)) {
            ToastUtils.showToast(this, "请输入问题描述！");
            return;
        } else if (question.length() < 5) {
            ToastUtils.showToast(this, "问题描述不少于5个字！");
            return;
        }
        ArrayList<String> fils = new ArrayList<>();
        fils.addAll(list);
        fils.remove(IMG_ADD_TAG);
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
        datas.put("QUESTIONTYPECODE", eventQuestionBean == null ? "" : eventQuestionBean.getQUESTIONTYPECODE());
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
        parametersMap.put("METHOD", questionbean == null ? "addSave" : "updateSave");// updateSave,delete,search
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
        try {
            jsonObject = ParamUtils.params2JsonObject("KLB_QUESTION_INFO", datas, parametersMap);
            LogUtils.e("提问发送数据",jsonObject.toString());
            if (fils.isEmpty()) {
                HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        processAskBackData(response);
                    }

                    @Override
                    public void onFailure(String error) {
                        ToastUtils.showToast(AskQuestionActivity.this, "服务器正忙！");
                        LogUtils.e("提问失败" + error);
                    }
                });
            } else {
                HttpUtils.doPostFile(HttpContacts.UI_URL, jsonObject, fils, new MyStringCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        processAskBackData(response);
                    }

                    @Override
                    public void onFailure(String error) {
                        ToastUtils.showToast(AskQuestionActivity.this, "服务器正忙！");
                        LogUtils.e("提问失败" + error);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(AskQuestionActivity.this, "服务器正忙！");
        }
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
                    ToastUtils.showToastCenter(AskQuestionActivity.this, reason);
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
            ToastUtils.showToastCenter(AskQuestionActivity.this, "服务器正忙！");
            e.printStackTrace();
        }
    }

    public void getParam() {
        questionbean = (QuestionBean) getIntent().getSerializableExtra(TAG);
    }


    /**
     * 提交提醒框
     */
    private void comittwarningDialog() {
        if (alertDialog == null) {
            alertDialog = new AlertDialog(this).builder();
            alertDialog
                    .setTitle("提示").setMsg("确定要提交这个提问吗?")
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


    private class GVAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplication()).inflate(R.layout.activity_add_photo_gv_items, parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.main_gridView_item_photo);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.main_gridView_item_cb);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String s = list.get(position);
            if (!s.equals(IMG_ADD_TAG)) {
                holder.checkBox.setVisibility(View.VISIBLE);
                ImageLoaderUtil.displayBitmap(AskQuestionActivity.this, holder.imageView, s, true);
            } else {
                holder.checkBox.setVisibility(View.GONE);
                holder.imageView.setBackgroundResource(R.mipmap.icon_addpic_unfocused);
            }
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    refreshAdapter();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            CheckBox checkBox;
        }

    }

    @Subscribe
    public void onEventMainThread(AskQuestionEvent event) {
        if (event != null) {
            eventQuestionBean = event.getQuestionBean();
            tvTag.setText(eventQuestionBean.getQUESTIONTYPENAME());
        }
    }
}
