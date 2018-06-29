package com.zc.pickuplearn.ui.teamcircle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.beans.QuestionTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签适配器
 */
public class TeamWenDaTipGridAdapter extends BaseAdapter {
    private List<QuestionTypeBean> mList = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;
    private QuestionBean mQuestionBean;
    public TeamWenDaTipGridAdapter(Context context, List<QuestionTypeBean> list, QuestionBean questionBean) {
        super();
        mList = list;
        mQuestionBean = questionBean;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO 自动生成的方法存根
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO 自动生成的方法存根
        return mList.get(position);
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
            convertView = mInflater.inflate(R.layout.item_tip, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_tip_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final Holder finalHolder = holder;
//        holder.textView.setOnClickListener(new View.OnClickListener() {//标签点击事件
//            @Override
//            public void onClick(View v) {
//                mQuestionBean.setQUESTIONTYPECODE(mList.get(position).getQUESTIONTYPECODE());
//                mQuestionBean.setQUESTIONTYPENAME(mList.get(position).getQUESTIONTYPENAME());
//                DynamicMoreActivity.startDynamicMoreActivity(context, "", mQuestionBean, "");//根据标签分类去找找问题
//            }
//        });
        holder.textView.setText(mList.get(position).getQUESTIONTYPENAME());
        return convertView;
    }

    private class Holder {
        TextView textView;
    }
}
