package com.zc.pickuplearn.ui.district_manager.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pitt.library.fresh.FreshDownloadView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBankItem2Bean;
import com.zc.pickuplearn.beans.QuestionBankItemBean;
import com.zc.pickuplearn.beans.SkillMoudleItemBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.ui.district_manager.adapter.QuestionBankFunction2Adapter;
import com.zc.pickuplearn.ui.view.MaxRecycleView;
import com.zc.pickuplearn.ui.view.MyFreshDownloadView;
import com.zc.pickuplearn.utils.LogUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SkillMoudleFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    @BindView(R.id.pitt)
    MyFreshDownloadView progressbar;
    @BindView(R.id.pitt1)
    FreshDownloadView progressbar1;

    @BindView(R.id.mrc_content)
    MaxRecycleView rc_content;
    List<QuestionBankItem2Bean> mData = new ArrayList<>();
    private List<SkillMoudleItemBean> mParam1;
    private QuestionBankFunction2Adapter questionBankFunctionAdapter;
    private List<QuestionBankItem2Bean> mParam2;
    private String module_id;
    private String abilitycode;

    public SkillMoudleFragment() {
    }

    public static SkillMoudleFragment newInstance(ArrayList<SkillMoudleItemBean> bean, ArrayList<QuestionBankItem2Bean> bean2,String abilitycode) {
        SkillMoudleFragment fragment = new SkillMoudleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, bean);
        args.putSerializable(ARG_PARAM2, bean2);
        args.putSerializable(ARG_PARAM3, abilitycode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_skill_moudle;
    }


    @Override
    public void initView() {

        if (getArguments() != null) {
            mParam1 = (List<SkillMoudleItemBean>) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = (List<QuestionBankItem2Bean>) getArguments().getSerializable(ARG_PARAM2);
            abilitycode = getArguments().getString(ARG_PARAM3);
            setProgressbar("0");
            setProgressbar1("0");
            if (mParam1!=null&&mParam1.size()>0){
                module_id = mParam1.get(0).getModule_id();

                if (mParam1.size()==1){
                    setProgressbar(mParam1.get(0).getScore());
                }else {
                    setProgressbar(mParam1.get(0).getScore());
                    setProgressbar1(mParam1.get(1).getScore());
                }


            }


//            tvTime.setText(Double.parseDouble(new DecimalFormat("#.##").format((mParam1.getTime_pass() / 3600f))) + "h");
//            tvQuestionSum.setText(mParam1.getDone_num() + "");

            mData.addAll(mParam2);
            rc_content.setHasFixedSize(true);
            rc_content.setLayoutManager(new LinearLayoutManager(getmCtx()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            rc_content.setFocusableInTouchMode(false);
            rc_content.setItemAnimator(new DefaultItemAnimator());
            questionBankFunctionAdapter = new QuestionBankFunction2Adapter(getmCtx(),module_id,abilitycode,mData);
            rc_content.setAdapter(questionBankFunctionAdapter);
        }
    }

    /**
     * 第一个进度框
     * @param progress
     */
    private void setProgressbar(String progress) {
        if (TextUtils.isEmpty(progress)){
            progress ="0";
        }

        progressbar.startDownload();
        LogUtils.e(Float.parseFloat(progress)+"");
        progressbar.upDateProgress(Float.parseFloat(progress)/100);
        if (Float.parseFloat(progress)<80){
            progressbar.setProgressColor(getResources().getColor(R.color.jmui_send_press_color));
        }else {
            progressbar.setProgressColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    /**
     * 第二个进度框
     * @param progress
     */
    private void setProgressbar1(String progress) {
        if (TextUtils.isEmpty(progress)){
            progress ="0";
        }
        LogUtils.e(Float.parseFloat(progress)+"");
        progressbar1.startDownload();
        progressbar1.upDateProgress(Float.parseFloat(progress)/100);
        if (Float.parseFloat(progress)<80){
            progressbar1.setProgressColor(getResources().getColor(R.color.jmui_send_press_color));
        }else {
            progressbar1.setProgressColor(getResources().getColor(R.color.colorPrimary));
        }
    }
    @OnClick({R.id.cv_practice_specification, R.id.cv_my_practice, R.id.cv_chapter_practice, R.id.cv_mock_test, R.id.cv_random_practice, R.id.cv_hard_question
            , R.id.my_note, R.id.my_record, R.id.my_collection, R.id.my_error})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_practice_specification://实操练习
//                API.getQuestionBankPracticalNorm("PracticalNorm", "module_code", mParam1.getModule_code(), new CommonCallBack<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        WebViewActivity.skip(getmCtx(), s, "", false, false);
//                    }
//
//                    @Override
//                    public void onFailure() {
//
//                    }
//                });
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
        API.getQuestionBankQuery(methon, module_id, abilitycode,new CommonCallBack<String>() {
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
