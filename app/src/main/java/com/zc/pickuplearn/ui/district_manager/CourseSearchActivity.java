package com.zc.pickuplearn.ui.district_manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.district_manager.fragment.CoursewareRecommendFragment;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.SearchActivity;
import com.zc.pickuplearn.ui.question_and_answer.TypeQuestion;
import com.zc.pickuplearn.ui.view.ClearEditText;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.StatusBarUtil;

public class CourseSearchActivity extends BaseActivity {

    private ClearEditText mSearchEditext;
    private CoursewareRecommendFragment fragment;
    private final static String PARAM = SearchActivity.class.getSimpleName();

    public static void open(Context mContext) {
        Intent intent = new Intent(mContext, CourseSearchActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_course_search;
    }

    @Override
    protected void initData() {
        //处理状态栏及间距
        StatusBarUtil.darkMode(this);
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.ll_main));
        //搜索框
        mSearchEditext = (ClearEditText) findViewById(R.id.et_search);
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        mSearchEditext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.getId() == R.id.et_search && EditorInfo.IME_ACTION_SEARCH == actionId) {
                    String searchtext = mSearchEditext.getText().toString();
                    KeyBoardUtils.closeKeybord(mSearchEditext, CourseSearchActivity.this);
                    if (fragment != null && !TextUtils.isEmpty(searchtext)) {

                        fragment.setSearchText(searchtext);
                    }
//                    mSearchEditext.setText("");
//                    DynamicMoreActivity.startDynamicMoreActivity(SearchActivity.this, WenDaFragment.THE_SEARCH, null, searchtext);
//                    finish();
                    return true;
                }
                return false;
            }
        });
        CoursewareRecommendFragment.RecommendBean recommendBean = new CoursewareRecommendFragment.RecommendBean();
        recommendBean.setType(CoursewareRecommendFragment.Type.Search);
        fragment = CoursewareRecommendFragment.newInstance(recommendBean,"");
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, fragment).commit();
    }

    @Override
    public void initView() {

    }
}
