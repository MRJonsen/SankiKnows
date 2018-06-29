package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.question.adapter.GVAdapter;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.dialog.ActionSheetDialog;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.ImageSelectUtil;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class TeamAnswerQuestionActivity extends BaseActivity {
    private static final String IMG_ADD_TAG = "ADD_IMAGE";
    private static final String TAG = "TeamAnswerQuestionActivity";
    private static final int IMG_LIMIT = 3;//添加图片数量
    private List<String> list = new ArrayList<>();
    private ActionSheetDialog builder = null;// 头像选择弹框
    private GVAdapter imageAdapter;
    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.hv_head)
    HeadView header;
    @BindView(R.id.et_question)
    EditText mEdittext;
    @BindView(R.id.tv_question)
    TextView mQuestion;
    @BindView(R.id.tv_sugguest_hint)
    TextView tvsugguesthint;
    private AlertDialog dialog;
    private QuestionBean questionBean;
    private AlertDialog alertDialog;

    /**
     * 开启本页面的方法
     *
     * @param context
     */
    public static void startTeamAnswerQuestionActivity(Context context, QuestionBean questionBean) {
        Intent intent = new Intent(context, TeamAnswerQuestionActivity.class);
        intent.putExtra(TAG, questionBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_answer_question;
    }

    @Override
    public void initView() {
        initQuestionEditText();
        getParam();
        initHeader();
    }

    private void initHeader() {
        header.setLeftBackClickListener(new HeadView.LeftbackclickListener() {
            @Override
            public void onLeftBackClick() {
                if (!TextUtils.isEmpty(mEdittext.getText().toString().trim())){
                    new AlertDialog(TeamAnswerQuestionActivity.this).builder()
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
        header.setTitle("回答");
        header.setRightText("提交");
        header.setMyOnClickListener(new HeadView.myOnClickListener() {
            @Override
            public void rightButtonClick() {
                comittwarningDialog();
            }
        });
        if (questionBean != null) {
            mQuestion.setText("问:" + questionBean.getQUESTIONEXPLAIN());
        }
    }

    @Override
    protected void initData() {
        initAlbumView();
    }


    private void initQuestionEditText() {
        mEdittext.addTextChangedListener(new TextWatcher() {//对输入框进行监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int length = s.length();
                tvsugguesthint.setText(length + "/500");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                tvsugguesthint.setText(length + "/500");
                if (length>=500)
                    showToast("最多输入500个字！");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    /**
     * 初始化图片选择布局
     */
    private void initAlbumView() {
        imageAdapter = new GVAdapter(this, list);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).equals(GVAdapter.IMG_ADD_TAG)) {
                    if (imageAdapter.getData().size() < IMG_LIMIT) {
                        showPickPhotoDialog();
                    } else
                        ToastUtils.showToast(TeamAnswerQuestionActivity.this, "最多只能选择" + (IMG_LIMIT) + "张照片！");
                }
            }
        });
    }

    /**
     * 图片选择弹框
     */
    public void showPickPhotoDialog() {
        if (imageAdapter.getData().size() > IMG_LIMIT - 1) {
            ToastUtils.showToast(TeamAnswerQuestionActivity.this, "最多只能选择" + (IMG_LIMIT) + "张照片！");
            return;
        }
        if (builder == null) {
            builder = new ActionSheetDialog(this).builder();
            builder.setCancelable(false)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            ImageSelectUtil.takePhoto(TeamAnswerQuestionActivity.this, new ImageSelectUtil.ImageSelectCallBack() {
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
                                    ImageSelectUtil.choicePicture(TeamAnswerQuestionActivity.this, IMG_LIMIT, imageAdapter.getData(), new Action<ArrayList<AlbumFile>>() {
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

    /**
     * 提交答案
     */
    public void commitAnswer() {
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
        datas.put("SYSCREATEDATE", DateUtils.dataFormatNow("yyyy-MM-dd MM:hh:ss"));
        datas.put("ANSWERUSERNAME", userInfo.getUSERNAME());
        datas.put("ANSWERUSERCODE", userInfo.getUSERCODE());
        datas.put("ANSWEREXPLAIN", question);
        datas.put("QUESTIONID", questionBean.getSEQKEY());
        datas.put("ISPASS", "0");
        // 第一条记录结束
        // 参数区域赋值
        // String whereSql = "QUESTIONID='"+seqkey+"'";
        parametersMap.put("CLASSNAME",
                UrlMethod.TEAM_CIRCLE_ANSWER);//"com.nci.klb.app.question.QuestionPoints"
        parametersMap.put("METHOD", "addSave");// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");// ISPASS desc
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAMCIRCLE_ANSWER_INFO);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject = null;
        showProgress();
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAMCIRCLE_ANSWER_INFO, datas, parametersMap);
            LogUtils.e("团队回答", jsonObject.toString());
            HttpUtils.doPostFile(HttpContacts.UI_URL, jsonObject, fils, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    processAskBackData(response);
                }

                @Override
                public void onFailure(String error) {
                    ToastUtils.showToast(TeamAnswerQuestionActivity.this, "操作失败！");
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    hideProgress();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            hideProgress();
            ToastUtils.showToast(TeamAnswerQuestionActivity.this, "操作失败！");
        }
    }

    /**
     * 处理 回答成功返回数据
     *
     * @param response
     */
    private void processAskBackData(String response) {
        try {
            JsonElement datas = JsonUtils.decoElementJSONObject(response, "EXCEPTIONDATA");
            if (!TextUtils.isEmpty(datas.toString())) {
                JSONArray jsonArray = new JSONArray(datas.toString());
                if (jsonArray.length() > 0) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String reason = jsonObject1.getString("reason");
                    ToastUtils.showToastCenter(TeamAnswerQuestionActivity.this, reason);
                } else {
                    sendMsgToHome();
                    ToastUtils.showToastCenter(TeamAnswerQuestionActivity.this, "回答成功");
                    finish();
                }
            } else {
                sendMsgToHome();
                ToastUtils.showToastCenter(TeamAnswerQuestionActivity.this, "回答成功");
                finish();
            }
        } catch (Exception e) {
            ToastUtils.showToastCenter(TeamAnswerQuestionActivity.this, "操作失败！");
            e.printStackTrace();
        }
    }

    /**
     * 通知主页刷新
     */
    private void sendMsgToHome() {
        //// TODO: 2017/3/16 更新页面
//        EventBus.getDefault().post(WenDaFragment.TAG_REFRESH);//通知主页刷新
    }

    public void getParam() {
        questionBean = (QuestionBean) getIntent().getSerializableExtra(TAG);
    }

    /**
     * 提交提醒框
     */
    private void comittwarningDialog() {
        String question = mEdittext.getText().toString().trim();
        if (TextUtils.isEmpty(question)) {
            showToast(XFrame.getString(R.string.warning_anwer_empty));
            return;
        } else if (question.length() > 500) {
            showToast(XFrame.getString(R.string.warning_anwer_too_long));
            return;
        }
        if (alertDialog == null) {
            alertDialog = new AlertDialog(this).builder();
            alertDialog
                    .setTitle("提示").setMsg("确定要提交答案吗?")
                    .setPositiveButton("提交", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commitAnswer();
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


    @Override
    protected void onDestroy() {
        KeyBoardUtils.closeKeybord(gridView, this);
        super.onDestroy();
    }
}
