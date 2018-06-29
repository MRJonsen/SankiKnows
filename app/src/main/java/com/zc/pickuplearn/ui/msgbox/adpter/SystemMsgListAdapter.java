package com.zc.pickuplearn.ui.msgbox.adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.SystemMsgBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.base.WebViewActivity;
import com.zc.pickuplearn.ui.msgbox.event.EventPersonMsg;
import com.zc.pickuplearn.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 系统通知适配器
 * Created by Administrator on 2017/5/15.
 */

public class SystemMsgListAdapter extends CommonAdapter<SystemMsgBean> {
    private Context mContext;

    public SystemMsgListAdapter(Context context, List<SystemMsgBean> data) {
        this(context, R.layout.item_system_msg, data);
        this.mContext = context;
    }

    private SystemMsgListAdapter(Context context, int layoutId, List<SystemMsgBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final SystemMsgBean systemMsgBean, final int position) {
        holder.setText(R.id.tv_system_msg_title, systemMsgBean.getTITLE());
        holder.setText(R.id.tv_system_msg_content, systemMsgBean.getREMARK());
        holder.setText(R.id.tv_answer,"发布时间："+DateUtils.dataFormatNow("yyyy-MM-dd", systemMsgBean.getRELEASEDATE()));
        if ("0".equals(systemMsgBean.getISREAD())){
            holder.setVisible(R.id.v_msg,true);
        }else {
            holder.setVisible(R.id.v_msg,false);
        }
        ImageView iconView = holder.getView(R.id.iv_msg_icon);
        if (!TextUtils.isEmpty(systemMsgBean.getFILEURL())) {
            ImageLoaderUtil.display(mContext, iconView, systemMsgBean.getFILEURL());
        } else {
            iconView.setImageResource(R.mipmap.default_img);
        }
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(systemMsgBean.getURLPATH())) {
                    WebViewActivity.skip(mContext, systemMsgBean.getURLPATH(), systemMsgBean.getTITLE(), true);
                }else {
                    WebViewActivity.skip(mContext,systemMsgBean.getCONTENTHTM(),systemMsgBean.getTITLE());
                }
                systemMsgBean.setISREAD("1");//设置为消息已读
                notifyDataSetChanged();
                API.systemMessageClick(systemMsgBean, new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        EventBus.getDefault().post(new EventPersonMsg());//通知通知页面刷新提示条数
                    }

                    @Override
                    public void onFailure() {

                    }
                });//系统消息点击量
            }
        });
    }
}
