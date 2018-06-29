package com.zc.pickuplearn.ui.msgbox.adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.base.ItemViewDelegate;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.PersonMsgBean;
import com.zc.pickuplearn.beans.TeamMessageBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.msgbox.event.EventPersonMsg;
import com.zc.pickuplearn.ui.teamcircle.view.TeamMsgHandleActivity;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.ui.webview.BaseWebViewActivity;
import com.zc.pickuplearn.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 网页类型的消息
 * Created by Administrator on 2017/5/15 0015.
 */

public class MsgWebItemDelagate implements ItemViewDelegate<PersonMsgBean> {
    private Context mContext;
    private List<PersonMsgBean> mData;
    private PersonMsgListAdapter mAdapter;
    private boolean isInEditMode;
    private ProgressDialog progressDialog;

    public MsgWebItemDelagate(Context context, List<PersonMsgBean> datas, PersonMsgListAdapter personMsgListAdapter, boolean isInEditMode) {
        mContext = context;
        mData = datas;
        mAdapter = personMsgListAdapter;
        this.isInEditMode = isInEditMode;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_person_msg_web;
    }

    @Override
    public boolean isForViewType(PersonMsgBean item, int position) {
        //2问答 4撤回的团队通知 1 团队通知 5 web消息
        return !"2".equals(item.getMESTYPE())&&!"4".equals(item.getMESTYPE())&&!"1".equals(item.getMESTYPE());
    }

    @Override
    public void convert(ViewHolder holder, final PersonMsgBean personMsgBean, int position) {
        holder.setText(R.id.tv_msg_content, personMsgBean.getQUESTIONEXPLAIN());
        holder.setText(R.id.tv_msg_title, personMsgBean.getMESNAME());
        holder.setText(R.id.tv_time, DateUtils.dataFormatNow("yyyy-MM-dd", personMsgBean.getSYSCREATEDATE()));
        holder.setText(R.id.tv_name, "发布人:" + personMsgBean.getCREATORNAME());
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReadStatus(personMsgBean);
                showProgress();
                API.getFunctionUrl4("ToHtmlPath", personMsgBean.getURLPATH(),personMsgBean.getMESTYPE() ,new CommonCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        BaseWebViewActivity.open(mContext,s,false);
                        hideProgress();
                    }

                    @Override
                    public void onFailure() {
                        hideProgress();
                    }
                });
            }
        });

        CircleImageView iconView = holder.getView(R.id.iv_msg_icon);
        ImageLoaderUtil.showImageView(mContext, ResultStringCommonUtils.subUrlToWholeUrl(personMsgBean.getFILEURL()),iconView);
//        if (!TextUtils.isEmpty(personMsgBean.getFILEURL())) {
//            ImageLoaderUtil.displayCircleView(mContext, iconView, personMsgBean.getFILEURL(), false);
//        } else {
//            iconView.setImageResource(R.mipmap.icon_msg_onlin_exam);
//        }

        holder.setOnLongClickListener(R.id.item, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog(mContext).builder();
                alertDialog
                        .setTitle("提示").setMsg("是否删除?")
                        .setPositiveButton("是", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deletData(personMsgBean);
                            }
                        }).setNegativeButton("否", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                alertDialog.show();
                return true;
            }
        });

        switch (personMsgBean.getISREAD()) {
            case "0":
                holder.setVisible(R.id.v_msg, true);
                break;
            case "1":
                holder.setVisible(R.id.v_msg, false);
                break;
        }
        CheckBox cb = holder.getView(R.id.cb_choice);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAdapter.addDataToRemoveList(personMsgBean);
                } else {
                    mAdapter.removeOneObjectFromChoice(personMsgBean);
                }
            }
        });
        if (mAdapter.removeListContaintObject(personMsgBean)) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
        holder.setText(R.id.tv_back, "");
        switch (personMsgBean.getMESTYPE()) {
            case "4":
                holder.setText(R.id.tv_back, "已撤回");
                break;
            default:
                holder.setText(R.id.tv_back, "");
                break;
        }
//        switch (personMsgBean.getDYNAMICTYPE()) {
//            case "3":
//                holder.setText(R.id.tv_back, "已回执");
//                break;
//            case "2":
//                holder.setText(R.id.tv_back, "已阅读");
//                break;
//            case "1":
//                holder.setText(R.id.tv_back, "需回执");
//                break;
//            case "0":
//                break;
//        }
        if (isInEditMode) {
            holder.setVisible(R.id.ll_item_edit, true);
        } else {
            holder.setVisible(R.id.ll_item_edit, false);
        }
    }

    public void setInEditMode(boolean mode) {
        isInEditMode = mode;
    }

    private void deletData(final PersonMsgBean personMsgBean) {
        API.deletPersonMessage(false, personMsgBean, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String personMsgBeen) {
                mData.remove(personMsgBean);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void showProgress(){
        if (progressDialog==null){
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setCanceLable(false);
        }
        progressDialog.showProgressDialog();
    }

    private void hideProgress(){
        if (progressDialog!=null){
            progressDialog.dissMissProgressDialog();
        }
    }
    private void setReadStatus(final PersonMsgBean personMsgBean) {
        API.setPersonMsgReadStatus(false, personMsgBean, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                personMsgBean.setISREAD("1");
                mAdapter.notifyDataSetChanged();
                EventBus.getDefault().post(new EventPersonMsg());//通知通知页面刷新提示条数
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
