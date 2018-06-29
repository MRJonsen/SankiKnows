package com.zc.pickuplearn.ui.teamcircle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dalong.francyconverflow.FancyCoverFlow;
import com.dalong.francyconverflow.FancyCoverFlowAdapter;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyFancyCoverFlowAdapter extends FancyCoverFlowAdapter {
    private Context mContext;

    public List<TeamCircleBean> list;

    public MyFancyCoverFlowAdapter(Context context, List<TeamCircleBean> list) {
        mContext = context;
        this.list = list;
    }

    @Override
    public View getCoverFlowItem(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fancycoverflow, null);
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            convertView.setLayoutParams(new FancyCoverFlow.LayoutParams(width / 3, FancyCoverFlow.LayoutParams.WRAP_CONTENT));
            holder = new ViewHolder();
            holder.product_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.ll_root = (LinearLayout) convertView.findViewById(R.id.ll_root);
            holder.iv_team_icon = (CircleImageView) convertView.findViewById(R.id.iv_team_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TeamCircleBean item = getItem(position);
        holder.product_name.setText(item.getTEAMCIRCLENAME());
        if (!TextUtils.isEmpty(item.getFILEURL())) {
            Glide.with(mContext)
                    .load(ResultStringCommonUtils.subUrlToWholeUrl(item.getFILEURL()))
                    .dontAnimate()
                    .placeholder(R.mipmap.team_default_icon_3)
                    .into(holder.iv_team_icon);
//            ImageLoaderUtil.showCirCleViewImageResource(mContext, holder.iv_team_icon, item.getFILEURL(),false);
        } else {
            holder.iv_team_icon.setImageResource(R.mipmap.team_default_icon_3);
        }
        //给位置0 最后一个设置
        holder.product_name.setVisibility(View.VISIBLE);
        holder.iv_team_icon.setVisibility(View.VISIBLE);
        if (position % list.size() == 0) {
            holder.ll_root.setBackgroundResource(R.mipmap.add_team);
            holder.product_name.setVisibility(View.INVISIBLE);
            holder.iv_team_icon.setVisibility(View.INVISIBLE);
        }
        if (list != null && list.size() > 0 && position % list.size() == list.size() - 1) {
            holder.ll_root.setBackgroundResource(R.mipmap.add_team);
            holder.product_name.setVisibility(View.INVISIBLE);
            holder.iv_team_icon.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public void setSelected(int position) {
        position = position % list.size();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i == position) {
//                    list.get(i).setSelected(true);
                } else {
//                    list.get(i).setSelected(false);
                }
            }
        }
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
//        return Integer.MAX_VALUE;//无限循环
        return list==null?0:list.size();
    }

    @Override
    public TeamCircleBean getItem(int i) {
        return list.get(i % list.size());
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    static class ViewHolder {
        TextView product_name;
        LinearLayout ll_root;
        CircleImageView iv_team_icon;
    }
}
