package com.zc.pickuplearn.ui.teamcircle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.TeamMsgStatsUserBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.utils.PictureUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 团队问答适配器
 * 作者： Jonsen
 * 时间: 2017/3/15 17:05
 * 联系方式：chenbin252@163.com
 */

public class TeamMsgStatusPersonListAapter extends CommonAdapter<TeamMsgStatsUserBean> {
    private List<TeamMsgStatsUserBean> mDatas;
    private Context mContext;
    private final UserBean userInfo;
    private SparseArray<Bitmap> bitmaps ;
    public TeamMsgStatusPersonListAapter(Context context, List<TeamMsgStatsUserBean> datas) {
        this(context, R.layout.item_team_msg_status_person, datas);
        bitmaps = new SparseArray<>();
    }

    private TeamMsgStatusPersonListAapter(Context context, int layoutId, List<TeamMsgStatsUserBean> datas) {
        super(context, layoutId, datas);
        userInfo = DataUtils.getUserInfo();
        mDatas = datas;
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final TeamMsgStatsUserBean teamRankPersonBean, final int position) {
        holder.setVisible(R.id.iv_rank, false);
        holder.setVisible(R.id.tv_rank, true);
        holder.setText(R.id.tv_rank, position + "");
        CircleImageView view = holder.getView(R.id.civ_head);
        if (TextUtils.isEmpty(teamRankPersonBean.getFILEURL())) {
            String substring ="匿";
            if (!TextUtils.isEmpty(teamRankPersonBean.getUSERNAME())){
                 substring = teamRankPersonBean.getUSERNAME().substring(0, 1);
            }
            if (bitmaps.get(position)==null){
              bitmaps.put(position,PictureUtil.textAsBitmap(mContext,substring,50));
            }
            view.setImageBitmap(bitmaps.get(position));
        } else {
            ImageLoaderUtil.displayImage(mContext, view, ResultStringCommonUtils.subUrlToWholeUrl(teamRankPersonBean.getFILEURL()), R.mipmap.default_user_circle_icon);
        }
        holder.setText(R.id.tv_status, teamRankPersonBean.getUSERNAME());
//        switch (teamRankPersonBean.getMESFLAG()){
//            case "1":
//                holder.setText(R.id.tv_status,"未回执");
//                break;
//            case "2":
//                holder.setText(R.id.tv_status,"已阅读");
//                break;
//            case "3":
//                holder.setText(R.id.tv_status,"已回执");
//                break;
//        }
    }

}
