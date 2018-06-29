package com.zc.pickuplearn.ui.district_manager.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.SkillBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.district_manager.SkillDetailActivity;

import java.util.List;

/**
 * 技能清单适配器
 * Created by chenbin on 2017/12/8.
 */

public class SkillInventoryAdapter extends CommonAdapter<SkillBean.DataBean> {

    public SkillInventoryAdapter(Context context, List<SkillBean.DataBean> datas) {
        super(context, R.layout.item_skill_inventory, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final SkillBean.DataBean s, int position) {
        holder.setText(R.id.tv_skill_name, s.getABILITYNAME());
        holder.setText(R.id.tv_skill_tag, s.getABILITYTYPE());
        ImageLoaderUtil.showImageViewByResourceId(mContext, getLevelIcon(s.getABILITY_LEVEL()), (ImageView) holder.getView(R.id.iv_level));
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkillDetailActivity.open(mContext,s);
            }
        });
    }

    public int getLevelIcon(String level) {
        int icon = R.mipmap.icon_skill_level_1;
        if ("I".equals(level))
            icon = R.mipmap.icon_skill_level_1;
        if ("II".equals(level))
            icon = R.mipmap.icon_skill_level_2;
        if ("III".equals(level))
            icon = R.mipmap.icon_skill_level_3;

        return icon;
    }
}
