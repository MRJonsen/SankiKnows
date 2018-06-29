package com.zc.pickuplearn.ui.group.widget.professional.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.group.widget.prodetail.view.ProCircleDetailActivity;
import com.zc.pickuplearn.ui.mine.mine.widget.MyCircleActivity;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/4 10:23
 * 联系方式：chenbin252@163.com
 */

public class ProfessionAdapter extends CommonAdapter<ProfessionalCircleBean> {
    private Context mContext;
    private String mfrom;

    public ProfessionAdapter(Context context, List<ProfessionalCircleBean> datas, String from) {
        this(context, R.layout.item_circle_professional, datas);
        mContext = context;
        mfrom = from;
    }

    public ProfessionAdapter(Context context, int layoutId, List<ProfessionalCircleBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final ProfessionalCircleBean professionalCircleBean, final int position) {
        if (!mfrom.equals(MyCircleActivity.TAG)) {
            ImageView iv_ranking = holder.getView(R.id.iv_ranking);
            iv_ranking.setVisibility(View.VISIBLE);
            if (position == 1) {
                iv_ranking.setImageResource(R.mipmap.group_first);
            } else if (position == 2) {
                iv_ranking.setImageResource(R.mipmap.group_sec);
            } else if (position == 3) {
                iv_ranking.setImageResource(R.mipmap.group_thr);
            } else {
                iv_ranking.setVisibility(View.INVISIBLE);
            }
        }
        holder.setText(R.id.cirlce_name, professionalCircleBean.getPROCIRCLENAME());
        holder.setVisible(R.id.professional_item_selected, professionalCircleBean.getISJOIN());
        ImageView view = holder.getView(R.id.iv_circle_icon);
        if (!TextUtils.isEmpty(professionalCircleBean.getICOPATH())) {
            ImageLoaderUtil.displayCircleICon(mContext, view, professionalCircleBean.getICOPATH());
        } else {
            view.setImageResource(R.mipmap.default_user_circle_icon);
        }
        holder.setOnClickListener(R.id.rl_circle_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProCircleDetailActivity.startProCircleDetailActivity(mContext, professionalCircleBean);
            }
        });
        holder.setOnClickListener(R.id.cirlce_name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProCircleDetailActivity.startProCircleDetailActivity(mContext, professionalCircleBean);
            }
        });
    }
}
