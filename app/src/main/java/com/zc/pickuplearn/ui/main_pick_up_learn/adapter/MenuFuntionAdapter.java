package com.zc.pickuplearn.ui.main_pick_up_learn.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.FunctionBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.main_pick_up_learn.FunctionEditActivity;
import com.zc.pickuplearn.ui.view.drag.DragAdapterInterface;

import java.util.ArrayList;
import java.util.List;


public class MenuFuntionAdapter extends BaseAdapter implements DragAdapterInterface {
    private boolean IsEdit = false;
    private List<FunctionBean> datas = new ArrayList<FunctionBean>();
    private FunctionEditActivity context;

    public MenuFuntionAdapter(FunctionEditActivity context, List<FunctionBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setDatas(List<FunctionBean> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FunctionBean bean = datas.get(position);
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_item, null);
            holder.deleteImg = (ImageView) convertView.findViewById(R.id.delete_img);
            holder.iconImg = (ImageView) convertView.findViewById(R.id.icon_img);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.container = convertView.findViewById(R.id.item_container);
            holder.newImg = (ImageView) convertView.findViewById(R.id.iv_new_tag);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
//        if ("1".equals(bean.getIS_SPREAD())) {
//            holder.newImg.setVisibility(View.VISIBLE);
//        } else {
//            holder.newImg.setVisibility(View.GONE);
//        }
        holder.deleteImg.setImageResource(R.mipmap.menu_add);
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.AddMenu(bean);
            }
        });
        holder.iconImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsEdit) {
                    context.AddMenu(bean);
                }
            }
        });
        if (IsEdit) {
            holder.deleteImg.setVisibility(View.VISIBLE);
        } else {
            holder.deleteImg.setVisibility(View.GONE);
        }
        //获取资源图片
        ImageLoaderUtil.display(context, (ImageView) holder.iconImg, bean.getFUNCTIONURL());
        holder.nameTv.setText(bean.getFUNCTIONNAME());
        holder.container.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    static class Holder {
        private ImageView newImg;
        private ImageView deleteImg;
        private ImageView iconImg;
        private TextView nameTv;
        private View container;
    }

    @Override
    public void reOrder(int startPosition, int endPosition) {
        if (endPosition < datas.size()) {
            FunctionBean object = datas.remove(startPosition);
            datas.add(endPosition, object);
            notifyDataSetChanged();
        }
    }

    public void setEdit() {
        IsEdit = true;
        notifyDataSetChanged();
    }

    public void getDatas() {
        for (FunctionBean data : datas) {
            // DebugLog.i("点击 Item " + data.getId());
        }
    }

    public void endEdit() {
        IsEdit = false;
        notifyDataSetChanged();
    }

    public boolean getEditStatue() {
        return IsEdit;
    }


}
