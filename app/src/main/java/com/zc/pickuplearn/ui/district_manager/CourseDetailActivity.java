package com.zc.pickuplearn.ui.district_manager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.event.CourseCollectChangeEvent;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.ui.district_manager.fragment.CourseCommentFragment;
import com.zc.pickuplearn.ui.district_manager.fragment.CourseIntroduceFragment;
import com.zc.pickuplearn.ui.district_manager.fragment.CoursewareRecommendFragment;
import com.zc.pickuplearn.ui.file_view.FileDoer;
import com.zc.pickuplearn.ui.question_and_answer.DynamicActivity;
import com.zc.pickuplearn.ui.question_and_answer.DynamicType;
import com.zc.pickuplearn.ui.videoplayer.VideoPlayerActivity;
import com.zc.pickuplearn.ui.webview.BaseWebViewActivity;
import com.zc.pickuplearn.ui.webview.x5.CommonActivity;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hss01248.dialog.StyledDialog.context;

/**
 * 课件详情页面
 */
public class CourseDetailActivity extends BaseActivity {


    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.bt_right)
    ImageView btRight;
    @BindView(R.id.ll_bt_right)
    LinearLayout ll_btRight;
    @BindView(R.id.root)
    LinearLayout root;
    private static final String PARAM = CourseDetailActivity.class.getSimpleName();
    @BindView(R.id.ll_collection)
    LinearLayout llCollection;
    @BindView(R.id.ll_question_bank)
    LinearLayout llQuestionBank;
    @BindView(R.id.ll_discuss)
    LinearLayout llDiscuss;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    public static CourseWareBean.DatasBean bean;
    @BindView(R.id.tl_3)
    SlidingTabLayout tabLayout;
    @BindView(R.id.iv_middle_icon)
    ImageView ivMiddleIcon;
    @BindView(R.id.tv_middle_icon)
    TextView tvMiddleIcon;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.vp_viewpager)
    ViewPager mViewPage;
    List<String> title = Arrays.asList("课件简介", "相关课件", "评论");
    SparseArray<Fragment> mFragment = new SparseArray<>();//存放fragment

    public static void open(Context context, CourseWareBean.DatasBean bean) {
        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra(PARAM, bean);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        initStatusBar(root);
        initToolBar();
        initMiddleButton();
        initCoverView();
        initCollectionButton();
        mFragment.put(0, CourseIntroduceFragment.newInstance(bean));
        CoursewareRecommendFragment.RecommendBean recommendBean = new CoursewareRecommendFragment.RecommendBean();
        recommendBean.setType(CoursewareRecommendFragment.Type.Relative);
        recommendBean.setBean(bean);
        mFragment.put(1, CoursewareRecommendFragment.newInstance(recommendBean,""));
        mFragment.put(2, CourseCommentFragment.newInstance(bean));
        mViewPage.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setViewPager(mViewPage);
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==2){
                    llBottom.setVisibility(View.GONE);
                }else {
                    llBottom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initCollectionButton() {
        if (bean != null) {
            if ("1".equals(bean.getISCOLLECT())) {
                btRight.setImageResource(R.mipmap.icon_star);
            } else {
                btRight.setImageResource(R.mipmap.icon_star_un);
            }
        }
    }

    private void initCoverView() {
        ImageLoaderUtil.showImageView(this, ResultStringCommonUtils.subUrlToWholeUrl(bean.getPHOTOURL()), ivCover);
        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(bean.getH5URL())){
                    if (bean.getFILEURL().endsWith(".mp4")){
                        VideoPlayerActivity.open(mContext,ResultStringCommonUtils
                                .subUrlToWholeUrl(bean.getFILEURL()),bean);
                    }else {
                        FileDoer.getInstance().openFile(mContext, ResultStringCommonUtils
                                .subUrlToWholeUrl(bean.getFILEURL()));
                        postPlaytime();
                    }


                }else {
                    if (bean.getH5URL().endsWith(".mp4")){
                        VideoPlayerActivity.open(mContext,bean.getH5URL(),bean);
                    }else {
                        FileDoer.getInstance().openFile(mContext, bean.getH5URL());
                    }


//                    BaseWebViewActivity.open(CourseDetailActivity.this,bean.getH5URL(),true);
//                    CommonActivity.open(CourseDetailActivity.this,bean.getH5URL());
                }
            }
        });
    }

    public void postPlaytime(){
        HashMap<String, String> datas = new HashMap<>();
        datas.put("psnnum", DataUtils.getUserInfo().getUSERACCOUNT());
        datas.put("ABILITYTYPECODE",bean.getABILITYTYPE());
        datas.put("playtime",1+"");
        datas.put("coursecode",bean.getSEQKEY());
        datas.put("coursename",bean.getCOURSENAME());
        API.postStudyProgress(datas, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure() {

            }
        });
    }


    private void initMiddleButton() {
        switch (bean.getISTHEORY()) {
            case "1":
                ivMiddleIcon.setImageResource(R.mipmap.icon_question_bank);
                tvMiddleIcon.setText("题库练习");
                llQuestionBank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        API.getInstance().getQuestionBankChapterPractice(bean.getABILITYTYPE(), new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                if (!TextUtils.isEmpty(s)&&!s.startsWith("http")){
                                    ToastUtils.showToast(CourseDetailActivity.this,s);
                                    return;
                                }
                                WebViewActivity.skip(mContext, s, "", false, false);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                });
                break;
            case "2":
                ivMiddleIcon.setImageResource(R.mipmap.icon_practice_specification);
                tvMiddleIcon.setText("实操规范");
                llQuestionBank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        API.getQuestionBankPracticalNorm("PracticalNorm","abilitycode", bean.getABILITYCODE(), new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                WebViewActivity.skip(CourseDetailActivity.this, s, "", false, false);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                });
                break;
        }
    }


    @Override
    public void initVariables() {
        super.initVariables();
        bean = (CourseWareBean.DatasBean) getIntent().getSerializableExtra(PARAM);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_course_detail;
    }

    @Override
    protected void initData() {

    }


    private void initToolBar() {
        if (bean != null) {
            tvTitle.setText(bean.getCOURSENAME());
        }
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }


    @OnClick({R.id.ll_collection, R.id.ll_discuss, R.id.ll_bt_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_collection:
                CourseCollectActivity.open(this);
                break;
            case R.id.ll_bt_right:
                API.getInstance().collectCourseWare(bean, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        EventBus.getDefault().post(new CourseCollectChangeEvent());
                        if ("1".equals(bean.getISCOLLECT())){
                            bean.setISCOLLECT("");
                            initCollectionButton();
                            showToast("取消收藏");
                        }else {
                            bean.setISCOLLECT("1");
                            showToast("收藏成功");
                            initCollectionButton();
                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                });
                break;
            case R.id.ll_discuss:
                DynamicActivity.open(this, DynamicType.CourseWareDetail);
                break;


        }

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
