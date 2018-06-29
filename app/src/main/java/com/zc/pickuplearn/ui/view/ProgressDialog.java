package com.zc.pickuplearn.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import com.zc.pickuplearn.R;

/**
 * 作者： Jonsen
 * 时间: 2016/12/5 19:52
 * 联系方式：chenbin252@163.com
 */
public class ProgressDialog {
    private Dialog progressDialog;
    private final TextView mTextView;

    public ProgressDialog(Context context) {
        progressDialog = new Dialog(context, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.progress_dialog_ios);
        progressDialog.setCancelable(true);
        Window window = progressDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
//            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        mTextView = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
    }

    public void showProgressDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }
    public void setCanceLable(boolean cancelable){
        if (progressDialog!=null){
            progressDialog.setCancelable(cancelable);
        }
    }
    public void setMsg(String msg) {
        if (progressDialog != null && mTextView != null) {
            mTextView.setText(msg);
        }
    }
    public void dissMissProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
