package com.zc.pickuplearn.ui.teamcircle.adapter;

import android.content.Context;

import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.QuestionBean;

import java.util.List;

/**
 * 团队问题搜索适配器
 * 作者： Jonsen
 * 时间: 2017/3/17 15:15
 * 联系方式：chenbin252@163.com
 */

public class TeamSearchAdapter extends CommonAdapter<List<QuestionBean>> {
    public TeamSearchAdapter(Context context, int layoutId, List<List<QuestionBean>> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, List<QuestionBean> questionBeen, int position) {

    }
}
