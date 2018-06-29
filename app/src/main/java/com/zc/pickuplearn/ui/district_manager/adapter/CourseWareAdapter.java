package com.zc.pickuplearn.ui.district_manager.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.common.io.BaseEncoding;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.district_manager.CourseDetailActivity;

import java.util.List;

/**
 * 课件列表适配器
 * Created by chenbin on 2017/12/11.
 */

public class CourseWareAdapter extends CommonAdapter<CourseWareBean.DatasBean> {
    private String[] TYPES_WORD = {".doc", ".docx"};//文本类型的
    private String[] TYPES_XLS = {".xls", ".xlsx", ".xls"};//文本类型的
    private String[] TYPES_PDF = {".pdf"};//文本类型的
    Type type;

    public CourseWareAdapter(Context context, List<CourseWareBean.DatasBean> datas, Type type) {
        super(context, R.layout.item_course_ware, datas);
        this.type = type;
    }

    @Override
    protected void convert(ViewHolder holder, final CourseWareBean.DatasBean bean, int position) {
        if (bean != null) {


            holder.setText(R.id.tv_course_name, bean.getCOURSENAME());
//        TextView typetext = holder.getView(R.id.tv_type);
//        typetext.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            holder.setText(R.id.tv_type, bean.getTYPENAME());
            holder.setBackgroundRes(R.id.iv_icon, getFileIcon(bean.getFILETYPE()));
            holder.setText(R.id.tv_content_len, bean.getCONTENTLEN());
            holder.setText(R.id.tv_comment_times, TextUtils.isEmpty(bean.getCOMMENTCOUNT()) ? "0" : bean.getCOMMENTCOUNT());
            holder.setText(R.id.tv_click_times, bean.getCLICKCOUNT() + "");
            holder.setVisible(R.id.iv_collect, "1".equals(bean.getISCOLLECT()));
            holder.setVisible(R.id.tv_function, Type.MyCollect == type);
            if (bean.getPLAYTIME() == null || TextUtils.isEmpty(bean.getPLAYTIME())) {
                bean.setPLAYTIME("0");
            }
            holder.setText(R.id.tv_progress, (int) (Float.parseFloat(bean.getPLAYTIME()) * 100) + "%");//学习进度
            if (Type.MyCollect == type) {
                holder.setVisible(R.id.iv_collect, Type.MyCollect == type);
                bean.setISCOLLECT("1");
            }
            holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CourseDetailActivity.open(mContext, bean);
                    API.getInstance().courseWareClick(bean, new CommonCallBack<String>() {
                        @Override
                        public void onSuccess(String s) {

                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }
            });
        }
    }


    public int getFileIcon(String fileType) {
        String s = fileType.toLowerCase();
        int icon = R.mipmap.video;
        for (String type :
                TYPES_WORD) {
            if (fileType.endsWith(type)) {
                icon = R.mipmap.word;
            }
        }
        for (String type :
                TYPES_XLS) {
            if (fileType.endsWith(type)) {
                icon = R.mipmap.excel;
            }
        }
        for (String type :
                TYPES_PDF) {
            if (fileType.endsWith(type)) {
                icon = R.mipmap.pdf;
            }
        }
        return icon;
    }

    public enum Type {
        MyCollect, Other
    }
}
