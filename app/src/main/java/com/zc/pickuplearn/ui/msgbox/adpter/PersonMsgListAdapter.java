package com.zc.pickuplearn.ui.msgbox.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;

import com.zc.pickuplearn.adapter.rcAdapter.MultiItemTypeAdapter;
import com.zc.pickuplearn.beans.PersonMsgBean;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.ui.msgbox.event.EventPersonMsg;
import com.zc.pickuplearn.utils.ToastUtils;
import com.zc.pickuplearn.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2017/5/15 0015.
 */

public class PersonMsgListAdapter extends MultiItemTypeAdapter<PersonMsgBean> {

    private List<PersonMsgBean> mData;
    private List<PersonMsgBean> mRemove = new ArrayList<>();
    private final MsgQuestionItemDelagate questionItemDelagate;
    private final MsgTeamInfoItemDelagate teamInfoItemDelagate;
    private final MsgWebItemDelagate webItemDelegate;
    public PersonMsgListAdapter(Context context, List<PersonMsgBean> datas,boolean isInEditMode) {
        super(context, datas);
        mData = datas;
        questionItemDelagate = new MsgQuestionItemDelagate(context, datas, this,isInEditMode);
        addItemViewDelegate(questionItemDelagate);
        teamInfoItemDelagate = new MsgTeamInfoItemDelagate(context, datas, this,isInEditMode);
        addItemViewDelegate(teamInfoItemDelagate);
        webItemDelegate = new MsgWebItemDelagate(context, datas, this,isInEditMode);
        addItemViewDelegate(webItemDelegate);
    }

    public void setInEditMode(boolean mode){
        questionItemDelagate.setInEditMode(mode);
        teamInfoItemDelagate.setInEditMode(mode);
        webItemDelegate.setInEditMode(mode);
        notifyDataSetChanged();
    }

    public void setReadData(){
        if (mData!=null){
            for (PersonMsgBean personMsg:mData) {
                personMsg.setISREAD("1");
                notifyDataSetChanged();
            }
        }
    }
    public void addDataToRemoveList(PersonMsgBean personmsgBean){
        mRemove.add(personmsgBean);
    }
    public void removeRemoveList(){
        mRemove.clear();
    }
    public boolean removeListContaintObject(PersonMsgBean personmsgBean){
        if (mRemove.contains(personmsgBean))
            return true;
        return false;
    }
    public void removeOneObjectFromChoice(PersonMsgBean personmsgBean){
        mRemove.remove(personmsgBean);
    }
    public void clearSomeData(){
        if (mRemove.size()==0){
            ToastUtils.showToast(UIUtils.getContext(),"请选择要删除的消息！");
        }else {
            API.deletSomePersonMessage(mRemove, new CommonCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    mData.removeAll(mRemove);
                    mRemove.clear();
                    notifyDataSetChanged();
                    EventBus.getDefault().post(new EventPersonMsg());//通知通知页面刷新提示条数
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }
    public void clearData(){
        mData.clear();
        notifyDataSetChanged();
    }
}
