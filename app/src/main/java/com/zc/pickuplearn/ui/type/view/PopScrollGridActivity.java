package com.zc.pickuplearn.ui.type.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.event.ClassicCaseTypeEvent;
import com.zc.pickuplearn.event.ExpertTypeEvent;
import com.zc.pickuplearn.event.GetTypeEvent;
import com.zc.pickuplearn.event.IndustryTypeEvent;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.askquestion.widget.AskQuestionEvent;
import com.zc.pickuplearn.ui.category.ModelCategory;
import com.zc.pickuplearn.ui.category.QusetionTypeBean;
import com.zc.pickuplearn.ui.classiccase.model.ClassCaseTypeEvent;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.DynamicMoreActivity;
import com.zc.pickuplearn.ui.question.ChangeQustionEvent;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.CreatTeamCircleTypeEvent;
import com.zc.pickuplearn.ui.teamcircle.eventbusevent.IndustryStandardTypeEvent;
import com.zc.pickuplearn.ui.type.widget.SubTypeFragment;
import com.zc.pickuplearn.ui.type.widget.TypeGreenDaoEvent;
import com.zc.pickuplearn.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 分类列表
 */
public class PopScrollGridActivity extends FragmentActivity {

    private TextView[] tvList;
    private View[] views;
    private LayoutInflater inflater;
    private ScrollView scrollView;
    private ViewPager viewpager;
    private int currentItem = 0;
    private ShopAdapter shopAdapter;
    public static String CLOSE = "closeScrollGridActivity";
    List<QusetionTypeBean> parentGroup = new ArrayList<QusetionTypeBean>();
    HashMap<QusetionTypeBean, List<QusetionTypeBean>> dataHashMap = new HashMap<QusetionTypeBean, List<QusetionTypeBean>>();

    static List<QusetionTypeBean> localData = new ArrayList<>();
    public static final String TAG = "ScrollGridActivity";
    private TypeGreenDaoEvent function;

    /**
     * 启动分类列表方法
     */
    public static void startScrollGridActivity(Context context, TypeGreenDaoEvent event) {
        Intent intent = new Intent(context, PopScrollGridActivity.class);
        intent.putExtra(TAG, event);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_scrollgrid);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    /**
     * 初始化数据 先取本地再取网络
     */
    private void initData() {
//        String object = (String) SPUtils.get(this, ModelCategory.TAG, "");// 先取本地分类数据
        if (localData != null && !localData.isEmpty()) {
            orderQuestionType(localData);
            return;
        }
        ModelCategory modelCategory = new ModelCategory();
        modelCategory.getQuestionTypeData(new ModelCategory.GetCatogoryDataCallBack() {
            @Override
            public void onSuccess(List<QusetionTypeBean> qustionTypeBeen) {
//                processData(qustionTypeBeen);
                if (qustionTypeBeen != null) {
                    if (localData == null) {
                        localData = new ArrayList<QusetionTypeBean>();
                    }
                    localData.addAll(qustionTypeBeen);
                }
                orderQuestionType(qustionTypeBeen);
            }

            @Override
            public void onFail() {

            }
        });

    }

    /**
     * 排序
     *
     * @param qusetionTypeBeen
     */
    private void orderQuestionType(final List<QusetionTypeBean> qusetionTypeBeen) {
        processData(qusetionTypeBeen);
//        new ProfessionModelImpl().getProfessionalCircleData(new ProfessionModelImpl.AllProCircleCallBack() {
//            @Override
//            public void onSuccess(List<ProfessionalCircleBean> professionalCircleBeen) {
//                ArrayList<QusetionTypeBean> qusetionTypeBeen1 = new ArrayList<>();
//                if (professionalCircleBeen != null && professionalCircleBeen.size() > 0 && qusetionTypeBeen != null) {
//                    for (ProfessionalCircleBean professbean :
//                            professionalCircleBeen) {
//                        for (QusetionTypeBean typebean :
//                                qusetionTypeBeen) {
//                            if (professbean.getPROCIRCLECODE().equals(typebean.getCODE())) {
//                                qusetionTypeBeen1.add(typebean);
//                            }
//                        }
//                    }
//                    qusetionTypeBeen.removeAll(qusetionTypeBeen1);
//                    qusetionTypeBeen.addAll(qusetionTypeBeen1);
//                    processData(qusetionTypeBeen);
//                } else {
//                    processData(qusetionTypeBeen);
//                }
//            }
//
//            @Override
//            public void onFail() {
//                processData(qusetionTypeBeen);
//            }
//        });
    }

    private void processData(List<QusetionTypeBean> qusetionTypeBeen) {
        HashMap<QusetionTypeBean, List<QusetionTypeBean>> parenGroup = new HashMap<QusetionTypeBean, List<QusetionTypeBean>>();
        for (QusetionTypeBean qusetionTypeBean : qusetionTypeBeen) {
            if ("-1".equals(qusetionTypeBean.getPARENTID())) {
                parentGroup.add(qusetionTypeBean);
                ArrayList<QusetionTypeBean> childGroup = new ArrayList<QusetionTypeBean>();
                for (QusetionTypeBean bean : qusetionTypeBeen) {
                    if (bean.getPARENTID().equals(
                            qusetionTypeBean.getCODE())) {
                        childGroup.add(bean);
                    }
                }
                parenGroup.put(qusetionTypeBean, childGroup);
            }
        }
        dataHashMap.putAll(parenGroup);
        showToolsView();//设置一级导航数据
        initPager();
    }

    private void initView() {
        getParam();
        scrollView = (ScrollView) findViewById(R.id.tools_scrlllview);
        shopAdapter = new ShopAdapter(getSupportFragmentManager());
        inflater = LayoutInflater.from(this);
    }

    /**
     * 获取传入参数
     */
    public void getParam() {
        Intent intent = getIntent();
        function = (TypeGreenDaoEvent) intent.getSerializableExtra(TAG);
    }

    /**
     * 动态生成显示items中的textview 一级分类
     */
    private void showToolsView() {
        LinearLayout toolsLayout = (LinearLayout) findViewById(R.id.tools);
        tvList = new TextView[parentGroup.size()];
        views = new View[parentGroup.size()];
        for (int i = 0; i < parentGroup.size(); i++) {
            View view = inflater.inflate(R.layout.item_list_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            mClickTime.put(view.getId(), 0L);
            TextView textView = (TextView) view.findViewById(R.id.text);
            ImageView imgicon = (ImageView) view.findViewById(R.id.iv_type_icon);
            if (!TextUtils.isEmpty(parentGroup.get(i).getICOPATH())) {
                ImageLoaderUtil.displayCircleICon(this, imgicon, parentGroup.get(i).getICOPATH());
            }
            textView.setText(parentGroup.get(i).getNAME());
            toolsLayout.addView(view);
            tvList[i] = textView;
            views[i] = view;
        }
        changeTextColor(0);
    }

    private HashMap<Integer, Long> mClickTime = new HashMap<>();
    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (System.currentTimeMillis() - mClickTime.get(v.getId()) < 200) {
                LogUtils.e("双击");
                //双击
                QusetionTypeBean bean = parentGroup.get(v.getId());
                QuestionBean questionBean = new QuestionBean();
                questionBean.setQUESTIONTYPECODE(bean.getCODE());
                questionBean.setQUESTIONTYPENAME(bean.getNAME());
                questionBean.setPARENTID(bean.getPARENTID());
                if (function == TypeGreenDaoEvent.Type) {
                    EventBus.getDefault().post(new GetTypeEvent(questionBean));
                } else if (function == TypeGreenDaoEvent.HomeCategory) {
                    DynamicMoreActivity.startDynamicMoreActivity(PopScrollGridActivity.this, "", questionBean, "");//根据分类去找找问题
                } else if (function == TypeGreenDaoEvent.ChangeQuestion) {//修改问题
                    ChangeQustionEvent changeQustionEvent = new ChangeQustionEvent();
                    changeQustionEvent.setQuestionBean(questionBean);
                    EventBus.getDefault().post(changeQustionEvent);
                } else if (function == TypeGreenDaoEvent.AskQuestion) {//提问
                    AskQuestionEvent askQustionEvent = new AskQuestionEvent();
                    askQustionEvent.setQuestionBean(questionBean);
                    EventBus.getDefault().post(askQustionEvent);
                } else if (function == TypeGreenDaoEvent.TeamCreate) {//团队创建
                    CreatTeamCircleTypeEvent creatTeamCircleTypeEvent = new CreatTeamCircleTypeEvent();
                    creatTeamCircleTypeEvent.setQuestionBean(bean);
                    EventBus.getDefault().post(creatTeamCircleTypeEvent);
                } else if (function == TypeGreenDaoEvent.IndustryStandard) {//行业规范
                    IndustryStandardTypeEvent industryStandardTypeEvent = new IndustryStandardTypeEvent();
                    industryStandardTypeEvent.setQuestionBean(bean);
                    EventBus.getDefault().post(industryStandardTypeEvent);
                } else if (function == TypeGreenDaoEvent.ClassicCase) {
                    ClassCaseTypeEvent classCaseTypeEvent = new ClassCaseTypeEvent();
                    classCaseTypeEvent.setQuestionBean(bean);
                    EventBus.getDefault().post(classCaseTypeEvent);
                } else if (function == TypeGreenDaoEvent.EXPERT) {
                    ExpertTypeEvent classCaseTypeEvent = new ExpertTypeEvent(questionBean);
                    EventBus.getDefault().post(classCaseTypeEvent);
                } else if (function == TypeGreenDaoEvent.INDUSTRY) {
                    IndustryTypeEvent classCaseTypeEvent = new IndustryTypeEvent(questionBean);
                    EventBus.getDefault().post(classCaseTypeEvent);
                } else if (function == TypeGreenDaoEvent.CASE) {
                    ClassicCaseTypeEvent classCaseTypeEvent = new ClassicCaseTypeEvent(questionBean);
                    EventBus.getDefault().post(classCaseTypeEvent);
                }
                finish();
            } else {
                LogUtils.e("单击");
                //单击
                mClickTime.put(v.getId(), System.currentTimeMillis());
                viewpager.setCurrentItem(v.getId());
            }
        }
    };

    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager() {
        viewpager = (ViewPager) findViewById(R.id.goods_pager);
        viewpager.setAdapter(shopAdapter);
        viewpager.addOnPageChangeListener(onPageChangeListener);
    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */
    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if (viewpager.getCurrentItem() != arg0)
                viewpager.setCurrentItem(arg0);
            if (currentItem != arg0) {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem = arg0;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    };

    /**
     * ViewPager 加载选项卡
     *
     * @author Administrator
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            List<QusetionTypeBean> qusetionTypeBeen = dataHashMap.get(parentGroup.get(index));
            SubTypeFragment proTypeFragment = SubTypeFragment.newInstance((ArrayList<QusetionTypeBean>) qusetionTypeBeen, function);
            return proTypeFragment;
        }

        @Override
        public int getCount() {
            return parentGroup.size();
        }
    }

    /**
     * 改变textView的颜色
     *
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < tvList.length; i++) {
            if (i != id) {
                views[i].setBackgroundColor(getResources().getColor(R.color.white));
                tvList[i].setBackgroundColor(0x00000000);
                tvList[i].setTextColor(getResources().getColor(R.color.gray));
            }
        }
        tvList[id].setBackgroundColor(getResources().getColor(R.color.background));
        tvList[id].setTextColor(getResources().getColor(R.color.gray));
        views[id].setBackgroundColor(getResources().getColor(R.color.background));
    }

    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop());
        scrollView.smoothScrollTo(0, x);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }


    @Subscribe
    public void onEventMainThread(String msg) {
        if (CLOSE.equals(msg)) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}