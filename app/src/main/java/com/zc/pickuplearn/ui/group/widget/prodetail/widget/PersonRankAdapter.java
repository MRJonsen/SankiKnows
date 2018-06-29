package com.zc.pickuplearn.ui.group.widget.prodetail.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.CircleRankingBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonRankAdapter extends BaseAdapter {
	private List<CircleRankingBean> list;
	private Context context;
	private LayoutInflater mInflater;

	public PersonRankAdapter(List<CircleRankingBean> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	static class Holder {
		@BindView(R.id.tv_postion)
		TextView tv_postion;
		@BindView(R.id.tv_answer_sum)
		TextView tv_answer_sum;
		@BindView(R.id.tv_take_sum)
		TextView tv_take_sum;
		@BindView(R.id.tv_qustion_sum)
		TextView tv_qustion_sum;
		@BindView(R.id.tv_nickname)
		TextView tv_nickname;
		@BindView(R.id.tv_sumpoint)
		TextView tv_sumpoint;

		public Holder(View v) {
			ButterKnife.bind(this, v);
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.item_circle_person_rank_listview, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (position == 0) {
			holder.tv_postion.setText(list.get(position).getPOSITION());
		} else if (position == 1) {
			holder.tv_postion.setText("1st");
		} else if (position == 2) {
			holder.tv_postion.setText("2nd");
		} else if (position == 3) {
			holder.tv_postion.setText("3rd");
		} else {
			holder.tv_postion.setText(position + "th");
		}
		holder.tv_nickname.setText(list.get(position).getNICKNAME());
		holder.tv_answer_sum.setText(list.get(position).getANSWERSUM());
		holder.tv_take_sum.setText(list.get(position).getANSWERTAKESUM());
		holder.tv_qustion_sum.setText(list.get(position).getQUESTIONSUM());
		holder.tv_sumpoint.setText(list.get(position).getSUMPOINTS());
		return convertView;
	}
}
