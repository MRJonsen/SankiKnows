package com.zc.pickuplearn.ui.group.widget.professional.presenter;

import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.beans.ProfessionalCircleRealUserBean;
import com.zc.pickuplearn.ui.group.widget.professional.model.ProfessionModelImpl;
import com.zc.pickuplearn.ui.group.widget.professional.view.IProfessionView;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/1/4 10:18
 * 联系方式：chenbin252@163.com
 */

public class ProfessionPresenterImpl implements IProfessionPresenter {
    private IProfessionView mView;
    private final ProfessionModelImpl model;

    public ProfessionPresenterImpl(IProfessionView view) {
        mView = view;
        model = new ProfessionModelImpl();
    }

    @Override
    public void getAllProfessionalData() {
        model.getProfessionalCircleData(new ProfessionModelImpl.AllProCircleCallBack() {
            @Override
            public void onSuccess(List<ProfessionalCircleBean> professionalCircleBeen) {
                mView.addData(professionalCircleBeen);
                getMyJoinProfessionalData(professionalCircleBeen);
                mView.disShowRefresh();
            }

            @Override
            public void onFail() {
                mView.disShowRefresh();
            }
        });
    }

    @Override
    public void getMyJoinProfessionalData(final List<ProfessionalCircleBean> professionalCircleBeen) {
        model.getMyProfessionalCircleData(new ProfessionModelImpl.MyProCircleCallBack() {
            @Override
            public void onSuccess(List<ProfessionalCircleRealUserBean> professionalCircleRealUserBeen) {
                for (ProfessionalCircleBean bean :
                        professionalCircleBeen) {
                    for (ProfessionalCircleRealUserBean user :
                            professionalCircleRealUserBeen) {
                        if (bean.getPROCIRCLECODE().equals(user.getPROCIRCLECODE())) {
                            bean.setISJOIN(true);//标记已经加入该圈子
                        }

                    }
                }
                mView.addData(professionalCircleBeen);
                mView.disShowRefresh();
            }

            @Override
            public void onFail() {
                mView.disShowRefresh();
            }
        });
    }
}
