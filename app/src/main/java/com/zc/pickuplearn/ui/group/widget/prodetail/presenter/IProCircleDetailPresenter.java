package com.zc.pickuplearn.ui.group.widget.prodetail.presenter;

import com.zc.pickuplearn.beans.ProfessionalCircleBean;

/**
 * 作者： Jonsen
 * 时间: 2017/1/4 16:29
 * 联系方式：chenbin252@163.com
 */

public interface IProCircleDetailPresenter {
    /**
     * 获取排名数据
     */
    void getRankingData(ProfessionalCircleBean bean);
    void JoinCirclew(ProfessionalCircleBean bean);
}
