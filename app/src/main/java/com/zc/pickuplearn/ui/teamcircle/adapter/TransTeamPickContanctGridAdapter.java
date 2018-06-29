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
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 转移团队选择人员 适配器
 *
 * @author admin_new
 */
public class TransTeamPickContanctGridAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<UserBean> list = new ArrayList<UserBean>();
    private Context mContext;
    private UserBean exitedMembers;
    private OnPickContactItemClickListener mListener;

    public TransTeamPickContanctGridAdapter(Context activity,
                                            List<UserBean> users) {
        layoutInflater = LayoutInflater.from(activity);
        this.mContext = activity;
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
        if (position == 0) {
            holder.checkBox.setButtonDrawable(R.drawable.fx_bg_checkbox);
        } else {
            boolean containUser = isContainUser(exitedMembers, user);
            holder.checkBox.setButtonDrawable(R.drawable.fx_bg_checkbox_blue);
            holder.checkBox.setChecked(containUser);
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) { //此处为团长不可点
                    if (exitedMembers == null) {
                        holder.checkBox.toggle();
                        if (holder.checkBox.isChecked()) {
                            exitedMembers = user;
                        }
                        if (mListener != null) {
                            mListener.OnClick();
                        }
                    } else {
                        if (holder.checkBox.isChecked()) {
                            holder.checkBox.toggle();
                            exitedMembers = null;
                        } else {
                            ToastUtils.showToast(mContext, "只能选择一人");
                        }
                        if (mListener != null) {
                            mListener.OnClick();
                        }
                    }
                }
            }
        });
        return convertView;
    }

    public UserBean getChooseUser() {
        return exitedMembers;
    }

    /**
     * 判断是否包含
     *
     * @param userBean 人员
     * @return 存在true
     */
    private boolean isContainUser(UserBean list, UserBean userBean) {
        boolean isContain = false;
        if (list != null && userBean != null) {
            if (list.getUSERCODE().equals(userBean.getUSERCODE())) {
                isContain = true;
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

