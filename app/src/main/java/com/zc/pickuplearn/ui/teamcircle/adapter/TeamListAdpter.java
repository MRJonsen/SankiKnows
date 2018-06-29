package com.zc.pickuplearn.ui.teamcircle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.teamcircle.view.TeamInfoEditActivity;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/3/13 21:08
 * 联系方式：chenbin252@163.com
 */

public class TeamListAdpter extends CommonAdapter<TeamCircleBean> {
    private List<TeamCircleBean> mDatas;
    private Context mContext;

    public TeamListAdpter(Context context, int layoutId, List<TeamCircleBean> datas) {
        super(context, layoutId, datas);
        mDatas = datas;
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final TeamCircleBean teamCircleBean, final int position) {
        holder.setText(R.id.tv_team_name, teamCircleBean.getTEAMCIRCLENAME());
        holder.setText(R.id.tv_team_tip, teamCircleBean.getTEAMCIRCLECODENAME());
        holder.setText(R.id.tv_team_member_number, TextUtils.isEmpty(teamCircleBean.getTEAMMEMBERS()) ? "0" : teamCircleBean.getTEAMMEMBERS());
        holder.setText(R.id.tv_team_claim, teamCircleBean.getTEAMMANIFESTO());
        ImageView iconView = holder.getView(R.id.iv_team_list_icon);
        if (!TextUtils.isEmpty(teamCircleBean.getFILEURL())) {
            ImageLoaderUtil.display(mContext,iconView,teamCircleBean.getFILEURL());
//            ImageLoaderUtil.displayBitmap(mContext, iconView, teamCircleBean.getFILEURL(), false);
        } else {
//            iconView.setBackgroundResource(R.mipmap.team_default_icon);
            Glide.with(mContext).load(R.mipmap.team_default_icon).into(iconView);
        }
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamInfoEditActivity.startTeamEditInfoActivity(mContext, teamCircleBean);
            }
        });
        final Button checkBox = holder.getView(R.id.cb_join);//加入团队按钮
        checkBox.setBackgroundResource(R.drawable.item_join_team_unpress);
        if (Type.join.equals(teamCircleBean.getSTATUS())) {
            checkBox.setText("申请加入");
        } else if (Type.joining.equals(teamCircleBean.getSTATUS())) {
            checkBox.setText("审核中");
            checkBox.setBackgroundResource(R.drawable.item_join_team_press);
        } else if (Type.joined.equals(teamCircleBean.getSTATUS())) {
            checkBox.setText("已加入");
            checkBox.setBackgroundResource(R.drawable.item_join_team_press);
        } else if (Type.builder.equals(teamCircleBean.getSTATUS())) {
            checkBox.setText("已加入");
            checkBox.setBackgroundResource(R.drawable.item_join_team_press);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Type.join.equals(teamCircleBean.getSTATUS())) {
//                    LogUtils.e("buttoncheck----------->>>>>");
                    API.applyJoinTeam(teamCircleBean, new CommonCallBack<String>() {
                        @Override
                        public void onSuccess(String s) {
                            teamCircleBean.setSTATUS(Type.joining);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                } else {
//                    LogUtils.e("buttonchec-----------false>>>>>");
                }
            }
        });
    }

    /**
     * 加入团队的状态
     */
    //status   0:未加入   1:审核中    2:一加入   3:已创建
    public interface Type {
        String join = "0";
        String joining = "1";
        String joined = "2";
        String builder = "3";
    }
}
