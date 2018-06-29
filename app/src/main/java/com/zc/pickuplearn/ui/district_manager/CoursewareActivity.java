package com.zc.pickuplearn.ui.district_manager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.jiang.android.indicatordialog.IndicatorBuilder;
import com.jiang.android.indicatordialog.IndicatorDialog;
import com.youth.xframe.XFrame;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.MultiItemTypeAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.district_manager.fragment.CourseWareHomeFragment;
import com.zc.pickuplearn.ui.district_manager.fragment.CoursewareRecommendFragment;
import com.zc.pickuplearn.ui.question_and_answer.DynamicActivity;
import com.zc.pickuplearn.ui.question_and_answer.DynamicType;
import com.zc.pickuplearn.utils.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 学习课件
 */
public class CoursewareActivity extends BaseActivity {

    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.viewpager)
    ViewPager mViewPage;
    @BindView(R.id.tl_3)
    SlidingTabLayout tabLayout;
    @BindView(R.id.iv_order)
    ImageView ivOrder;
    @BindView(R.id.ll_order)
    LinearLayout ll_order;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    SparseArray<Fragment> mFragment = new SparseArray<>();//存放fragment
    List<String> title = Arrays.asList("全部", "推荐");
    @BindView(R.id.et_search)
    TextView etSearch;
    private int ORDER_TYPE = 1;
    private static String ABILITYTYPE = "ABILITYTYPE";
    private CoursewareRecommendFragment coursewareRecommendFragment;
    private CourseWareHomeFragment courseWareHomeFragment;
    private String abilitytype;

    public static void open(Context context,String abilitytype) {
        Intent intent = new Intent(context, CoursewareActivity.class);
        intent.putExtra(ABILITYTYPE,abilitytype);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_courseware;
    }

    @Override
    public void initVariables() {
        super.initVariables();
        abilitytype = getIntent().getStringExtra(ABILITYTYPE);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initView() {
        initStatusBar(root);
        initToolBar();
        courseWareHomeFragment = CourseWareHomeFragment.newInstance(abilitytype);
        mFragment.put(0, courseWareHomeFragment);
        CoursewareRecommendFragment.RecommendBean recommendBean = new CoursewareRecommendFragment.RecommendBean();
        recommendBean.setType(CoursewareRecommendFragment.Type.Recommend);
        if (TextUtils.isEmpty(abilitytype)) {
            coursewareRecommendFragment = CoursewareRecommendFragment.newInstance(recommendBean, abilitytype);
            mFragment.put(1, coursewareRecommendFragment);
        }
        mViewPage.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setViewPager(mViewPage);

        ll_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderDialog(v);
            }
        });
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseSearchActivity.open(CoursewareActivity.this);
            }
        });
    }

    public void showOrderDialog(View v) {
        final ArrayList<String> objects = new ArrayList<String>();
        CommonAdapter popAdapter = new CommonAdapter<String>(mContext, R.layout.simple_item, objects) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                holder.setText(R.id.tv, s);
                if (position + 1 == ORDER_TYPE) {
                    holder.setTextColor(R.id.tv, XFrame.getColor(R.color.colorPrimary));
                }
            }
        };
        objects.add("按时间");
        objects.add("按热度");
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        final IndicatorDialog dialog = new IndicatorBuilder(this)
                .arrowWidth(UIUtils.dip2px(15))
                .height((int) (height * 0.2))
                .height(-1)
                .width((int) (width * 0.3))
                .ArrowDirection(IndicatorBuilder.TOP)
                .bgColor(Color.WHITE)
                .gravity(IndicatorBuilder.GRAVITY_RIGHT)
                .ArrowRectage(0.8f)
                .radius(8)
                .layoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false))
                .adapter(popAdapter).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show(v);
        popAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (!(ORDER_TYPE == position + 1)) {
                    ORDER_TYPE = position + 1;
//                    ISFRESH = true;
//                    getCommentData();
                    if (coursewareRecommendFragment != null)
                        coursewareRecommendFragment.setOrderType(ORDER_TYPE);
                    if (courseWareHomeFragment != null)
                        courseWareHomeFragment.setOrderType(ORDER_TYPE);
                    tvOrder.setText(objects.get(position));
                }
                dialog.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
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
        tvTitle.setText(XFrame.getString(R.string.courseware));
    }

    @OnClick({R.id.ll_collection, R.id.ll_question_bank, R.id.ll_discuss})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_collection:
                CourseCollectActivity.open(this);
                break;
            case R.id.ll_question_bank:
                QuestionBankActivity.open(this);
                break;
            case R.id.ll_discuss:
                DynamicActivity.open(this, DynamicType.CourseWareHome);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        private PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragment.get(position);
        }
    }
}
