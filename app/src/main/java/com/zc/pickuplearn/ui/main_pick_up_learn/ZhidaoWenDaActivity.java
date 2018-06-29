package com.zc.pickuplearn.ui.main_pick_up_learn;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.group.view.GroupFragment;
import com.zc.pickuplearn.ui.home_new.view.HomeNewFragment;


/*知道问答界面*/
public class ZhidaoWenDaActivity extends BaseActivity {
    private static String PRAM_ARG = "pram";
    /*** @param context
     */
    public static void starAction(Context context, int index) {

        Intent intent = new Intent(context, ZhidaoWenDaActivity.class);
        intent.putExtra(PRAM_ARG, index);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_zhidao_wen_da;
    }

    @Override
    public void initView() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        switch (getIntent().getIntExtra(PRAM_ARG, 0)) {
            case 0: //  知道问答
                supportFragmentManager.beginTransaction().add(R.id.fl_content, new HomeNewFragment()).commit();
                break;
            case 1: //班组圈
                supportFragmentManager.beginTransaction().add(R.id.fl_content, new GroupFragment()).commit();
                break;
        }
    }

    @Override
    protected void initData() {

    }
}
