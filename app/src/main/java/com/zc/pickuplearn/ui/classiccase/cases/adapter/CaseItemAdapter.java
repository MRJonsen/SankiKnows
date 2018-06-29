package com.zc.pickuplearn.ui.classiccase.cases.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.classiccase.view.ClassicCaseDetailActivity;

import java.util.List;

/**
 * 典型案例适配器
 * Created by chen bin on 2017/11/20.
 */

public class CaseItemAdapter extends CommonAdapter<ClassicCaseBean> {

    private Type mType;

    public CaseItemAdapter(Context context, List<ClassicCaseBean> data, Type type) {
        this(context, R.layout.item_case, data);
        mType = type;
    }

    private CaseItemAdapter(Context context, int layoutId, List<ClassicCaseBean> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(final ViewHolder holder, final ClassicCaseBean caseBean, int position) {
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassicCaseDetailActivity.startClassicDetailActivity(mContext, caseBean);
                API.clickClassicCase(caseBean.getSEQKEY(), new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            String clickcount = caseBean.getCLICKCOUNT();
                            if (!TextUtils.isEmpty(clickcount)) {
                                int i = Integer.parseInt(clickcount) + 1;
                                clickcount = i + "";
                            } else {
                                clickcount = "1";
                            }
                            caseBean.setCLICKCOUNT(clickcount);
                            notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
        holder.setText(R.id.tv_case_tag, caseBean.getQUESTIONTYPENAME());
        holder.setText(R.id.tv_case_name, caseBean.getCASENAME());
        holder.setText(R.id.tv_case_click_times, caseBean.getCLICKCOUNT());
        ImageView view = holder.getView(R.id.iv_case_face);
        ImageLoaderUtil.showImageView(mContext, ResultStringCommonUtils.subUrlToWholeUrl(caseBean.getFILEURL()), view);
        switch (mType) {
            case CASE_LIST:
                holder.setVisible(R.id.view_top_divider, true);
                holder.setVisible(R.id.tv_simple_intro, true);
                holder.setText(R.id.tv_simple_intro,"简介："+caseBean.getCASEEXPLAIN());
                break;
        }
    }

    public enum Type {
        CASE_HOME, CASE_LIST
    }
}
