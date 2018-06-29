package com.zc.pickuplearn.ui.group.widget.prodetail.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.Professor;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.group.widget.prodetail.view.ProfessorDetailActivity;

import java.util.List;

/**
 * 作者： Jonsen
 * 时间: 2017/4/5 14:27
 * 联系方式：chenbin252@163.com
 */

public class ProfessorListAdapter extends CommonAdapter<Professor> {
    private Context mContext;
    public ProfessorListAdapter(Context context, List<Professor> datas) {
        this(context, R.layout.item_professor_list, datas);
        mContext =context;
    }

    public ProfessorListAdapter(Context context, int layoutId, List<Professor> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final Professor professor, int position) {
        holder.setText(R.id.tv_name, professor.getUSERNAME());
        holder.setText(R.id.tv_good_field, professor.getGOODFIELD());
        ImageView view = holder.getView(R.id.iv_team_list_icon);
        ImageLoaderUtil.displayBitmap(mContext,view,professor.getFILEURL(),false);
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfessorDetailActivity.startProfessorDetail(mContext,professor);
            }
        });
    }
}
