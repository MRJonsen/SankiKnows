package com.zc.pickuplearn.ui.home.presenter;

import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.ui.home.model.HomeModelImpl;
import com.zc.pickuplearn.ui.home.view.IHomeView;

import java.util.List;

/**
 * home fragment业务处理实现类
 * 作者： Jonsen
 * 时间: 2016/12/5 15:35
 * 联系方式：chenbin252@163.com
 */

public class HomePresenterImpl implements HomePresenter {
    private IHomeView mHomeView;
    private final HomeModelImpl homeModel;

    public HomePresenterImpl(IHomeView homeView) {
        mHomeView = homeView;
        homeModel = new HomeModelImpl();
    }

    @Override
    public void loadClassicCaseDatas() {
        homeModel.getClassicCaseDatas(null,new HomeModelImpl.GetClassicCaseDatasCallBack() {
            @Override
            public void onSuccess(List<ClassicCaseBean> datas) {
                mHomeView.addClassCaseData(datas);
                mHomeView.disShowRefreshProgress();
            }

            @Override
            public void onFailure() {
                mHomeView.disShowRefreshProgress();
            }
        });
    }

    @Override
    public void loadDynamicDatas() {
        String searchText = mHomeView.getSearchText();
        mHomeView.clearSearchString();
        homeModel.getDynamicDatas(searchText, new HomeModelImpl.GetDynamicDatasCallBack() {
            @Override
            public void onSuccess(List<QuestionBean> strings) {
                mHomeView.addDynamicData(strings);
                mHomeView.disShowRefreshProgress();
            }

            @Override
            public void onFailure() {
                mHomeView.disShowRefreshProgress();
            }
        });
    }

    @Override
    public void enterClassicCaseDetail(ClassicCaseBean classicCaseBean) {
        mHomeView.enterClassicCaseDetailView(classicCaseBean);
    }

    @Override
    public void enterClassicCaseList() {
        mHomeView.enterClassicCaseListView();
    }
}
