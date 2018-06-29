package com.zc.pickuplearn.ui.group.adapter;

import android.content.Context;
import android.view.View;

import com.zc.pickuplearn.ui.file_view.FileDoer;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.IndustryStandardBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;

import okhttp3.Call;

/**
 * 作者： Jonsen
 * 时间: 2017/4/5 11:06
 * 联系方式：chenbin252@163.com
 */

public class IndustryStandardAdapter extends CommonAdapter<IndustryStandardBean> {

    private Context mContext;

    public IndustryStandardAdapter(Context context, List<IndustryStandardBean> datas) {
        this(context, R.layout.item_list_industry_standard_2, datas);
    }

    public IndustryStandardAdapter(Context context, int layoutId, List<IndustryStandardBean> datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final IndustryStandardBean bean, int position) {
        String filetype = bean.getFILETYPE();
        if (filetype.contains("pdf")) {
            holder.setImageResource(R.id.iv_icon, R.mipmap.icon_pdf);
        } else {
            holder.setImageResource(R.id.iv_icon, R.mipmap.icon_office);
        }
        holder.setText(R.id.tv_name, bean.getSTANDARDNAME());
        holder.setText(R.id.tv_click_count,bean.getCLICKCOUNT()+"");
        holder.setText(R.id.tv_tag,bean.getQUESTIONTYPENAME());
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(bean);
                addClickCount(bean);
            }
        });
    }

    private void openFile(final IndustryStandardBean bean) {
        String childUrl = bean.getFILEURL();
        int lastIndexOf = childUrl.lastIndexOf("/");
        final String filename = childUrl.substring(lastIndexOf + 1);
        String url = ResultStringCommonUtils
                .subUrlToWholeUrl(childUrl);
        LogUtils.e(url);
        File file = new File(HttpContacts.absolutePath + filename);
        boolean IS_DOWN = file.exists();// 判断文件是否下载存在了
        if (IS_DOWN) {
            openFile(file,bean.getFILENAME());
            return;
        }
        ToastUtils.showToastCenter(mContext,"处理中，请等待！");
        HttpUtils.doDownFile(url, new FileCallBack(HttpContacts.absolutePath2, filename) {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                ToastUtils.showToastCenter(mContext,"操作失败！");
            }

            @Override
            public void onResponse(File response, int id) {
                openFile(response,bean.getFILENAME());
            }
        });
    }

    private void openFile(File file,String title) {
        FileDoer.getInstance().openDocument(mContext,file.getAbsolutePath());
    }

    private void addClickCount(final IndustryStandardBean bean) {
        API.clickIndustryStandard(bean, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                bean.setCLICKCOUNT(bean.getCLICKCOUNT()+1);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
