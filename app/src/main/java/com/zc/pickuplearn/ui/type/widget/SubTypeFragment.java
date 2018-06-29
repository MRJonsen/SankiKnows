package com.zc.pickuplearn.ui.type.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.MultiItemTypeAdapter;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.event.ClassicCaseTypeEvent;
import com.zc.pickuplearn.event.ExpertTypeEvent;
import com.zc.pickuplearn.event.GetTypeEvent;
import com.zc.pickuplearn.event.IndustryTypeEvent;
import com.zc.pickuplearn.ui.askquestion.widget.AskQuestionEvent;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;
import com.zc.pickuplearn.ui.classiccase.model.ClassCaseTypeEvent;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.DynamicMoreActivity;
import com.zc.pickuplearn.ui.question.ChangeQustionEvent;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.CreatTeamCircleTypeEvent;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.IndustryStandardTypeEvent;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;
import com.zc.pickuplearn.ui.view.MaxRecycleView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class SubTypeFragment extends Fragment {

    private MaxRecycleView rcContent;
    private SubTypeAdapter adapter;
    private Type type;
    private static final String TAG = "ProTypeFragment";
    private static final String TAG2 = "ProTypeFragmentFunction";
    private ArrayList<QusetionTypeBean> datalist;
    private TypeGreenDaoEvent function;

    public static SubTypeFragment newInstance(ArrayList<QusetionTypeBean> list, TypeGreenDaoEvent event) {
        SubTypeFragment fragment = new SubTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TAG, list);
        bundle.putSerializable(TAG2, event);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pro_sub_type, null);

        rcContent = (MaxRecycleView) view.findViewById(R.id.listView);
        datalist = getArguments().getParcelableArrayList(TAG);
        function = (TypeGreenDaoEvent) getArguments().getSerializable(TAG2);

        rcContent.setHasFixedSize(true);
        rcContent.setLayoutManager(new LinearLayoutManager(getContext()));
        rcContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画

        adapter = new SubTypeAdapter(getActivity(), datalist);
        rcContent.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                QusetionTypeBean bean = datalist.get(position);
                QuestionBean questionBean = new QuestionBean();
                questionBean.setQUESTIONTYPECODE(bean.getCODE());
                questionBean.setQUESTIONTYPENAME(bean.getNAME());
                if (function == TypeGreenDaoEvent.INDUSTRY) {
                    IndustryTypeEvent classCaseTypeEvent = new IndustryTypeEvent(questionBean);
                    EventBus.getDefault().post(classCaseTypeEvent);
                } else if (function == TypeGreenDaoEvent.CASE) {
                    ClassicCaseTypeEvent classCaseTypeEvent = new ClassicCaseTypeEvent(questionBean);
                    EventBus.getDefault().post(classCaseTypeEvent);
                } else if (function == TypeGreenDaoEvent.EXPERT) {
                    ExpertTypeEvent classCaseTypeEvent = new ExpertTypeEvent(questionBean);
                    EventBus.getDefault().post(classCaseTypeEvent);
                } else if (function == TypeGreenDaoEvent.Type) {
                    EventBus.getDefault().post(new GetTypeEvent(questionBean));
                } else if (function == TypeGreenDaoEvent.HomeCategory) {
                    DynamicMoreActivity.startDynamicMoreActivity(getContext(), "", questionBean, "");//根据分类去找找问题
                } else if (function == TypeGreenDaoEvent.ChangeQuestion) {
                    ChangeQustionEvent changeQustionEvent = new ChangeQustionEvent();
                    changeQustionEvent.setQuestionBean(questionBean);
                    EventBus.getDefault().post(changeQustionEvent);
                } else if (function == TypeGreenDaoEvent.AskQuestion) {
                    AskQuestionEvent askQustionEvent = new AskQuestionEvent();
                    askQustionEvent.setQuestionBean(questionBean);
                    EventBus.getDefault().post(askQustionEvent);
                } else if (function == TypeGreenDaoEvent.TeamCreate) {
                    CreatTeamCircleTypeEvent creatTeamCircleTypeEvent = new CreatTeamCircleTypeEvent();
                    creatTeamCircleTypeEvent.setQuestionBean(bean);
                    EventBus.getDefault().post(creatTeamCircleTypeEvent);
                } else if (function == TypeGreenDaoEvent.IndustryStandard) {
                    IndustryStandardTypeEvent industryStandardTypeEvent = new IndustryStandardTypeEvent();
                    industryStandardTypeEvent.setQuestionBean(bean);
                    EventBus.getDefault().post(industryStandardTypeEvent);
                } else if (function == TypeGreenDaoEvent.ClassicCase) {
                    ClassCaseTypeEvent classCaseTypeEvent = new ClassCaseTypeEvent();
                    classCaseTypeEvent.setQuestionBean(bean);
                    EventBus.getDefault().post(classCaseTypeEvent);
                }
                EventBus.getDefault().post(ScrollGridActivity.CLOSE);//发送消息 关闭分类树界面
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        return view;
    }
}
