package com.zc.pickuplearn.ui.mine.mine.view;

/**
 * 作者： Jonsen
 * 时间: 2016/12/8 16:34
 * 联系方式：chenbin252@163.com
 */

public interface IMineView {

    /**
     * 设置显示名称
     * @param name
     */
    void setName(String name);

    /**
     * 设置头像
     * @param url
     * @param islocal
     */
    void setHeadImage(String url,boolean islocal);

    void showPickPhotoDialog();
    void setInfo();
}
