package com.zc.pickuplearn.ui.dynamic.widget.recANDnew.presenter;

import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.model.ImplWenDaModel;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view.IWenDaView;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/10 15:58
 * 联系方式：chenbin252@163.com
 */

public class ImplWenDaPresenter implements IWenDaPresenter {
    private IWenDaView mView;
    private ImplWenDaModel model;
    public ImplWenDaPresenter(IWenDaView view) {
        mView = view;
        model = new ImplWenDaModel();
    }



    @Override
    public void loadDynamicDatas(String from,boolean needmore,QuestionBean bean) {
        String searchString = mView.getSearchString();
        mView.clearSearchString();
        model.getDynamicDatas(searchString,mView.getIndex(),from,needmore,bean,new ImplWenDaModel.GetDynamicDatasCallBack(){

            @Override
            public void onSuccess(List<QuestionBean> dynamic_data) {
                if (mView.getIndex()==0){
                    mView.disShowRefreshView();
                }
                mView.showEmptyView(false);
                mView.addData(dynamic_data);
            }

            @Override
            public void onFailure() {
                if (mView.getIndex()==0){
                    mView.disShowRefreshView();
                    mView.showEmptyView(true);
                }
            }
        });
    }
}
