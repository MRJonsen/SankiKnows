package com.zc.pickuplearn.ui.capabilitymap;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiang.android.indicatordialog.IndicatorBuilder;
import com.jiang.android.indicatordialog.IndicatorDialog;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.MultiItemTypeAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.AbilityBankBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.SPUtils;
import com.zc.pickuplearn.utils.StatusBarUtil;
import com.zc.pickuplearn.utils.SystemUtils;
import com.zc.pickuplearn.utils.UIUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CapAbilityMapActivity extends BaseActivity {
    @BindView(R.id.ll_bottom)
    LinearLayout mBottomLayout;
    @BindView(R.id.tv_top_title)
    TextView tv_title;
    @BindView(R.id.tv_right_rate)
    TextView tv_right_rate;
    @BindView(R.id.tv_bank_name)
    TextView tv_bank_name;
    @BindView(R.id.tv_question_time)
    TextView tv_time;
    @BindView(R.id.tv_question_sum)
    TextView tv_num;
    @BindView(R.id.iv_change_bank)
    ImageView iv_change;
    @BindView(R.id.ll_flower_father)
    LinearLayout ll_flower_father;
    @BindView(R.id.fr_flower)
    FrameLayout fr_flower;
    @BindView(R.id.ll_practice)
    LinearLayout ll_practice;
    private AbilityBankBean abilityBank;
    private AbilityBankBean.ModuleListBean.DATASBean bank;
    private IndicatorDialog dialog;

    @Override
    public int setLayout() {
        return R.layout.activity_cap_ability_map;
    }

    @Override
    public void initView() {
        initTop();
        initListener();
        initFlower();
    }

    /**
     * 调整花瓣布局的大小
     */
    private void initFlower() {
        fr_flower.post(new Runnable() {
            @Override
            public void run() {
                int measuredHeight = fr_flower.getHeight() < fr_flower.getWidth() ? fr_flower.getHeight() : fr_flower.getWidth();
                ViewGroup.LayoutParams layoutParams = fr_flower.getLayoutParams();
                layoutParams.width = measuredHeight;
                layoutParams.height = measuredHeight;
                fr_flower.setLayoutParams(layoutParams);
                ViewGroup.LayoutParams layoutParams1 = ll_practice.getLayoutParams();
                layoutParams1.width = measuredHeight / 5 * 2;
                layoutParams1.height = measuredHeight / 5 * 2;
                ll_practice.setLayoutParams(layoutParams1);

            }
        });
    }

    @Override
    protected void initData() {
        API.getQuestionBank(new CommonCallBack<AbilityBankBean>() {

            @Override
            public void onSuccess(AbilityBankBean abilityBankBean) {
                if (abilityBankBean != null)
                    updateData(abilityBankBean);
            }

            @Override
            public void onFailure() {

            }
        });


    }

    private String getChoiceBank(){
        return (String) SPUtils.get(this,"CapAbilityMapActivityBank","");
    }

    private void setChoiceBank(String Module_id){
        SPUtils.put(this,"CapAbilityMapActivityBank",Module_id);
    }
    private void updateData(AbilityBankBean abilityBankBean) {
        this.abilityBank = abilityBankBean;
        if (abilityBankBean.getModule_list().getDATAS().size() > 0) {
            String choiceBank = getChoiceBank();
            for (AbilityBankBean.ModuleListBean.DATASBean bean:
                 abilityBankBean.getModule_list().getDATAS()) {
                if (choiceBank.equals(bean.getMODULE_ID())){
                    bank = bean;
                }
            }
            if (bank == null){
                bank = abilityBankBean.getModule_list().getDATAS().get(0);
            }

            tv_bank_name.setText(bank.getMODULE_NAME());
        }
        tv_num.setText(abilityBankBean.getDone_num() + "");
        tv_right_rate.setText(abilityBankBean.getRight_rate() + "%");
        tv_time.setText(Double.parseDouble(new DecimalFormat("#.##").format((abilityBankBean.getTime_pass() / 3600f))) + "h");
    }

    private void initTop() {
        //处理状态栏及间距
        StatusBarUtil.darkMode((this));
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.ll_root));
        tv_title = (TextView) findViewById(R.id.tv_top_title);
        tv_title.setText("能力题库");
        ViewGroup.LayoutParams layoutParams = mBottomLayout.getLayoutParams();
        layoutParams.height = SystemUtils.dipToPixel(this, 56);
        mBottomLayout.setLayoutParams(layoutParams);
    }

    private void initListener() {

    }

    /**
     * 点击事件
     *
     * @param v
     */
    @OnClick({R.id.my_collection, R.id.my_note, R.id.my_error,
            R.id.my_record, R.id.ll_top_back, R.id.ll_chapter,
            R.id.ll_random, R.id.ll_simulate, R.id.ll_hard,
            R.id.ll_practice, R.id.ll_change_bank})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_error://我的错题
                startDetail("errorSubject");
                break;
            case R.id.my_collection://我的收藏
                startDetail("myCollect");
                break;
            case R.id.my_record://练习记录
                startDetail("exerciseRecord");
                break;
            case R.id.my_note://我的笔记
                startDetail("myNotes");
                break;
            case R.id.ll_top_back://返回
                finish();
                break;
            case R.id.ll_chapter://章节练习
                startDetail("chapterPractice");
                break;
            case R.id.ll_random://随机练习
                startDetail("randomPractice");
                break;
            case R.id.ll_simulate://模拟考试
                startDetail("mockExam");
                break;
            case R.id.ll_hard://难题攻克
                startDetail("problemTake");
                break;
            case R.id.ll_practice://每日一练
                startDetail("dailyPractice");
                break;
            case R.id.ll_change_bank://切换题库
                showChangePop();
                break;
        }
    }


    private void showChangePop() {
        if (dialog != null) {
            dialog.show(iv_change);
            return;
        }
        if (abilityBank != null) {
            List<AbilityBankBean.ModuleListBean.DATASBean> datas = abilityBank.getModule_list().getDATAS();
            CommonAdapter popAdpter = new CommonAdapter<AbilityBankBean.ModuleListBean.DATASBean>(this, R.layout.info_ignore_pop_item, datas) {
                @Override
                protected void convert(ViewHolder holder, AbilityBankBean.ModuleListBean.DATASBean o, int position) {
                    holder.setText(R.id.tv_ignore_warning, o.getMODULE_NAME());
                    holder.setVisible(R.id.iv_ignore, false);
                }
            };
            Resources resources = getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            dialog = new IndicatorBuilder(this)
                    .arrowWidth(UIUtils.dip2px(15))
                    .height((int) (height * 0.4))
                    .width((int) (width * 0.9))
                    .ArrowDirection(IndicatorBuilder.TOP)
                    .bgColor(Color.WHITE)
                    .gravity(IndicatorBuilder.GRAVITY_CENTER)
                    .ArrowRectage(0.9f)
                    .radius(8)
                    .layoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
                    .adapter(popAdpter).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show(iv_change);

            popAdpter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                    bank = abilityBank.getModule_list().getDATAS().get(position);
                    tv_bank_name.setText(bank.getMODULE_NAME());
                    setChoiceBank(bank.getMODULE_ID());
                    dialog.dismiss();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }
    }

    public void startDetail(String methon) {
        if (bank == null)
            return;
        API.getQuestionBankDetail(methon, bank.getMODULE_ID(), new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                WebViewActivity.skip(CapAbilityMapActivity.this, s, "", false, true);
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
