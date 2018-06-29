package com.zc.pickuplearn.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static com.zc.pickuplearn.utils.UIUtils.getResources;

public class PictureUtil {
	/** 
     * 1.质量压缩 
     * @param image 
     * @return 
     */  
    private static Bitmap compressImage(Bitmap image) {  
  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>1024) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少5
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }  
      
      
    /** 
     * 图片按比例大小压缩方法 
     * @param srcPath  图片路径 
     * @return
     *
     */  
    private static Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 720f;//这里设置高度为800f
        float ww = 1280f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        //进行 质量压缩  
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    }  
      
    /** 
     * 得到临时图片路径 
     * @param filePath 
     * @return  
     * @return 
     * @throws IOException  
     */  
    public static String bitmapToPath(String filePath) throws IOException {
        Bitmap bm = getimage(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //得到文件名  
        String imgName=getfilepath(filePath);
        //得到存放路径  
        String sdPath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/ImgTmp";  
        //获取 sdcard的跟目录
        File parent =new File(sdPath);  
        if(!parent.exists()){  
            //创建路径  
            parent.mkdirs();  
        }  
        //写入 临时文件  
        File file =new File(parent,imgName);  
        file.createNewFile();  
        FileOutputStream fos = new FileOutputStream(file);  
        fos.write(baos.toByteArray());  
        fos.flush();  
        fos.close();  
        baos.close();  
        //返回图片路径  
        return sdPath+"/"+imgName;  
          
    }


    /**
     * 获取 glide加载图片的路径
     * @param context
     * @param url
     * @return
     */
    public String getImgPathFromCache(Context context, String url) {
        FutureTarget<File> future = Glide.with(context).load(url).downloadOnly(100, 100);
        try {
            File cacheFile = future.get();
            String path = cacheFile.getAbsolutePath();
            return path;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
    /** 
     *  
     * @param path 
     * @return 
     */  
    private static String getfilepath(String path){
        return System.currentTimeMillis()+getExtensionName(path);  
    }  
      
      
    /* 
     * 获取文件扩展名 
     */  
   private static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {  
            int dot = filename.lastIndexOf('.');  
            if ((dot > -1) && (dot < (filename.length() - 1))) {  
                return filename.substring(dot, filename.length());  
            }  
        }  
        return filename;  
    }  
  
     
   /** 
    * 删除临时文件 
    * @param imgs 
    */  
   public static void deleteImgTmp(List<String> imgs){  
         
       for (String string : imgs) {  
        File file=new File(string);  
        if(file.exists()){  
            file.delete();  
        }  
    }  
         
   }

    //drawable 着色
    public static void setImageViewColor(ImageView view, int colorResId) {
        //mutate()
        Drawable modeDrawable = view.getDrawable().mutate();
        Drawable temp = DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList =     ColorStateList.valueOf(view.getResources().getColor(colorResId));
        DrawableCompat.setTintList(temp, colorStateList);
        view.setImageDrawable(temp);
    }


    /**
     * 纯色图片着色
     * @param drawableId
     * @param colorId
     * @return
     */
    public static Drawable tintDrawAble(int drawableId,int colorId){
        Drawable icon = getResources().getDrawable(drawableId);
        Drawable tintIcon = DrawableCompat.wrap(icon);
        DrawableCompat.setTintList(tintIcon, getResources().getColorStateList(colorId));
        return tintIcon;
    }


    public static Bitmap textAsBitmap(Context context,String text, int size) {
        int[] ints = new int[]{
            Color.GRAY,Color.LTGRAY,Color.BLUE,Color.GREEN
        };
        Random random = new Random();
        int measuredWidth = SystemUtils.dipToPixel(context,size);
        Bitmap bitmap = Bitmap.createBitmap(measuredWidth,
                measuredWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(ints[random.nextInt(ints.length)]);
        Paint textPaint = new Paint();          // 创建画笔
        textPaint.setColor(Color.WHITE);        // 设置颜色
        textPaint.setStyle(Paint.Style.FILL);   // 设置样式
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(60);
        canvas.drawText(text,measuredWidth/2,measuredWidth/2+20,textPaint);
        return bitmap;
    }
}
