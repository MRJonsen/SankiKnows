package com.zc.pickuplearn.ui.industry.adapter;

import android.content.Context;
import android.view.View;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.SourceBean;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.file_view.FileDoer;

import java.util.List;

/**
 * 行业规范 资源列表适配器
 * Created by chen bin on 2017/11/20.
 */

public class IndustryResAdapter extends CommonAdapter<SourceBean> {
    private static String[] TYPE_WORD = {".doc", ".docx"};
    private static String[] TYPES_XLS = {".xlsx", ".xls", "ppt", "pptx"};
    private static String[] TYPES_PDF = {".pdf"};

    public IndustryResAdapter(Context context, List<SourceBean> data) {
        this(context, R.layout.item_industry_res, data);
    }

    private IndustryResAdapter(Context context, int layoutId, List<SourceBean> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(final ViewHolder holder, final SourceBean sourceBean, int position) {
        holder.setText(R.id.tv_res_name, sourceBean.getName());
        if (fileType(sourceBean.getName(), TYPE_WORD)) {
            holder.setImageResource(R.id.iv_res_icon, R.mipmap.word);
        } else if (fileType(sourceBean.getName(), TYPES_PDF)) {
            holder.setImageResource(R.id.iv_res_icon, R.mipmap.pdf);
        } else if (fileType(sourceBean.getName(), TYPES_XLS)) {
            holder.setImageResource(R.id.iv_res_icon, R.mipmap.excel);
        } else {
            holder.setImageResource(R.id.iv_res_icon, R.mipmap.video);
        }
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDoer.getInstance().openFile(mContext, ResultStringCommonUtils
                        .subUrlToWholeUrl(sourceBean.getUrl()));
            }
        });
    }

    private boolean fileType(String filePath, String[] types) {
        for (String type : types) {
            if (filePath.endsWith(type)) {
                return true;
            }
        }
        return false;
    }
}
