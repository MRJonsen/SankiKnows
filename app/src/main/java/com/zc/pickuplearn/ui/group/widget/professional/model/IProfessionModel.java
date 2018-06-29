package com.zc.pickuplearn.ui.group.widget.professional.model;

/**
 * 作者： Jonsen
 * 时间: 2017/1/4 10:19
 * 联系方式：chenbin252@163.com
 */

public interface IProfessionModel {
    void getProfessionalCircleData(ProfessionModelImpl.AllProCircleCallBack callBack);

    void getMyProfessionalCircleData(ProfessionModelImpl.MyProCircleCallBack callBack);
}
