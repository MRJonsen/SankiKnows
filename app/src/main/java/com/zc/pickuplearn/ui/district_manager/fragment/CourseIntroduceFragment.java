package com.zc.pickuplearn.ui.district_manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 课件简介
 */
public class CourseIntroduceFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    Unbinder unbinder;

    private CourseWareBean.DatasBean mParam1;


    public CourseIntroduceFragment() {
        // Required empty public constructor
    }

    public static CourseIntroduceFragment newInstance(CourseWareBean.DatasBean bean) {
        CourseIntroduceFragment fragment = new CourseIntroduceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_course_introduce;
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            mParam1 = (CourseWareBean.DatasBean) getArguments().getSerializable(ARG_PARAM1);
            if (mParam1!=null)
            tvIntroduce.setText(mParam1.getCOURSEXPLAIN());
        }
    }

}
