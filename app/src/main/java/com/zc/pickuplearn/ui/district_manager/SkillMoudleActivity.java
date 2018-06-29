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

import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBankItem2Bean;
import com.zc.pickuplearn.beans.QuestionBankItemBean;
import com.zc.pickuplearn.beans.SkillMoudleItemBean;
import com.zc.pickuplearn.beans.SkillMoudleLearnBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.Common2CallBack;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.district_manager.fragment.QuestionBankFragment;
import com.zc.pickuplearn.ui.district_manager.fragment.SkillMoudleFragment;
import com.zc.pickuplearn.utils.LogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 能力模块学习
 */

public class SkillMoudleActivity extends BaseActivity {


    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
//    @BindView(R.id.ctl_indicator)
//    SlidingTabLayout ctlIndicator;
    @BindView(R.id.vp_home_content)
    ViewPager mViewPage;
    @BindView(R.id.root)
    LinearLayout root;
    private String abilitytypecode;
    private String abilitytype;
    private SkillMoudleLearnBean skillMoudleLearnBean;


    public static void open(Context context,String ABILITYTYPECODE,String abilitytype,SkillMoudleLearnBean skillMoudleLearnBean) {
        Intent intent = new Intent(context, SkillMoudleActivity.class);
        intent.putExtra("ABILITYTYPECODE",ABILITYTYPECODE);
        intent.putExtra("ABILITYTYPE",abilitytype);
        intent.putExtra("skillMoudleLearnBean",skillMoudleLearnBean);
        context.startActivity(intent);
    }

    SparseArray<Fragment> mFragment = new SparseArray<>();//存放fragment
    List<String> title = new ArrayList<>();

    @Override
    public int setLayout() {
        return R.layout.activity_skill_moudle;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initVariables() {
        super.initVariables();
        abilitytypecode = getIntent().getStringExtra("ABILITYTYPECODE");
        abilitytype = getIntent().getStringExtra("ABILITYTYPE");
        skillMoudleLearnBean = (SkillMoudleLearnBean) getIntent().getSerializableExtra("skillMoudleLearnBean");
    }

    @Override
    public void initView() {
        initStatusBar(root);
        initToolBar();
        initFragment();
    }

    public void initFragment() {
        API.getInstance().getSkillMoudleData(skillMoudleLearnBean, new Common2CallBack<List<SkillMoudleItemBean>, List<QuestionBankItem2Bean>>() {
            /**
             * @param questionBankItem2Been
             */
            @Override
            public void onSuccess(List<SkillMoudleItemBean> dataBeen, List<QuestionBankItem2Bean> questionBankItem2Been) {
                ArrayList<QuestionBankItem2Bean> objects = new ArrayList<>();
                ArrayList<SkillMoudleItemBean> objects1 = new ArrayList<>();
                objects.addAll(questionBankItem2Been);
                objects1.addAll(dataBeen);
                mFragment.put(0, SkillMoudleFragment.newInstance(objects1,objects,abilitytypecode));
                mViewPage.setAdapter(new PageAdapter(getSupportFragmentManager(), mFragment));
            }

            @Override
            public void onFailure() {

            }
        });

//        API.getInstance().practiceQuestionBank(new Common2CallBack<List<QuestionBankItemBean.DataBean>,List<QuestionBankItem2Bean>>() {
//            @Override
//            public void onSuccess(List<QuestionBankItemBean.DataBean> dataBeen,List<QuestionBankItem2Bean> date) {
//                for (int i = 0; i < dataBeen.size(); i++) {
//                    LogUtils.e(dataBeen.get(i).getModule_name());
//                    title.add(dataBeen.get(i).getModule_name());
//                    ArrayList<QuestionBankItem2Bean> objects = new ArrayList<>();
//                    objects.addAll(date);
//                    mFragment.put(i, QuestionBankFragment.newInstance(dataBeen.get(i),objects));
//                }
//                mViewPage.setOffscreenPageLimit(2);//ViewPage设置缓存的页面个数解决后面数据加载重复问题
//                mViewPage.setAdapter(new PageAdapter(getSupportFragmentManager(), mFragment));
//            }
//
//            @Override
//            public void onFailure() {
//            }
//        });
    }

    private void initToolBar() {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        tvTitle.setText(abilitytype);
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

    }
}
