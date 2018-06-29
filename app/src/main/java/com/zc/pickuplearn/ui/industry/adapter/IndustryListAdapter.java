package com.zc.pickuplearn.ui.industry.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.IndustryStandardBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.classiccase.view.ClassicCaseDetailActivity;
import com.zc.pickuplearn.ui.industry.IndustryDetailActivity;

import java.util.List;

/**
 * 典型案例适配器
 * Created by chen bin on 2017/11/20.
 */

public class IndustryListAdapter extends CommonAdapter<IndustryStandardBean> {


    public IndustryListAdapter(Context context, List<IndustryStandardBean> data) {
        this(context, R.layout.item_industry, data);
    }

    private IndustryListAdapter(Context context, int layoutId, List<IndustryStandardBean> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(final ViewHolder holder, final IndustryStandardBean industryStandardBean, int position) {
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndustryDetailActivity.open(mContext,industryStandardBean);
                API.clickIndustryStandard(industryStandardBean, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        try {
                            industryStandardBean.setCLICKCOUNT(industryStandardBean.getCLICKCOUNT()+1);
                            notifyDataSetChanged();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                });//点击量增加
            }
        });
        holder.setText(R.id.tv_case_tag, industryStandardBean.getQUESTIONTYPENAME());
        holder.setText(R.id.tv_case_name, industryStandardBean.getSTANDARDNAME());
        holder.setText(R.id.tv_case_click_times, industryStandardBean.getCLICKCOUNT()+"");
        ImageView view = holder.getView(R.id.iv_case_face);
        ImageLoaderUtil.showImageView(mContext, ResultStringCommonUtils.subUrlToWholeUrl(industryStandardBean.getPHOTOURL()), view);
    }

}
