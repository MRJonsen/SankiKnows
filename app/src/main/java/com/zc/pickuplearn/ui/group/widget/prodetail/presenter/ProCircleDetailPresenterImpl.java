package com.zc.pickuplearn.ui.group.widget.prodetail.presenter;

import com.zc.pickuplearn.beans.CircleRankingBean;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.ui.group.widget.prodetail.model.ProCircleDetailModelImpl;
import com.zc.pickuplearn.ui.group.widget.prodetail.view.IProCircleDetailView;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/4 16:29
 * 联系方式：chenbin252@163.com
 */

public class ProCircleDetailPresenterImpl implements IProCircleDetailPresenter{
    private IProCircleDetailView mView;
    private final ProCircleDetailModelImpl proCircleDetailModel;

    public ProCircleDetailPresenterImpl(IProCircleDetailView view){
        mView = view ;
        proCircleDetailModel = new ProCircleDetailModelImpl();
    }

    @Override
    public void getRankingData(ProfessionalCircleBean bean) {
        proCircleDetailModel.getCircleDetailData(bean,new ProCircleDetailModelImpl.ProCircleDetailCallBack(){

            @Override
            public void onSuccess(List<CircleRankingBean> rankingBeanList) {
                mView.addData(rankingBeanList);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    public void JoinCirclew(final ProfessionalCircleBean bean) {
        proCircleDetailModel.joinCircle(bean,new ProCircleDetailModelImpl.ProCircleJoinCallBack(){

            @Override
            public void onSuccess() {
                bean.setISJOIN(!bean.getISJOIN());
                mView.setJoinButton(bean.getISJOIN());
                EventBus.getDefault().post(bean);//通知圈子分类界面更新
                getRankingData(bean);//更新加入按钮
                ToastUtils.showToast(UIUtils.getContext(),bean.getISJOIN()?"加入圈子成功":"退出圈子成功");
            }

            @Override
            public void onFailure() {
                ToastUtils.showToast(UIUtils.getContext(),"操作失败");
            }
        });
    }
}
