package com.zc.pickuplearn.beans;

import java.io.Serializable;

/**
 * 经典案咧资料下载条目 bean
 * 作者： Jonsen
 * 时间: 2016/12/14 11:41
 * 联系方式：chenbin252@163.com
 */

public class SourceBean implements Serializable {
    public String name; //资源名称
    public String url;//资源链接
    private boolean isDownLoad;//是否下载了

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDownLoad() {
        return isDownLoad;
    }

    public void setDownLoad(boolean downLoad) {
        isDownLoad = downLoad;
    }


}
