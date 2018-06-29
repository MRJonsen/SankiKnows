package com.zc.pickuplearn.ui.group.widget.prodetail.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.MultiItemTypeAdapter;
import com.zc.pickuplearn.beans.CircleRankingBean;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.beans.IndustryStandardBean;
import com.zc.pickuplearn.beans.ProfessionalCircleBean;
import com.zc.pickuplearn.beans.Professor;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.classiccase.view.ClassicCaseDetailActivity;
import com.zc.pickuplearn.ui.classiccase.view.ClassicCaseListActivity;
import com.zc.pickuplearn.ui.group.view.IndustryStandardActivity;
import com.zc.pickuplearn.ui.group.widget.prodetail.presenter.ProCircleDetailPresenterImpl;
import com.zc.pickuplearn.ui.group.widget.prodetail.widget.IndustryAdapter;
import com.zc.pickuplearn.ui.group.widget.prodetail.widget.PersonRankAdapter;
import com.zc.pickuplearn.ui.group.widget.prodetail.widget.ProfessorAdapter;
import com.zc.pickuplearn.ui.home.model.HomeModelImpl;
import com.zc.pickuplearn.ui.home.widget.ClassicCaseAdapter;
import com.zc.pickuplearn.ui.view.NoScrollListView;
import com.zc.pickuplearn.ui.view.RecyclerViewGridDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ProCircleDetailActivity extends BaseActivity implements IProCircleDetailView {
    private final static String TAG = "ProCircleDetailActivity";
    @BindView(R.id.item_circle_activity_title)
    TextView title;
    @BindView(R.id.item_circle_activity_join)
    TextView join;
    @BindView(R.id.item_circle_activity_personRanking)
    LinearLayout personRanking;
    @BindView(R.id.item_circle_activity_listView)
    NoScrollListView listView;
    @BindView(R.id.tv_postion)
    TextView tvPostion;
    @BindView(R.id.tv_answer_sum)
    TextView tvAnswerSum;
    @BindView(R.id.tv_take_sum)
    TextView tvTakeSum;
    @BindView(R.id.tv_activity)
    TextView tvAcitivity;
    @BindView(R.id.tv_question_sum)
    TextView tvQustionSum;
    @BindView(R.id.rv_classic_case)
    RecyclerView mClassCaseRecyclerView;
    @BindView(R.id.rv_professor_list)
    RecyclerView mProfessorRecyclerView;

    @BindView(R.id.rc_industry_standard)
    RecyclerView rcIndustryStandard;
    @BindView(R.id.tv_group_rank)
    TextView tvGroupRank;
    @BindView(R.id.iv_rank)
    ImageView ivRank;
    @BindView(R.id.group_liveness)
    TextView groupLiveness;

    private ProfessionalCircleBean mBean;
    private List<CircleRankingBean> list = new ArrayList<CircleRankingBean>();
    private PersonRankAdapter rankAdapter;//排名适配器
    private ProCircleDetailPresenterImpl presenter;
    private List<ClassicCaseBean> mCaseDatas;//经典案咧数据
    private List<IndustryStandardBean> mIndustryStandard;//行业规范数据集
    private List<Professor> mProfessorDatas;//专家
    private ClassicCaseAdapter mClassiccaseadapter;//经典案咧适配器
    private ProfessorAdapter mProfessorAdapter;
    private IndustryAdapter industryAdapter;

    /**
     * 启动方法
     *
     * @param context
     * @param bean
     */
    public static void startProCircleDetailActivity(Context context, ProfessionalCircleBean bean) {
        Intent intent = new Intent(context, ProCircleDetailActivity.class);
        intent.putExtra(TAG, bean);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_pro_circle_detail;
    }

    @Override
    public void initView() {
        rankAdapter = new PersonRankAdapter(list, this);
        listView.setAdapter(rankAdapter);
        presenter = new ProCircleDetailPresenterImpl(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mBean = (ProfessionalCircleBean) intent.getSerializableExtra(TAG);
        initRank(mBean);
        setJoinButton(mBean.getISJOIN());
        initProfessorView();
        initIndustryStandard();
        initClassCaseView();
    }

    private void initIndustryStandard() {
        rcIndustryStandard.setHasFixedSize(true);
        rcIndustryStandard.setLayoutManager(new LinearLayoutManager(this));//gridView效果
        rcIndustryStandard.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        rcIndustryStandard.addItemDecoration(new RecyclerViewGridDivider(this));
        mIndustryStandard = new ArrayList<>();
        industryAdapter = new IndustryAdapter(this, mIndustryStandard);
        rcIndustryStandard.setAdapter(industryAdapter);
        // 获取行业规范数据
        API.getIndustryStandard(null, "5", 0, UrlMethod.TYPE_HOT, mBean, new CommonCallBack<List<IndustryStandardBean>>() {
            @Override
            public void onSuccess(List<IndustryStandardBean> teamRankPersonBeen) {
                mIndustryStandard.clear();
                mIndustryStandard.addAll(teamRankPersonBeen);
                industryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void initRank(ProfessionalCircleBean professionalCircleBean) {
//        tvAcitivity.setText(mBean.getACTIVELEVEL());
//        tvAnswerSum.setText(mBean.getANSWERSUM());
//        tvQustionSum.setText(mBean.getQUESTIONSUM());
//        tvPostion.setText(mBean.getPOSITION());
//        tvTakeSum.setText(mBean.getANSWERTAKESUM());
//        presenter.getRankingData(mBean);//初始化请求排名参数
        title.setText(mBean.getPROCIRCLENAME());
       //6.21 排名取消掉前三名的图片设置
//        String position = professionalCircleBean.getPOSITION();
//        switch (position) {
//            case "1":
//                ivRank.setVisibility(View.VISIBLE);
//                tvGroupRank.setVisibility(View.GONE);
//                ivRank.setImageResource(R.mipmap.group_first);
//                break;
//            case "2":
//                ivRank.setVisibility(View.VISIBLE);
//                tvGroupRank.setVisibility(View.GONE);
//                ivRank.setImageResource(R.mipmap.group_sec);
//                break;
//            case "3":
//                ivRank.setVisibility(View.VISIBLE);
//                tvGroupRank.setVisibility(View.GONE);
//                ivRank.setImageResource(R.mipmap.group_thr);
//                break;
//            default:
                tvGroupRank.setText("第"+professionalCircleBean.getPOSITION()+"名");
//                break;
//        }
        groupLiveness.setText(professionalCircleBean.getACTIVELEVEL());
    }

    @Override
    public void addData(List<CircleRankingBean> beanList) {
        if (beanList != null) {
            list.clear();
            list.addAll(beanList);
            rankAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setJoinButton(boolean isJoin) {
        if (join != null) {
            join.setText(isJoin ? "退出圈子" : "加入圈子");
        }
    }

    /**
     * 初始化经典案例布局
     */
    public void initClassCaseView() {
        mClassCaseRecyclerView.setHasFixedSize(true);
        mClassCaseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mClassCaseRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        mClassCaseRecyclerView.addItemDecoration(new RecyclerViewGridDivider(this));
        mCaseDatas = new ArrayList<>();
        mClassiccaseadapter = new ClassicCaseAdapter(this, mCaseDatas);
        mClassCaseRecyclerView.setAdapter(mClassiccaseadapter);
        mClassiccaseadapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                final ClassicCaseBean bean = mCaseDatas.get(position);
                ClassicCaseDetailActivity.startClassicDetailActivity(ProCircleDetailActivity.this, bean);
                API.clickClassicCase(bean.getSEQKEY(), new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        String clickcount = bean.getCLICKCOUNT();
                        if (!TextUtils.isEmpty(clickcount)) {
                            int i = Integer.parseInt(clickcount) + 1;
                            clickcount = i + "";
                        } else {
                            clickcount = "1";
                        }
                        bean.setCLICKCOUNT(clickcount);
                        mClassiccaseadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        new HomeModelImpl().getClassicCaseDatas(mBean, new HomeModelImpl.GetClassicCaseDatasCallBack() {
            @Override
            public void onSuccess(List<ClassicCaseBean> strings) {
                mCaseDatas.addAll(strings);
                mClassiccaseadapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * 初始化专家布局
     */
    public void initProfessorView() {
        mProfessorRecyclerView.setHasFixedSize(true);
        mProfessorRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));//gridView效果
        mProfessorRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        mProfessorRecyclerView.addItemDecoration(new RecyclerViewGridDivider(this));
        mProfessorDatas = new ArrayList<>();
        mProfessorAdapter = new ProfessorAdapter(this, mProfessorDatas);
        mProfessorRecyclerView.setAdapter(mProfessorAdapter);
        mProfessorAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                ClassicCaseDetailActivity.startClassicDetailActivity(ProCircleDetailActivity.this, mCaseDatas.get(position));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        API.getProfessorList(mBean, 0, "3", new CommonCallBack<List<Professor>>() {
            @Override
            public void onSuccess(List<Professor> professors) {
                mProfessorDatas.addAll(professors);
                mProfessorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @OnClick({R.id.item_circle_activity_back_btn, R.id.item_circle_activity_join, R.id.rl_home_more_classicCase, R.id.rl_more_professor, R.id.rl_group_rank_more, R.id.rl_industry_standard_more})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_circle_activity_back_btn:
                finish();
                break;
            case R.id.item_circle_activity_join:
                presenter.JoinCirclew(mBean);
                break;
            case R.id.rl_home_more_classicCase:
                ClassicCaseListActivity.startClassicCaseListActivity(this, mBean);
//                startActivity(new Intent(this, ClassicCaseListActivity.class));
                break;
            case R.id.rl_more_professor:
                ProfessorListActivity.startProfessorList(this, mBean);//专家列表
                break;
            case R.id.rl_group_rank_more:
                showToast(getResources().getString(R.string.function_not_open));
                break;
            case R.id.rl_industry_standard_more:
                IndustryStandardActivity.startIndustryStandardActivity(this, mBean);
                break;
            default:
                break;
        }
    }
}
