package com.zc.pickuplearn.ui.home.model;

import com.zc.pickuplearn.beans.ProfessionalCircleBean;

/**
 * 作者： Jonsen
 * 时间: 2016/12/12 16:31
 * 联系方式：chenbin252@163.com
 */

public interface IHomeModel {
    void getClassicCaseDatas(ProfessionalCircleBean mbean,HomeModelImpl.GetClassicCaseDatasCallBack callBack);
    void getDynamicDatas(String search,HomeModelImpl.GetDynamicDatasCallBack callBack);
}
