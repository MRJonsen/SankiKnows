package com.zc.pickuplearn.ui.mine.mine.widget;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.view.HeadView;
import com.zc.pickuplearn.utils.UIUtils;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.hv_head)
    HeadView hvHead;

    @BindView(R.id.iv_scan_pic)
    ImageView imageView;
    public static void startAbout(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    @Override
    public int setLayout() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        hvHead.setTitle(UIUtils.getString(R.string.about));
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = width - UIUtils.dip2px(10);
        layoutParams.height = width - UIUtils.dip2px(10);
        imageView.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {

    }
}
