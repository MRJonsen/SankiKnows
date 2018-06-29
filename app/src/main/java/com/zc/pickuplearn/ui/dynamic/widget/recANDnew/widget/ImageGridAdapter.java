package com.zc.pickuplearn.ui.dynamic.widget.recANDnew.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.previewphoto.PreViewImageActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageGridAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater mInflater;

    public ImageGridAdapter(Context context, String urlsource) {
        super();
        list = ResultStringCommonUtils.doSplitUrls(urlsource);
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO 自动生成的方法存根
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO 自动生成的方法存根
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO 自动生成的方法存根
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO 自动生成的方法存根
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.image_gridview_item, null);
            holder.image = (ImageView) convertView.findViewById(R.id.image_gridview_item_image);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final Holder finalHolder = holder;
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> strings = new ArrayList<>();
                for (String url :
                        list) {
                    String imgUrl = ResultStringCommonUtils.subUrlToWholeUrl(url);
                    strings.add(imgUrl);
                }
                PreViewImageActivity.startPreView(context, strings, position);
            }
        });
        ImageLoaderUtil.display(context, holder.image, list.get(position));
        return convertView;
    }

    private class Holder {
        ImageView image;
    }
}
