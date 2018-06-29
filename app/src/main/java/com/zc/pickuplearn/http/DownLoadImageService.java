package com.zc.pickuplearn.http;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.zc.pickuplearn.utils.UIUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片下载
 */
public class DownLoadImageService implements Runnable {
    private String url;
    private Context context;
    private ImageDownLoadCallBack callBack;
    private File currentFile;

    public DownLoadImageService(Context context, String url, ImageDownLoadCallBack callBack) {
        this.url = url;
        this.callBack = callBack;
        this.context = context;
    }

    @Override
    public void run() {
        File file = null;
        try {
            file = Glide.with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            if (file != null) {
                saveImageFileToGallery(context, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null && currentFile.exists()) {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onDownLoadSuccess(currentFile);
                    }
                });

            } else {
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onDownLoadFailed();
                    }
                });

            }
        }
    }

    private void saveImageFileToGallery(Context context, File file) {
        // 首先保存图片
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();//注意小米手机必须这样获得public绝对路径
        String fileName = "picture";
        File appDir = new File(filePath, fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName2 = url.substring(url.lastIndexOf("/") + 1);
        currentFile = new File(appDir, fileName2);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int n = 0;
            while ((n = fis.read(b)) != -1) {
                fos.write(b, 0, n);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis!=null){
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    currentFile.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));
    }
}