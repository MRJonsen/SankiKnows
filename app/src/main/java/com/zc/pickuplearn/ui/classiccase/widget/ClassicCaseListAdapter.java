package com.zc.pickuplearn.ui.classiccase.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.classiccase.view.ClassicCaseDetailActivity;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2016/12/14 11:12
 * 联系方式：chenbin252@163.com
 */

public class ClassicCaseListAdapter extends CommonAdapter<ClassicCaseBean> {
    private Context mContext;

    public ClassicCaseListAdapter(Context context, int layoutId, List<ClassicCaseBean> datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final ClassicCaseBean classicCaseBean, int position) {
        holder.setText(R.id.tv_title, classicCaseBean.getCASENAME());
        holder.setText(R.id.tv_score, TextUtils.isEmpty(classicCaseBean.getCASESCORE()) ? "0" : classicCaseBean.getCASESCORE());
        holder.setText(R.id.tv_comment_count, classicCaseBean.getCLICKCOUNT() + "");
        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassicCaseDetailActivity.startClassicDetailActivity(mContext, classicCaseBean);
                API.clickClassicCase(classicCaseBean.getSEQKEY(), new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        String clickcount = classicCaseBean.getCLICKCOUNT();
                        if (!TextUtils.isEmpty(clickcount)) {
                            int i = Integer.parseInt(clickcount) + 1;
                            clickcount = i + "";
                        } else {
                            clickcount = "1";
                        }
                        classicCaseBean.setCLICKCOUNT(clickcount);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
        ImageView iconView = holder.getView(R.id.iv_icon);
        if (!TextUtils.isEmpty(classicCaseBean.getFILEURL())) {
            Glide.with(mContext).load(ResultStringCommonUtils.subUrlToWholeUrl(classicCaseBean.getFILEURL())).into(iconView);
//            ImageLoaderUtils.displayBitmap(UIUtils.getContext(), (ImageView) iconView, classicCaseBean.getFILEURL(), false);
        }else {
            Glide.with(mContext).load(R.mipmap.default_image).into(iconView);
//            iconView.setBackgroundResource(R.mipmap.default_image);
        }
    }

}
