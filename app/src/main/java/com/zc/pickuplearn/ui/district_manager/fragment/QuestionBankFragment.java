package com.zc.pickuplearn.ui.district_manager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pitt.library.fresh.FreshDownloadView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.FunctionBean;
import com.zc.pickuplearn.beans.QuestionBankItem2Bean;
import com.zc.pickuplearn.beans.QuestionBankItemBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.ui.district_manager.PracticeSpecificationActivity;
import com.zc.pickuplearn.ui.district_manager.adapter.QuestionBankFunctionAdapter;
import com.zc.pickuplearn.ui.view.MaxRecycleView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionBankFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.pitt)
    FreshDownloadView progressbar;
    @BindView(R.id.tv_question_sum)
    TextView tvQuestionSum;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.mrc_content)
    MaxRecycleView rc_content;
    List<QuestionBankItem2Bean> mData = new ArrayList<>();
    private QuestionBankItemBean.DataBean mParam1;
    private QuestionBankFunctionAdapter questionBankFunctionAdapter;
    private List<QuestionBankItem2Bean> mParam2;

    public QuestionBankFragment() {
    }

    public static QuestionBankFragment newInstance(QuestionBankItemBean.DataBean bean, ArrayList<QuestionBankItem2Bean> bean2) {
        QuestionBankFragment fragment = new QuestionBankFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, bean);
        args.putSerializable(ARG_PARAM2, bean2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_question_bank;
    }


//    public void initContent() {
//        rc_content.setHasFixedSize(true);
//        rc_content.setLayoutManager(new LinearLayoutManager(getmCtx()) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        });
//        rc_content.setItemAnimator(new DefaultItemAnimator());
//        questionBankFunctionAdapter = new QuestionBankFunctionAdapter(getmCtx(), mData);
//        rc_content.setAdapter(questionBankFunctionAdapter);
//    }

    @Override
    public void initView() {

        if (getArguments() != null) {
            mParam1 = (QuestionBankItemBean.DataBean) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = (List<QuestionBankItem2Bean>) getArguments().getSerializable(ARG_PARAM2);
            mData.addAll(mParam2);
            progressbar.startDownload();
            progressbar.upDateProgress(mParam1.getRight_rate());
            tvTime.setText(Double.parseDouble(new DecimalFormat("#.##").format((mParam1.getTime_pass() / 3600f))) + "h");
            tvQuestionSum.setText(mParam1.getDone_num() + "");

            rc_content.setHasFixedSize(true);
            rc_content.setLayoutManager(new LinearLayoutManager(getmCtx()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            rc_content.setFocusableInTouchMode(false);
            rc_content.setItemAnimator(new DefaultItemAnimator());
            questionBankFunctionAdapter = new QuestionBankFunctionAdapter(getmCtx(),mParam1 ,mData);
            rc_content.setAdapter(questionBankFunctionAdapter);
        }

//        API.getInstance().getQuestionBankFunctionList(new CommonCallBack<List<FunctionBean>>() {
//            @Override
//            public void onSuccess(List<FunctionBean> functionBeanList) {
//
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });
    }

    @OnClick({R.id.cv_practice_specification, R.id.cv_my_practice, R.id.cv_chapter_practice, R.id.cv_mock_test, R.id.cv_random_practice, R.id.cv_hard_question
            , R.id.my_note, R.id.my_record, R.id.my_collection, R.id.my_error})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_practice_specification://实操练习
                API.getQuestionBankPracticalNorm("PracticalNorm", "module_code", mParam1.getModule_code(), new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        WebViewActivity.skip(getmCtx(), s, "", false, false);
                    }

                    @Override
                    public void onFailure() {

                    }
                });
//                PracticeSpecificationActivity.open(getmCtx());
                break;
            case R.id.cv_my_practice://每日一练
                startDetail("dailyPractice");
                break;
            case R.id.cv_chapter_practice://章节练习
                startDetail("chapterPractice");
                break;
            case R.id.cv_mock_test://模拟考试
                startDetail("mockExam");
                break;
            case R.id.cv_random_practice://随机练习icon_play.png
                startDetail("randomPractice");
                break;
            case R.id.cv_hard_question://难题攻克
                startDetail("problemTake");
                break;
            case R.id.my_note://我的笔记
                startDetail("myNotes");
                break;
            case R.id.my_record://我的练习
                startDetail("exerciseRecord");
                break;
            case R.id.my_collection://我的收藏
                startDetail("myCollect");
                break;
            case R.id.my_error://我的错题
                startDetail("errorSubject");
                break;

        }
    }


    public void startDetail(String methon) {
        API.getQuestionBankDetail(methon, mParam1.getModule_id(), new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                WebViewActivity.skip(getmCtx(), s, "", false, true);
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
