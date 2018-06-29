package com.zc.pickuplearn.ui.type.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.event.GetTypeEvent;
import com.zc.pickuplearn.ui.askquestion.widget.AskQuestionEvent;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;
import com.zc.pickuplearn.ui.im.chatting.entity.Event;
import com.zc.pickuplearn.ui.question.ChangeQustionEvent;
import com.zc.pickuplearn.ui.classiccase.model.ClassCaseTypeEvent;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.DynamicMoreActivity;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.CreatTeamCircleTypeEvent;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.IndustryStandardTypeEvent;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ProTypeFragment extends Fragment {

    private GridView gridView;
    private GridViewAdapter adapter;
    private Type type;
    private static final String TAG = "ProTypeFragment";
    private static final String TAG2 = "ProTypeFragmentFunction";
    private ArrayList<QusetionTypeBean> datalist;
    private TypeGreenDaoEvent function;

    public static ProTypeFragment newInstance(ArrayList<QusetionTypeBean> list, TypeGreenDaoEvent event) {
        ProTypeFragment fragment = new ProTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TAG, list);
        bundle.putSerializable(TAG2, event);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pro_type, null);
        gridView = (GridView) view.findViewById(R.id.listView);
        datalist = getArguments().getParcelableArrayList(TAG);
        function = (TypeGreenDaoEvent) getArguments().getSerializable(TAG2);
//		((TextView) view.findViewById(R.id.toptype)).setText(typename);
        adapter = new GridViewAdapter(getActivity(), datalist);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                QusetionTypeBean bean = datalist.get(arg2);
                QuestionBean questionBean = new QuestionBean();
                questionBean.setQUESTIONTYPECODE(bean.getCODE());
                questionBean.setQUESTIONTYPENAME(bean.getNAME());
                if (function==TypeGreenDaoEvent.Type){
                    EventBus.getDefault().post(new GetTypeEvent(questionBean));
                }else if (function == TypeGreenDaoEvent.HomeCategory) {
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
                }else if (function == TypeGreenDaoEvent.ClassicCase){
                    ClassCaseTypeEvent classCaseTypeEvent = new ClassCaseTypeEvent();
                    classCaseTypeEvent.setQuestionBean(bean);
                    EventBus.getDefault().post(classCaseTypeEvent);
                }
                EventBus.getDefault().post(ScrollGridActivity.CLOSE);//发送消息 关闭分类树界面
            }
        });

        return view;
    }
}
