package com.zc.pickuplearn.ui.classiccase.view.comment.view;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.view.NoScrollListView;
import com.zc.pickuplearn.utils.UIUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentAdapter extends BaseAdapter {

	private int resourceId;
	private Context context;
	private Handler handler;
	private List<CommentBean> list;
	private LayoutInflater inflater;

	public CommentAdapter(Context context, List<CommentBean> list,
			int resourceId, Handler handler) {
		this.list = list;
		this.context = context;
		this.handler = handler;
		this.resourceId = resourceId;
		inflater = LayoutInflater.from(context);
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
		CommentBean bean = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			// convertView = inflater.inflate(resourceId, null);
			convertView = UIUtils.inflate(resourceId);
			holder.commentItemImg = (CircleImageView) convertView
					.findViewById(R.id.commentItemImg);
			holder.commentNickname = (TextView) convertView
					.findViewById(R.id.commentNickname);
			holder.replyText = (TextView) convertView
					.findViewById(R.id.replyText);
			holder.commentItemTime = (TextView) convertView
					.findViewById(R.id.commentItemTime);
			holder.commentItemContent = (TextView) convertView
					.findViewById(R.id.commentItemContent);
			holder.replyList = (NoScrollListView) convertView
					.findViewById(R.id.replyList);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String commentImgUrl = bean.getCommentImgId();
		ImageLoaderUtil.displayCircleView(context,holder.commentItemImg,commentImgUrl,false);
		holder.commentNickname.setText(bean.getCommentNickname());
		String commentTime = bean.getCommentTime();
		if (commentTime.contains(".")) {
			commentTime = commentTime
					.substring(0, commentTime.lastIndexOf("."));
		}
		holder.commentItemTime.setText(commentTime);
		holder.commentItemContent.setText(bean.getCommentContent());
		holder.replyText.setVisibility(View.GONE);// 回复按钮隐藏
		ReplyAdapter adapter = new ReplyAdapter(context, bean.getReplyList(),
				R.layout.reply_item);
		holder.replyList.setAdapter(adapter);
		TextviewClickListener tcl = new TextviewClickListener(position);
		holder.replyText.setOnClickListener(tcl);

		return convertView;
	}

	private final class ViewHolder {
		public CircleImageView commentItemImg; // 评论人图片
		public TextView commentNickname; // 评论人昵称
		public TextView replyText; // 回复
		public TextView commentItemTime; // 评论时间
		public TextView commentItemContent; // 评论内容
		public NoScrollListView replyList; // 评论回复列表
	}

	/**
	 * 获取回复评论
	 */
	public void getReplyComment(ReplyBean bean, int position) {
		List<ReplyBean> rList = list.get(position).getReplyList();
		rList.add(rList.size(), bean);
	}

	/**
	 * 事件点击监听器
	 */
	private final class TextviewClickListener implements OnClickListener {
		private int position;

		public TextviewClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.replyText:
				handler.sendMessage(handler.obtainMessage(10, position));
				break;
			}
		}
	}

}
