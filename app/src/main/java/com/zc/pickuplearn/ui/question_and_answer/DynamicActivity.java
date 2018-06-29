package com.zc.pickuplearn.ui.question_and_answer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.SearchActivity;
import com.zc.pickuplearn.ui.question.ChangeQuestionActivity;
import com.zc.pickuplearn.ui.question.FunctionType;

import com.zc.pickuplearn.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;
import me.majiajie.pagerbottomtabstrip.LazyViewPager;

/**
 * 问答动态页面
 */
public class DynamicActivity extends BaseActivity {

    private static final  String  PARAM  = DynamicActivity.class.getSimpleName();
    private DynamicType from;

    public static void open(Context context,DynamicType from) {
        Intent intent = new Intent(context, DynamicActivity.class);
        intent.putExtra(PARAM,from);
        context.startActivity(intent);
    }


    @BindView(R.id.vp)
    LazyViewPager vp;
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.stl_segment)
    SegmentTabLayout stl_segment;
    private String[] mTitles = {"最新", "推荐", "悬赏"};

    @Override
    public int setLayout() {
        return R.layout.activity_dynamic;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initVariables() {
        super.initVariables();
        from = (DynamicType) getIntent().getSerializableExtra(PARAM);
    }

    @Override
    public void initView() {
        initStatusBar(root);
        initViewPage();
    }

    private void initViewPage() {
        stl_segment.setTabData(mTitles);
        vp.setAdapter(new DynamicFragmentAdapter(getSupportFragmentManager()));
        stl_segment.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                LogUtils.e("stl_segment+++++++" + position);
                vp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        vp.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.e("VP+++++++" + position);
                stl_segment.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (from==DynamicType.CourseWareHome){

        }
        switch (from){
            case CourseWareDetail:
                vp.setCurrentItem(1);
                break;
            case CourseWareHome:
                vp.setCurrentItem(1);
                break;
        }
//
    }

    @OnClick({R.id.ib_search, R.id.iv_left,R.id.bt_ask})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_search:
                SearchActivity.starAction(this,TypeQuestion.SEARCH,null);
                break;
            case R.id.iv_left:
                close();
                break;
            case R.id.bt_ask:
                ChangeQuestionActivity.startChangeQuestionActivity(this,new QuestionBean(), FunctionType.QUESTION_ASK);
                break;
        }
    }

    private class DynamicFragmentAdapter extends FragmentPagerAdapter {

        DynamicFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.createFragment(position,from);
        }


        @Override
        public int getCount() {
            return mTitles.length;
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        FragmentFactory.clear();
    }
}
