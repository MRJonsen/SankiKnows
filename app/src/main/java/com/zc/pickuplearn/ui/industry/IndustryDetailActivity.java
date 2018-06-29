package com.zc.pickuplearn.ui.industry;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.IndustryStandardBean;
import com.zc.pickuplearn.beans.SourceBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.industry.adapter.IndustryResAdapter;
import com.zc.pickuplearn.ui.view.MaxRecycleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 行业规范 详情页面
 */

public class IndustryDetailActivity extends BaseActivity {


    private static String PARAM = IndustryDetailActivity.class.getSimpleName();
    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.bt_right)
    Button btRight;
    @BindView(R.id.iv_case_face)
    ImageView ivCaseFace;
    @BindView(R.id.tv_case_name)
    TextView tvCaseName;
    @BindView(R.id.tv_case_tag)
    TextView tvCaseTag;
    @BindView(R.id.tv_case_click_times)
    TextView tvCaseClickTimes;
    @BindView(R.id.item)
    LinearLayout item;
    @BindView(R.id.tv_simple_intro)
    TextView tvSimpleIntro;
    @BindView(R.id.tv_new_more)
    TextView tvNewMore;
    @BindView(R.id.mrv_content)
    MaxRecycleView rcContent;
    @BindView(R.id.root)
    LinearLayout root;

    private IndustryStandardBean industryStandardBean;
    private IndustryResAdapter industryResAdapter;

    public static void open(Context context, IndustryStandardBean industryStandardBean) {
        Intent intent = new Intent(context, IndustryDetailActivity.class);
        intent.putExtra(PARAM, industryStandardBean);
        context.startActivity(intent);
    }

    @Override
    public void initVariables() {
        super.initVariables();
        industryStandardBean = (IndustryStandardBean) getIntent().getSerializableExtra(PARAM);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_industry_detail;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void initView() {
        initStatusBar(root);
        initToolBar();
        initTopView();
        initContent();
    }

    private void initTopView() {
        tvSimpleIntro.setText("简介："+industryStandardBean.getSTANDARDEXPLAIN());
        tvCaseName.setText(industryStandardBean.getSTANDARDNAME());
        tvCaseClickTimes.setText(industryStandardBean.getCLICKCOUNT()+"");
        ImageLoaderUtil.showImageView(this, ResultStringCommonUtils.subUrlToWholeUrl(industryStandardBean.getPHOTOURL()), ivCaseFace);
    }

    public void initContent() {
        List<SourceBean> sourceBeen = ResultStringCommonUtils.splitUrls(industryStandardBean.getFILEURL(), industryStandardBean.getFILENAME());
        rcContent.setHasFixedSize(true);
        rcContent.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        industryResAdapter = new IndustryResAdapter(this, sourceBeen);
        rcContent.setAdapter(industryResAdapter);
    }

    private void initToolBar() {
        tvTitle.setText(industryStandardBean.getSTANDARDNAME());
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

}
