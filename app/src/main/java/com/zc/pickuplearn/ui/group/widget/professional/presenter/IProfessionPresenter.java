package com.zc.pickuplearn.ui.group.widget.professional.presenter;

import com.zc.pickuplearn.beans.ProfessionalCircleBean;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/4 10:18
 * 联系方式：chenbin252@163.com
 */

public interface IProfessionPresenter {
    void getAllProfessionalData();
    void getMyJoinProfessionalData(List<ProfessionalCircleBean> professionalCircleBeen);
}
