package com.zc.pickuplearn.ui.question_and_answer.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.QuestionTypeBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.ui.answerdetail.ZhuiwenZhuidaActivity;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.view.AnswerActivity;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.model.ImplWenDaModel;
import com.zc.pickuplearn.ui.question_and_answer.QuestionDetailActivity;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.PictureUtil;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 问题列表适配器
 * Created by chen bin on 2017/11/8.
 */

public class QuestionListAdapter extends CommonAdapter<QuestionBean> {

    private HashMap<String, Drawable> imagehold;
    private int etvWidth;
    private final UserBean userInfo;
    private ProgressDialog progressDialog;
    public QuestionListAdapter(Context context, List<QuestionBean> datas) {
        super(context, R.layout.item_question, datas);
        userInfo = DataUtils.getUserInfo();
    }

    @Override
    protected void convert(final ViewHolder holder, final QuestionBean questionBean, int position) {
        //头像
        CircleImageView headimg = holder.getView(R.id.civ_head);
        if (!questionBean.getHEADIMAGE().isEmpty()) {
            if ("1".equals(questionBean.getISANONYMITY())) {
                ImageLoaderUtil.showHeadView(mContext, "", headimg);//加载默认头像
            } else {
                ImageLoaderUtil.showHeadView(mContext, ResultStringCommonUtils.subUrlToWholeUrl(questionBean.getHEADIMAGE()), headimg);//加载头像
            }
        } else {
            ImageLoaderUtil.showHeadView(mContext, "", headimg);//加载默认头像
        }

        //标签
//        StringBuilder tag = new StringBuilder();
//        if (questionBean.getTip() != null) {
//            for (QuestionTypeBean questionTypeBean :
//                    questionBean.getTip()) {
//                tag.append(" " + questionTypeBean.getQUESTIONTYPENAME());
//            }
//        }
//        holder.setText(R.id.tv_tag, tag.toString());
        holder.setText(R.id.tv_tag, questionBean.getQUESTIONTYPENAME());

        //名称
        holder.setText(R.id.tv_name, TextUtils.isEmpty(questionBean.getNICKNAME()) ? questionBean.getQUESTIONUSERNAME() : questionBean.getNICKNAME());
        if ("1".equals(questionBean.getISANONYMITY())) {
            holder.setText(R.id.tv_name, "匿名用户");
        }
        //时间
        holder.setText(R.id.tv_time, DateUtils.getCompareDate(questionBean.getSYSCREATEDATE()));

        //回复
        String answersum = questionBean.getANSWERSUM();
        final String isanswer = questionBean.getISANSWER();//1 answer 0 not answer
        int answerColor = "1".equals(isanswer) ? R.color.colorPrimary : R.color.text_color_gray;
        holder.setText(R.id.tv_reply, TextUtils.isEmpty(answersum) ? "回答" : "0".equals(answersum) ? "回答" : answersum);
        holder.setTextColorRes(R.id.tv_reply, answerColor);
        holder.setImageDrawable(R.id.iv_reply, getColorDrawable(R.mipmap.icon_reply_new, answerColor));
        holder.setOnClickListener(R.id.ll_reply, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(isanswer)) {
                    // TODO: 2017/11/15 追文追答
                    //1.是回答者直接进入追问追答界面 否者进入回答界面
                    showProgress();
                    if (questionBean.getISANSWER().equals("1")) {
                        new ImplWenDaModel().getAnsewData(questionBean, new ImplWenDaModel.GetAnswerDatasCallBack() {
                            @Override
                            public void onSuccess(List<AnswerBean> dynamic_data) {
                                hideProgress();
                                ZhuiwenZhuidaActivity.startZhuiwenZhuidaActivity(mContext, questionBean, dynamic_data.get(0));
                            }

                            @Override
                            public void onFailure() {
                                hideProgress();
                                ToastUtils.showToastCenter(mContext, "请稍后再试");
                            }
                        });
                    }
                } else {
                    if (userInfo.getUSERCODE().equals(questionBean.getQUESTIONUSERCODE())) {
                        ToastUtils.showToastCenter(mContext, XFrame.getString(R.string.warning_no_answer_self_question));
                    } else {
                        AnswerActivity.startAnswerActivity(mContext, questionBean);//进入回答页面
                    }
                }
            }
        });

        //点赞
        holder.setText(R.id.tv_zan, questionBean.getGOODNUMS() == 0 ? "点赞" : "" + questionBean.getGOODNUMS());
        final String isgood = questionBean.getISGOOD();
        int zanColor = "1".equals(isgood) ? R.color.colorPrimary : R.color.text_color_gray;
        holder.setTextColorRes(R.id.tv_zan, zanColor);
        holder.setImageDrawable(R.id.iv_zan, getColorDrawable(R.mipmap.icon_zan_new, zanColor));
        holder.setOnClickListener(R.id.ll_zan, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(isgood))
                    return;
                questionBean.setISGOOD("1");
                questionBean.setGOODNUMS(questionBean.getGOODNUMS() + 1);
                notifyDataSetChanged();
                API.goodQuestion(questionBean, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
                holder.setImageDrawable(R.id.iv_zan, getColorDrawable(R.mipmap.icon_zan_new, R.color.colorPrimary));
            }
        });


        //奖励
        holder.setImageDrawable(R.id.iv_reward, getColorDrawable(R.mipmap.icon_reward, R.color.text_color_gold));
        String bonuspoints = questionBean.getBONUSPOINTS();
        if (TextUtils.isEmpty(bonuspoints)) {
            holder.setVisible(R.id.iv_reward, false);
        } else {
            holder.setVisible(R.id.iv_reward, true);
        }
        holder.setText(R.id.tv_reward, bonuspoints);


        //问题内容
        final ExpandableTextView tv_question = holder.getView(R.id.tv_question);
        tv_question.post(new Runnable() {
            @Override
            public void run() {
                etvWidth = tv_question.getWidth();
            }
        });
        tv_question.updateForRecyclerView(questionBean.getQUESTIONEXPLAIN(), etvWidth, 0);
        //进入问题详情页面
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionDetailActivity.open(mContext, questionBean);
            }
        });
        tv_question.setExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(ExpandableTextView view) {
                tv_question.updateForRecyclerView(questionBean.getQUESTIONEXPLAIN(), etvWidth, 0);
            }

            @Override
            public void onShrink(ExpandableTextView view) {
                tv_question.updateForRecyclerView(questionBean.getQUESTIONEXPLAIN(), etvWidth, 0);
            }
        });
        holder.setOnClickListener(R.id.tv_question, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionDetailActivity.open(mContext, questionBean);
            }
        });
        //问题图片
        NineGridView view = holder.getView(R.id.nineGrid);
        List<String> imgurls = ResultStringCommonUtils.splitUrls(questionBean.getFILEURL());
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        for (String url :
                imgurls) {
            ImageInfo info = new ImageInfo();
            info.setThumbnailUrl(url);
            info.setBigImageUrl(url);
            imageInfo.add(info);
        }
        view.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));


    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dissMissProgressDialog();
        }
    }

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
        }
        progressDialog.setCanceLable(false);
        progressDialog.showProgressDialog();
    }
    private Drawable getColorDrawable(int res, int color) {
        Drawable drawable;
        if (imagehold == null) {
            imagehold = new HashMap<>();
        }
        drawable = imagehold.get(res + "" + color);
        if (drawable == null) {
            drawable = PictureUtil.tintDrawAble(res, color);
            imagehold.put(res + "" + color, drawable);
        }
        return drawable;
    }
}
