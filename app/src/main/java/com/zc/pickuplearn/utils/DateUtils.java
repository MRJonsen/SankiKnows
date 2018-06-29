package com.zc.pickuplearn.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 作者： Jonsen
 * 时间: 2017/1/17 16:55
 * 联系方式：chenbin252@163.com
 */

public class DateUtils {
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    /**
     * 获取当前时间
     *
     * @param format 当前时间的时间格式
     * @return
     */
    public static String dataFormatNow(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINESE);
        return simpleDateFormat.format(new Date());
    }

    /**
     *
     * @param format 时间格式
     * @return
     */
    public static String dataFormatNow(String format, String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINESE);
        String nowtime = "";
        try {
            Date parse = simpleDateFormat.parse(time);
            nowtime = simpleDateFormat.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nowtime;
    }


    /**
     * 比较两个时间点返回留言的时间
     *
     * @param dateString 需要比较的时间
     * @return
     */
    public static String getCompareDate(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:m:s", Locale.CHINESE);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }

    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


    public static String  formatDigit(String mm){
        String sign =  mm.substring(4);
        LogUtils.e(sign);
        if("00" .equals(sign) )
            sign = "0";
        if("01".equals(sign))
            sign = "一月";
        if("02".equals(sign))
            sign = "二月";
        if("03".equals(sign))
            sign = "三月";
        if("04".equals(sign))
            sign = "四月";
        if("05".equals(sign))
            sign = "五月";
        if("06".equals(sign))
            sign = "六月";
        if("07".equals(sign))
            sign = "七月";
        if("08".equals(sign))
            sign = "八月";
        if("09".equals(sign))
            sign = "九月";
        if("10".equals(sign))
            sign = "十月";
        if("11".equals(sign))
            sign = "十一月";
        if("12".equals(sign))
            sign = "十二月";
        return sign;
    }
    /**
     * 获取一个时间点前几个月的日期集合
     * @param date
     * @param index
     * @return
     */
    public static List<String> getDateList(Date date, int index) {
        List<String> arrayList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        for (int i = index; i > -1; i--) {
            System.out.println(i);
            cal.setTime(date);
            cal.add(Calendar.MONTH, -i);
            arrayList.add(sdf.format(cal.getTime()));
        }

        return arrayList;
    }

}
