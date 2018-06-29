package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.album.AlbumFile;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.ui.answerdetail.AnswerDetailBean;
import com.zc.pickuplearn.ui.answerdetail.ChatAdapter;
import com.zc.pickuplearn.ui.answerdetail.ZhuiwenZhuidaActivity;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.model.ImplQuestionDetailModel;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.ImageSelectUtil;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.ViewClickUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 团队追问追答
 */
public class TeamQuestionAndAnswerActivity extends BaseActivity {

    @BindView(R.id.lv_data)
    ListView lvData;
    @BindView(R.id.ll_answer)
    LinearLayout mAnswerLayout;
    @BindView(R.id.et_sendmessage)
    EditText mMsgEditText;
    @BindView(R.id.hv_headbar)
    HeadView headView;
    private static final int ACTIVITY_REQUEST_SELECT_PHOTO = 88;
    private AnswerBean answer;
    private List<AnswerDetailBean> mdata;
    private ChatAdapter chatAdapter;
    private QuestionBean questioner;
    private final int TAKE_PICTURE = 1;
    private String mac_image;
    private boolean IS_FIRST_IN = true;
    String uploadimageurl = "";//上传的图片
    private UserBean userInfo;
    ArrayList mAlbumFile = new ArrayList<AlbumFile>();

    public static void startTeamQuestionAndAnswerActivity(Context context, QuestionBean questionBean, AnswerBean answerBean) {
        Intent intent = new Intent(context, TeamQuestionAndAnswerActivity.class);
        intent.putExtra("questioner", questionBean);
        intent.putExtra("answer", answerBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_question_and_answer;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        answer = (AnswerBean) intent.getSerializableExtra("answer");
        questioner = (QuestionBean) intent
                .getSerializableExtra("questioner");
        getAnswerListData();
        userInfo = DataUtils.getUserInfo();
        mdata = new ArrayList<AnswerDetailBean>();
        chatAdapter = new ChatAdapter(this, mdata);
        lvData.setAdapter(chatAdapter);
        takeAnswer();
    }

    @Override
    public void initData() {
        if (answer != null) {
            headView.setTitle(answer.getANSWERUSERNAME() + "的回答");
        }
        setBottomLayoutVisablility();
    }

    private void setBottomLayoutVisablility() {
        String userCode = DataUtils.getUserInfo().getUSERCODE();
        if (answer != null && questioner != null) {
            if (userCode.equals(answer.getSYSCREATORID())
                    || userCode.equals(questioner.getSYSCREATORID())) {
                LogUtils.e("底部布局", "进来了");
                mAnswerLayout.setVisibility(View.VISIBLE);
            }
        }

    }

    public void getAnswerListData() {
        Map<String, String> datas = new HashMap<String, String>();
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "ANSWERCODE='" + answer.getSEQKEY() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_QUEANS_DETAIL);
        parametersMap.put("METHOD", "search");// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE asc");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAM_QUE_ANS_DETAIL);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("OPERATETYPE", "");//消息提示 刷新时不传
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("OPERATETYPE", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAM_QUE_ANS_DETAIL, datas, parametersMap);
            LogUtils.e("追问追答请求json" + jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("TEAM_CIRCLE_QUEANS_DETAIL" + response);
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_TEAM_QUE_ANS_DETAIL);
                    try {
                        final List<AnswerDetailBean> answerDetailBeen = JsonUtils.parseJson2Object(datasString, new TypeToken<List<AnswerDetailBean>>() {
                        });
                        String userCode = DataUtils.getUserInfo().getUSERCODE();
                        boolean isSociation = false;
                        AnswerDetailBean answerDetailBean1 = new AnswerDetailBean();
                        answerDetailBean1.setEXPLAIN(questioner.getQUESTIONEXPLAIN());
                        answerDetailBean1.setSYSCREATORID(questioner.getQUESTIONUSERCODE());
                        answerDetailBean1.setHAEDIMAG(questioner.getHEADIMAGE());
                        answerDetailBeen.add(0, answerDetailBean1);
                        int index = 1;
                        List<String> urls = ResultStringCommonUtils.doSplitUrls(questioner.getFILEURL());
                        if (urls.size() > 0) {
                            for (String url :
                                    urls) {
                                LogUtils.e(url);
                                AnswerDetailBean bean = new AnswerDetailBean();
                                bean.setSYSCREATORID(questioner.getQUESTIONUSERCODE());
                                bean.setHAEDIMAG(questioner.getHEADIMAGE());
                                bean.setFILEURL(url);
                                answerDetailBeen.add(index, bean);
                                index++;
                            }
                        }
                        AnswerDetailBean answerDetailBean2 = new AnswerDetailBean();
                        answerDetailBean2.setEXPLAIN(answer.getANSWEREXPLAIN());
                        answerDetailBean2.setSYSCREATORID(answer.getSYSCREATORID());
                        answerDetailBean2.setHAEDIMAG(answer.getHEADIMAGE());
                        answerDetailBeen.add(index, answerDetailBean2);
                        index++;
                        List<String> urls2 = ResultStringCommonUtils.doSplitUrls(answer.getFILEURL());
                        if (urls2.size() > 0) {
                            for (String url :
                                    urls2) {
                                AnswerDetailBean bean = new AnswerDetailBean();
                                bean.setSYSCREATORID(answer.getSYSCREATORID());
                                bean.setHAEDIMAG(answer.getHEADIMAGE());
                                bean.setFILEURL(url);
                                answerDetailBeen.add(index, bean);
                                index++;
                            }
                        }
                        for (AnswerDetailBean answerDetailBean : answerDetailBeen) {
                            if (answerDetailBean.getSYSCREATORID().equals(
                                    questioner.getSYSCREATORID())) {
                                answerDetailBean.setHAEDIMAG(questioner
                                        .getHEADIMAGE());
                                LogUtils.e("追问追答提问者头像", questioner.getHEADIMAGE());
                            } else {
                                answerDetailBean.setHAEDIMAG(answer
                                        .getHEADIMAGE());
                            }

                            if (userCode.equals(answerDetailBean
                                    .getSYSCREATORID())) {
                                isSociation = true;
                            }
                        }
                        if (isSociation) {
                            doDataRelative(answerDetailBeen);

                        } else {
                            doDataNoRelative(answerDetailBeen);
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mdata.clear();
                                mdata.addAll(answerDetailBeen);
                                chatAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("追问追答错误" + error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendMsg() {
        String sendmsg = mMsgEditText.getText().toString().trim();
        if (TextUtils.isEmpty(sendmsg)) {
            ToastUtils.showToast(this, "请输入内容！");
            return;
        }
        clearEdittext();
        Map<String, String> datas = new HashMap<String, String>();

        datas.put("EXPLAIN", sendmsg);
        datas.put("ANSWERCODE", answer.getSEQKEY());
        datas.put("OPERATETYPE", userInfo.getUSERCODE().equals(questioner.getQUESTIONUSERCODE()) ? "1" : "2");//1:追问；2：追答
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        //// TODO: 2017/3/10  换成查询接口  UrlMethod.QueAns_Detail
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_QUEANS_DETAIL);
        parametersMap.put("METHOD", UrlMethod.ADDSAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE asc");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAM_QUE_ANS_DETAIL);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAM_QUE_ANS_DETAIL, datas, parametersMap);
            Log.e("追问追答", jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
//                    LogUtils.e("追问追答成功了", response);
                    clearEdittext();
                    getAnswerListData();
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("追问追答失败了", error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPicture() {
        if (TextUtils.isEmpty(uploadimageurl)) {
            return;
        }
        String imageurl = uploadimageurl;
        uploadimageurl = "";
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("FILEURL", "");
        datas.put("ANSWERCODE", answer.getSEQKEY());
        datas.put("OPERATETYPE", userInfo.getUSERCODE().equals(questioner.getSYSCREATORID()) ? "1" : "2");
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.TEAM_CIRCLE_QUEANS_DETAIL);
        parametersMap.put("METHOD", UrlMethod.ADDSAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE asc");
        parametersMap.put("MASTERTABLE", TableName.KLB_TEAM_QUE_ANS_DETAIL);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        List<String> file = new ArrayList<>();
        file.add(imageurl);
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_TEAM_QUE_ANS_DETAIL, datas, parametersMap);
            HttpUtils.doPostFile(HttpContacts.UI_URL, jsonObject, file, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e("追问追答图片", response);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            getAnswerListData();
                        }
                    });
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("追问追答图片", error);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理 查看者不是追问追问答者
     *
     * @param parseJson2Object
     */
    protected void doDataNoRelative(List<AnswerDetailBean> parseJson2Object) {
        for (AnswerDetailBean answerDetailBean : parseJson2Object) {
            if (answer.getANSWERUSERCODE().equals(
                    answerDetailBean.getSYSCREATORID())) {
                if (TextUtils.isEmpty(answerDetailBean.getFILEURL())) {
                    answerDetailBean.setTYPE(ChatAdapter.VALUE_LEFT_TEXT);
                } else {
                    answerDetailBean.setTYPE(ChatAdapter.VALUE_LEFT_IMAGE);
                }
            } else {
                if (TextUtils.isEmpty(answerDetailBean.getFILEURL())) {
                    answerDetailBean.setTYPE(ChatAdapter.VALUE_RIGHT_TEXT);
                } else {
                    answerDetailBean.setTYPE(ChatAdapter.VALUE_RIGHT_IMAGE);

                }
            }
        }
    }

    /**
     * 处理追问者是追问追答者
     *
     * @param parseJson2Object
     */
    protected void doDataRelative(List<AnswerDetailBean> parseJson2Object) {
        String userCode = DataUtils.getUserInfo().getUSERCODE();
        for (AnswerDetailBean answerDetailBean : parseJson2Object) {
            if (userCode.equals(answerDetailBean.getSYSCREATORID())) {
                if (TextUtils.isEmpty(answerDetailBean.getFILEURL())) {
                    answerDetailBean.setTYPE(ChatAdapter.VALUE_RIGHT_TEXT);
                } else {
                    answerDetailBean.setTYPE(ChatAdapter.VALUE_RIGHT_IMAGE);

                }
            } else {
                if (TextUtils.isEmpty(answerDetailBean.getFILEURL())) {
                    answerDetailBean.setTYPE(ChatAdapter.VALUE_LEFT_TEXT);

                } else {
                    answerDetailBean.setTYPE(ChatAdapter.VALUE_LEFT_IMAGE);
                }

            }
        }

    }

    public void clearEdittext() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mMsgEditText.setText("");
                KeyBoardUtils.closeKeybord(mMsgEditText,
                        TeamQuestionAndAnswerActivity.this);

            }
        });
    }

    @OnClick({R.id.btn_send, R.id.btn_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                sendMsg();
                break;
            case R.id.btn_add:
                showPictureDialog();
                break;

            default:
                break;
        }
    }


    private void showPictureDialog() {
        ImageSelectUtil.choicePicture(TeamQuestionAndAnswerActivity.this, 1, mAlbumFile, new ImageSelectUtil.ImageSelectCallBack() {
            @Override
            public void onSuccess(ArrayList<AlbumFile> albumfiles, List<String> imgPaths) {
//                mAlbumFile = albumfiles;
                uploadimageurl = imgPaths.get(0);
                sendPicture();
//                refreshAdapter();
            }
        });
//        Album.startAlbum(TeamQuestionAndAnswerActivity.this, ACTIVITY_REQUEST_SELECT_PHOTO, 1, getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("onActivityResult", requestCode + resultCode + "");
        if (requestCode == ACTIVITY_REQUEST_SELECT_PHOTO) {
            if (resultCode == -1) {
//                List<String> pathList = Album.parseResult(data);
//                if (!pathList.isEmpty()) {
//                    uploadimageurl = pathList.get(0);
//                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IS_FIRST_IN) {
            IS_FIRST_IN = false;
        } else {
//            sendPicture();
        }

    }

    public void takeAnswer(){
        UserBean userInfo = DataUtils.getUserInfo();
        if (questioner!=null&&userInfo!=null){
            if (questioner.getQUESTIONUSERCODE().equals(userInfo.getUSERCODE())){
                headView.setRightBtVisable(true);
                if ("1".equals(questioner.getISSOLVE())){
                    headView.setRightText("已解决");
                    if (answer!=null){
                        if ("1".equals(answer.getISPASS())){
                            headView.setRightText("已采纳");
                        }
                    }
                }else {
                    if (answer!=null&&"1".equals(answer.getISPASS())){
                        headView.setRightText("已采纳");
                        return;
                    }
                    headView.setRightText("采纳");
                    headView.setMyOnClickListener(new HeadView.myOnClickListener() {
                        @Override
                        public void rightButtonClick() {
                            if (ViewClickUtil.isFastClick()) {
                                UserBean userInfo = DataUtils.getUserInfo();
                                if (answer==null||"1".equals(answer.getISPASS())){
                                    return;
                                }
                                if (!"1".equals(questioner.getISSOLVE()) && questioner.getQUESTIONUSERCODE().equals(userInfo.getUSERCODE())) {
                                    new AlertDialog(TeamQuestionAndAnswerActivity.this).builder()
                                            .setTitle("提示").setMsg(XFrame.getString(R.string.warning_take_answer))
                                            .setPositiveButton("确定", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    showProgress();
                                                    API.teamTakeAnswer(answer, new CommonCallBack<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            ToastUtils.showToast(TeamQuestionAndAnswerActivity.this, XFrame.getString(R.string.warning_take_answer_success));
                                                            answer.setISPASS("1");
                                                            headView.setRightText("已采纳");
                                                            hideProgress();
                                                        }

                                                        @Override
                                                        public void onFailure() {
                                                            hideProgress();
                                                            ToastUtils.showToast(TeamQuestionAndAnswerActivity.this, "请稍后再试！");
                                                        }
                                                    });
                                                }
                                            }).setNegativeButton(XFrame.getString(R.string.warning_let_me_think), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    }).show();
//                                    new AlertDialog(TeamQuestionAndAnswerActivity.this).builder()
//                                            .setTitle("提示").setMsg(XFrame.getString(R.string.warning_take_answer))
//                                            .setPositiveButton("确定", new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    new ImplQuestionDetailModel().takeAnswer(answer, new ImplQuestionDetailModel.TakeAnswerCallback() {
//                                                        @Override
//                                                        public void onSuccess() {
//                                                            ToastUtils.showToast(ZhuiwenZhuidaActivity.this, XFrame.getString(R.string.warning_take_answer_success));
//                                                            answer.setISPASS("1");
//                                                            headView.setRightText("已采纳");
//                                                        }
//
//                                                        @Override
//                                                        public void onFail() {
//                                                            ToastUtils.showToast(ZhuiwenZhuidaActivity.this, "服务器正忙！稍后再试！");
//                                                        }
//                                                    });
//                                                }
//                                            }).setNegativeButton(XFrame.getString(R.string.warning_let_me_think), new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                        }
//                                    }).show();
                                }
                            }
                        }
                    });
                }
            }
        }


    }
}
