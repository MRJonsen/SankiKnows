package com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.dynamic.widget.recANDnew.view.WenDaFragment;
import com.zc.pickuplearn.ui.view.HeadView;

import butterknife.BindView;

/**
 * 分类更多问题页面
 */
public class DynamicMoreActivity extends BaseActivity {
    private static final String TAG = "DynamicMoreActivity";
    private static final String BEAN_TAG = "questionbean";
    private static final String SEARCH_TEXT = "SEARCH_TEXT";
    private String title;
    private FragmentManager fragmentManager;
    private QuestionBean questionBean;
    private String stringExtra;

    public static void startDynamicMoreActivity(Context context, String title, QuestionBean bean,String searchtext) {
        Intent intent = new Intent(context, DynamicMoreActivity.class);
        intent.putExtra(TAG, title);
        intent.putExtra(BEAN_TAG,bean);
        intent.putExtra(SEARCH_TEXT,searchtext);
        context.startActivity(intent);
    }

    @BindView(R.id.hv_head)
    HeadView headView;
    @BindView(R.id.fl_dynamicMore_content)
    FrameLayout mContent;

    @Override
    public int setLayout() {
        return R.layout.activity_dynamic_more;
    }

    @Override
    public void initView() {
        getParams();
        fragmentManager = getSupportFragmentManager();
        headView.setTitle(title);
        if (questionBean!=null){
            headView.setTitle(questionBean.getQUESTIONTYPENAME());
        }
    }

    @Override
    protected void initData() {
        fragmentManager.beginTransaction().replace(R.id.fl_dynamicMore_content, WenDaFragment.newInstance(title, true,questionBean,stringExtra)).commit();
    }

    /**
     * 获取初始化参数
     */
    public void  getParams() {
        title =  getIntent().getStringExtra(TAG);
        stringExtra = getIntent().getStringExtra(SEARCH_TEXT);
        questionBean = (QuestionBean) getIntent().getSerializableExtra(BEAN_TAG);
    }
}
