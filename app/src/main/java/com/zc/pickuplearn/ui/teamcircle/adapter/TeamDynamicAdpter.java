package com.zc.pickuplearn.ui.teamcircle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.teamcircle.view.TeamDetailActivity;
import com.zc.pickuplearn.ui.teamcircle.view.TeamWenDaFragment;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.UIUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 团队动态适配器
 * 作者： Jonsen
 * 时间: 2017/3/13 21:08
 * 联系方式：chenbin252@163.com
 */

public class TeamDynamicAdpter extends CommonAdapter<TeamDynamicBean> {

    public TeamDynamicAdpter(Context context, List<TeamDynamicBean> datas) {
        this(context, R.layout.item_team_dynamic, datas);
    }

    private TeamDynamicAdpter(Context context, int layoutId, List<TeamDynamicBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final TeamDynamicBean teamDynamicBean, int position) {
        holder.setText(R.id.tv_name, teamDynamicBean.getTEAMCIRCLENAME());
//        holder.setText(R.id.tv_time, DateUtils.dataFormatNow("yyyy-MM-dd", teamDynamicBean.getSYSCREATEDATE()));
        holder.setText(R.id.tv_time, DateUtils.getCompareDate(teamDynamicBean.getSYSCREATEDATE()));
        if ("1".equals(teamDynamicBean.getDYNAMICTYPE())) {
            holder.setText(R.id.tv_dynamic_content, "你的小伙伴又有新的问题需要你的帮助，快来帮助他吧!");
        } else if ("2".equals(teamDynamicBean.getDYNAMICTYPE())) {
            holder.setText(R.id.tv_dynamic_content, "有人回答了你的问题，快去看看吧!");
        } else if ("3".equals(teamDynamicBean.getDYNAMICTYPE())) {
            holder.setText(R.id.tv_dynamic_content, "有人对你提问了，快去挑战吧!");
        }
        CircleImageView circleImageView = holder.getView(R.id.iv_icon);
        if (!TextUtils.isEmpty(teamDynamicBean.getFILEURL())) {
            setIcon(position, circleImageView, ResultStringCommonUtils.subUrlToWholeUrl(teamDynamicBean.getFILEURL()));
        } else {
            setIcon(position, circleImageView, "");
        }
        holder.setOnClickListener(R.id.item, new View.OnClickListener() { //设置条目点击跳转方法
            @Override
            public void onClick(View v) {
                API.getAnMyJoinTeamList(teamDynamicBean, new CommonCallBack<List<TeamCircleBean>>() {
                    @Override
                    public void onSuccess(List<TeamCircleBean> teamCircleBeen) {
                        LogUtils.e("动态条目点击");
                        if (teamCircleBeen != null && teamCircleBeen.size() > 0) {
                            TeamWenDaFragment.TeamWenDaType type = TeamWenDaFragment.TeamWenDaType.ACTION;
                            if ("1".equals(teamDynamicBean.getDYNAMICTYPE())) {
                                type = TeamWenDaFragment.TeamWenDaType.ACTION;
                            } else if ("2".equals(teamDynamicBean.getDYNAMICTYPE())) {
                                type = TeamWenDaFragment.TeamWenDaType.QUSETION;
                            } else if ("3".equals(teamDynamicBean.getDYNAMICTYPE())) {
                                type = TeamWenDaFragment.TeamWenDaType.CHALLENGE;
                            }
                            TeamCircleBean teamCircleBean = teamCircleBeen.get(0);
                            teamCircleBean.setSEQKEY(teamCircleBean.getTEAMCODE());
                            TeamDetailActivity.starTeamDetailActivity(mContext, teamCircleBean, type);
                        }
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
    }

    /**
     * 设置头像 边框和默认图片
     *
     * @param position
     * @param circleImageView
     * @param url             功能选择 1，默认图 2，边框
     */
    private void setIcon(int position, CircleImageView circleImageView, String url) {
        switch (position % 4) {
            case 0:
                circleImageView.setBorderColor(UIUtils.getResources().getColor(R.color.team_border_4));
                Glide.with(mContext).load(url).placeholder(R.mipmap.msg_type_team_4)
                        .error(R.mipmap.msg_type_team_4).fitCenter().crossFade().into(circleImageView);
                break;
            case 1:
                circleImageView.setBorderColor(UIUtils.getResources().getColor(R.color.team_border_1));
                Glide.with(mContext).load(url).placeholder(R.mipmap.msg_type_team_1)
                        .error(R.mipmap.msg_type_team_1).fitCenter().crossFade().into(circleImageView);
                break;
            case 2:
                circleImageView.setBorderColor(UIUtils.getResources().getColor(R.color.team_border_2));
                Glide.with(mContext).load(url).placeholder(R.mipmap.msg_type_team_2)
                        .error(R.mipmap.msg_type_team_2).fitCenter().crossFade().into(circleImageView);
                break;
            case 3:
                circleImageView.setBorderColor(UIUtils.getResources().getColor(R.color.team_border_3));
                Glide.with(mContext).load(url).placeholder(R.mipmap.msg_type_team_3)
                        .error(R.mipmap.msg_type_team_3).fitCenter().crossFade().into(circleImageView);
                break;
        }
    }
}
