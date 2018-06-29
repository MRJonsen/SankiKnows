package com.zc.pickuplearn.ui.previewphoto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.http.ResultStringCommonUtils;
import com.zc.pickuplearn.ui.view.PinchImageView;
import com.zc.pickuplearn.ui.view.ProgressDialog;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.LinkedList;

public class PreViewImageActivity extends AppCompatActivity {
    private final static String TAG = PreViewImageActivity.class.getSimpleName();
    private final static String TAG_POSITION = PreViewImageActivity.class.getSimpleName() + "POSITION";
    private ProgressDialog progressDialog;

    public static void startPreView(Context context, String imageUrl) {
        String imgUrl = ResultStringCommonUtils.subUrlToWholeUrl(imageUrl);
        ArrayList<String> strings = new ArrayList<>();
        strings.add(imgUrl);
        Intent intent = new Intent(context, PreViewImageActivity.class);
        intent.putExtra(TAG, strings);
        context.startActivity(intent);
    }

    public static void startPreView(Context context, ArrayList<String> imageUrl, int position) {
        Intent intent = new Intent(context, PreViewImageActivity.class);
        intent.putExtra(TAG, imageUrl);
        intent.putExtra(TAG_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view_image);
        initView();
    }

    private void initView() {
//        Intent intent = getIntent();
//        String mImageUrl = intent.getStringExtra(TAG);
//        String imgurl = ResultStringCommonUtils.subUrlToWholeUrl(mImageUrl);
//        PinchImageView mImageView = (PinchImageView) findViewById(R.id.pv_view);
//        Glide.with(this)
//                .load(imgurl)
//                .fitCenter()
//                .placeholder(R.mipmap.default_img)
//                .into(mImageView);

        final LinkedList<PinchImageView> viewCache = new LinkedList<PinchImageView>();
        final ArrayList<String> urls = getIntent().getStringArrayListExtra(TAG);
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        int position = getIntent().getIntExtra(TAG_POSITION, 0);
        LogUtils.e("preViewImageActivity当前位置", position + "");
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return urls == null ? 0 : urls.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                final PinchImageView piv;
                if (viewCache.size() > 0) {
                    piv = viewCache.remove();
                    piv.reset();
                } else {
                    piv = new PinchImageView(PreViewImageActivity.this);
                }

//                Glide.with(PreViewImageActivity.this)
//                        .load(urls.get(position))
//                        .fitCenter()
//                        .crossFade()
//                        .placeholder(R.mipmap.default_img)
//                        .into(piv);
//                container.addView(piv);
                showProgress();
                Glide.with(PreViewImageActivity.this)
                        .load(urls.get(position))
                        .fitCenter()
                        .crossFade()
                        .placeholder(R.mipmap.default_img)
                        .into(new GlideDrawableImageViewTarget(piv) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                hideProgress();
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                ToastUtils.showToast(PreViewImageActivity.this, "图片加载失败");
                                hideProgress();
                            }
                        });
                container.addView(piv);

                return piv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                PinchImageView piv = (PinchImageView) object;
                container.removeView(piv);
                viewCache.add(piv);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
//                PinchImageView piv = (PinchImageView) object;
//                ImageSource image = Global.getTestImage(position);
//                Global.getImageLoader(getApplicationContext()).displayImage(image.getOrigin().url, piv, originOptions);
            }
        });
        pager.setCurrentItem(position);

    }

    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(PreViewImageActivity.this);
                    progressDialog.setMsg("图片加载中..");
                    progressDialog.setCanceLable(false);
                }
                progressDialog.showProgressDialog();
            }
        });
    }

    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dissMissProgressDialog();
                }
            }
        });
    }
}
