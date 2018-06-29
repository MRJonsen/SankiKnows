package com.zc.pickuplearn.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.youth.xframe.base.XActivity;
import com.youth.xframe.common.XActivityStack;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.utils.LogUtils;
import com.zc.pickuplearn.utils.ThreadUtils;
import com.zc.pickuplearn.utils.UIUtils;

import java.io.File;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * 图片工具类
 */
public class ImageLoaderUtil {


    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(ResultStringCommonUtils.subUrlToWholeUrl(url)).placeholder(placeholder)
                .error(error).crossFade().into(imageView);
    }

    /**
     * 问题列表里的图片加载方法
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void display(Context context, final ImageView imageView, String url) {
//        LogUtils.e("加载"+ResultStringCommonUtils.subUrlToWholeUrl(url));
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        try {
            String string = ResultStringCommonUtils.subUrlToWholeUrl(url);
            Glide.with(XActivityStack.getInstance().topActivity()).load(string)
                    .placeholder(R.mipmap.default_img)
                    .error(R.mipmap.default_img)
                    .fitCenter().crossFade().into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置带有图片处理的加载方法
     *
     * @param context              上下文
     * @param imgurl               图片路径
     * @param bitmapTransformation 图片处理
     * @param target               imagevie
     */
    public static void showTransImage(Context context, String imgurl, Transformation bitmapTransformation, ImageView target) {
        try {
            Glide.with(context).load(imgurl).placeholder(R.mipmap.default_img).bitmapTransform(bitmapTransformation).placeholder(R.mipmap.default_img).fitCenter().crossFade().into(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在circleimageview 中显示
     *
     * @param context
     * @param imageView
     * @param url
     * @param isLocal   是否是本地图片
     */
    public static void displayCircleView(Context context, ImageView imageView, String url, boolean isLocal) {
        try {
            Glide.with(context)
                    .load(isLocal ? url : ResultStringCommonUtils.subUrlToWholeUrl(url))
                    .dontAnimate()
                    .placeholder(R.mipmap.default_img)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在circleimageview 中显示
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void displayImage(Context context, ImageView imageView, String url, int hold_img) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(hold_img).error(hold_img).fitCenter().crossFade().into(imageView);
    }


    /**
     * 设置成背景
     *
     * @param context
     * @param imageView
     * @param url
     * @param isLocal
     */
    public static void displayBitmap(Context context, final ImageView imageView, String url, boolean isLocal) {
        Glide.with(context)
                .load(isLocal ? url : ResultStringCommonUtils.subUrlToWholeUrl(url))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null) {
                            imageView.setBackground(new BitmapDrawable(UIUtils
                                    .getResources(), resource));
                        }
                    }
                });
    }

    public static void displayBitmapImageResource(Context context, final ImageView imageView, String url, boolean isLocal) {
        Glide.with(context)
                .load(isLocal ? url : ResultStringCommonUtils.subUrlToWholeUrl(url))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null) {
                            imageView.setImageBitmap(resource);
                        }
                    }
                });
    }


    public static void displayImageResource(Context context, final ImageView imageView, String url, boolean isLocal) {
        try {
            Glide.with(context)
                    .load(isLocal ? url : ResultStringCommonUtils.subUrlToWholeUrl(url)).placeholder(R.mipmap.default_img).error(R.mipmap.default_img).thumbnail(0.1f)
                    .fitCenter().into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 圈子icon展示
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void displayCircleICon(Context context, final ImageView imageView, String url) {
        Glide.with(context)
                .load(ResultStringCommonUtils.circleUrlString(url))
                .dontAnimate()
                .placeholder(R.mipmap.default_img)
                .into(imageView);

    }

    /**
     * 毛玻璃效果
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void displayBlurBitmap(Context context, final View imageView, String url) {
        Glide.with(context)
                .load(ResultStringCommonUtils.subUrlToWholeUrl(url))
                .asBitmap()
                .transform(new BlurTransformation(context, 25))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null) {

                            imageView.setBackground(new BitmapDrawable(UIUtils
                                    .getResources(), resource));
                        }
                    }
                });
    }

    /**
     * 在circleimageview 中显示前景
     *
     * @param context   上下文
     * @param imageView 目标控件
     * @param url       地址
     * @param isLocal   是否是本地图片
     */
    public static void showCirCleViewImageResource(Context context, ImageView imageView, String url, boolean isLocal) {
        Glide.with(context)
                .load(isLocal ? url : ResultStringCommonUtils.subUrlToWholeUrl(url))
                .dontAnimate()
                .placeholder(R.mipmap.default_img)
                .into(imageView);
    }

    /**
     * 在circleimageview 中成背景显示
     *
     * @param context   上下文
     * @param imageView 目标控件
     * @param url       图片地址
     * @param isLocal   是否是本地图片
     */
    public static void showCirCleBackground(Context context, final ImageView imageView, String url, boolean isLocal) {
        Glide.with(context)
                .load(isLocal ? url : ResultStringCommonUtils.subUrlToWholeUrl(url))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null) {
                            imageView.setBackground(new BitmapDrawable(UIUtils
                                    .getResources(), resource));
                        }
                    }
                });
    }

    /**
     * 获取
     *
     * @param mContext
     * @param url
     */
    public static void getImageCachePath(final Context mContext, final String url) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> future = Glide.with(mContext)
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                try {
                    File cacheFile = future.get();
                    String path = cacheFile.getAbsolutePath();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 清除缓存
     *
     * @param mContext
     */
    public static void clearMemory(Context mContext) {
        Glide.get(mContext).clearMemory();
    }


    /**
     * 加载图片
     *
     * @param mContext
     * @param url
     * @param targetView
     */
    public static void showImageView(Context mContext, String url, ImageView targetView) {
        try {
            Glide.with(mContext).load(url).placeholder(R.mipmap.default_img)
                    .error(R.mipmap.default_img).fitCenter().crossFade().into(targetView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载图片
     *
     * @param mContext
     * @param resourceId
     * @param targetView
     */
    public static void showImageViewByResourceId(Context mContext, int resourceId, ImageView targetView) {
        try {
            Glide.with(mContext).load(resourceId).placeholder(R.mipmap.default_img)
                    .error(R.mipmap.default_img).fitCenter().crossFade().into(targetView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存图片到本地
     *
     * @param imgUrl   图片链接
     * @param callBack 下载回调
     */
    public static void downLoadImg(String imgUrl, ImageDownLoadCallBack callBack) {
        ThreadUtils.runOnSubThread(
                new DownLoadImageService(UIUtils.getContext(),
                        imgUrl,
                        callBack));
    }

    /**
     * 显示头像的
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void showHeadView(Context context, String url, ImageView imageView) {
        try {
            Glide.with(context)
                    .load(url)
                    .crossFade()
                    .dontAnimate()
                    .placeholder(R.mipmap.icon_person_head)
                    .error(R.mipmap.icon_person_head)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
