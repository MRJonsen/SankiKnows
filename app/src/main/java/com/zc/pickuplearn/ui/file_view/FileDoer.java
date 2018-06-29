package com.zc.pickuplearn.ui.file_view;

import android.content.Context;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.ui.videoplayer.VideoPlayerActivity;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.ui.webview.BaseWebViewActivity;
import com.zc.pickuplearn.ui.webview.x5.CommonActivity;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * 文件处理类
 * Created by Administrator on 2017/6/30 0030.
 */

public class FileDoer {
    private static String[] TYPES = {".ppt", ".xls", ".pdf", ".doc", ".docx", ".pptx", ".xlsx", ".xls"};//文本类型的
    private ProgressDialog progressDialog;

    private static FileDoer instance;

    private FileDoer() {
    }

    public static FileDoer getInstance() {
        if (instance == null) {
            synchronized (FileDoer.class) {
                if (instance == null) {
                    instance = new FileDoer();
                }
            }
        }
        return instance;
    }

    public void openDocument(Context context, String filePath) {
        Log.e("打开的文件路径", filePath);
        if (isOfficeFile(filePath)) {
//            PDFReadActivity.start(context,fileurl,title);
//            QbSdk.openBrowserList(context,"http://114.55.55.205:8080/emarkspg_nbsjzd/file/2017-08-09/b3bc6784-39b6-4a40-ac93-5313908c4097.mp4",null);
            QbSdk.openFileReader(context, filePath, null, null);
//            FileDisplayActivity.show(context, filePath);
        }else {
            MIMEUtil.getInstance().openFile(new File(filePath), context);
        }
    }


    public void openFile(final Context context, String url) {
        Log.e("打开的文件链接", url);
        if (isOfficeFile(url)) {
            int lastIndexOf = url.lastIndexOf("/");
            final String filename = url.substring(lastIndexOf + 1);
            final File file = new File(HttpContacts.absolutePath + filename);
            boolean IS_DOWN = file.exists();// 判断文件是否下载存在了
            if (IS_DOWN) {
                openDocument(context, file.getAbsolutePath());
                return;
            }
            showProgress(context);
            HttpUtils.doDownFile(url, new FileCallBack(HttpContacts.absolutePath2, filename) {
                @Override
                public void onError(Call call, Exception e, int id) {
                    e.printStackTrace();
                    ToastUtils.showToastCenter(context, "操作失败！");
                }

                @Override
                public void onResponse(File response, int id) {
                    openDocument(context, file.getAbsolutePath());
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    hideProgress();
                }
            });
        } else if (url.endsWith(".mp4")) {
//            VideoPlayerActivity.open(context,url);
            CommonActivity.open(context,url);
//            BaseWebViewActivity.open(context,url,true);
//            BaseWebViewActivity.open(context,"http://vod.hzzcdz.com/67c74df6e267453dbbeddcd6bdc0f320/a9974c667bcc4d15962214f474554133-5287d2089db37e62345123a1be272f8b.mp4",true);
        }else if (url.endsWith(".html")) {
            BaseWebViewActivity.open(context,url,true);
        }else {
                QbSdk.openBrowserList(context,url,null);
//            MIMEUtil.getInstance().openFile(new File(url), context);
            }
    }


    public void showProgress(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMsg("加载中..");
        progressDialog.setCanceLable(false);
        progressDialog.showProgressDialog();
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dissMissProgressDialog();
        }

    }

    private boolean isOfficeFile(String filePath) {
        for (String type :
                TYPES) {
            if (filePath.endsWith(type)) {
                Log.e("判断", type);
                return true;
            }
        }
        return false;
    }
}
