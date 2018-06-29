package com.zc.pickuplearn.ui.main_pick_up_learn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.adapter.rcAdapter.CommonAdapter;
import com.zc.pickuplearn.adapter.rcAdapter.base.ViewHolder;
import com.zc.pickuplearn.beans.FunctionBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.capabilitymap.CapAbilityMapActivity;
import com.zc.pickuplearn.ui.district_manager.SkillMoudleLearnActivity;
import com.zc.pickuplearn.ui.question_and_answer.DynamicActivity;
import com.zc.pickuplearn.ui.question_and_answer.DynamicType;
import com.zc.pickuplearn.ui.webview.BaseWebViewActivity;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.UIUtils;

import java.util.List;

/**
 * 固定功能块适配器
 * Created by chenbin on 2017/8/7.
 */

public class FixFunctionAdapter extends CommonAdapter<FunctionBean> {

    public FixFunctionAdapter(Context context, List<FunctionBean> datas) {
        super(context, R.layout.item_home_fixed_function, datas);

    }
    @Override
    protected void convert(ViewHolder holder, FunctionBean bean, int position) {
        holder.setText(R.id.tv_function_name, bean.getFUNCTIONNAME());
        holder.setImageResource(R.id.iv_icon, bean.icon);
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                switch (position) {
                    case 0:
                        mContext.startActivity(new Intent(mContext, CapAbilityMapActivity.class));//能力地图
                        break;
                    case 1:
                        DynamicActivity.open(mContext, DynamicType.Home);
                        break;
                    case 2:
//                        UIUtils.startActionUrl(mContext,"CourseHome");
                        API.getFunctionUrl("CourseHome", new CommonCallBack<String>() {

                            @Override
                            public void onSuccess(String s) {
                                LogUtils.e(s);
//                                WebViewActivity.skip(mContext, s, "", false);
                                BaseWebViewActivity.open(mContext,s,false);
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                        break;
                    case 3:
                        UIUtils.startActionUrl(mContext,"DevelopNavigation");

                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


}
