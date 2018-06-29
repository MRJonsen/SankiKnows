package com.zc.pickuplearn.ui.classiccase.model;

import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;

/**
 * 作者： Jonsen
 * 时间: 2016/12/14 15:36
 * 联系方式：chenbin252@163.com
 */

public interface IClassicCaseListModel {
    void getClassicCaseData(QusetionTypeBean typeBean, ProfessionalCircleBean circleBean, String type, int index, ClassicCaseListModelImpl.GetClassicCaseDatasCallBack callback);
}
