package com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget;

import android.content.Context;

import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.QuestionBean;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/11 13:34
 * 联系方式：chenbin252@163.com
 */

public class WenDaAdapter extends CommonAdapter<QuestionBean> {

    public WenDaAdapter(Context context, int layoutId, List<QuestionBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QuestionBean questionBean, int position) {

    }
}
