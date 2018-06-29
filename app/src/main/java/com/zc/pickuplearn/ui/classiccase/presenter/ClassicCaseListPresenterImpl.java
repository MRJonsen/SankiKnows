package com.zc.pickuplearn.ui.classiccase.presenter;

import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;
import com.zc.pickuplearn.ui.classiccase.model.ClassicCaseListModelImpl;
import com.zc.pickuplearn.ui.classiccase.view.IClassicCaseListView;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2016/12/14 15:36
 * 联系方式：chenbin252@163.com
 */

public class ClassicCaseListPresenterImpl implements IClassicCaseListPresenter {
    private IClassicCaseListView mView;
    private ClassicCaseListModelImpl mModel;

    public ClassicCaseListPresenterImpl(IClassicCaseListView view) {
        mView = view;
        mModel = new ClassicCaseListModelImpl();
    }

    @Override
    public void loadClassicCaseListData(QusetionTypeBean typeBean, ProfessionalCircleBean bean) {
        mModel.getClassicCaseData(typeBean,bean,mView.getTypeNow(),mView.getIndex(),new ClassicCaseListModelImpl.GetClassicCaseDatasCallBack(){

            @Override
            public void onSuccess(List<ClassicCaseBean> strings) {
                mView.addCaseListData(strings);
            }

            @Override
            public void onFailure() {
                mView.addCaseListData(null);
            }
        });
    }
}
