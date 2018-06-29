package com.zc.pickuplearn.ui.classiccase.presenter;

import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;

/**
 * 作者： Jonsen
 * 时间: 2016/12/14 15:36
 * 联系方式：chenbin252@163.com
 */

public interface IClassicCaseListPresenter {
    void loadClassicCaseListData(QusetionTypeBean typeBean, ProfessionalCircleBean mbean);
}
