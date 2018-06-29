package com.zc.pickuplearn.ui.question.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.http.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 提问 修改问题 图片选择适配器
 * Created by chenbin on 2017/10/18.
 */

public class GVAdapter extends BaseAdapter {
    public static final String IMG_ADD_TAG = "ADD_IMAGE";
    private Context mContext;
    private List<String> mData;

    public GVAdapter(Context context, List<String> urls) {
        mContext = context;
        mData = urls;
        if (mData != null)
            mData.add(IMG_ADD_TAG);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_add_photo_gv_items, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.main_gridView_item_photo);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.main_gridView_item_cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String url = mData.get(position);
        if (!IMG_ADD_TAG.equals(url)) {
            holder.checkBox.setVisibility(View.VISIBLE);
            ImageLoaderUtil.showImageView(mContext, url, holder.imageView);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            ImageLoaderUtil.showImageViewByResourceId(mContext, R.mipmap.icon_addpic_unfocused, holder.imageView);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(mData.get(position));
            }
        });
        return convertView;
    }


    public List<String> getData() {
        ArrayList<String> strings = new ArrayList<>();
        strings.addAll(mData);
        if (strings.contains(IMG_ADD_TAG))
            strings.remove(IMG_ADD_TAG);
        return strings;
    }

    private void removeData(String url) {
        mData.remove(url);
        isNeedAdd();
        notifyDataSetChanged();
    }

    public void addData(String url) {
        if (mData.size() < 4) {
            mData.remove(IMG_ADD_TAG);
            if (!mData.contains(url))
                mData.add(url);
        }
        isNeedAdd();
        notifyDataSetChanged();
    }
    public void addData(List<String> urls) {
        mData.clear();
        mData.addAll(urls);
        isNeedAdd();
        notifyDataSetChanged();
    }
    private void isNeedAdd() {
        if (mData.size() == 3) {
            mData.remove(IMG_ADD_TAG);
        } else if (mData.size() < 3) {
            if (!mData.contains(IMG_ADD_TAG))
                mData.add(IMG_ADD_TAG);
        }
    }

    private class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }
}
