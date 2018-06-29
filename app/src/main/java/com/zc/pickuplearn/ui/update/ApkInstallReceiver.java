package com.zc.pickuplearn.ui.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.zc.pickuplearn.utils.SPUtils;
import com.zc.pickuplearn.utils.UIUtils;

/**
 * Created by Song on 2016/11/2.
 */
public class ApkInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            installApk(context, downloadApkId);
        }
    }

    /**
     * 安装apk
     */
    private void installApk(Context context, long downloadId) {

        long downId = (long) SPUtils.get(UIUtils.getContext(), DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        if (downloadId == downId) {
            DownloadManager downManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = downManager.getUriForDownloadedFile(downloadId);
            if (downloadUri != null) {
                SPUtils.put(UIUtils.getContext(), "downloadApk", downloadUri.getPath());
                Intent install = new Intent(Intent.ACTION_VIEW);
                String downloadPath = DownLoadUtils.getInstance(UIUtils.getContext()).getDownloadPath(downId);
                install.setDataAndType(Uri.parse(downloadPath), "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } else {
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
