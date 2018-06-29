package com.zc.pickuplearn.ui.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.utils.DataUtils;
import com.zc.pickuplearn.utils.LogUtils;

import java.math.BigDecimal;
import java.util.HashMap;

import butterknife.BindView;

public class VideoPlayerActivity extends AppCompatActivity {


    private AliyunVodPlayerView mAliyunVodPlayerView;
    private int currentPosition;

    private static String PARAM_1= "URL";
    private static String PARAM_2= "PARAM_2";
    private int videotime;
    private  CourseWareBean.DatasBean bean;
    private long pagebegintime;
    private long pageendtime;
    private long videobegintime;
    String playtime ="";
    public static void open(Context context, String url, CourseWareBean.DatasBean bean) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(PARAM_1,url);
        intent.putExtra(PARAM_2,bean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);


        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("Position");
        }else {
            pagebegintime = System.currentTimeMillis();
        }

        String video_url = getIntent().getStringExtra(PARAM_1);
        bean = (CourseWareBean.DatasBean) getIntent().getSerializableExtra(PARAM_2);
        mAliyunVodPlayerView = (AliyunVodPlayerView) findViewById(R.id.video_view);
        mAliyunVodPlayerView.setTheme(AliyunVodPlayerView.Theme.Blue);
        mAliyunVodPlayerView.setKeepScreenOn(true);//保持屏幕常亮
        AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
        alsb.setSource(video_url);
        AliyunLocalSource localSource = alsb.build();

        mAliyunVodPlayerView.setLocalSource(localSource);

        setPlayerListener();

    }

    private void setPlayerListener() {

        //设置播放器监听
        mAliyunVodPlayerView.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {


                mAliyunVodPlayerView.start();

            }
        });

        mAliyunVodPlayerView.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                playtime ="1";
            }
        });

        mAliyunVodPlayerView.setOnFirstFrameStartListener(new IAliyunVodPlayer.OnFirstFrameStartListener() {
            @Override
            public void onFirstFrameStart() {
                //首帧显示时触发
                if (currentPosition==0){
                    if (bean.getPLAYTIME()==null|| TextUtils.isEmpty(bean.getPLAYTIME())){
                        bean.setPLAYTIME("0");
                    }
                    videobegintime = (long) (Float.parseFloat(bean.getPLAYTIME())*videotime);
                    currentPosition = (int) videobegintime;
                }
                //准备完成时触发
                mAliyunVodPlayerView.seekTo(currentPosition);
            }
        });
        mAliyunVodPlayerView.setOnChangeQualityListener(new IAliyunVodPlayer.OnChangeQualityListener() {
            @Override
            public void onChangeQualitySuccess(String finalQuality) {
                //清晰度切换成功时触发
            }

            @Override
            public void onChangeQualityFail(int code, String msg) {
                //清晰度切换失败时触发
            }
        });
        mAliyunVodPlayerView.setOnStoppedListener(new IAliyunVodPlayer.OnStoppedListener() {
            @Override
            public void onStopped() {
                //使用stop接口时触发
                Log.e("setOnStoppedListener", "setOnStoppedListener");
            }
        });
        mAliyunVodPlayerView.setOnVideoSizeChangedListener(new IAliyunVodPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(int i, int i1) {
            }
        });



    }

    private boolean isStrangePhone() {
        boolean strangePhone = Build.DEVICE.equalsIgnoreCase("mx5")
                || Build.DEVICE.equalsIgnoreCase("Redmi Note2")
                || Build.DEVICE.equalsIgnoreCase("Z00A_1")
                || Build.DEVICE.equalsIgnoreCase("hwH60-L02")
                || Build.DEVICE.equalsIgnoreCase("hermes")
                || (Build.DEVICE.equalsIgnoreCase("V4") && Build.MANUFACTURER.equalsIgnoreCase("Meitu"))
                || (Build.DEVICE.equalsIgnoreCase("m1metal") && Build.MANUFACTURER.equalsIgnoreCase("Meizu"));
        return strangePhone;

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAliyunVodPlayerView != null) {
            currentPosition = mAliyunVodPlayerView.getCurrentPosition();

        }
        Log.e("onSaveInstanceState", "" + currentPosition);
        outState.putInt("Position", currentPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mAliyunVodPlayerView != null) {
            currentPosition = mAliyunVodPlayerView.getCurrentPosition();

        }
        Log.e("onConfigurationChanged", "" + currentPosition);
        setScreenConfig();

        mAliyunVodPlayerView.seekTo(currentPosition);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setScreenConfig();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAliyunVodPlayerView != null && mAliyunVodPlayerView.isPlaying()) {
            mAliyunVodPlayerView.onStop();
        }
    }

    private void setScreenConfig() {
        if (mAliyunVodPlayerView != null) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {                //转为竖屏了。
                //显示状态栏
//                if (!isStrangePhone()) {
//                    getSupportActionBar().show();
//                }

                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                //设置view的布局，宽高之类
                LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams) mAliyunVodPlayerView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;//(int) (ScreenUtils.getWidth(this) * 9.0f / 16)
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                if (!isStrangePhone()) {
//                    aliVcVideoViewLayoutParams.topMargin = getSupportActionBar().getHeight();
//                }

            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {                //转到横屏了。
                //隐藏状态栏
                if (!isStrangePhone()) {
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }

                //设置view的布局，宽高
                LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams) mAliyunVodPlayerView.getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                if (!isStrangePhone()) {
//                    aliVcVideoViewLayoutParams.topMargin = 0;
//                }

            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAliyunVodPlayerView != null) {
            boolean handler = mAliyunVodPlayerView.onKeyDown(keyCode, event);
            Log.e("------------", handler + "");
            if (!handler) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mAliyunVodPlayerView != null) {
            videotime = mAliyunVodPlayerView.getDuration();
            LogUtils.e("时长"+videotime);
            pageendtime = System.currentTimeMillis();
            postPlaytime();
            mAliyunVodPlayerView.onDestroy();
        }
        super.onDestroy();
    }


    public void postPlaytime(){
        HashMap<String, String> datas = new HashMap<>();
        datas.put("psnnum", DataUtils.getUserInfo().getUSERACCOUNT());
        datas.put("videoendtime", System.currentTimeMillis()+"");
        datas.put("ABILITYTYPECODE",bean.getABILITYTYPE());
        datas.put("playtime",TextUtils.isEmpty(playtime)?(float)mAliyunVodPlayerView.getCurrentPosition()/(float)videotime+"":playtime);
        datas.put("videotime",videotime+"");
        datas.put("videocurrenttime",mAliyunVodPlayerView.getCurrentPosition()+"");
        datas.put("coursecode",bean.getSEQKEY());
        datas.put("videobegintime",videobegintime+"");
        datas.put("pagebegintime",pagebegintime+"");
        datas.put("pageendtime",pageendtime+"");
        datas.put("coursename",bean.getCOURSENAME());
        API.postStudyProgress(datas, new CommonCallBack<String>() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure() {

            }
        });
    }
}
