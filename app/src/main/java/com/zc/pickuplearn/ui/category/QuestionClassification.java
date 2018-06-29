package com.zc.pickuplearn.ui.category;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.askquestion.view.AskQuestionActivity;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.DynamicMoreActivity;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.SPUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


/**
 * 分类列表
 *
 * @author Administrator
 */
public class QuestionClassification extends EventBusActivity {
    @BindView(R.id.expandableListView)
    ExpandableListView mExpandableListView;
    @BindView(R.id.hv_headbar)
    HeadView mHeadView;
    List<QusetionTypeBean> parentGroup = new ArrayList<QusetionTypeBean>();
    HashMap<QusetionTypeBean, List<QusetionTypeBean>> dataHashMap = new HashMap<QusetionTypeBean, List<QusetionTypeBean>>();
    ExpandableListViewAdapter mExpandableListViewAdapter;
    String action = "closeQuestionClassification";
    public static final String TIWEN = "tiwen";//提问
    public static final String WENTI = "wenti";//查看问题
    public static final String TAG = "QuestionClassification";
    private String from;

    public static void startQuestionClassfication(Context context, String type) {
        Intent intent = new Intent(context, QuestionClassification.class);
        intent.putExtra(TAG, type);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_question_classification;
    }

    @Override
    public void initView() {
        getParam();
        mHeadView.setTitle("问题分类");
        mExpandableListViewAdapter = new ExpandableListViewAdapter(this,
                parentGroup, dataHashMap);
        mExpandableListView.setAdapter(mExpandableListViewAdapter);
        // mExpandableListView.expandGroup(0);//默认展开第一级
        mExpandableListView
                .setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent,
                                                View v, int groupPosition, long id) {
                        return false;
                    }
                });
        mExpandableListView
                .setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent,
                                                View v, int groupPosition, int childPosition,
                                                long id) {
                        LogUtils.e("groupPosition=" + groupPosition
                                + ",childPosition=" + childPosition);
                        return false;
                    }
                });
        mExpandableListViewAdapter.setChildViewOnClickListener(new ExpandableListViewAdapter.ChildListener() {
            @Override
            public void onClick(QusetionTypeBean bean) {//子类列表项监听
                QuestionBean questionBean = new QuestionBean();
                questionBean.setQUESTIONTYPECODE(bean.getCODE());
                questionBean.setQUESTIONTYPENAME(bean.getNAME());

                if (TIWEN.equals(from)) {
                    AskQuestionActivity.startAskQuestionActivity(QuestionClassification.this, questionBean);
                } else if (WENTI.equals(from)) {
                    DynamicMoreActivity.startDynamicMoreActivity(QuestionClassification.this, "", questionBean, "");//根据分类去找找问题
                }
            }
        });
    }

    @Override
    public void initData() {
        String object = (String) SPUtils.get(this, action, "");// 先取本地分类数据
        if (!TextUtils.isEmpty(object)) {
            processStringData(object);
        }
        getQuestionTypeData();
    }

    /**
     * 获取分类树数据
     */
    private void getQuestionTypeData() {
        Map<String, String> datas = new HashMap<String, String>();
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        // parametersMap.put("CLASSNAME", UrlMethods.CASE_SCORE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", "V_KLB_QUESTION_TYPE");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "-1");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject("V_KLB_QUESTION_TYPE", datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL,
                    jsonObject, new MyStringCallBack() {
                        @Override
                        public void onSuccess(String response) {
                            SPUtils.put(QuestionClassification.this, action,
                                    response);// 访问数据成功保存到本地
                            processStringData(response);
                        }

                        @Override
                        public void onFailure(String error) {

                        }
                    });
        } catch (Exception e) {

        }
    }

    /**
     * 数据列表格式化数据源
     *
     * @param list
     * @return
     */
    protected void initExpandableData(List<QusetionTypeBean> list) {
        HashMap<QusetionTypeBean, List<QusetionTypeBean>> parenGroup = new HashMap<QusetionTypeBean, List<QusetionTypeBean>>();
        if (list != null && list.size() > 0) {
            parentGroup.clear();
            dataHashMap.clear();
            for (QusetionTypeBean qusetionTypeBean : list) {
                if ("-1".equals(qusetionTypeBean.getPARENTID())) {
                    parentGroup.add(qusetionTypeBean);
                    ArrayList<QusetionTypeBean> childGroup = new ArrayList<QusetionTypeBean>();
                    for (QusetionTypeBean bean : list) {
                        if (bean.getPARENTID().equals(
                                qusetionTypeBean.getCODE())) {
                            childGroup.add(bean);
                        }
                    }
                    parenGroup.put(qusetionTypeBean, childGroup);
                }
            }
            dataHashMap.putAll(parenGroup);
        }
    }

    @Subscribe
    public void onEventMainThread(String msg) {
        if (action.equals(msg)) {
            finish();
        }
    }


    private void processStringData(String response) {
        try {
            dataHashMap.clear();
            String datasString = ResultStringCommonUtils.getDatasString(response, "V_KLB_QUESTION_TYPE");
            List<QusetionTypeBean> list = JsonUtils.parseJson2Object(datasString,
                    new TypeToken<List<QusetionTypeBean>>() {
                    });
            initExpandableData(list);

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            runOnUiThread(new Runnable() {
                public void run() {
                    mExpandableListViewAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    public void getParam() {
        Intent intent = getIntent();
        from = intent.getStringExtra(TAG);
    }
}
