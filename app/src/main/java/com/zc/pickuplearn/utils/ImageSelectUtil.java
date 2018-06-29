package com.zc.pickuplearn.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.zc.pickuplearn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择和拍照帮助类
 * Created by chenbin on 2017/8/28.
 */

public class ImageSelectUtil {
    public static void choicePicture(Context context, int selectCount, ArrayList<AlbumFile> checkedArray, final ImageSelectCallBack callBack) {
        int color = context.getResources().getColor(R.color.colorPrimary);
        Album.image(context)
                .multipleChoice()
                .widget(Widget.newDarkBuilder(context)
                        .title("图片选择") // 标题。
                        .statusBarColor(color) // 状态栏颜色。
                        .toolBarColor(color) // Toolbar颜色。
                        .navigationBarColor(color) // Android5.0+的虚拟导航栏颜色。
                        .mediaItemCheckSelector(color, color) // 图片或者视频选择框的选择器。
                        .bucketItemCheckSelector(color, color) // 切换文件夹时文件夹选择框的选择器。
                        .buttonStyle( // 用来配置当没有发现图片/视频时的拍照按钮和录视频按钮的风格。
                                Widget.ButtonStyle.newLightBuilder(context) // 同Widget的Builder模式。
                                        .setButtonSelector(Color.WHITE, Color.WHITE) // 按钮的选择器。
                                        .build()
                        )
                        .build())
                .requestCode(200)
                .camera(true)
                .columnCount(3)
                .selectCount(selectCount)
                .checkedList(checkedArray)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        List<String> paths = new ArrayList<>();
                        for (AlbumFile file :
                                result) {
                            paths.add(file.getPath());
                        }
                        callBack.onSuccess(result, paths);
                    }

//                    @Override
//                    public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
//                        List<String> paths = new ArrayList<>();
//                        for (AlbumFile file :
//                                result) {
//                            paths.add(file.getPath());
//                        }
//                        callBack.onSuccess(result, paths);
//                    }
//
//                    @Override
//                    public void onAlbumCancel(int requestCode) {
//                        Log.e("result", requestCode + "");
//                    }
                })
                .start();
    }

    /**
     * 图片选择
     * @param context 上下文
     * @param selectCount 选择数量
     * @param checkedArray 以选中的集合
     * @param chooseList 选择结果
     */
    public static void choicePicture(Context context, int selectCount, List<String> checkedArray, Action<ArrayList<AlbumFile>> chooseList) {
        ArrayList<AlbumFile> albumFiles = new ArrayList<>();
        if (checkedArray != null) {
            for (String url :
                    checkedArray) {
                AlbumFile albumFile = new AlbumFile();
                albumFile.setPath(url);
                albumFiles.add(albumFile);
            }
        }
        int color = context.getResources().getColor(R.color.colorPrimary);
        Album.image(context)
                .multipleChoice()
                .widget(Widget.newDarkBuilder(context)
                        .title("图片选择") // 标题。
                        .statusBarColor(color) // 状态栏颜色。
                        .toolBarColor(color) // Toolbar颜色。
                        .navigationBarColor(color) // Android5.0+的虚拟导航栏颜色。
                        .mediaItemCheckSelector(color, color) // 图片或者视频选择框的选择器。
                        .bucketItemCheckSelector(color, color) // 切换文件夹时文件夹选择框的选择器。
                        .buttonStyle( // 用来配置当没有发现图片/视频时的拍照按钮和录视频按钮的风格。
                                Widget.ButtonStyle.newLightBuilder(context) // 同Widget的Builder模式。
                                        .setButtonSelector(Color.WHITE, Color.WHITE) // 按钮的选择器。
                                        .build()
                        )
                        .build())
                .requestCode(200)
                .camera(true)
                .columnCount(3)
                .selectCount(selectCount)
                .checkedList(albumFiles)
                .onResult(chooseList)
                .start();
    }

    public static void takePhoto(Context context, final ImageSelectCallBack callBack) {
        Album.camera(context) // 相机功能。
                .image() // 拍照。
                // .filePath() // 文件保存路径，非必须。
                .requestCode(2)
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        List<String> paths = new ArrayList<>();
                        paths.add(result);
                        callBack.onSuccess(null, paths);
                    }

//                    @Override
//                    public void onAlbumResult(int requestCode, @NonNull String result) {
//                        List<String> paths = new ArrayList<>();
//                        paths.add(result);
//                        callBack.onSuccess(null, paths);
//                    }
//
//                    @Override
//                    public void onAlbumCancel(int requestCode) {
//                    }
                })
                .start();
    }

    public static interface ImageSelectCallBack {
        void onSuccess(ArrayList<AlbumFile> albumFiles, List<String> imgPaths);
    }
}
