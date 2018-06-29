package com.zc.pickuplearn.ui.classiccase.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.ClassicCaseBean;
import com.zc.pickuplearn.http.HttpContacts;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.TableName;
import com.zc.pickuplearn.http.UrlMethod;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.HttpUtils;
import com.zc.pickuplearn.http.JsonUtils;
import com.zc.pickuplearn.http.MyStringCallBack;
import com.zc.pickuplearn.http.ParamUtils;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.classiccase.view.comment.view.CommentFragment;
import com.zc.pickuplearn.ui.classiccase.view.resource.view.IResurceView;
import com.zc.pickuplearn.ui.classiccase.view.resource.view.ResourceListFragment;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.ui.view.RatingBarView;
import com.zc.pickuplearn.ui.view.SHsegmentControl.SegmentControl;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.UIUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class ClassicCaseDetailActivity extends BaseActivity implements IResurceView {
    private static final String TAG = "ClassicCaseDetailActivity";
    @BindView(R.id.hv_headbar_head)
    HeadView mHead;
    @BindView(R.id.segment_control)
    SegmentControl mSegmentHorzontal;
    @BindView(R.id.fl_class_case_main)
    FrameLayout fl_class_case_main;// 主页
    @BindView(R.id.sv_star)
    RatingBarView sv_star;// 评分控件
    @BindView(R.id.ll_head_bg)
    LinearLayout ll_head_bg;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.tv_title)
    TextView tv_title;// 标题
    @BindView(R.id.tv_maker)
    TextView tv_maker;// 制作者
    @BindView(R.id.tv_score)
    TextView tv_score;// 评分
    private ResourceListFragment resourceListFragment;
    private CommentFragment commentFragment;// 评论列表
    Fragment currentFragment;// 记录当前fragment
    private FragmentManager supportFragmentManager;
    int score = 0;// 记录评分，初始为0

    public static void startClassicDetailActivity(Context context, ClassicCaseBean bean) {
        Intent intent = new Intent(context, ClassicCaseDetailActivity.class);
        intent.putExtra(TAG, bean);
        context.startActivity(intent);
    }


    @Override
    public int setLayout() {
        return R.layout.activity_classic_case_detail;
    }

    @Override
    public void initView() {
        supportFragmentManager = getSupportFragmentManager();
        mSegmentHorzontal.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                changefragment(index);
            }
        });
        mHead.setRigthIMBVisable();// 设置顶部 右边iamgebutton可用
        searchEnableScore();
        mHead.setRightIBMOnClickListener(new HeadView.myLeftIBMClickListener() {

            @Override
            public void rightIButtonClick() {
                showScoreDialog();
            }
        });
    }

    @Override
    protected void initData() {
        ClassicCaseBean classicCaseBean = getClassicCaseBean();
        if (classicCaseBean != null) {
            setAuthor(classicCaseBean.getCASEAUTHOR());
            setTitle(classicCaseBean.getCASENAME());
            setScore(classicCaseBean.getCASESCORE() + "");
            setRatingLevel(classicCaseBean.getCASESCORE() + "");
            setImage(classicCaseBean.getFILEURL());
        }
        changefragment(0);//初始化定位到资源列表
    }

    private ClassicCaseBean getClassicCaseBean() {
        Intent intent = getIntent();
        return (ClassicCaseBean) intent.getSerializableExtra(TAG);
    }

    @Override
    public void changefragment(int index) {
        if (resourceListFragment == null) {
            resourceListFragment = ResourceListFragment.newInstance(getClassicCaseBean());
        }
        if (commentFragment == null && index != 0) {
            commentFragment = CommentFragment.newInstance(getClassicCaseBean());
        }
        Fragment fg = null;
        if (index == 0) {
            fg = resourceListFragment;

        } else if (index == 1) {
            fg = commentFragment;
        }
        if (currentFragment != null) {
            supportFragmentManager.beginTransaction().hide(currentFragment).commit();
        }
        // 如果之前没有添加过
        if (fg != null) {
            if (fg.isAdded()) {
                supportFragmentManager.beginTransaction().show(fg).commit();
            } else {
                supportFragmentManager.beginTransaction().add(R.id.fl_class_case_main, fg).commit();
            }
        }
        currentFragment = fg;
    }

    @Override
    public void setImage(String url) {
        ImageLoaderUtil.displayBlurBitmap(this, ll_head_bg, url);
        ImageLoaderUtil.displayBitmap(this, iv_icon, url, false);
    }

    @Override
    public void setRatingLevel(String ratingLevel) {
        int mark = 0;
        if (sv_star != null) {
            try {
                if (!TextUtils.isEmpty(ratingLevel)) {
                    mark = (int) (Float.parseFloat(ratingLevel) + 0.5);
                }
            } catch (Exception e) {
                e.printStackTrace();
//                throw new RuntimeException("分数转化出错");
            } finally {
                sv_star.setStar(mark);
            }
        }
    }

    /**
     * 获取是否评分了
     */
    protected void searchEnableScore() {
        Map<String, String> datas = new HashMap<String, String>();
        ClassicCaseBean bean = getClassicCaseBean();
        if (bean != null) {
            datas.put("CASECODE", bean.getSEQKEY());
        }
        datas.put("USERCODE", DataUtils.getUserInfo().getUSERCODE());
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        String whereSql = "";
        parametersMap.put("CLASSNAME", UrlMethod.CASE_SCORE);
        parametersMap.put("METHOD", UrlMethod.SEARCH);
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", whereSql);
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("MASTERTABLE", "KLB_CLASSICCASE_SCORE");
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_CLASSICCASE_SCORE, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("是否评分"+response);
                    try {
                        JsonElement datas = JsonUtils.decoElementJSONObject(response, "DATAS");
                        JSONObject jsonObject = new JSONObject(datas.toString());
                        JSONObject jsonObject2 = jsonObject
                                .getJSONObject("KLB_CLASSICCASE_SCORE");
                        String string = jsonObject2.getString("totalCount");
                        if (!"0".equals(string)) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    mHead.setRigthIMBSrcAndAble(false,
                                            R.mipmap.ic_flower_pressed);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("评分" + error);
                    showToast("评分失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showToast("评分失败");
        }

    }
    /**
     * 评分弹框
     */

    protected void showScoreDialog() {
        score = 0;
        final Builder builder = new AlertDialog.Builder(this);
        View inflate = UIUtils.inflate(R.layout.score_dialog);
        RatingBarView ratingBarView = (RatingBarView) inflate
                .findViewById(R.id.sv_star);
        ratingBarView.setmClickable(true);
        ratingBarView.setOnRatingListener(new RatingBarView.OnRatingListener() {

            @Override
            public void onRating(Object bindObject, int RatingScore) {
                score = RatingScore;
            }
        });
        builder.setView(inflate);
        builder.setCancelable(false);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog create = builder.create();
        create.show();
        create.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (score != 0) {
                            commitScore();
                            create.dismiss();
                        } else {
                            showToast("请选择分数");
                        }
                    }
                });
    }

    @Override
    public void setScore(String score) {
        if (tv_score != null) {
            String mark = "0";
            if (!TextUtils.isEmpty(score)) {
                mark = score;
            }
            tv_score.setText(mark + "分");
        }
    }

    @Override
    public void setTitle(String title) {
        if (tv_title != null) {
            tv_title.setText(title);
        }
    }

    @Override
    public void setAuthor(String author) {
        if (tv_maker != null) {
            tv_maker.setText(author);
        }
    }

    @Override
    public void setColloctionIcon() {

    }

    @Override
    public void setScoreIcon() {

    }

    /**
     * 提交评分
     */
    protected void commitScore() {
        Map<String, String> datas = new HashMap<String, String>();
        ClassicCaseBean classicCaseBean = getClassicCaseBean();
        if (classicCaseBean != null) {
            datas.put("CASECODE", classicCaseBean.getSEQKEY());
        }
        datas.put("CASESCORE", score + "");
        datas.put("USERCODE", DataUtils.getUserInfo().getUSERCODE());
        // 参数区域赋值
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        // String whereSql =
        // "ISLOSE=0 and ISEXPERTANSWER = 2 and QUESTIONEXPLAIN like '%"
        // + searchStr + "%'";
        parametersMap.put("CLASSNAME", UrlMethod.CASE_SCORE);
        parametersMap.put("METHOD", UrlMethod.ADDSAVE);// updateSave,delete,search
        parametersMap.put("MENUAPP", "EMARK_APP");
        parametersMap.put("WHERESQL", "");
        parametersMap.put("ORDERSQL", "SYSCREATEDATE desc");
        parametersMap.put("MASTERTABLE", TableName.KLB_CLASSICCASE_SCORE);
        parametersMap.put("DETAILTABLE", "");
        parametersMap.put("MASTERFIELD", "SEQKEY");
        parametersMap.put("DETAILFIELD", "");
        parametersMap.put("start", "0");
        parametersMap.put("limit", "20");
        JSONObject jsonObject;
        try {
            jsonObject = ParamUtils.params2JsonObject(TableName.KLB_CLASSICCASE_SCORE, datas, parametersMap);
            HttpUtils.doPost(HttpContacts.UI_URL, jsonObject, new MyStringCallBack() {

                @Override
                public void onSuccess(String response) {
                    LogUtils.e("评分" + response);
                    showToast("评分成功");
                    mHead.setRigthIMBSrcAndAble(false,
                            R.mipmap.ic_flower_pressed);
                }

                @Override
                public void onFailure(String error) {
                    LogUtils.e("评分" + error);
                    showToast("评分失败");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showToast("评分失败");
        }
    }

}
