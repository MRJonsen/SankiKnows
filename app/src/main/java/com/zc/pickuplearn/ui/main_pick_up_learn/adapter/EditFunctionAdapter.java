package com.zc.pickuplearn.ui.main_pick_up_learn.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.FunctionBean;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.ui.classiccase.cases.CaseHomeActivity;
import com.zc.pickuplearn.ui.district_manager.CoursewareActivity;
import com.zc.pickuplearn.ui.district_manager.QuestionBankActivity;
import com.zc.pickuplearn.ui.district_manager.SkillInventoryActivity;
import com.zc.pickuplearn.ui.district_manager.SkillMoudleLearnActivity;
import com.zc.pickuplearn.ui.district_manager.SkillReportActivity;
import com.zc.pickuplearn.ui.expert.ExpertListActivity;
import com.zc.pickuplearn.ui.group.widget.prodetail.view.ProfessorListActivity;
import com.zc.pickuplearn.ui.industry.IndustryListActivity;
import com.zc.pickuplearn.ui.main_pick_up_learn.FunctionEditActivity;
import com.zc.pickuplearn.ui.main_pick_up_learn.ZhidaoWenDaActivity;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.ui.webview.BaseWebViewActivity;
import com.zc.pickuplearn.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 可选功能块适配器
 * Created by chenbin on 2017/8/7.
 */

public class EditFunctionAdapter extends CommonAdapter<FunctionBean> {

    private ProgressDialog progressDialog;
    private List<FunctionBean> mData = new ArrayList<>();
    public EditFunctionAdapter(Context context, List<FunctionBean> datas) {
        super(context, R.layout.item_home_edit_function, datas);
        mData = datas;
    }
//
//    private EditFunctionAdapter(Context context, int layoutId, List<FunctionBean> datas) {
//        super(context, layoutId, datas);
//    }

    @Override
    protected void convert(final ViewHolder holder, final FunctionBean bean, int position) {
        holder.setText(R.id.tv_function_name, bean.getFUNCTIONNAME());
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bean.getFUNCTIONCODE()) {
                    case "zjzx":
                        ProfessorListActivity.startProfessorList(mContext, new ProfessionalCircleBean());
                        break;
                    case "zyq":
                        ZhidaoWenDaActivity.starAction(mContext, 1);
                        break;
                    case "HYGF":
                        IndustryListActivity.open(mContext);
                        break;
                    case "ZJJD":
                        ExpertListActivity.open(mContext);
                        break;
                    case "DXAL":
                        CaseHomeActivity.open(mContext);
                        break;
                    case "jnqd":
                        SkillInventoryActivity.open(mContext);
                        break;
                    case "xxkj":
                        CoursewareActivity.open(mContext,"");
                        break;
//                    case "jnbg":
//                        API.getFunctionUrl("jnbg", new CommonCallBack<String>() {
//
//                            @Override
//                            public void onSuccess(String s) {
////                                WebViewActivity.skip(mContext, s, "", false);
//                                BaseWebViewActivity.open(mContext,s,false);
//                            }
//
//                            @Override
//                            public void onFailure() {
//
//                            }
//                        });
//                        break;
                    case "lxtk":
                        QuestionBankActivity.open(mContext);
                        break;
                    case "jnxx":
                        SkillMoudleLearnActivity.openSkillMoudleLearn(mContext,bean.getFUNCTIONCODE());
                        break;
                    default:
                        showProgress();
                        API.getFunctionUrl3(bean.getFUNCTIONCODE(), new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                if (!TextUtils.isEmpty(s))
                                    WebViewActivity.skip(mContext, s, "", false);
                                hideProgress();
                            }

                            @Override
                            public void onFailure() {
                                hideProgress();
                            }
                        });
                        break;
                }
//                switch (bean.getFUNCTIONCODE()) {
//                    case "zjzx":
//                        ProfessorListActivity.startProfessorList(mContext, new ProfessionalCircleBean());
//                        break;
//                    case "zyq":
//                        ZhidaoWenDaActivity.starAction(mContext, 1);
//                        break;
//                    case "zxcs":
//                        API.getFunctionUrl2("onlineExam", new CommonCallBack<String>() {
//                            @Override
//                            public void onSuccess(String s) {
//                                if (!TextUtils.isEmpty(s))
//                                    WebViewActivity.skip(mContext, s, "", false);
//                            }
//
//                            @Override
//                            public void onFailure() {
//
//                            }
//                        });
//                        break;
//                }

            }
        });
        //显示推广图案
        if ("1".equals(bean.getIS_SPREAD())) {
            holder.setVisible(R.id.iv_new_tag, true);
        } else {
            holder.setVisible(R.id.iv_new_tag, false);
        }
        if (bean.icon != 0) {
            holder.setImageResource(R.id.iv_icon, bean.icon);
            holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FunctionEditActivity.startView(mContext, mData);
                }
            });
        } else {
//            ImageLoaderUtil.display(mContext, (ImageView) holder.getView(R.id.iv_icon), bean.getFUNCTIONURL());
            try {
                if (mContext!=null&&holder.getView(R.id.iv_icon)!=null){
                    String string = ResultStringCommonUtils.subUrlToWholeUrl(bean.getFUNCTIONURL());
                    Glide.with(mContext).load(string)
                            .placeholder(R.mipmap.default_img)
                            .error(R.mipmap.default_img)
                            .fitCenter().crossFade().into((ImageView) holder.getView(R.id.iv_icon));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMsg("数据加载中..");
            progressDialog.setCanceLable(true);
        }
        progressDialog.showProgressDialog();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dissMissProgressDialog();
        }

    }
}
