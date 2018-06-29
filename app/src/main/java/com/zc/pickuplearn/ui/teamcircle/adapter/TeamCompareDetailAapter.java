package com.zc.pickuplearn.ui.teamcircle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.TeamRankPersonBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.teamcircle.view.TeamCompareDetailFragment.Type;
import com.zc.pickuplearn.utils.PictureUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 团队比一比 榜 适配器
 * 作者： Jonsen
 * 时间: 2017/3/15 17:05
 * 联系方式：chenbin252@163.com
 */

public class TeamCompareDetailAapter extends CommonAdapter<TeamRankPersonBean> {
    private List<TeamRankPersonBean> mDatas;
    private Context mContext;
    private final UserBean userInfo;
    private TeamCircleBean mTeamCircleBean;
    private Type FROM;
    private SparseArray<Bitmap> bitmaps;

    public TeamCompareDetailAapter(Context context, List<TeamRankPersonBean> datas, TeamCircleBean teamCircleBean, Type from) {
        this(context, R.layout.item_team_compare_item, datas);
        mTeamCircleBean = teamCircleBean;
        FROM = from;
        bitmaps = new SparseArray<>();
    }

    private TeamCompareDetailAapter(Context context, int layoutId, List<TeamRankPersonBean> datas) {
        super(context, layoutId, datas);
        userInfo = DataUtils.getUserInfo();
        mDatas = datas;
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final TeamRankPersonBean teamRankPersonBean, final int position) {
        holder.setVisible(R.id.iv_rank, false);
        holder.setVisible(R.id.tv_rank, true);
        switch (position) {
            case 1:
                holder.setVisible(R.id.iv_rank, true);
                holder.setImageResource(R.id.iv_rank, R.mipmap.yy_one);
                holder.setVisible(R.id.tv_rank, false);
                break;
            case 2:
                holder.setVisible(R.id.iv_rank, true);
                holder.setImageResource(R.id.iv_rank, R.mipmap.yy_two);
                holder.setVisible(R.id.tv_rank, false);
                break;
            case 3:
                holder.setVisible(R.id.iv_rank, true);
                holder.setImageResource(R.id.iv_rank, R.mipmap.yy_three);
                holder.setVisible(R.id.tv_rank, false);
                break;
        }
        holder.setText(R.id.tv_rank, position + "");
        CircleImageView view = holder.getView(R.id.civ_head);
        if (TextUtils.isEmpty(teamRankPersonBean.getFILEURL())) {
            String substring = teamRankPersonBean.getUSERNAME().substring(0, 1);
            if (bitmaps.get(position) == null) {
                bitmaps.put(position, PictureUtil.textAsBitmap(mContext, substring, 50));
            }
            view.setImageBitmap(bitmaps.get(position));
        } else {
            ImageLoaderUtil.displayImage(mContext, view, ResultStringCommonUtils.subUrlToWholeUrl(teamRankPersonBean.getFILEURL()),R.mipmap.default_user_circle_icon);
        }
        holder.setText(R.id.tv_name, teamRankPersonBean.getUSERNAME());
        if (Type.QUESTION.equals(FROM)) {
            holder.setText(R.id.tv_count, teamRankPersonBean.getQUESTIONSUM() + "个");
        } else if (Type.ANSWER.equals(FROM)) {
            holder.setText(R.id.tv_count, teamRankPersonBean.getANSWERSUM() + "个");
        }
    }

}
