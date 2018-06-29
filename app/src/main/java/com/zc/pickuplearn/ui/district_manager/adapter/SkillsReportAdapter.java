package com.zc.pickuplearn.ui.district_manager.adapter;

import android.content.Context;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;

import java.util.List;

/**
 * Created by chenbin on 2017/12/11.
 */

public class SkillsReportAdapter extends CommonAdapter<String> {
    public SkillsReportAdapter(Context context,List<String> datas) {
        super(context, R.layout.item_skill_report, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {

    }
}
