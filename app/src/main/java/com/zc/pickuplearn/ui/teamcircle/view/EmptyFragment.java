package com.zc.pickuplearn.ui.teamcircle.view;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.utils.SystemUtils;
import com.zc.pickuplearn.utils.UIUtils;

import butterknife.BindView;


public class EmptyFragment extends BaseFragment {
    @BindView(R.id.iv_image)
    ImageView mEmpty;

    public static EmptyFragment newInstance() {
        EmptyFragment fragment = new EmptyFragment();
        return fragment;
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_compare;
    }

    @Override
    public void initView() {
        int screenWidth = SystemUtils.getScreenWidth(getmCtx());
        ViewGroup.LayoutParams layoutParams = mEmpty.getLayoutParams();
        layoutParams.height = screenWidth - UIUtils.dip2px(15);
        layoutParams.width = screenWidth - UIUtils.dip2px(15);
        mEmpty.setLayoutParams(layoutParams);
        Glide.with(getmCtx()).load(R.mipmap.building).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mEmpty);
    }
}
