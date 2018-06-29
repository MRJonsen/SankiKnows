package com.zc.pickuplearn.broadcastreciver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zc.pickuplearn.ui.msgbox.PersonalMsgActivity;
import com.zc.pickuplearn.utils.LogUtils;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.helpers.IMReceiver;

/**
 * Created by Administrator on 2017/5/19 0019.
 */

public class ImReceiver extends IMReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        JMessageClient.init(context);
        String var3;
        if((var3 = intent.getAction()) == null) {
            Log.d("IMReceiver", "onReceive - the action is null");
        }else if (var3.equals("cn.jpush.im.android.action.NOTIFICATION_CLICK_PROXY")){
            LogUtils.e("用户点击");
            Intent i = new Intent(context, PersonalMsgActivity.class);
            i.putExtra("location","conversation");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity(i);
            return;
        }else {
            Log.e("unhandlemsg","");
        }
        super.onReceive(context, intent);
    }
}
