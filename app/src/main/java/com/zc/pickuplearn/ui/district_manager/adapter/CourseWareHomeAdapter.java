package com.zc.pickuplearn.ui.district_manager.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hss01248.dialog.StyledDialog;
import com.youth.xframe.common.XActivityStack;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.beans.CourseWareHomeBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.ui.view.MaxRecycleView;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 课件首页列表适配器
 * Created by chenbin on 2017/12/11.
 */

public class CourseWareHomeAdapter extends CommonAdapter<CourseWareHomeBean> {
    private int orderType = 1;//1时间 2 热度
    SparseIntArray index = new SparseIntArray();
    public CourseWareHomeAdapter(Context context, List<CourseWareHomeBean> datas) {
        super(context, R.layout.item_course_ware_home, datas);
    }


    @Override
    protected void convert(final ViewHolder holder, final CourseWareHomeBean courseWareHomeBean, final int position) {
        holder.setText(R.id.title, courseWareHomeBean.getTYPENAME());
        final List<CourseWareBean.DatasBean> mData = new ArrayList<>();
        if (!TextUtils.isEmpty(courseWareHomeBean.getDATALIST())) {
            try {
                CourseWareBean courseWareBean = JsonUtils.parseJson2Object(courseWareHomeBean.getDATALIST(), new TypeToken<CourseWareBean>() {
                });
                index.append(position,courseWareBean.getTotalCount());
                mData.addAll(courseWareBean.getDatas());
                holder.setVisible(R.id.ll_more, mData.size() < courseWareBean.getTotalCount());//加载更多
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MaxRecycleView rcContent = holder.getView(R.id.content);
        rcContent.setHasFixedSize(true);
        rcContent.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        final CourseWareAdapter courseWareAdapter = new CourseWareAdapter(mContext, mData, CourseWareAdapter.Type.Other);
        rcContent.setAdapter(courseWareAdapter);

        //加载更多
        holder.setOnClickListener(R.id.ll_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StyledDialog.buildLoading("拼命加载中").setActivity(XActivityStack.getInstance().topActivity()).show();
                API.getInstance().getMoreCourseWare(mData.size(), orderType + "", courseWareHomeBean.getABILITYTYPE(), new CommonCallBack<List<CourseWareBean.DatasBean>>() {
                    @Override
                    public void onSuccess(List<CourseWareBean.DatasBean> datasBeen) {
                        if (datasBeen == null || datasBeen.size() == 0)
                            holder.setVisible(R.id.ll_more, false);
                        mData.addAll(datasBeen);
                        holder.setVisible(R.id.ll_more, mData.size() < index.get(position));//加载更多
                        courseWareAdapter.notifyDataSetChanged();
                        StyledDialog.dismissLoading();
                    }

                    @Override
                    public void onFailure() {
                        StyledDialog.dismissLoading();
                    }
                });
            }
        });

        holder.setOnClickListener(R.id.ib_ability_chapter_practice, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API.getInstance().getQuestionBankChapterPractice(courseWareHomeBean.getABILITYTYPE(), new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s) && !s.startsWith("http")) {
                            ToastUtils.showToast(mContext, s);
                            return;
                        }
                        WebViewActivity.skip(mContext, s, "", false, false);
//                        BaseWebViewActivity.open(mContext, s,false);
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
