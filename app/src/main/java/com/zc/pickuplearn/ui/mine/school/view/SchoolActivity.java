package com.zc.pickuplearn.ui.mine.school.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.base.WebViewFragment;
import com.zc.pickuplearn.utils.UIUtils;

import butterknife.BindView;

public class SchoolActivity extends BaseActivity {

    @BindView(R.id.fl_school_main)
    FrameLayout flSchoolMain;
    private FragmentManager fragmentManager;
    private BaseFragment current_fragment;

    public static void startSchoolActivity(Context context){
        Intent intent = new Intent(context, SchoolActivity.class);
        context.startActivity(intent);

    }
    @Override
    public int setLayout() {
        return R.layout.activity_school;
    }

    @Override
    public void initView() {
        fragmentManager = getSupportFragmentManager();

        current_fragment = WebViewFragment.newInstance(UIUtils.getString(R.string.school_url), UIUtils.getString(R.string.tab_tag_school),true);
        fragmentManager.beginTransaction().add(R.id.fl_school_main, current_fragment, "school").commit();
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (current_fragment instanceof WebViewFragment && ((WebViewFragment) current_fragment).onKeyDown(keyCode, event)) {
                    return true;//处理学堂页面回退
                }
                break;
            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
