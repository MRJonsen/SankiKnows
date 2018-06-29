package com.zc.pickuplearn.ui.home_new.view;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.question.ChangeQuestionActivity;
import com.zc.pickuplearn.ui.question.FunctionType;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view.WenDaFragment;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.DynamicMoreActivity;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.SearchActivity;
import com.zc.pickuplearn.ui.msgbox.PersonalMsgActivity;
import com.zc.pickuplearn.ui.question_and_answer.TypeQuestion;
import com.zc.pickuplearn.ui.type.view.ScrollGridActivity;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;
import com.zc.pickuplearn.ui.view.ClearEditText;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.LogUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;

public class HomeNewFragment extends BaseFragment {
    @BindView(R.id.et_search)
    ClearEditText mSearchEditext;//搜索框
    @BindView(R.id.bt_search)
    Button mBtSearch;//搜索按钮
    @BindView(R.id.tl_indicator)
    TabLayout mTablayout;
    @BindView(R.id.vp_home_content)
    ViewPager mViewPage;
    @BindView(R.id.tv_mine_new_msg_num)
    TextView tvMyNewMsg;
    SparseArray<BaseFragment> mFragment = new SparseArray<>();//存放fragment
    ArrayList<String> title = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_new;
    }

    @Override
    public void initView() {
        mFragment.put(0, WenDaFragment.newInstance(WenDaFragment.THE_NEW, true, null, ""));
        mFragment.put(1, WenDaFragment.newInstance(WenDaFragment.THE_RECOMMENT, true, null, ""));
        mFragment.put(2, WenDaFragment.newInstance(WenDaFragment.THE_SCORE, true, null, ""));
        title.add("最新");
        title.add("推荐");
        title.add("悬赏");
        mViewPage.setOffscreenPageLimit(2);//ViewPage设置缓存的页面个数解决后面数据加载重复问题
        mViewPage.setAdapter(new DynamicFragmentAdapter(getChildFragmentManager(), mFragment));
        mTablayout.setupWithViewPager(mViewPage);
        mViewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mSearchEditext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(v.getId() == R.id.et_search &&EditorInfo.IME_ACTION_SEARCH == actionId){
                    searchQuestion();
                    return true;
                }
                return false;
            }
        });
    }


    public class DynamicFragmentAdapter extends FragmentPagerAdapter {
        SparseArray<BaseFragment> mData;

        public DynamicFragmentAdapter(FragmentManager fm, SparseArray<BaseFragment> mFragment) {
            super(fm);
            mData = mFragment;
        }

        @Override
        public Fragment getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }

//    @OnEditorAction(R.id.et_search)
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if(v.getId() == R.id.et_search &&EditorInfo.IME_ACTION_SEARCH == actionId){
//            searchQuestion();
//            return true;
//        }
//        return false;
//    }
    /**
     * 初始化搜索条儿
     */
//    @OnTextChanged(value = R.id.et_search, callback = AFTER_TEXT_CHANGED)
//    void afterTextChanged(Editable s) {
//        if (!TextUtils.isEmpty(s.toString())) {
//            if (!mBtSearch.isShown()) {
//                mBtSearch.setVisibility(View.VISIBLE);
//            }
//        } else {
//            mBtSearch.setVisibility(View.GONE);
//        }
//    }

    @OnClick({R.id.ib_classification, R.id.bt_search,R.id.rl_mine_new_msg,R.id.ib_edit_ask,R.id.ib_edit_search,R.id.ib_edit_type,R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_classification:
                ScrollGridActivity.startScrollGridActivity(getmCtx(), TypeGreenDaoEvent.HomeCategory);
                break;
            case R.id.bt_search:
                searchQuestion();
                break;
            case R.id.ib_edit_search:
                SearchActivity.starAction(getmCtx(), TypeQuestion.SEARCH,null);
                break;
            case R.id.rl_mine_new_msg:
                PersonalMsgActivity.startPersonalMsgActivity(getmCtx());
                break;
            case R.id.ib_edit_ask:
                ChangeQuestionActivity.startChangeQuestionActivity(getmCtx(),new QuestionBean(), FunctionType.QUESTION_ASK);
//                AskQuestionActivity.startAskQuestionActivity(getmCtx(), null);
                break;
            case R.id.iv_back:
                ((BaseActivity)getmCtx()).close();
                break;
            case R.id.ib_edit_type:
                ScrollGridActivity.startScrollGridActivity(getmCtx(), TypeGreenDaoEvent.HomeCategory);
                break;
        }
    }

    private void searchQuestion() {
        String searchtext = mSearchEditext.getText().toString();
        KeyBoardUtils.closeKeybord(mBtSearch, getContext());
        mSearchEditext.setText("");
        DynamicMoreActivity.startDynamicMoreActivity(getActivity(), WenDaFragment.THE_SEARCH, null, searchtext);
    }
    public void setMyNewMsg(String msg){
        if (tvMyNewMsg!=null){
            int allUnReadMsgCount = JMessageClient.getAllUnReadMsgCount();
            LogUtils.e("会话消息"+allUnReadMsgCount);
            if (!TextUtils.isEmpty(msg)){
                Integer integer = Integer.valueOf(msg);
                if (integer >0){
                    allUnReadMsgCount=allUnReadMsgCount+integer;
                }
            }
            if (allUnReadMsgCount>0){
                if (allUnReadMsgCount > 99) {
                    tvMyNewMsg.setText("99+");
                } else {
                    tvMyNewMsg.setText(allUnReadMsgCount + "");
                }
                LogUtils.e("消息总数"+allUnReadMsgCount);
                tvMyNewMsg.setVisibility(View.VISIBLE);
            }else {
                tvMyNewMsg.setVisibility(View.GONE);
            }
        }
    }
}
