package com.zc.pickuplearn.ui.im.chatting;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.youth.xframe.XFrame;
import com.youth.xframe.utils.log.XLog;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.application.JChatDemoApplication;
import com.zc.pickuplearn.ui.im.chatting.activity.ConversationListAdapter;
import com.zc.pickuplearn.ui.im.chatting.tools.SortConvList;
import com.zc.pickuplearn.ui.im.chatting.view.ConversationListView;
import com.zc.pickuplearn.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;


public class ConversationListController implements OnClickListener,
        OnItemClickListener, OnItemLongClickListener {

    private ConversationListView mConvListView;
    private ConversationListFragment mContext;
    private List<Conversation> mDatas = new ArrayList<Conversation>();
    private ConversationListAdapter mListAdapter;
    private int mWidth;
    private Dialog mDialog;
    private ConversationListFragment.ListType mListtype;
    public ConversationListController(ConversationListView listView, ConversationListFragment context,
                                      int width, ConversationListFragment.ListType listTye) {
        this.mConvListView = listView;
        this.mContext = context;
        this.mWidth = width;
        this.mListtype = listTye;
        initConvListAdapter();
    }

    // 获得到会话列表
    private void initConvListAdapter() {
        mDatas=JMessageClient.getConversationList();
        if (ConversationListFragment.ListType.SINGLE.equals(mListtype)&&mDatas != null) {
            ArrayList<Conversation> conversations = new ArrayList<>();
            for (Conversation convList : mDatas) {
                if ("single".equals(convList.getType().toString())) {
                    conversations.add(convList);
                }
            }
            mDatas = conversations;
        }
        //对会话列表进行时间排序
        if (mDatas!=null&&mDatas.size() > 1) {
            SortConvList sortList = new SortConvList();
            Collections.sort(mDatas, sortList);
        }
        if (mDatas!=null&&mDatas.size()>1){//乱码问题
            ArrayList<Conversation> conversations = new ArrayList<>();
            for (Conversation conversation:mDatas){
                if (conversation.getTitle().equals(conversation.getTargetId())){
                    conversations.add(conversation);
                }
            }
            mDatas.removeAll(conversations);
        }
        mListAdapter = new ConversationListAdapter(mContext.getActivity(), mDatas);
        mConvListView.setConvListAdapter(mListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_group_btn:
                mContext.showMenuPopWindow();
                break;
        }
    }

    // 点击会话列表
    @Override
    public void onItemClick(AdapterView<?> viewAdapter, View view, int position, long id) {
        // TODO Auto-generated method stub
        final Intent intent = new Intent();
        if (position > 0) {
            Conversation conv = mDatas.get(position - 1);
            intent.putExtra(JChatDemoApplication.CONV_TITLE, conv.getTitle());
            if (null != conv) {
                // 当前点击的会话是否为群组
                LogUtils.e("会话",conv.toString());
                if (conv.getType() == ConversationType.group) {
                    long groupId = ((GroupInfo) conv.getTargetInfo()).getGroupID();
                    intent.putExtra(JChatDemoApplication.GROUP_ID, groupId);
                    intent.putExtra(JChatDemoApplication.DRAFT, getAdapter().getDraft(conv.getId()));
                    intent.setClass(mContext.getActivity(), ChatActivity.class);
                    mContext.getActivity().startActivity(intent);
                    return;
                } else {
                    String targetId = ((UserInfo) conv.getTargetInfo()).getUserName();
                    intent.putExtra(JChatDemoApplication.TARGET_ID, targetId);
                    intent.putExtra(JChatDemoApplication.TARGET_APP_KEY, conv.getTargetAppKey());
                    Log.d(targetId+"ConversationList", "Target app key from conversation: " + conv.getTargetAppKey());
                    intent.putExtra(JChatDemoApplication.DRAFT, getAdapter().getDraft(conv.getId()));
                }
                intent.setClass(mContext.getActivity(), ChatActivity.class);
                mContext.getActivity().startActivity(intent);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> viewAdapter, View view, final int position, long id) {
//        if (position > 0) {
//            final Conversation conv = mDatas.get(position - 1);
//            if (conv != null) {
//                OnClickListener listener = new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (conv.getType() == ConversationType.group) {
//                            JMessageClient.deleteGroupConversation(((GroupInfo) conv.getTargetInfo())
//                                    .getGroupID());
//                        } else {
//                            //使用带AppKey的接口,可以删除跨/非跨应用的会话(如果不是跨应用,conv拿到的AppKey则是默认的)
//                            JMessageClient.deleteSingleConversation(((UserInfo) conv.getTargetInfo())
//                                    .getUserName(), conv.getTargetAppKey());
//                        }
//                        mDatas.remove(position - 1);
//                        mListAdapter.notifyDataSetChanged();
//                        mDialog.dismiss();
//                    }
//                };
//                mDialog = DialogCreator.createDelConversationDialog(mContext.getActivity(), conv.getTitle(),
//                        listener);
//                mDialog.show();
//                mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
//            }
//        }
        return true;
    }

    public ConversationListAdapter getAdapter() {
        return mListAdapter;
    }

}
