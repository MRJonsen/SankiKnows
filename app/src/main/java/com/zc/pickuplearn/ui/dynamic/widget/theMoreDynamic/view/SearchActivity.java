package com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.event.GetTypeEvent;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view.WenDaFragment;
import com.zc.pickuplearn.ui.question_and_answer.DynamicType;
import com.zc.pickuplearn.ui.question_and_answer.QuestionListFragment;
import com.zc.pickuplearn.ui.question_and_answer.TypeQuestion;
import com.zc.pickuplearn.ui.type.view.PopScrollGridActivity;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;
import com.zc.pickuplearn.ui.view.ClearEditText;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends EventBusActivity {


    private ClearEditText mSearchEditext;
    private QuestionListFragment fragment;
    private final static String PARAM = SearchActivity.class.getSimpleName();
    private final static String PARAM_1 = SearchActivity.class.getSimpleName()+"_1";
    private TypeQuestion typeQuestion;
    private UserBean userBean;

    public static void starAction(Context mContext, TypeQuestion typeQuestion, UserBean userBean) {

        Intent intent = new Intent(mContext, SearchActivity.class);
        intent.putExtra(PARAM,typeQuestion);
        intent.putExtra(PARAM_1,userBean);
        mContext.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initVariables() {
        super.initVariables();
        typeQuestion = (TypeQuestion) getIntent().getSerializableExtra(PARAM);
        userBean = (UserBean) getIntent().getSerializableExtra(PARAM_1);
    }

    @Override
    public void initView() {
        //处理状态栏及间距
        StatusBarUtil.darkMode(this);
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.ll_main));
        //搜索框
        mSearchEditext = (ClearEditText) findViewById(R.id.et_search);
        findViewById(R.id.ib_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopScrollGridActivity.startScrollGridActivity(SearchActivity.this, TypeGreenDaoEvent.Type);
            }
        });
        switch (typeQuestion){
            case EXPERT:
                findViewById(R.id.ib_type).setVisibility(View.GONE);
                break;
        }
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
                    KeyBoardUtils.closeKeybord(mSearchEditext, SearchActivity.this);
                    if (fragment != null && !TextUtils.isEmpty(searchtext)) {
                        QuestionBean questionBean = new QuestionBean();
                        questionBean.setQUESTIONEXPLAIN(searchtext);
                        fragment.setSearchData(questionBean);
                    }
//                    mSearchEditext.setText("");
//                    DynamicMoreActivity.startDynamicMoreActivity(SearchActivity.this, WenDaFragment.THE_SEARCH, null, searchtext);
//                    finish();
                    return true;
                }
                return false;
            }
        });
        fragment = QuestionListFragment.newInstance(typeQuestion,userBean, DynamicType.Home);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, fragment).commit();
    }

    @Override
    protected void initData() {

    }

    @Subscribe
    public void onEventMainThread(GetTypeEvent event) {
        if (event != null) {
            if (fragment != null)
                fragment.setSearchData(event.getQuestionBean());
            if (mSearchEditext != null)
                mSearchEditext.setText("");
        }
    }

}
