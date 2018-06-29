package com.zc.pickuplearn.ui.classiccase.view.resource.widget;

import android.content.Context;
import android.view.View;

import com.zc.pickuplearn.ui.file_view.FileDoer;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.SourceBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.webview.BaseWebViewActivity;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;

import okhttp3.Call;

/**
 * @author bin E-mail: chenbin252@163.COM
 * @version 创建时间：2016-11-2 上午11:33:26
 * @Describe
 */
public class ResourceListAdapter extends CommonAdapter<SourceBean> {

    private List<SourceBean> mSourceBean;
    private Context mContext;

    public ResourceListAdapter(Context context, int layoutId, List<SourceBean> sourceBean) {
        super(context, layoutId, sourceBean);
        mContext = context;
        mSourceBean = sourceBean;
    }

    @Override
    protected void convert(ViewHolder holder, final SourceBean bean, final int position) {
        holder.setText(R.id.tv_name, bean.getName());
        holder.setText(R.id.tv_status, "打开");
        holder.setBackgroundRes(R.id.iv_status_icon, R.mipmap.course_catalog_play_style_green);
        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getUrl().startsWith("http")){
                    BaseWebViewActivity.open(mContext,bean.getUrl(),true);
//                    WebViewActivity.skip(mContext,bean.getUrl(),bean.getName(),true);
                    return;
                }

                FileDoer.getInstance().openFile(mContext,ResultStringCommonUtils
                        .subUrlToWholeUrl(bean.getUrl()));
//                String childUrl = bean.getUrl();
//                int lastIndexOf = childUrl.lastIndexOf("/");
//                final String filename = childUrl.substring(lastIndexOf + 1);
//                String url = ResultStringCommonUtils
//                        .subUrlToWholeUrl(childUrl);
//                LogUtils.e(url);
//                File file = new File(HttpContacts.absolutePath + filename);
//                boolean IS_DOWN = file.exists();// 判断文件是否下载存在了
//                if (IS_DOWN) {
//                    openFile(file,bean.getName());
//                    return;
//                }
//                ToastUtils.showToastCenter(mContext,"处理中，请等待！");
//                HttpUtils.doDownFile(url, new FileCallBack(HttpContacts.absolutePath2, filename) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        e.printStackTrace();
//                        ToastUtils.showToastCenter(mContext,"操作失败！");
//                    }
//
//                    @Override
//                    public void onResponse(File response, int id) {
//                        openFile(response,bean.getName());
//                    }
//                });
            }
        });
    }

    /**
     * 打开文件
     *
     * @param file
     */
    public void openFile(File file,String title) {
        FileDoer.getInstance().openDocument(mContext,file.getAbsolutePath());
    }

    /**
     * 设置下载按钮状态
     *
     * @param type 0 下载 1 下载中 2 下载完成
     */
    private void setIconstatus(ViewHolder viewHolder, int type) {

        switch (type) {
            case 0:
                viewHolder.setBackgroundRes(R.id.iv_status_icon, R.mipmap.course_catalog_download);
                viewHolder.setText(R.id.tv_status, "下载");
                break;
            case 1:
                viewHolder.setBackgroundRes(R.id.iv_status_icon, R.mipmap.course_wait);
                viewHolder.setText(R.id.tv_status, "下载中");
                break;
            case 2:
                viewHolder.setBackgroundRes(R.id.iv_status_icon, R.mipmap.course_catalog_play_style_green);
                viewHolder.setText(R.id.tv_status, "打开");
                break;
            default:
                break;
        }

    }
}
