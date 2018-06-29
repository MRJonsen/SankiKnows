package com.zc.pickuplearn.ui.group.widget.professional.view;

import com.zc.pickuplearn.beans.ProfessionalCircleBean;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2016/12/13 16:45
 * 联系方式：chenbin252@163.com
 */

public interface IProfessionView {
    /**
     * 添加排名数据
     * @param data
     */
    void addData(List<ProfessionalCircleBean> data);

    void disShowRefresh();

}
