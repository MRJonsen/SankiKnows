package com.zc.pickuplearn.ui.home.widget;

import android.content.Context;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.utils.UIUtils;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2016/12/14 11:12
 * 联系方式：chenbin252@163.com
 */

public class ClassicCaseAdapter extends CommonAdapter<ClassicCaseBean> {

    public ClassicCaseAdapter(Context context, List<ClassicCaseBean> datas) {
        this(context, R.layout.item_classic_case, datas);
    }

    private ClassicCaseAdapter(Context context, int layoutId, List<ClassicCaseBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ClassicCaseBean classicCaseBean, int position) {
        holder.setText(R.id.tv_title, classicCaseBean.getCASENAME());
        holder.setText(R.id.tv_click_count, classicCaseBean.getCLICKCOUNT() + "");
        holder.setText(R.id.tv_explain,classicCaseBean.getCASEEXPLAIN());
        ImageLoaderUtil.displayBitmap(UIUtils.getContext(), (ImageView) holder.getView(R.id.iv_icon), classicCaseBean.getFILEURL(), false);
    }
}
