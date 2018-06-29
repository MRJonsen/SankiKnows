package com.zc.pickuplearn.ui.district_manager.adapter;

import android.content.Context;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;

import java.util.List;

/**
 * Created by chenbin on 2017/12/11.
 */

public class PracticeSpecificationListAdapter extends CommonAdapter<String> {
    public PracticeSpecificationListAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_practice_specification, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tv_name,"变压器电阻测量");
        holder.setText(R.id.tv_tag,"电力安全生产及测试");
    }
}
