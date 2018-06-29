package com.zc.pickuplearn.ui.teamcircle.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.TeamCircleBean;
import com.zc.pickuplearn.beans.UserBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.category.ModelCategory;
import com.zc.pickuplearn.ui.teamcircle.model.UserType;
import com.zc.pickuplearn.ui.teamcircle.view.GroupAddMembersActivity;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组成员gridadapter
 *
 * @author admin_new
 */
public class TeamInfoGridAdapter extends BaseAdapter {

    public boolean isInDeleteMode;
    private List<UserBean> users;
    private Context context;
    private UserBean operator;
    private static final int ITEM_USER = 1;
    private static final int ITEM_ADD = 2;
    private static final int ITEM_DEL = 3;
    private TeamCircleBean team;

    public TeamInfoGridAdapter(Context context, List<UserBean> users, UserBean operater, TeamCircleBean teamCircleBean) {
        this.operator = operater;
        this.users = users;
        this.context = context;
        isInDeleteMode = false;
        team = teamCircleBean;
    }

    @Override
    public int getCount() {
        if (isManager()) {
            return users.size() + 2;
        } else {
            return users.size();
        }

    }

    @Override
    public Object getItem(int position) {

        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.fx_item_grid_user, null);
            holder = new ViewHolder();
            holder.ivAvatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            holder.ivDel = (ImageView) convertView
                    .findViewById(R.id.iv_delete);
            holder.tvNick = (TextView) convertView
                    .findViewById(R.id.tv_nick);
            holder.tvRole = (TextView) convertView
                    .findViewById(R.id.tv_role);
            convertView.setTag(holder);
        } else {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.fx_item_grid_user, null);
            holder = new ViewHolder();
            holder.ivAvatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);
            holder.ivDel = (ImageView) convertView
                    .findViewById(R.id.iv_delete);
            holder.tvNick = (TextView) convertView
                    .findViewById(R.id.tv_nick);
            holder.tvRole = (TextView) convertView
                    .findViewById(R.id.tv_role);
            convertView.setTag(holder);
            //解决图片错乱问题
//            holder = (ViewHolder) convertView.getTag();
        }


        if (position == (getCount() - 2) && isManager()) {
            // 添加群组成员按钮
            // 正处于删除模式下,隐藏添加按钮
            if (isInDeleteMode) {
                convertView.setVisibility(View.GONE);
            } else {
                convertView.setVisibility(View.VISIBLE);
            }
            holder.tvNick.setText("");
            holder.ivDel.setVisibility(View.GONE);
            holder.ivAvatar.setImageResource(R.mipmap.fx_icon_add);
            holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 进入选人页面
                    ArrayList<UserBean> userBeen = new ArrayList<>();
                    userBeen.addAll(users);
                    GroupAddMembersActivity.startAddmemberAcitivity(context, userBeen,operator);
                }
            });
        } else if (position == (getCount() - 1) && isManager()) {
            // 最后一个item，减人按钮
            holder.tvNick.setText("");
            holder.ivDel.setVisibility(View.GONE);
            holder.ivAvatar.setImageResource(R.mipmap.fx_icon_delete);
            if (isInDeleteMode) {
                // 正处于删除模式下，隐藏删除按钮
                convertView.setVisibility(View.GONE);
            } else {
                convertView.setVisibility(View.VISIBLE);
            }

            holder.ivAvatar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    isInDeleteMode = true;
                    notifyDataSetChanged();
                }

            });

        } else {
            // 普通item，显示群组成员
            final UserBean user = users.get(position);
            final String userhid = user.getUSERCODE();
            holder.tvNick.setText(user.getUSERNAME());
            holder.tvRole.setVisibility(View.GONE);
            String usertype = user.getUSERTYPE();
            if (!TextUtils.isEmpty(usertype)) {
                if (UserType.holder.equals(usertype)) {
                    holder.tvRole.setVisibility(View.VISIBLE);
                    holder.tvRole.setText("团长");
                    holder.tvRole.setBackgroundResource(R.drawable.item_join_team_unpress);
                } else if (UserType.preliminarymember.equals(usertype)) {
                    holder.tvRole.setVisibility(View.VISIBLE);
                    holder.tvRole.setText("待审核");
                    holder.tvRole.setBackgroundResource(R.drawable.item_join_team_press);
                    holder.ivAvatar.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            LogUtils.e("点击了");
                            if (isManager()) {
                                LogUtils.e("管理员点击了");
                                approveMember(user);
                            }
                            return true;
                        } //审核
                    });
                }else if (UserType.manager.equals(usertype)){
                    holder.tvRole.setVisibility(View.VISIBLE);
                    holder.tvRole.setText("管理员");
                    holder.tvRole.setBackgroundResource(R.drawable.orange_ret);
                    holder.ivAvatar.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            LogUtils.e("点击了");
                            if (isHolder()) {
                                LogUtils.e("管理员点击了");
                                setManager(user);
                            }
                            return true;
                        } //审核
                    });
                }else if (UserType.member.equals(usertype)){
                    holder.ivAvatar.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            LogUtils.e("点击了");
                            if (isHolder()) {
                                LogUtils.e("管理员点击了");
                                setManager(user);
                            }
                            return true;
                        } //审核
                    });
                }
            }
            if (TextUtils.isEmpty(user.getFILEURL())) {
                holder.ivAvatar.setImageResource(R.mipmap.fx_default_useravatar);
            } else {
                holder.ivAvatar.setTag(R.id.imageloader_uri, user.getFILEURL());
                if (holder.ivAvatar.getTag(R.id.imageloader_uri) != null && holder.ivAvatar.getTag(R.id.imageloader_uri).equals(user.getFILEURL())) {
                    ImageLoaderUtil.display(context, holder.ivAvatar, user.getFILEURL());
                }
            }

            // demo群组成员的头像都用默认头像，需由开发者自己去设置头像
            if (isInDeleteMode) {
                // 如果是删除模式下，显示减人图标
                convertView.findViewById(R.id.iv_delete).setVisibility(
                        View.VISIBLE);
            } else {
                convertView.findViewById(R.id.iv_delete).setVisibility(
                        View.INVISIBLE);
            }
            holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isInDeleteMode) {
                        // 如果是删除自己，return
//                        if (DataUtils.getUserInfo().getUSERCODE()
//                                .equals(userhid)) {
//                            return;
//                        }
                        if (UserType.holder.equals(operator.getUSERTYPE())){
                            if (UserType.holder.equals(user.getUSERTYPE())) {
                                return;
                            }
                        }else {
                            if (UserType.holder.equals(user.getUSERTYPE())||UserType.manager.equals(user.getUSERTYPE())){
                                return;
                            }
                        }
                        deleteMembersFromGroup(userhid);
                    } else {
                        //正常情况下点击user，可以进入用户详情或者聊天页面等等
//                         context.startActivity(new Intent(context, UserDetailsActivity.class).putExtra(FXConstant.KEY_USER_INFO,user.getUserInfo()));
                    }
                }

            });

        }
        return convertView;
    }

    private boolean isManager() {
        return UserType.holder.equals(operator.getUSERTYPE())||UserType.manager.equals(operator.getUSERTYPE());
    }
    private boolean isHolder(){
        return UserType.holder.equals(operator.getUSERTYPE());
    }
    public void approveMember(final UserBean user) {
        AlertDialog alertDialog = new AlertDialog(context).builder();
        alertDialog
                .setTitle("提示").setMsg("是否同意用户加入团队?")
                .setPositiveButton("同意", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        API.agreeJoinTeam(team, UserType.member,false,user, new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                user.setUSERTYPE(UserType.member);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure() {
                                ToastUtils.showToast(context, "操作失败！");
                            }
                        });
                    }
                }).setNegativeButton("不同意", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API.quiteTeam(team, user, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        users.remove(user);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure() {
                        ToastUtils.showToast(context, "操作失败！");
                    }
                });
            }
        });
        alertDialog.show();
    }

    public void setManager(final UserBean user) {
        AlertDialog alertDialog = new AlertDialog(context).builder();
        alertDialog
                .setTitle("提示").setMsg(UserType.manager.equals(user.getUSERTYPE())?"是否撤销"+user.getUSERNAME()+"的管理员权限？":"是否指定"+user.getUSERNAME()+"为管理员?")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String type = UserType.manager.equals(user.getUSERTYPE()) ? UserType.member : UserType.manager;
                        API.agreeJoinTeam(team,type ,true,user, new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                ToastUtils.showToast(context,"设置成功");
                                user.setUSERTYPE(type);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure() {
                                ToastUtils.showToast(context, "操作失败！");
                            }
                        });
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        alertDialog.show();
    }

    private void deleteMembersFromGroup(String userhid) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUSERCODE().equals(userhid)) {
                users.remove(users.get(i));
                notifyDataSetChanged();
                break;
            }
        }
    }

    private static class ViewHolder {
        private ImageView ivDel;
        private ImageView ivAvatar;
        private TextView tvNick;
        private TextView tvRole;//角色
    }

}

