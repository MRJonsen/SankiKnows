package com.zc.pickuplearn.ui.sign.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.event.PickLearnMainFragmentInfoRefreshEvent;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.sign.widget.MonthSignData;
import com.zc.pickuplearn.ui.sign.widget.SignCalendar;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ViewClickUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressLint("SimpleDateFormat")
public class SignInActivity extends BaseActivity implements OnClickListener {
    //	private SignCalendar calendar;
    private String date;
    private int years;
    private String months;
    private TextView signIn;
    private Button back;
    List<String> list = new ArrayList<String>();
    private String ym;
    private boolean isSigned;
    private SignCalendar signCalendar;
    private Date today;
    private UserBean userInfo;
    private ProgressDialog progressDialog;
    public static void startSignInActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SignInActivity.class);
        intent.putExtra("isSigned", false);
        context.startActivity(intent);
    }


    @Override
    public int setLayout() {
        return R.layout.sign_in;
    }

    @Override
    public void initView() {
        userInfo = DataUtils.getUserInfo();
        isSigned = getIntent().getExtras().getBoolean("isSigned");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date();// 获取当前时间
        date = formatter.format(curDate);
        ym = date.substring(0, 7);
        signCalendar = (SignCalendar) findViewById(R.id.sign_in_min_calendar);
        back = (Button) findViewById(R.id.sign_in_back_btn);
        signIn = (TextView) findViewById(R.id.sign_in_sign_in);
        back.setOnClickListener(this);
//	     if(isSigned){
//	    	 signIn.setText("已签到");
//	    	 signIn.setTextColor(Color.GRAY);
//	     }else{
//	    	 signIn.setText("签到");
        signIn.setOnClickListener(this);
//	     }
        //存储多个月份的签到数据
        ArrayList<MonthSignData> monthDatas = new ArrayList<MonthSignData>();
        //一个月份的签到数据
        MonthSignData monthData = new MonthSignData();
        Date date = new Date();
        monthData.setYear(date.getYear() + 1900);
        monthData.setMonth(date.getMonth());
        //一个月内的签到天数
        ArrayList<Date> signDates = new ArrayList<Date>();
        monthData.setSignDates(signDates);
        monthDatas.add(monthData);
        //给签到日历设置今天是哪一天
        signCalendar.setToday(curDate);
        signCalendar.setSignDatas(monthDatas);
//	     signCalendar.clearDisappearingChildren();
        getSignData();
    }
    @Override
    protected void initData() {

    }

    private void getSignData() {
        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("SIGNINDATE", "");
        datas.put("USERNAME", "");
        datas.put("USERCODE", userInfo.getUSERCODE());
        datas.put("PERIOD", "");
        datas.put("STATUS", "");
        dataMap.put("KLB_SIGNIN_INFO", datas);
        dataList.add(dataMap);
        //第一条记录结束
        // 参数区域赋值
        String whereSql = "PERIOD='" + ym + "'";
        parametersMap.put("CLASSNAME", "com.nci.app.operation.business.AppBizOperation");
        parametersMap.put("METHOD", "search");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", "KLB_SIGNIN_INFO");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "32");

        // command赋值区域
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_SIGNIN_INFO, datas, parametersMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String datasString = ResultStringCommonUtils.getDatasString(response, TableName.KLB_SIGNIN_INFO);
                    try {
                        JSONArray arr = new JSONArray(datasString);
                        if (arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                String signDate = obj.getString("SIGNINDATE");
                                if (!signDate.equals("")) {
                                    if (signDate.length() > 10) {
                                        signDate = signDate.substring(0, 10);
                                        list.add(signDate);
                                    }
                                }
                            }
                            Message msg = new Message();
                            msg.what = 1;
                            mHandle.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 签到
     */
    private void SignIn() {
        // 数据区域
        ArrayList<Map<String, Map<String, String>>> dataList = new ArrayList<Map<String, Map<String, String>>>();
        // 参数区域
        Map<String, String> parametersMap = new HashMap<String, String>();
        // command区域
        Map<String, String> commandMap = new HashMap<String, String>();
        // 数据区域赋值
        // 如果多条记录，就循环下面代码段 循环开始
        //第一条记录开始
        Map<String, Map<String, String>> dataMap = new HashMap<String, Map<String, String>>();
        Map<String, String> datas = new HashMap<String, String>();
        datas.put("SIGNINDATE", date);
        datas.put("USERNAME", userInfo.getUSERNAME());
        datas.put("USERCODE", userInfo.getUSERCODE());
        datas.put("PERIOD", ym);
        datas.put("STATUS", "1");
        dataMap.put("KLB_SIGNIN_INFO", datas);
        dataList.add(dataMap);
        //第一条记录结束
        // 参数区域赋值
        parametersMap.put("CLASSNAME", "com.nci.klb.app.signin.SigninPoints");
        parametersMap.put("METHOD", "addSave");//updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "");
        parametersMap.put("MASTERTABLE", "KLB_SIGNIN_INFO");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "31");

        // command赋值区域
        commandMap.put("mobileapp", "true");
        commandMap.put("actionflag", "select");
        showProgressDialog();
        JSONObject jsonObject = null;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_SIGNIN_INFO, datas, parametersMap);
            LogUtils.e(jsonObject.toString());
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    String retMsg = null;
                    try {
                        LogUtils.e(response);
                        retMsg = JsonUtils.decoElementASString(response, UrlMethod.RETMSG);
                        boolean flag = ResultStringCommonUtils.getRet(retMsg);
                        if (flag) {
                            list.add(date);
                            Message msg = new Message();
                            msg.what = 2;
                            mHandle.sendMessage(msg);
                            JsonElement datas = JsonUtils.decoElementJSONObject(response, "COMMAND");
                            String target = JsonUtils.decoElementASString(datas.toString(), "target");
                            Toast.makeText(SignInActivity.this, target, Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new PickLearnMainFragmentInfoRefreshEvent());//发送消息同志首页更新消息
                        } else {
                            Toast.makeText(SignInActivity.this, retMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                        disshowProgressDialog();
                }

                @Override
                public void onFailure(String error) {
                    disshowProgressDialog();
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    disshowProgressDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            disshowProgressDialog();
        }
    }

    private void initDate() {
        // TODO 自动生成的方法存根
        if (list.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ArrayList<Date> signDates = new ArrayList<Date>();
            for (int i = 0; i < list.size(); i++) {
                String l1 = list.get(i);
                try {
                    Date date1 = sdf.parse(l1);
                    signDates.add(date1);
                    String format = sdf.format(date1);
                    if (format.equals(date)) {
                        signIn.setText("已签到");
                        signIn.setTextColor(Color.GRAY);
                        signIn.setEnabled(false);
                    }
                } catch (ParseException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
            }
            //存储多个月份的签到数据
            ArrayList<MonthSignData> monthDatas = new ArrayList<MonthSignData>();
            //一个月份的签到数据
            MonthSignData monthData = new MonthSignData();
            Date date = new Date();
            monthData.setYear(date.getYear() + 1900);
            monthData.setMonth(date.getMonth());
            //一个月内的签到天数
            monthData.setSignDates(signDates);
            monthDatas.add(monthData);
            signCalendar.setSignDatas(monthDatas);
        }
    }

    Handler mHandle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initDate();
                    break;
                case 2:
                    isSigned = true;
                    signIn.setText("已签到");
                    signIn.setTextColor(Color.GRAY);
                    signIn.setEnabled(false);
                    initDate();
//		    	EventBus.getDefault().post(new Signbean(isSigned));//发消息给我的界面 签到成功
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        // TODO 自动生成的方法存根
        switch (v.getId()) {
            case R.id.sign_in_back_btn:
                finish();
                break;
            case R.id.sign_in_sign_in:
                if (ViewClickUtil.isFastClick()){
                    SignIn();
                }
                break;
            default:
                break;
        }
    }

    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMsg("签到中...");
            progressDialog.setCanceLable(false);
        }
        progressDialog.showProgressDialog();
    }

    private void disshowProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dissMissProgressDialog();
        }
    }


}
