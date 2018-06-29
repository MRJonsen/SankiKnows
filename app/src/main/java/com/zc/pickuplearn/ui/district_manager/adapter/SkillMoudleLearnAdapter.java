package com.zc.pickuplearn.ui.district_manager.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.hss01248.dialog.StyledDialog;
import com.youth.xframe.common.XActivityStack;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.beans.CourseWareHomeBean;
import com.zc.pickuplearn.beans.SkillMoudleBean;
import com.zc.pickuplearn.beans.SkillMoudleLearnBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.ui.view.GridSpacingItemDecoration;
import com.zc.pickuplearn.ui.view.MaxRecycleView;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zc.pickuplearn.ui.district_manager.CourseDetailActivity.bean;

/**
 * 课件学习模块
 * Created by chenbin on 2017/12/11.
 */

public class SkillMoudleLearnAdapter extends CommonAdapter<SkillMoudleBean> {

    private int COLUMN = 2;
    public SkillMoudleLearnAdapter(Context context, List<SkillMoudleBean> datas) {
        super(context, R.layout.item_skill_moudle_learn, datas);
    }


    @Override
    protected void convert(final ViewHolder holder, final SkillMoudleBean skillMoudleBean, final int position) {
        holder.setText(R.id.title, skillMoudleBean.getName());
        MaxRecycleView rcContent = holder.getView(R.id.content);
        rcContent.setHasFixedSize(true);
        GridLayoutManager layout = new GridLayoutManager(mContext, COLUMN, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rcContent.setLayoutManager(layout);
        if (!skillMoudleBean.isHasDivider()){
            rcContent.addItemDecoration(new GridSpacingItemDecoration(COLUMN, 50, true));
        }
        rcContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        final SkillMoudleAdapter courseWareAdapter = new SkillMoudleAdapter(mContext, skillMoudleBean.getData());
        rcContent.setAdapter(courseWareAdapter);
        skillMoudleBean.setHasDivider(true);

    }


}
