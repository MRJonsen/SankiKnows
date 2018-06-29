package com.zc.pickuplearn.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zc.pickuplearn.utils.UIUtils.getResources;


public class SystemUtils {
    private static final boolean isDebug = true;
    public static final String TAG = "debuginfo";

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String toHexString(byte[] b) {
        // String to byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * MD5加密
     */
    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据logourl生成对应的文件名称
     */
    public static String getNameFromLogoUrl(String logoUrl) {
        if (TextUtils.isEmpty(logoUrl)) {
            return null;
        }

        return md5(URLEncoder.encode(logoUrl));
    }

    public static String getFileNameFronUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static boolean checkExternalStorageAvailable() {
        boolean bAvailable = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        if (bAvailable) {
            StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            if ((long) statfs.getAvailableBlocks() * (long) statfs.getBlockSize() < 1048576) {
                //ToastManager.show(sApplication, R.string.prompt_sdcard_full);
                bAvailable = false;
            }
        } else {
            //ToastManager.show(sApplication, R.string.prompt_sdcard_unavailable);
        }
        return bAvailable;
    }

    private static float sDensity = 0;

    private static int WIDTH = 0;
    private static int HEIGHT = 0;
    private static int DPI = 0;

    /**
     * 计算视图的宽高。
     *
     * @param view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int nWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int nHeight = p.height;
        int nHeightSpec;

        if (nHeight > 0) {
            nHeightSpec = MeasureSpec.makeMeasureSpec(nHeight, MeasureSpec.EXACTLY);
        } else {
            nHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(nWidthSpec, nHeightSpec);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (WIDTH == 0) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            DPI = dm.densityDpi;
            WIDTH = dm.widthPixels;
            HEIGHT = dm.heightPixels;
        }
        return WIDTH;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (HEIGHT == 0) {
            getScreenWidth(context);
        }
        return HEIGHT;
    }

    /***
     * 获取屏幕密度
     *
     * @return
     */
    public static int getScreenDpi(Context context) {
        if (DPI == 0) {
            getScreenWidth(context);
        }
        return DPI;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public static int getStateHeight(Activity context) {
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }


    /***
     * 是否连接的wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /***
     * 当前网络是否连通
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 当前网络是否连通
     *
     * @param intent
     * @return
     */
    public static boolean isNetworkAvailable(Intent intent) {
        Parcelable p = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        if (p != null) {
            NetworkInfo ni = (NetworkInfo) p;
            if (ni.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取随机数
     *
     * @param nStart 最小数值
     * @param nEnd   最大数值
     * @return
     */
    public static int randomRange(int nStart, int nEnd) {
        if (nStart >= nEnd) {
            return nEnd;
        }
        return nStart + (int) (Math.random() * (nEnd - nStart));
    }

    /**
     * sd卡是否挂载并可读写
     *
     * @return
     */
    public static boolean isExternalStorageMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取外部应用程序缓存路径
     *
     * @param context
     * @return
     */
    public static String getExternalCachePath(Context context) {
        String path = Environment.getExternalStorageDirectory().getPath() +
                "/Android/data/" + context.getPackageName() + "/cache";
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return path;
    }

    /**
     * dip转px
     *
     * @param context
     * @param nDip
     * @return
     */
    public static int dipToPixel(Context context, int nDip) {
        if (sDensity == 0) {
            final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            sDensity = dm.density;
        }
        return (int) (sDensity * nDip);
    }

    /**
     * 获取系统uuid
     *
     * @param context
     * @return
     */
    public static String getDeviceUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String strIMEI = tm.getDeviceId();
        if (TextUtils.isEmpty(strIMEI)) {
            strIMEI = "1";
        }

        String strMacAddress = getMacAddress(context);
        if (TextUtils.isEmpty(strMacAddress)) {
            strMacAddress = "1";
        }

        return strIMEI + strMacAddress;
    }

    /**
     * 获取手机IMEI码
     */
    public static String getIMEI(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String strIMEI = tm.getDeviceId();
        if (TextUtils.isEmpty(strIMEI)) {
            strIMEI = "1";
        }
        return strIMEI;
    }

    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        final WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm.getConnectionInfo().getMacAddress();
    }

    public static void copyToClipBoard(Context context, String strText) {
        final ClipboardManager manager = (ClipboardManager) context.getSystemService(
                Context.CLIPBOARD_SERVICE);
        manager.setText(strText);
    }

    /**
     * 验证是否符合email规范
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(strEmail);
        return matcher.matches();
    }

    /**
     * 是否符合手机号码规范
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,3,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 是否符合手机号码规范2
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile2(String mobiles) {
        Pattern p = Pattern.compile("^[1][3-8]+\\d{9}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 获取应用版本号名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本机ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取位置信息
     * @param context
     * @return
     */
//	public static Location getLocation(Context context){
//		final LocationManager locationManager = (LocationManager)context
//				.getSystemService(Context.LOCATION_SERVICE);
//		Criteria criteria = new Criteria();
//		final String strProvider = locationManager.getBestProvider(criteria, false);
//		Location location = locationManager.getLastKnownLocation(strProvider);
//		if(location == null){
//			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//		}
//		if(location == null){
//			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//		}
//		return location;
//	}

    /**
     * 添加输入框输入长度限制
     *
     * @param editText
     * @param nLengthLimit
     */
    public static void addEditTextLengthFilter(EditText editText, int nLengthLimit) {
        InputFilter filters[] = editText.getFilters();
        if (filters == null) {
            editText.getEditableText().setFilters(
                    new InputFilter[]{new InputFilter.LengthFilter(nLengthLimit)});
        } else {
            final int nSize = filters.length + 1;
            InputFilter newFilters[] = new InputFilter[nSize];
            int nIndex = 0;
            for (InputFilter filter : filters) {
                newFilters[nIndex++] = filter;
            }
            newFilters[nIndex] = new InputFilter.LengthFilter(nLengthLimit);
            editText.getEditableText().setFilters(newFilters);
        }
    }

    /**
     * 图片转为圆形
     *
     * @param bitmap
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * bitmap转base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            baos.flush();
            baos.close();

            byte[] bitmapBytes = baos.toByteArray();
            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void print(String str) {
        int l = 160;
        if (isDebug) {
            Log.d(TAG, str);
//			if(str.length() <= l) {
//				Log.d(TAG, str);
//				return;
//			}
//			String[] infos = str.split("\r\n");
//			for(String info : infos) {
//				int nums = info.length() / l;
//				int aa = info.length() % l;
//				for(int i = 0; i < nums; i++) {
//					Log.d(TAG, info.substring(i * l, i * l + l));
//				}
//				Log.d(TAG, info.substring(nums * l, nums * l + aa));
//			}
        }
    }


    /**
     * 获取当前系统的SDK版本
     */
    public static int getSdkLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统软件包版本名称
     */
    public static String getApkVersionName(Context context) {
        if (null == context) {
            return null;
        }

        PackageManager pkgManager = context.getPackageManager();
        if (null == pkgManager) {
            return null;
        }

        try {
            PackageInfo info = pkgManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (null == info) {
                return null;
            }

            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取系统软件包版本号
     */
    public static String getApkVersionCode(Context context) {
        if (null == context) {
            return null;
        }

        PackageManager pkgManager = context.getPackageManager();
        if (null == pkgManager) {
            return null;
        }

        try {
            PackageInfo info = pkgManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (null == info) {
                return null;
            }

            return String.valueOf(info.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取archiveFilePath该文件的版本号
     *
     * @param context
     * @param archiveFilePath 为apk的路径
     * @return 版本号
     */
    public static String getApkVersion(Context context, String archiveFilePath) {
        if (null == context) {
            return null;
        }

        PackageManager pkgManager = context.getPackageManager();
        if (null == pkgManager) {
            return null;
        }

        try {
            PackageInfo info = pkgManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
            if (null == info) {
                return null;
            }

            return String.valueOf(info.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机品牌
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取操作系统型号
     */
    public static String getOS() {
        return "Android";
    }


    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;

    }

    /**
     * 获取本机号码
     */
    public static String getTelePhoneNumber(Context context) {
        //获取手机号码
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = tm.getDeviceId();// 获取智能设备唯一编号
        String te1 = tm.getLine1Number();// 获取本机号码
        String imei = tm.getSimSerialNumber();// 获得SIM卡的序号
        String imsi = tm.getSubscriberId();// 得到用户Id

        return te1;
    }

    /**
     * 获取当前应用程序的包名
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    /**
     * 校验Tag Alias 只能是数字,英文字母和中文
     */
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    public static void call(Context context,String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
