package com.zc.pickuplearn.ui.group.widget.prodetail.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.application.JChatDemoApplication;
import com.zc.pickuplearn.beans.Professor;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.dynamic.widget.theMoreDynamic.view.SearchActivity;
import com.zc.pickuplearn.ui.im.chatting.ChatActivity;
import com.zc.pickuplearn.ui.question_and_answer.TypeQuestion;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.UIUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfessorDetailActivity extends BaseActivity {

    @BindView(R.id.civ_icon)
    CircleImageView civ_icon;
    @BindView(R.id.tv_professor_name)
    TextView tvProfessorName;
    @BindView(R.id.tv_professor_introduce)
    TextView tvProfessorIntroduce;
    @BindView(R.id.tv_professional)
    TextView tvProfessional;
    @BindView(R.id.tv_good_field)
    TextView tvGoodField;
    private final static String TAG = "ProfessorDetailActivity";
    @BindView(R.id.iv_left)
    ImageButton ivLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_expert_grade)
    TextView tv_expert_grade;
    @BindView(R.id.tv_expert_score)
    TextView tv_expert_score;  @BindView(R.id.tv_expert_online_status)
    TextView tv_expert_online_status;
    @BindView(R.id.root)
    LinearLayout root;
    private Professor professor;
    private UserBean opertor;

    public static void startProfessorDetail(Context context, Professor professor) {
        Intent intent = new Intent(context, ProfessorDetailActivity.class);
        intent.putExtra(TAG, professor);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_professor_detail;
    }

    @Override
    public void initView() {
        initStatusBar(root);
        initHead();
    }


    private void initHead() {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        tvTitle.setText("专家信息");
    }

    @Override
    protected void initData() {
        professor = (Professor) getIntent().getSerializableExtra(TAG);
        opertor = DataUtils.getUserInfo();
        setData(professor);
    }

    private void setData(Professor professor) {
        ImageLoaderUtil.showHeadView(this, ResultStringCommonUtils.subUrlToWholeUrl(professor.getFILEURL()), civ_icon);
        tvProfessorIntroduce.setText(professor.getDESCRIBE());
        tvGoodField.setText(professor.getGOODFIELD());
        tvProfessional.setText(professor.getPROFESSIONAL());
        tvProfessorName.setText(professor.getUSERNAME());
        tv_expert_grade.setText(professor.getGRADENAME());
        tv_expert_score.setText("0.0");
        tv_expert_online_status.setText( "1".equals(professor.getISONLINE()) ? "" : "[离线请留言]");
        if (!TextUtils.isEmpty(professor.getSCORE())) {
            double v = Double.parseDouble(professor.getSCORE());
            DecimalFormat df = new DecimalFormat("######0.0");
            String format = df.format(v);
            tv_expert_score.setText(format);
        }
    }

    @OnClick({R.id.ll_professor_online, R.id.ll_professor_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_professor_history:
                UserBean userBean = new UserBean();
                userBean.setUSERCODE(professor.getUSERCODE());
                SearchActivity.starAction(this, TypeQuestion.EXPERT, userBean);
//                ProfessorHistroyAnswerActivity.startProfessorHistoryAnswerActivity(this, professor);//专家历时解答
                break;
            case R.id.ll_professor_online:
                if (opertor.getUSERCODE().equals(professor.getUSERCODE())) {
                    ProfessTalkListActivity.startProfessorttalkActivity(this);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(JChatDemoApplication.CONV_TITLE, professor.getUSERNAME());
                    intent.putExtra(JChatDemoApplication.TARGET_ID, professor.getUSERCODE());
                    intent.putExtra(JChatDemoApplication.TARGET_APP_KEY, UIUtils.getString(R.string.APP_KEY));
                    intent.setClass(this, ChatActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
