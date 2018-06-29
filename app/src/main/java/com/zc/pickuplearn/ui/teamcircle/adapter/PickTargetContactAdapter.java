package com.zc.pickuplearn.ui.teamcircle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 提问添加指定回答人员适配器
 */
public class PickTargetContactAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<UserBean> list = new ArrayList<UserBean>();
    private List<UserBean> exitedMembers = new ArrayList<>();
    private Context mContext;
    private OnPickContactItemClickListener mListener;

    public PickTargetContactAdapter(Context context,
                                    List<UserBean> users) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.list = users;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.fx_item_contact_checkbox, parent, false);
            holder = new ViewHolder();
            holder.ivAvatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            holder.tvNick = (TextView) convertView
                    .findViewById(R.id.tv_name);
            holder.checkBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            holder.tvHeader = (TextView) convertView
                    .findViewById(R.id.header);
            holder.tvCode = (TextView) convertView.findViewById(R.id.tv_code);
            holder.item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final UserBean user = list.get(position);
        holder.tvNick.setText(user.getUSERNAME());
        holder.tvCode.setText(user.getUSERCODE());
        String fileurl = user.getFILEURL();
        if (TextUtils.isEmpty(fileurl)) {
            holder.ivAvatar.setImageResource(R.mipmap.fx_default_useravatar);
        } else {
            LogUtils.e("包含吗" + fileurl);
            ImageLoaderUtil.display(mContext, holder.ivAvatar, fileurl);
        }
        boolean containUser = isContainUser(exitedMembers, user);
        holder.checkBox.setButtonDrawable(R.drawable.fx_bg_checkbox_blue);
        holder.checkBox.setChecked(containUser);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkBox.toggle();
                if (holder.checkBox.isChecked()) {
                    if (!exitedMembers.contains(user))
                        exitedMembers.add(user);
                } else {
                    if (exitedMembers.contains(user))
                        exitedMembers.remove(user);
                }
                if (mListener != null) {
                    mListener.OnClick();
                }
            }
        });
        return convertView;
    }

    public List<UserBean> getChooseUser() {
        return exitedMembers;
    }

    /**
     * 判断是否包含
     *
     * @param userBean 人员
     * @return 存在true
     */
    private boolean isContainUser(List<UserBean> list, UserBean userBean) {
        boolean isContain = false;
        if (list != null && userBean != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUSERCODE().equals(userBean.getUSERCODE())) {
                    isContain = true;
                }
            }
        }
        return isContain;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public UserBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * 初始化 选中人员数据
     * @param list
     */
    public void setContainData(List<UserBean> list){
        if (list!=null&&list.size()>0){
            exitedMembers.clear();
            exitedMembers.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void setOnPickContactItemClick(OnPickContactItemClickListener listener) {
        mListener = listener;
    }

    public interface OnPickContactItemClickListener {
        void OnClick();
    }

    private static class ViewHolder {
        private CheckBox checkBox;
        private ImageView ivAvatar;
        private TextView tvNick;
        private TextView tvHeader;
        private TextView tvCode;
        private RelativeLayout item;
    }
}



