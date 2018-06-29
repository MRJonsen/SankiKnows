package com.zc.pickuplearn.http.callback;

import com.zc.pickuplearn.beans.VersionBean;

/**
 * Created by chenbin on 2017/10/24.
 */

public interface UpdateCallback {
    void onSuccess(VersionBean bean);
    void noNew();
}
