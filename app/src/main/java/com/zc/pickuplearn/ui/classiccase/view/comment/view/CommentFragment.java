package com.zc.pickuplearn.ui.classiccase.view.comment.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.view.XListView.XListView;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author bin E-mail: chenbin252@163.COM
 * @version 创建时间：2016-10-25 下午4:48:57
 * @Describe
 */
public class CommentFragment extends BaseFragment implements
        XListView.IXListViewListener {
    @BindView(R.id.commentButton)
    Button commentButton;// 底部评论按钮发送
    @BindView(R.id.commentList)
    XListView commentList;
    @BindView(R.id.commentEdit)
    EditText commentEdit;
    public static final String TAG = "CommentFragment";
    private CommentAdapter adapter;
    private String comment = ""; // 记录对话框中的内容
    private int position; // 记录回复评论的索引
    private boolean isReply; // 是否是回复
    private int index = 0;// 评论索引

    public static CommentFragment newInstance(ClassicCaseBean classicCaseBean) {
        Bundle args = new Bundle();
        CommentFragment fragment = new CommentFragment();
        args.putSerializable(TAG, classicCaseBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        caseBean = (ClassicCaseBean) getArguments().getSerializable(TAG);
        commentList.setPullRefreshEnable(false);
        commentList.setPullLoadEnable(true);
        commentList.setXListViewListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            commentList.setNestedScrollingEnabled(true);//协调布局
        }
        // TODO Auto-generated method stub
        adapter = new CommentAdapter(getActivity(), getCommentData(),
                R.layout.comment_item, handler);
        commentList.setAdapter(adapter);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10) {
                isReply = true;
                position = (Integer) msg.obj;
                // commentLinear.setVisibility(View.VISIBLE);
                // bottomLinear.setVisibility(View.GONE);
                onFocusChange(true);
            }
        }
    };

    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) commentEdit
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);

                if (isFocus) {
                    // 显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    // 隐藏输入法
                    imm.hideSoftInputFromWindow(commentEdit.getWindowToken(), 0);
                }
            }
        }, 100);
    }

    @Override
    protected void lazyLoad() {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_comment;
    }

    @OnClick(R.id.commentButton)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commentButton:
                if (isEditEmply()) {

                    if (isReply) {
                        replyComment();
                    } else {
                        publishComment();
                    }
                }
                onFocusChange(false);
                break;

            default:
                break;
        }
    }

    /**
     * 获取评论列表数据
     */
    List<CommentBean> list;
    private int count; // 记录评论ID
    private ClassicCaseBean caseBean;

    private List<CommentBean> getCommentData() {
        list = new ArrayList<CommentBean>();
        getNetCommentData(false);
        return list;
    }

    private void getNetCommentData(final boolean IsLoadMore) {
        if (!IsLoadMore) {
            index = 0;
        }
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "CASECODE='" + caseBean.getSEQKEY() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.CASE_COMMENT);
        parametersMap.put("METHOD", UrlMethod.SEARCH);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("MASTERTABLE", TableName.V_KLB_CLASSICCASE_COMMENT);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", index + "");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.V_KLB_CLASSICCASE_COMMENT, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {


                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.V_KLB_CLASSICCASE_COMMENT);
                    try {
                        JSONArray arr = new JSONArray(datasString);
                        if (arr.length() > 0) {
                            if (!IsLoadMore) {
                                list.clear();
                            }
                            for (int j = 0; j < arr.length(); j++) {
                                JSONObject jsonObject = arr
                                        .getJSONObject(j);
                                CommentBean bean = new CommentBean();
                                bean.setCommentImgId(jsonObject
                                        .getString("FILEURL"));
                                bean.setCommentNickname(jsonObject
                                        .getString("NICKNAME"));
                                bean.setCommentTime(jsonObject
                                        .getString("SYSCREATEDATE"));
                                bean.setCommentContent(jsonObject
                                        .getString("COMMENTEXPLAIN"));
                                bean.setReplyList(getReplyData());
                                index++;
                                list.add(bean);
                            }
                            adapter.notifyDataSetChanged();
                            commentList.stopLoadMore();
                        } else {

                            if (IsLoadMore && commentList != null) {
                                commentList.stopLoadMore("没有更多");
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }


                @Override
                public void onFailure(String error) {

                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 获取回复列表数据
     */
    private List<ReplyBean> getReplyData() {
        List<ReplyBean> replyList = new ArrayList<ReplyBean>();
        return replyList;
    }

    /**
     * 判断对话框中是否输入内容
     */

    private boolean isEditEmply() {
        comment = commentEdit.getText().toString().trim();
        if (comment.equals("")) {
            ToastUtils.showToast(getActivity(), "评论不能为空");
            return false;
        }
        commentEdit.setText("");
        return true;
    }

    /**
     * 发表评论
     */
    private void publishComment() {
        Map<String, String> datas = new HashMap<String, String>();
        // 第一条记录结束
        datas.put("USERCODE", DataUtils.getUserInfo().getUSERCODE());
        datas.put("CASECODE", caseBean.getSEQKEY());
        datas.put("COMMENTEXPLAIN", comment);
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        // String whereSql = "CASECODE='" + caseBean.getId() + "'";
        parametersMap.put("CLASSNAME", UrlMethod.CASE_COMMENT);
        parametersMap.put("METHOD", "addSave");// updateSave,delete,search,addSave
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", TableName.KLB_CLASSICCASE_COMMENT);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        HashMap<String, String> commandMap = new HashMap<>();
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "addcomment");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject4(TableName.KLB_CLASSICCASE_COMMENT, datas, parametersMap,commandMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {
                @Override
                public void onSuccess(String response) {
                    LogUtils.e(response);
                    UIUtils.runInMainThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            getNetCommentData(false);
                        }
                    });
                }

                @Override
                public void onFailure(String error) {

                }
            });
        } catch (Exception e) {

        }


    }

    /**
     * 回复评论
     */
    private void replyComment() {
        ReplyBean bean = new ReplyBean();
        bean.setId(count + 10);
        bean.setCommentNickname(list.get(position).getCommentNickname());
        bean.setReplyNickname("我是回复的人");
        bean.setReplyContent(comment);
        adapter.getReplyComment(bean, position);
        adapter.notifyDataSetChanged();
        isReply = false;// 复位评论标志
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoadMore() {
        getNetCommentData(true);
    }
}
