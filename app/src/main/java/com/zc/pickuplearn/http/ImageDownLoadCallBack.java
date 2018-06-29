package com.zc.pickuplearn.http;

import java.io.File;

/**
 * 图片下载回调接口
 * Created by chenbin on 2017/10/18.
 */

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(File file);

    void onDownLoadFailed();
}
