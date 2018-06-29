package com.zc.pickuplearn.ui.teamcircle.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.application.JChatDemoApplication;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.base.EventBusActivity;
import com.zc.pickuplearn.ui.base.WebViewFragment;
import com.zc.pickuplearn.ui.im.chatting.ChatActivity;
import com.zc.pickuplearn.ui.question.FunctionType;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.QuitTeamEvent;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.RefreshEvent;
import com.zc.pickuplearn.ui.teamcircle.adapter.TeamDynamicBean;
import com.zc.pickuplearn.ui.teamcircle.question.TeamAskQuestionActivity;
import com.zc.pickuplearn.ui.view.MyRadioButton;
import com.zc.pickuplearn.utils.LogUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamDetailActivity extends EventBusActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_edit_team)
    ImageButton ibEditTeam;
    @BindView(R.id.ib_edit_search)
    ImageButton ibEditSearch;
    @BindView(R.id.ib_edit_ask)
    ImageButton ibEditAsk;
    @BindView(R.id.ib_communication)
    ImageButton ibCommunication;
    @BindView((R.id.wenyiwen))
    MyRadioButton mWenyiwen;
    @BindView((R.id.biyibi))
    MyRadioButton mBiyibi;
    @BindView((R.id.liaoyiliao))
    MyRadioButton mLiaoyiliao;
    @BindView((R.id.lianyilian))
    MyRadioButton mLianyilian;
    private FragmentManager fragmentManager;
    private BaseFragment current_fragment;
    private final static String TAG = "TeamDetailActivity";
    private final static String TAG_INDEX = "TeamDetailActivity_index";
    private TeamCircleBean teamcirclebean;
    private TeamWenDaFragment.TeamWenDaType teamWenDaType;
    SparseArray<BaseFragment> mFragment = new SparseArray<>();
    private int chooseCheckButton = 0;
    public static void starTeamDetailActivity(Context context, TeamCircleBean teamCircleBean, TeamWenDaFragment.TeamWenDaType type) {
        Intent intent = new Intent(context, TeamDetailActivity.class);
        intent.putExtra(TAG_INDEX, type);
        intent.putExtra(TAG, teamCircleBean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_team_detail;
    }

    @Override
    public void initView() {
        fragmentManager = getSupportFragmentManager();
        teamcirclebean = (TeamCircleBean) getIntent().getSerializableExtra(TAG);
        teamWenDaType = (TeamWenDaFragment.TeamWenDaType) getIntent().getSerializableExtra(TAG_INDEX);
        tvTitle.setText(teamcirclebean.getTEAMCIRCLENAME());
    }

    @Override
    protected void initData() {
        changeFragment(0);
        mWenyiwen.setChecked(true);
    }

    public void changeFragment(int index) {
        BaseFragment fragment = createFragment(index);
        if (current_fragment != null) {
            if (!fragment.isAdded()) {
                fragmentManager.beginTransaction().hide(current_fragment).add(R.id.fl_team_detail_content, fragment).commit();
            } else {
                fragmentManager.beginTransaction().hide(current_fragment).show(fragment).commit();
            }
        } else {
            fragmentManager.beginTransaction().add(R.id.fl_team_detail_content, fragment).commit();
        }
        current_fragment = fragment;
    }

    public boolean getWebViewNeedAlert() {
        LogUtils.e("访问web");
        if (current_fragment != null && current_fragment instanceof TeamTestLibraryMainFragment) {
            TeamTestLibraryMainFragment current_fragment = (TeamTestLibraryMainFragment) this.current_fragment;
            String url = current_fragment.getWebFragment().getWebView().getUrl();
            LogUtils.e("url" + url);
            ///ecm/mobile/MobileExamCaseStep.jspx
            if (url.contains("/ecm_grow/mobile/MobileExamCaseStepDailyPractice.jspx")||url.contains("/ecm_grow/mobile/MobileExamCaseStepTeamTest.jspx")) {
                if (url.contains("javasscriptss:tiaozhuang")) {
                        return true;
                } else {
                    current_fragment.getWebFragment().getWebView().loadUrl("javascript:dropOut()");
                    return false;
                }
            }
        }
        return true;
    }

    @OnClick({R.id.iv_back, R.id.ib_edit_team, R.id.ib_edit_search, R.id.ib_edit_ask, R.id.ib_communication, R.id.wenyiwen, R.id.lianyilian, R.id.biyibi, R.id.liaoyiliao})
    public void onClick(View view) {
        if (!getWebViewNeedAlert()){
            mWenyiwen.setChecked(false);
            mBiyibi.setChecked(false);
            mLianyilian.setChecked(false);
            switch (chooseCheckButton){
                case 0:
                    mWenyiwen.setChecked(true);
                    break;
                case 1:
                    mLianyilian.setChecked(true);
                    break;
                case 2:
                    mBiyibi.setChecked(true);
                    break;
            }
            return;
        }
        switch (view.getId()) {
            case R.id.iv_back:
                if (current_fragment != null && current_fragment instanceof WebViewFragment) {
                    WebViewFragment current_fragment = (WebViewFragment) this.current_fragment;
                    current_fragment.webBack();
                } else {
                    finish();
                }
                break;
            case R.id.ib_edit_team:
                TeamInfoEditActivity.startTeamEditInfoActivity(this, teamcirclebean);
                break;
            case R.id.ib_edit_search:
                TeamQusQustionSearchActivity.startTeamQusQustionSearchActivity(this, teamcirclebean);
                break;
            case R.id.ib_edit_ask:
                TeamAskQuestionActivity.startTeamAskQuestionActivity(this, null, teamcirclebean, FunctionType.TEAM_QUESTION_ASK);
                break;
            case R.id.ib_communication:

                enterGroupChat();
                break;
            case R.id.wenyiwen:
                chooseCheckButton = 0 ;
                mWenyiwen.setChecked(true);
                mLianyilian.setChecked(false);
                mBiyibi.setChecked(false);
                changeFragment(0);
                break;
            case R.id.lianyilian:
                chooseCheckButton = 1;
                mWenyiwen.setChecked(false);
                mLianyilian.setChecked(true);
                mBiyibi.setChecked(false);
                changeFragment(1);
                break;
            case R.id.biyibi:
                chooseCheckButton = 2;
                changeFragment(2);
                mWenyiwen.setChecked(false);
                mLianyilian.setChecked(false);
                mBiyibi.setChecked(true);
                break;
            case R.id.liaoyiliao:
                enterGroupChat();
                break;
        }
    }

    private void enterGroupChat() {
        Intent intent = new Intent();
        intent.putExtra(JChatDemoApplication.GROUP_ID, Long.parseLong(teamcirclebean.getJMESSAGEGROUPID()));
        intent.putExtra(JChatDemoApplication.CONV_TITLE, teamcirclebean.getTEAMCIRCLENAME());
        intent.setClass(TeamDetailActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    /**
     * 监听 回答 和提问 成功刷新监听
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
    }

    private boolean IS_FIRST_IN = true;//记录是不是第一次进入

    @Override
    protected void onResume() {
        super.onResume();
        if (IS_FIRST_IN) {
            IS_FIRST_IN = false;
        } else {
//            LogUtils.e("重新哪去数据");
            if (teamcirclebean != null) {
                TeamDynamicBean teamDynamicBean = new TeamDynamicBean();
                teamDynamicBean.setTEAMCIRCLECODE(teamcirclebean.getSEQKEY());
                API.getAnMyJoinTeamList(teamDynamicBean, new CommonCallBack<List<TeamCircleBean>>() {
                    @Override
                    public void onSuccess(List<TeamCircleBean> teamCircleBeen) {
                        if (teamCircleBeen != null && teamCircleBeen.size() > 0) {
                            TeamCircleBean teamCircleBean = teamCircleBeen.get(0);
                            teamCircleBean.setSEQKEY(teamCircleBean.getTEAMCODE());
                            teamcirclebean = teamCircleBean;
                            tvTitle.setText(teamcirclebean.getTEAMCIRCLENAME());
                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (current_fragment instanceof WebViewFragment && ((WebViewFragment) current_fragment).onKeyDown(keyCode, event)) {
                    return true;//处理web页面回退
                }
                break;
            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 接收退出团队的时间 关闭当前页面
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(QuitTeamEvent event) {
        if (event != null) {
            finish();
        }
    }

    public BaseFragment createFragment(int position) {
        BaseFragment baseFragment;
        baseFragment = mFragment.get(position);
        if (baseFragment != null) {
            //根据索引获取到了fragment对象,直接返回即可
            return baseFragment;
        } else {
            //在没有此索引指向fragment的时候创建逻辑
            switch (position) {
                case 0:
                    baseFragment = TeamDetailMainFragment.newInstance(teamcirclebean, teamWenDaType);
                    break;
                case 1:
                    baseFragment = TeamTestLibraryMainFragment.newInstance(teamcirclebean);
                    break;
                case 2:
                    baseFragment = TeamCompareFragment.newInstance(teamcirclebean);
                    break;
                case 3:
                    break;
                default:
                    baseFragment = null;
                    break;
            }
            mFragment.put(position, baseFragment);
            return baseFragment;
        }
    }
}
