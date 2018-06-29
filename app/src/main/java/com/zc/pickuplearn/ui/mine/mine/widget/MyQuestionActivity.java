package com.zc.pickuplearn.ui.mine.mine.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.question_and_answer.DynamicType;
import com.zc.pickuplearn.ui.question_and_answer.QuestionListFragment;
import com.zc.pickuplearn.ui.question_and_answer.TypeQuestion;
import com.zc.pickuplearn.ui.view.HeadView;

import butterknife.BindView;

//我的回答 我的提问
public class MyQuestionActivity extends BaseActivity {
    @BindView(R.id.hv_headbar)
    HeadView mHeadView;// 标题栏

    private FragmentManager mFM;
//    private MyAnswerFragment myAnswerFragment;
    private static final String PARAM_1 = "PARAM_1";
    private static final String PARAM_2 = "PARAM_2";
    private UserBean userBean;
    private TypeQuestion typeQuestion;
    private QuestionListFragment questionListFragment;

    public static void open(Context context, TypeQuestion typeQuestion, UserBean userBean) {
        Intent intent = new Intent(context, MyQuestionActivity.class);
        intent.putExtra(PARAM_1, typeQuestion);
        intent.putExtra(PARAM_2, userBean);
        context.startActivity(intent);
    }

    @Override
    public void initVariables() {
        super.initVariables();
        if (getIntent()!=null){
            typeQuestion = (TypeQuestion) getIntent().getSerializableExtra(PARAM_1);
            userBean = (UserBean) getIntent().getSerializableExtra(PARAM_2);
        }
    }

    public void initView() {
        initToolbar();
        mFM = getSupportFragmentManager();
        questionListFragment = QuestionListFragment.newInstance(typeQuestion, userBean, DynamicType.Home);
        changeFragment(mFM, questionListFragment);
//        myAnswerFragment = MyAnswerFragment.newInstance(MyAnswerFragment.MY_QUESTION);// 我的回答
    }

    private void initToolbar() {
        switch (typeQuestion){
            case PERSONQUESTION:
                mHeadView.setTitle("我的提问");
                break;
            case PERSONANSER:
                mHeadView.setTitle("我的回答");
                break;
        }
    }

    public void initData() {

    }

    private void changeFragment(FragmentManager fm, Fragment fragment) {
        fm.beginTransaction().replace(R.id.fl_my_answer_main, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_my_answer;
    }

}
