package com.zc.pickuplearn.ui.mine.mine.widget.myanswer.presenter;

import com.zc.pickuplearn.beans.AnswerBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view.IWenDaView;
import com.zc.pickuplearn.ui.mine.mine.widget.myanswer.model.ImplMyAnswerModel;
import com.zc.pickuplearn.utils.LogUtils;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/12 12:54
 * 联系方式：chenbin252@163.com
 */

public class ImplMyAnserPresenter implements IMyAnswerPresenter {
    private IWenDaView mView;
    private ImplMyAnswerModel model;

    public ImplMyAnserPresenter(IWenDaView view) {
        mView = view;
        model = new ImplMyAnswerModel();
    }


    @Override
    public void loadDynamicDatas(List<AnswerBean> beanList) {
        String searchString = mView.getSearchString();
        mView.clearSearchString();
        model.getDynamicDatas(searchString, mView.getIndex(), beanList, new ImplMyAnswerModel.GetDynamicDatasCallBack() {
            @Override
            public void onSuccess(List<QuestionBean> dynamic_data) {
//                LogUtils.e("我的回答问题"+dynamic_data.size());
                if (mView.getIndex() == 0) {
                    mView.disShowRefreshView();
                }
                mView.showEmptyView(false);
                mView.addData(dynamic_data);
            }

            @Override
            public void onFailure() {
                if (mView.getIndex() == 0) {
                    mView.showEmptyView(true);
                    mView.disShowRefreshView();
                }
            }
        });
    }

    @Override
    public void loadMyAnswerData() {
        model.getAnswerData(new ImplMyAnswerModel.GetAnswerDatasCallBack() {
            @Override
            public void onSuccess(List<AnswerBean> dynamic_data) {
                LogUtils.e("我的回答"+dynamic_data.size());
                loadDynamicDatas(dynamic_data);
            }

            @Override
            public void onFailure() {
                mView.disShowRefreshView();
            }
        });
    }
}
