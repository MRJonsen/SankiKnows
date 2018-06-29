package com.zc.pickuplearn.ui.main_pick_up_learn.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.jiang.android.indicatordialog.IndicatorBuilder;
import com.jiang.android.indicatordialog.IndicatorDialog;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.InfoBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.district_manager.CoursewareActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.model.ImplQuestionDetailModel;
import com.zc.pickuplearn.ui.question_and_answer.QuestionDetailActivity;
import com.zc.pickuplearn.ui.sign.view.SignInActivity;
import com.zc.pickuplearn.ui.teamcircle.view.TeamListActivity;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.ui.webview.BaseWebViewActivity;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zc.pickuplearn.utils.UIUtils.getResources;

/**
 * 固定功能块适配器
 * Created by chenbin on 2017/8/7.
 */

public class InfoAdapter extends CommonAdapter<InfoBean> {
    private ProgressDialog progressDialog;

    public InfoAdapter(Context context, List<InfoBean> datas) {
        this(context, R.layout.item_home_info_function, datas);
    }

    private InfoAdapter(Context context, int layoutId, List<InfoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(final ViewHolder holder, final InfoBean bean, int position) {
        switch (bean.getMES_TYPE()) {
            case "1": //问答
                holder.setText(R.id.tv_type, "问答");
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuestionBean questionBean = new QuestionBean();
                        questionBean.setSEQKEY(bean.getFORKEY());
                        showProgress();
                        new ImplQuestionDetailModel().getDynamicDatas(questionBean, new ImplQuestionDetailModel.GetDynamicDatasCallBack() {
                            @Override
                            public void onSuccess(List<QuestionBean> dynamic_data) {
                                UserBean userInfo = DataUtils.getUserInfo();
//                                boolean ISMYQUESTION = dynamic_data.get(0).getQUESTIONUSERCODE().equals(userInfo.getUSERCODE());
//                                QuestionDetailActivity.startQuestionDetailActivity(mContext, dynamic_data.get(0), ISMYQUESTION ? QuestionDetailType.MyQuestion : QuestionDetailType.MyAnswer);//进入问题详情
                                QuestionDetailActivity.open(mContext, dynamic_data.get(0));
                                hideProgress();
                            }

                            @Override
                            public void onFailure() {
                                hideProgress();
                            }
                        });
                    }
                });
                break;
            case "2":   //团队
                holder.setText(R.id.tv_type, "团队");
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TeamListActivity.startTeamListActivity(mContext);
                    }
                });
                break;
            case "3": //学堂
                holder.setText(R.id.tv_type, "在线考试");
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(bean.getURLPATH())) {
                            ToastUtils.showToast(mContext, "暂无信息");
                            return;
                        }
                        showProgress();
                        API.getFunctionUrl4("ToHtmlPath", bean.getURLPATH(), bean.getMES_TYPE(), new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                BaseWebViewActivity.open(mContext, s, false);
                                hideProgress();
                            }

                            @Override
                            public void onFailure() {
                                hideProgress();
                            }
                        });
                    }
                });
                break;
            case "4"://签到
                holder.setText(R.id.tv_type, "签到");
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SignInActivity.startSignInActivity(mContext);
                    }
                });
                break;
            case "5":
                holder.setText(R.id.tv_type, "在线考试");
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(bean.getURLPATH())) {
                            ToastUtils.showToast(mContext, "暂无信息");
                            return;
                        }
                        showProgress();
                        API.getFunctionUrl4("ToHtmlPath", bean.getURLPATH(), bean.getMES_TYPE(), new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                BaseWebViewActivity.open(mContext, s, false);
                                hideProgress();
                            }

                            @Override
                            public void onFailure() {
                                hideProgress();
                            }
                        });
                    }
                });
                break;
            case "8":
                holder.setText(R.id.tv_type,"学习课件");
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CoursewareActivity.open(mContext,"");
                    }
                });
                break;
            case "9"://
                holder.setText(R.id.tv_type, "技能报告");
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(bean.getURLPATH())) {
                            ToastUtils.showToast(mContext, "暂无信息");
                            return;
                        }
                        showProgress();
                        API.getFunctionUrl4("ToHtmlPath", bean.getURLPATH(), bean.getMES_TYPE(), new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                BaseWebViewActivity.open(mContext, s, false);
                                hideProgress();
                            }

                            @Override
                            public void onFailure() {
                                hideProgress();
                            }
                        });
                    }
                });
                break;
            default:
                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
        }
        holder.setText(R.id.tv_type, bean.getMES_NAME());
        if (TextUtils.isEmpty(bean.getMSG_CONTENT())) {
            holder.setVisible(R.id.tv_content, false);
        } else {
            holder.setVisible(R.id.tv_content, true);
            holder.setText(R.id.tv_content, bean.getMSG_CONTENT());
        }
        ImageLoaderUtil.display(mContext, (ImageView) holder.getView(R.id.iv_icon), bean.getFILEURL());
        holder.setText(R.id.tv_title, bean.getMSG_TITLE());
        holder.setText(R.id.tv_function, bean.getEXTRASPARAM());
        holder.setText(R.id.tv_time, DateUtils.getCompareDate(bean.getSYSCREATEDATE()));


        switch (bean.getMES_TYPE()){
            case "1":
                holder.setText(R.id.tv_title, bean.getMSG_CONTENT());
                holder.setText(R.id.tv_content, bean.getMSG_TITLE());
                break;
        }
        holder.setOnClickListener(R.id.ib_ignore, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(holder.getView(R.id.ib_ignore), 0.9f, IndicatorBuilder.GRAVITY_CENTER, bean);
            }
        });
    }

    private void showDialog(View v, float v1, int gravityLeft, final InfoBean bean) {
        ArrayList<String> objects = new ArrayList<>();
        CommonAdapter popAdpter = new CommonAdapter<String>(mContext, R.layout.info_ignore_pop_item, objects) {
            @Override
            protected void convert(ViewHolder holder, String o, int position) {
                holder.setText(R.id.tv_ignore_warning, o);
            }
        };
        objects.add("忽略该条消息");
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        final IndicatorDialog dialog = new IndicatorBuilder((Activity) mContext)
                .arrowWidth(UIUtils.dip2px(15))
                .height((int) (height * 0.2))
                .height(-1)
                .width((int) (width * 0.9))
                .ArrowDirection(IndicatorBuilder.BOTTOM)
                .bgColor(Color.WHITE)
                .gravity(gravityLeft)
                .ArrowRectage(v1)
                .radius(8)
                .layoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false))
                .adapter(popAdpter).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show(v);
        popAdpter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                API.setInfoIgnore(bean, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        mDatas.remove(bean);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public void showProgress() {
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setCanceLable(false);
                }
                progressDialog.showProgressDialog();
            }
        });

    }

    public void hideProgress() {
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dissMissProgressDialog();
                }
            }
        });
    }
}
