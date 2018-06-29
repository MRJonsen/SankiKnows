package com.zc.pickuplearn.ui.mine.mine.model;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/16 13:20
 * 联系方式：chenbin252@163.com
 */

public interface IMineModel {
    void uploadImage(List<String> list, MineModelImpl.UploadHeadImageCallBack callBack);
}
