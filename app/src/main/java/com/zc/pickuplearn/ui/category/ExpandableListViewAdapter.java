package com.zc.pickuplearn.ui.category;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 自定义Adapter
 * 
 * 
 */
@SuppressLint("InflateParams")
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

	LayoutInflater mInflater;
	Context context;
	List<QusetionTypeBean> mGroup;
	HashMap<QusetionTypeBean, List<QusetionTypeBean>> mChild;

	public ExpandableListViewAdapter(Context context,
			List<QusetionTypeBean> list,
			HashMap<QusetionTypeBean, List<QusetionTypeBean>> dataHashMap) {
		mInflater = LayoutInflater.from(context);
		this.context = context;
		mGroup = list;
		mChild = dataHashMap;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// return child[groupPosition][childPosition];
		return mChild.get(mGroup.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewChild = new ViewChild();
			convertView = mInflater.inflate(
					R.layout.channel_expandablelistview_item, null);
			mViewChild.gridView = (GridView) convertView
					.findViewById(R.id.channel_item_child_gridView);
			convertView.setTag(mViewChild);
		} else {
			mViewChild = (ViewChild) convertView.getTag();
		}
		MyGridViewAdapter myGridViewAdapter = new MyGridViewAdapter(
				mChild.get(mGroup.get(groupPosition)));
		mViewChild.gridView.setAdapter(myGridViewAdapter);
		mViewChild.gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// 返回值必须为1，否则会重复数据
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mGroup.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroup.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewChild = new ViewChild();
			convertView = mInflater.inflate(
					R.layout.channel_expandablelistview, null);
			mViewChild.textView = (TextView) convertView
					.findViewById(R.id.channel_group_name);
			mViewChild.imageView = (ImageView) convertView
					.findViewById(R.id.channel_imageview_orientation);
			convertView.setTag(mViewChild);
		} else {
			mViewChild = (ViewChild) convertView.getTag();
		}

		if (isExpanded) {
			mViewChild.imageView
					.setImageResource(R.mipmap.channel_expandablelistview_top_icon);
		} else {
			mViewChild.imageView
					.setImageResource(R.mipmap.channel_expandablelistview_bottom_icon);
		}
		mViewChild.textView.setText(mGroup.get(groupPosition).getNAME());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@SuppressLint("InflateParams")
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	ViewChild mViewChild;

	static class ViewChild {
		ImageView imageView;
		TextView textView;
		GridView gridView;
	}

	class MyGridViewAdapter extends BaseAdapter {
		List<QusetionTypeBean> mList = new ArrayList<QusetionTypeBean>();

		public MyGridViewAdapter(List<QusetionTypeBean> list) {
			this.mList = list;
		}

		@Override
		public int getCount() {

			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = UIUtils.inflate(R.layout.channel_gridview_item);
			}
			TextView findViewById = (TextView) convertView
					.findViewById(R.id.channel_gridview_item);
			findViewById.setText(mList.get(position).getNAME());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mListener!=null){
						mListener.onClick(mList.get(position));
					}
					EventBus.getDefault().post("closeQuestionClassification");//发送消息 关闭分类树界面
				}
			});
			return convertView;
		}
	}
	private ChildListener mListener;
	public void setChildViewOnClickListener(ChildListener listener){
		mListener = listener;
	}
	public interface ChildListener{
		void onClick(QusetionTypeBean bean);
	}
}