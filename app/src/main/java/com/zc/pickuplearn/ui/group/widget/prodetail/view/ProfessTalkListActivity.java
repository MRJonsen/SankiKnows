package com.zc.pickuplearn.ui.group.widget.prodetail.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.im.chatting.ConversationListFragment;
import com.zc.pickuplearn.ui.view.HeadView;

import butterknife.BindView;

public class ProfessTalkListActivity extends BaseActivity {

    @BindView(R.id.hv_head)
    HeadView hvHead;

    public static void startProfessorttalkActivity(Context context) {
        Intent intent = new Intent(context, ProfessTalkListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_profess_talk_list;
    }

    @Override
    public void initView() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().replace(R.id.fl_main, ConversationListFragment.newInstance(ConversationListFragment.ListType.SINGLE), null).commit();
        hvHead.setTitle("会话");
    }

    @Override
    protected void initData() {
    }

}
