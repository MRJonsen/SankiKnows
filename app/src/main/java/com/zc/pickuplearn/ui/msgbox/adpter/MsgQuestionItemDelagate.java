package com.zc.pickuplearn.ui.msgbox.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.base.ItemViewDelegate;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.PersonMsgBean;
import com.zc.pickuplearn.beans.QuestionBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.dynamic.widget.answerqustion.model.ImplQuestionDetailModel;
import com.zc.pickuplearn.ui.msgbox.event.EventPersonMsg;
import com.zc.pickuplearn.ui.question_and_answer.QuestionDetailActivity;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.ui.view.dialog.AlertDialog;
import com.zc.pickuplearn.utils.DateUtils;
import com.zc.pickuplearn.utils.PictureUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class MsgQuestionItemDelagate implements ItemViewDelegate<PersonMsgBean> {
    private Context mContext;
    private List<PersonMsgBean> mData;
    private PersonMsgListAdapter mAdapter;
    private boolean isInEditMode;
    private HashMap<String,Bitmap> bitmaps;

    private ProgressDialog progressDialog;
    public MsgQuestionItemDelagate(Context context, List<PersonMsgBean> datas, PersonMsgListAdapter personMsgListAdapter,boolean isInEditMode) {
        mContext = context;
        mData = datas;
        mAdapter = personMsgListAdapter;
        this.isInEditMode=isInEditMode;
        bitmaps = new HashMap<>();
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_person_msg_list;
    }

    @Override
    public boolean isForViewType(PersonMsgBean item, int position) {
        return "2".equals(item.getMESTYPE());
    }

    @Override
    public void convert(ViewHolder holder, final PersonMsgBean personMsgBean, int position) {
        holder.setText(R.id.tv_time, DateUtils.dataFormatNow("yyyy-MM-dd", personMsgBean.getSYSCREATEDATE()));
        holder.setText(R.id.tv_msg, personMsgBean.getTIPS());
        holder.setText(R.id.tv_msg_content, personMsgBean.getQUESTIONEXPLAIN());
        holder.setText(R.id.tv_answer, personMsgBean.getANSWEREXPLAIN());
        CircleImageView iconView = holder.getView(R.id.iv_msg_icon);
        if (!TextUtils.isEmpty(personMsgBean.getFILEURL())) {
            ImageLoaderUtil.displayCircleView(mContext, iconView, personMsgBean.getFILEURL(), false);
        } else {
            String username = personMsgBean.getUSERNAME();
            if (!TextUtils.isEmpty(username)){
                String substring = username.substring(0, 1);
                if (bitmaps.get(substring)==null){
                    bitmaps.put(substring, PictureUtil.textAsBitmap(mContext,substring,50));
                }
                iconView.setImageBitmap(bitmaps.get(substring));
            }else {
                String substring = "匿";
                if (bitmaps.get(substring)==null){
                    bitmaps.put(substring, PictureUtil.textAsBitmap(mContext,substring,50));
                }
                iconView.setImageBitmap(bitmaps.get(substring));
            }
        }
        holder.setOnClickListener(R.id.item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionBean questionBean = new QuestionBean();
                questionBean.setSEQKEY(personMsgBean.getQUESTIONID());
                showProgress();
                new ImplQuestionDetailModel().getDynamicDatas(questionBean, new ImplQuestionDetailModel.GetDynamicDatasCallBack() {
                    @Override
                    public void onSuccess(List<QuestionBean> dynamic_data) {
                        boolean ISMYQUESTION = dynamic_data.get(0).getQUESTIONUSERCODE().equals(personMsgBean.getUSERCODE());
                        QuestionDetailActivity.open(mContext, dynamic_data.get(0));
//                        QuestionDetailActivity.startQuestionDetailActivity(mContext, dynamic_data.get(0), ISMYQUESTION?QuestionDetailType.MyQuestion:QuestionDetailType.MyAnswer);//进入问题详情
                        hideProgress();
                    }

                    @Override
                    public void onFailure() {
                            hideProgress();
                    }
                });
                setReadStatus(personMsgBean);
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
        CheckBox cb = holder.getView(R.id.cb_choice);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mAdapter.addDataToRemoveList(personMsgBean);
                }else {
                    mAdapter.removeOneObjectFromChoice(personMsgBean);
                }
            }
        });
        if (mAdapter.removeListContaintObject(personMsgBean)){
            cb.setChecked(true);
        }else {
            cb.setChecked(false);
        }
        if (isInEditMode){
            holder.setVisible(R.id.ll_item_edit,true);
        }else {
            holder.setVisible(R.id.ll_item_edit,false);
        }
    }

    public void setInEditMode(boolean mode){
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
    public void setReadStatus(final PersonMsgBean personMsgBean){
        API.setPersonMsgReadStatus(false,personMsgBean, new CommonCallBack<String>() {
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
