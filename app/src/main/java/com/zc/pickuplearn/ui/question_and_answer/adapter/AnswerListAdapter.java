package com.zc.pickuplearn.ui.question_and_answer.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.AnswerSubBean;
import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.event.QuestionStatusChangeEvent;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.answerdetail.ZhuiwenZhuidaActivity;
import com.zc.pickuplearn.ui.view.GlobalDialog;
import com.zc.pickuplearn.ui.view.NoScrollListView;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.PictureUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.zc.pickuplearn.R.id.item;

/**
 * 答案列表适配器
 * Created by chen bin on 2017/11/8.
 */

public class AnswerListAdapter extends CommonAdapter<AnswerBean> {

    private HashMap<String, Drawable> imagehold;

    private QuestionBean mQuestionBean;
    private ProgressDialog progressDialog;
    private final UserBean userInfo;

    public AnswerListAdapter(Context context, QuestionBean questionBean, List<AnswerBean> datas) {
        super(context, R.layout.item_answer, datas);
        mQuestionBean = questionBean;
        userInfo = DataUtils.getUserInfo();
    }

    @Override
    protected void convert(final ViewHolder holder, final AnswerBean answerBean, int position) {
        holder.setText(R.id.tv_answer, answerBean.getANSWEREXPLAIN());


        //头像
        CircleImageView headimg = holder.getView(R.id.civ_head);
        if (!answerBean.getHEADIMAGE().isEmpty()) {
            ImageLoaderUtil.showHeadView(mContext, ResultStringCommonUtils.subUrlToWholeUrl(answerBean.getHEADIMAGE()), headimg);//加载头像
        } else {
            ImageLoaderUtil.showHeadView(mContext, "", headimg);//加载默认头像
        }

        //名称
        holder.setText(R.id.tv_username, TextUtils.isEmpty(answerBean.getNICKNAME()) ? answerBean.getANSWERUSERNAME() : answerBean.getNICKNAME());


        //问题图片
        NineGridView view = holder.getView(R.id.nineGrid);
        List<String> imgurls = ResultStringCommonUtils.splitUrls(answerBean.getFILEURL());
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        for (String url :
                imgurls) {
            ImageInfo info = new ImageInfo();
            info.setThumbnailUrl(url);
            info.setBigImageUrl(url);
            imageInfo.add(info);
        }
        view.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));

        //点赞
        holder.setText(R.id.tv_zan, TextUtils.isEmpty(answerBean.getGOODNUMS()) || "0".equals(answerBean.getGOODNUMS()) ? "点赞" : answerBean.getGOODNUMS());
        final String isgood = answerBean.getISGOOD();
        int zanColor = "1".equals(isgood) ? R.color.colorPrimary : R.color.text_color_gray;
        holder.setTextColorRes(R.id.tv_zan, zanColor);
        holder.setImageDrawable(R.id.iv_zan, getColorDrawable(R.mipmap.icon_zan_new, zanColor));
        holder.setOnClickListener(R.id.ll_zan, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(isgood))
                    return;
                answerBean.setISGOOD("1");
                if (!TextUtils.isEmpty(answerBean.getGOODNUMS())) {
                    String goodnums = answerBean.getGOODNUMS();
                    Integer integer = Integer.valueOf(goodnums);
                    answerBean.setGOODNUMS(integer + 1 + "");
                } else {
                    answerBean.setGOODNUMS("1");
                }
                API.goodAnswer(answerBean, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onFailure() {

                    }
                });
                notifyDataSetChanged();
            }
        });

        //追文追答
        if (TextUtils.isEmpty(answerBean.getDATALIST())) {
            holder.setVisible(R.id.lv_comments,false);
        } else {

            holder.setVisible(R.id.lv_comments,true);
            NoScrollListView commentview = holder.getView(R.id.lv_comments);
            try {
                AnswerSubBean ansersubBean = JsonUtils.parseJson2Object(answerBean.getDATALIST(), new TypeToken<AnswerSubBean>() {});
                commentview.setAdapter(new CommentsAdapter(mContext, ansersubBean.getDatas(),mQuestionBean,answerBean));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //时间
        holder.setText(R.id.tv_createTime, DateUtils.dataFormatNow("yyyy-MM-dd HH:mm", answerBean.getSYSCREATEDATE()));

        //点击进入问题详情
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhuiwenZhuidaActivity.startZhuiwenZhuidaActivity(mContext, mQuestionBean, answerBean);
            }
        });
        holder.setOnClickListener(R.id.tv_answer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhuiwenZhuidaActivity.startZhuiwenZhuidaActivity(mContext, mQuestionBean, answerBean);
            }
        });

        //采纳
        if ("1".equals(answerBean.getISPASS())){
            holder.setVisible(R.id.iv_taken,true);
        }else {
            holder.setVisible(R.id.iv_taken,false);
        }
        if (!"1".equals(mQuestionBean.getISSOLVE()) && mQuestionBean.getQUESTIONUSERCODE().equals(userInfo.getUSERCODE())) {
            holder.setVisible(R.id.tv_take, true);

        } else {
            holder.setVisible(R.id.tv_take, false);
        }

        holder.setOnClickListener(R.id.tv_take, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                take(answerBean);
            }
        });
    }

    private void take(final AnswerBean answerBean) {
        final GlobalDialog delDialog = new GlobalDialog(mContext);
        delDialog.setCanceledOnTouchOutside(true);
        delDialog.getContent().setText(XFrame.getString(R.string.warning_take_answer));
        delDialog.setLeftBtnText("取消");
        delDialog.setRightBtnText("确定");
        delDialog.setLeftOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delDialog.dismiss();
            }
        });
        delDialog.setRightOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                API.takeAnswer(answerBean, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        EventBus.getDefault().post(new QuestionStatusChangeEvent());
                        hideProgress();
                    }

                    @Override
                    public void onFailure() {
                        hideProgress();
                    }
                });

                delDialog.dismiss();
            }
        });
        delDialog.show();
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

    public void setQuestionBean(QuestionBean questionBean) {
        mQuestionBean = questionBean;
        notifyDataSetChanged();
    }
}
