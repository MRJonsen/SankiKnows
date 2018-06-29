package com.zc.pickuplearn.ui.teamcircle.adapter;


import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.TeamMessageBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.teamcircle.view.TeamMsgDetailActivity;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DateUtils;

import java.util.List;


/**
 * 团队通知管理列表适配器
 * Created by Administrator on 2017/5/9 0009.
 */

public class TeamMessageListAdapter extends CommonAdapter<TeamMessageBean> {

    private Context mContext;

    public TeamMessageListAdapter(Context context, List<TeamMessageBean> datas) {
        this(context, R.layout.item_team_msg_list, datas);
        mContext = context;
    }

    private TeamMessageListAdapter(Context context, int layoutId, List<TeamMessageBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final TeamMessageBean teamMessageBean, int position) {
        holder.setText(R.id.tv_msg_content, teamMessageBean.getMESSAGE());
        holder.setText(R.id.tv_pusher, teamMessageBean.getUSERNAME());
        holder.setText(R.id.tv_time, DateUtils.dataFormatNow("yyyy-MM-dd", teamMessageBean.getSYSCREATEDATE()));
        LinearLayout ll_back = holder.getView(R.id.ll_need_back);
        String mestype = teamMessageBean.getISRECEIPT();
        if ("1".equals(mestype)) {
            ll_back.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_back_num, teamMessageBean.getRECEIPTCOUNT());
        } else {
            ll_back.setVisibility(View.INVISIBLE);
        }
        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeamMsgDetailActivity.startTeamMsgDetailActivity(mContext, teamMessageBean);
            }
        });
        holder.setOnLongClickListener(R.id.ll_item, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAlert(teamMessageBean);
                return true;
            }
        });
    }

    private void showAlert(final TeamMessageBean teamMessageBean) {
        AlertDialog alertDialog = new AlertDialog(mContext).builder();
        alertDialog
                .setTitle("提示").setMsg("是否删除?")
                .setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteData(teamMessageBean);
                    }
                }).setNegativeButton("否", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        alertDialog.show();
    }

    private void deleteData(final TeamMessageBean teamMessageBean) {
        API.deletTeamMessage(false, teamMessageBean, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                mDatas.remove(teamMessageBean);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
