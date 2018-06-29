package com.zc.pickuplearn.ui.type.widget;

import android.content.Context;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;

import java.util.List;

/**
 *
 * Created by chenbin on 2017/11/22.
 */

public class SubTypeAdapter extends CommonAdapter<QusetionTypeBean> {

    public SubTypeAdapter(Context context, List<QusetionTypeBean> datas) {
        super(context, R.layout.item_sub_type, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QusetionTypeBean qusetionTypeBean, int position) {
        holder.setText(R.id.typename,qusetionTypeBean.getNAME());
    }
}
