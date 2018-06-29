package com.zc.pickuplearn.ui.district_manager.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.beans.SkillMoudleLearnBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.district_manager.CourseDetailActivity;
import com.zc.pickuplearn.ui.district_manager.SkillMoudleActivity;

import java.util.List;

/**
 *
 * Created by chenbin on 2017/12/11.
 */

public class SkillMoudleAdapter extends CommonAdapter<SkillMoudleLearnBean> {


    public SkillMoudleAdapter(Context context, List<SkillMoudleLearnBean> datas) {
        super(context, R.layout.item_skill_moudle, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final SkillMoudleLearnBean string, int position) {
            holder.setText(R.id.tv_moudle_name,string.getABILITYTYPE());
            ImageLoaderUtil.display(mContext, (ImageView) holder.getView(R.id.iv_icon), string.getFUNCTIONURL());
            holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SkillMoudleActivity.open(mContext,string.getABILITYTYPECODE(),string.getABILITYTYPE(),string);
                }
            });
    }
}
