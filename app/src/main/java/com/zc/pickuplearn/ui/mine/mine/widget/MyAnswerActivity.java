package com.zc.pickuplearn.ui.mine.mine.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.mine.mine.widget.myanswer.view.MyAnswerFragment;
import com.zc.pickuplearn.ui.view.HeadView;

import butterknife.BindView;

public class MyAnswerActivity extends BaseActivity {
    @BindView(R.id.hv_headbar)
    HeadView mHeadView;// 标题栏
    private FragmentManager mFM;

    private MyAnswerFragment myAnswerFragment;


    public void initView() {
        mHeadView.setTitle(MyAnswerFragment.MY_ANSWER);
        mFM = getSupportFragmentManager();
        myAnswerFragment = MyAnswerFragment.newInstance(MyAnswerFragment.MY_ANSWER);// 我的回答
    }

    public void initData() {
        changeFragment(mFM,myAnswerFragment);
    }

    private void changeFragment(FragmentManager fm,Fragment fragment) {
        fm.beginTransaction().replace(R.id.fl_my_answer_main, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public int setLayout() {
        return R.layout.activity_my_answer;
    }

}
