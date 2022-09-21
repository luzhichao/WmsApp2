package com.linkingwin.education.studyonline.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String lastHalfyear = "半年内";
    public static final String lastMonth = "近一个月";
    public static final String lastWeek = "近一周";
    public static final String moreTime = "更早";
    public static final String yearMonthDayHourMinSecond = "yyyy-MM-dd HH:mm:ss";
    public static final String hourMinSecond = "HH:mm:ss";
    public static final String yearMothDay = "yyyy-MM-dd";
    public static final String yyyymmdd = "yyyyMMdd";

    public static final String INITDATE = "1970-01-01 00:00:00";

    public static int compareDate(String paramString1, String paramString2)
            throws ParseException {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        Long localLong1 = Long.valueOf(localSimpleDateFormat
                .parse(paramString1).getTime());
        Long localLong2 = Long.valueOf(localSimpleDateFormat
                .parse(paramString2).getTime());
        if (localLong1.longValue() > localLong2.longValue())
            return -1;
        if (localLong1.longValue() < localLong2.longValue())
            return 1;
        return 0;
    }

    public static String computeTime(long paramLong) {
        if (paramLong >= 86400000L)
            return String.valueOf(paramLong / 86400000L) + "天";
        if (paramLong >= 3600000L)
            return String.valueOf(paramLong / 3600000L) + "小时";
        if (paramLong >= 60000L)
            return String.valueOf(paramLong / 60000L) + "分钟";
        return String.valueOf(paramLong / 1000L) + "秒";
    }

    public static Long difDate(String paramString) {
        Date localDate = pareseStringToDate(paramString);
        long l1 = getCurrentDate().getTime() - localDate.getTime();
        long l2 = l1 / 86400000L;
        long l3 = l1 / 3600000L - 24L * l2;
        long l4 = l1 / 60000L - 60L * (24L * l2) - 60L * l3;
        long l5 = l1 / 1000L - 60L * (60L * (24L * l2)) - 60L * (60L * l3)
                - 60L * l4;
        System.out.println(l2 + "天" + l3 + "小时" + l4 + "分" + l5 + "秒");
        return Long.valueOf(l2);
    }

    public static String difDate(String paramString1, String paramString2,
                                 String paramString3, String paramString4) {
        Date localDate = pareseStringToDate(paramString2, paramString1);
        return computeTime(pareseStringToDate(paramString4, paramString3)
                .getTime() - localDate.getTime());
    }

    public static String difDate(String paramString1, String paramString2,
                                 Date paramDate) {
        Date localDate = pareseStringToDate(paramString2, paramString1);
        return computeTime(paramDate.getTime() - localDate.getTime());
    }

    public static String getCurrentDate(String paramString) {
        return new SimpleDateFormat(paramString).format(new Date());
    }

    public static Date getCurrentDate() {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        try {
            Date localDate = localSimpleDateFormat.parse(localSimpleDateFormat
                    .format(new Date()));
            return localDate;
        } catch (ParseException localParseException) {
            localParseException.printStackTrace();
        }
        return null;
    }

    public static String getCurrentDateString() {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        try {
            String str = localSimpleDateFormat.format(new Date());
            return str;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

    public static String getDateScale(String paramString) {
        Long localLong = difDate(paramString);
        if (localLong.longValue() <= 6L)
            return "近一周";
        if (localLong.longValue() <= 29L)
            return "近一个月";
        if (localLong.longValue() <= 180L)
            return "半年内";
        return "更早";
    }

    public static Date getNowDate() {
        return new Date();
    }

    public static Date pareseStringToDate(String paramString) {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        try {
            Date localDate = localSimpleDateFormat.parse(paramString);
            return localDate;
        } catch (ParseException localParseException) {
            localParseException.printStackTrace();
        }
        return null;
    }

    public static Date pareseStringToDate(String paramString1,
                                          String paramString2) {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                paramString2);
        try {
            Date localDate = localSimpleDateFormat.parse(paramString1);
            return localDate;
        } catch (ParseException localParseException) {
            localParseException.printStackTrace();
        }
        return null;
    }

    public static String transDuration(String paramString) {
        int i = Integer.parseInt(paramString);
        if (i < 60)
            return paramString + "分钟";
        if (i < 1440)
            return String.valueOf(i / 60) + "小时";
        return String.valueOf(i / 1440) + "天";
    }

    /**
     * 功能：解析日期为指定格式的字符串
     *
     * @param date
     * @param formatStr
     * @return formatStr格式对应的字符串
     */
    public static String parseDateToStr(Date date, String formatStr) {

        if (date == null || formatStr == null || "".equals(formatStr)) return "";
        return new SimpleDateFormat(formatStr).format(date);
    }

    /**
     * 年、月、日转换为Date
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date parseYMDToDate(int year, int month, int day) {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        d.set(Calendar.YEAR, year);
        d.set(Calendar.MONTH, month);
        d.set(Calendar.DAY_OF_MONTH, day);
        return d.getTime();
    }

    public static void main(String[] args) {
        System.out.print(parseDateToStr(new Date(), yyyymmdd));
    }


}
