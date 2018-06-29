package com.zc.pickuplearn.ui.group.widget.prodetail.widget;

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

public class IndustryAdapter extends CommonAdapter<IndustryStandardBean> {

    private Context mContext;

    public IndustryAdapter(Context context, List<IndustryStandardBean> datas) {
        this(context, R.layout.item_list_industry_standard_1, datas);
    }

    public IndustryAdapter(Context context, int layoutId, List<IndustryStandardBean> datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final IndustryStandardBean bean, int position) {
//        String filetype = bean.getFILETYPE();
//        if (filetype.contains("pdf")) {
//            holder.setImageResource(R.id.iv_icon, R.mipmap.icon_pdf);
//        } else {
//            holder.setImageResource(R.id.iv_icon, R.mipmap.icon_office);
//        }
        switch (position) {
            case 0:
                holder.setVisible(R.id.iv_hot,true);
                break;
            case 1:
                holder.setVisible(R.id.iv_hot,true);
                break;
            case 2:
                holder.setVisible(R.id.iv_hot,true);
                break;
            default:
                holder.setVisible(R.id.iv_hot,false);
                break;
        }
        holder.setText(R.id.tv_name, bean.getSTANDARDNAME());
        //条目点击1.点击量增加 下载或者读取本地文件打开
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(bean);
                addClickCount(bean);
            }
        });
    }

    private void addClickCount(IndustryStandardBean bean) {
        API.clickIndustryStandard(bean, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void openFile(IndustryStandardBean bean) {
        String childUrl = bean.getFILEURL();
        int lastIndexOf = childUrl.lastIndexOf("/");
        final String filename = childUrl.substring(lastIndexOf + 1);
        String url = ResultStringCommonUtils
                .subUrlToWholeUrl(childUrl);
        LogUtils.e(url);
        File file = new File(HttpContacts.absolutePath + filename);
        boolean IS_DOWN = file.exists();// 判断文件是否下载存在了
        if (IS_DOWN) {
//                    LogUtils.e("文件存在");
            openFile(file);
            return;
        }
        ToastUtils.showToastCenter(mContext, "处理中，请等待！");
//                LogUtils.e("下载文件去了");
        HttpUtils.doDownFile(url, new FileCallBack(HttpContacts.absolutePath2, filename) {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                ToastUtils.showToastCenter(mContext, "操作失败！");
            }

            @Override
            public void onResponse(File response, int id) {
                openFile(response);
            }
        });
    }

    private void openFile(File file) {
        FileDoer.getInstance().openDocument(mContext,file.getAbsolutePath());
    }
}
