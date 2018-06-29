package com.zc.pickuplearn.ui.group.widget.prodetail.widget;

import android.content.Context;
import android.view.View;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.Professor;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.group.widget.prodetail.view.ProfessorDetailActivity;
import com.zc.pickuplearn.utils.SystemUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者： Jonsen
 * 时间: 2017/4/5 11:06
 * 联系方式：chenbin252@163.com
 */

public class ProfessorAdapter extends CommonAdapter<Professor> {

    private Context mContext;
    private int screenWidth;

    public ProfessorAdapter(Context context, List<Professor> datas) {
        this(context, R.layout.item_procircle_detail_professor, datas);
        screenWidth = SystemUtils.getScreenWidth(context);
    }

    public ProfessorAdapter(Context context, int layoutId, List<Professor> datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final Professor s, int position) {
        holder.setText(R.id.tv_professor_name, s.getUSERNAME());
        CircleImageView icon = holder.getView(R.id.iv_professor_icon);
//  ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
//        layoutParams.height = (int) (screenWidth/3*1.1);
//        icon.setLayoutParams(layoutParams);
        ImageLoaderUtil.displayCircleView(mContext, icon, s.getFILEURL(), false);
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfessorDetailActivity.startProfessorDetail(mContext, s);
            }
        });
    }
}
