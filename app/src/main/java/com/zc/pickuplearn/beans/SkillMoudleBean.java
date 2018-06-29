package com.zc.pickuplearn.beans;

import java.util.List;

/**
 * Created by chenbin on 2018/5/28.
 */

public class SkillMoudleBean {
    private String name;
    private boolean hasDivider;

    public boolean isHasDivider() {
        return hasDivider;
    }

    public void setHasDivider(boolean hasDivider) {
        this.hasDivider = hasDivider;
    }

    private List<SkillMoudleLearnBean> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SkillMoudleLearnBean> getData() {
        return data;
    }

    public void setData(List<SkillMoudleLearnBean> data) {
        this.data = data;
    }

    public SkillMoudleBean(String name, List<SkillMoudleLearnBean> data) {
        this.name = name;
        this.data = data;
    }
}

