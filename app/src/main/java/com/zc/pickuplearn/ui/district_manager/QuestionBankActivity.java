package com.zc.pickuplearn.ui.district_manager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.hss01248.dialog.StyledDialog;
import com.youth.xframe.XFrame;
import com.youth.xframe.common.XActivityStack;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.AbilityBankBean;
import com.zc.pickuplearn.beans.QuestionBankItem2Bean;
import com.zc.pickuplearn.beans.QuestionBankItemBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.Common2CallBack;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.district_manager.fragment.QuestionBankFragment;
import com.zc.pickuplearn.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 题库练习
 */
public class QuestionBankActivity extends BaseActivity {


    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ctl_indicator)
    SlidingTabLayout ctlIndicator;
    @BindView(R.id.vp_home_content)
    ViewPager mViewPage;
    @BindView(R.id.root)
    LinearLayout root;


    public static void open(Context context) {
        Intent intent = new Intent(context, QuestionBankActivity.class);
        context.startActivity(intent);
    }

    SparseArray<Fragment> mFragment = new SparseArray<>();//存放fragment
    List<String> title = new ArrayList<>();

    @Override
    public int setLayout() {
        return R.layout.activity_question_bank;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initView() {
        initStatusBar(root);
        initToolBar();
        initFragment();
    }

    public void initFragment() {
        API.getInstance().practiceQuestionBank(new Common2CallBack<List<QuestionBankItemBean.DataBean>,List<QuestionBankItem2Bean>>() {
            @Override
            public void onSuccess(List<QuestionBankItemBean.DataBean> dataBeen,List<QuestionBankItem2Bean> date) {
                for (int i = 0; i < dataBeen.size(); i++) {
                    LogUtils.e(dataBeen.get(i).getModule_name());
                    title.add(dataBeen.get(i).getModule_name());
                    ArrayList<QuestionBankItem2Bean> objects = new ArrayList<>();
                    objects.addAll(date);
                    mFragment.put(i, QuestionBankFragment.newInstance(dataBeen.get(i),objects));
                }
                mViewPage.setOffscreenPageLimit(2);//ViewPage设置缓存的页面个数解决后面数据加载重复问题
                mViewPage.setAdapter(new PageAdapter(getSupportFragmentManager(), mFragment));
                ctlIndicator.setViewPager(mViewPage);
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private void initToolBar() {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        tvTitle.setText(XFrame.getString(R.string.question_bank_practice));
    }



    private class PageAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> mData;

        PageAdapter(FragmentManager fm, SparseArray<Fragment> mFragment) {
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
}
