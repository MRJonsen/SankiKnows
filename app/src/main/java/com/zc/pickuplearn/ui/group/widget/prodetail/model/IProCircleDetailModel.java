package com.zc.pickuplearn.ui.group.widget.prodetail.model;

import com.zc.pickuplearn.beans.ProfessionalCircleBean;

/**
 * 作者： Jonsen
 * 时间: 2017/1/4 16:29
 * 联系方式：chenbin252@163.com
 */

public interface IProCircleDetailModel {
    /**
     * 获取圈子排名详细信息
     */
    void getCircleDetailData(ProfessionalCircleBean bean,ProCircleDetailModelImpl.ProCircleDetailCallBack callBack);
    void joinCircle(ProfessionalCircleBean bean,ProCircleDetailModelImpl.ProCircleJoinCallBack callBack);
}
