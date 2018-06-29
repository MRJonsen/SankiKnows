//package com.hzzenith.dofile.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.Window;
//import android.widget.LinearLayout;
//
//import com.hzzenith.dofile.R;
//import com.hzzenith.dofile.pdfview.PDFViewPager;
//
//import java.io.File;
//
//public class PDFReadActivity extends AppCompatActivity {
//
//    private LinearLayout llPdfRoot;
//    private Toolbar mCommonToolbar;
//    private static String TAG_TITLE = "title";
//    public static void start(Context context, String filePath,String name) {
//        Intent intent = new Intent(context, PDFReadActivity.class);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.putExtra(TAG_TITLE,name);
//        intent.setData(Uri.fromFile(new File(filePath)));
//        context.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_pdfread);
//        llPdfRoot = (LinearLayout) findViewById(R.id.llPdfRoot);
//        mCommonToolbar = (Toolbar) findViewById(R.id.common_toolbar);
//        initToolBar();
//        initDatas();
//    }
//
//    public void initToolBar() {
//        String filePath = Uri.decode(getIntent().getDataString().replace("file://", ""));
//        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
//        String name = getIntent().getStringExtra(TAG_TITLE);
//        if (TextUtils.isEmpty(name)){
//            mCommonToolbar.setTitle(fileName);
//        }else {
//            mCommonToolbar.setTitle(name);
//        }
//        mCommonToolbar.setNavigationIcon(R.mipmap.ab_back);
//        mCommonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//
//    public void initDatas() {
//        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
//            String filePath = Uri.decode(getIntent().getDataString().replace("file://", ""));
//
//            PDFViewPager pdfViewPager = new PDFViewPager(this, filePath);
//            llPdfRoot.addView(pdfViewPager);
//        }
//    }
//
//}
