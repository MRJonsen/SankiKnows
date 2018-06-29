package com.zc.pickuplearn.ui.district_manager.adapter;

import android.content.Context;
import android.view.View;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.CourseWareCommentBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.utils.DateUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 课件列表适配器
 * Created by chenbin on 2017/12/11.
 */

public class CourseWareCommentAdapter extends CommonAdapter<CourseWareCommentBean> {

    public CourseWareCommentAdapter(Context context, List<CourseWareCommentBean> datas) {
        super(context, R.layout.item_course_ware_comment, datas);

    }

    @Override
    protected void convert(ViewHolder holder, CourseWareCommentBean s, int position) {
        //赞
//        holder.setOnClickListener(R.id.ll_zan, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        holder.getView(R.id.tv_zan);
//        holder.getView(R.id.iv_zan);

        holder.setText(R.id.tv_createTime, DateUtils.getCompareDate(s.getSYSCREATEDATE()));
        CircleImageView headimg = holder.getView(R.id.civ_head);//头像
        if (!s.getFILEURL().isEmpty()) {
            ImageLoaderUtil.showHeadView(mContext, ResultStringCommonUtils.subUrlToWholeUrl(s.getFILEURL()), headimg);//加载头像
        } else {
            ImageLoaderUtil.showHeadView(mContext, "", headimg);//加载默认头像
        }
        holder.setText(R.id.tv_username,s.getUSERNAME());//名字
        holder.setText(R.id.tv_answer,s.getCONTENTEXPLAIN());//内容


    }
}
