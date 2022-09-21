package com.linkingwin.elearn.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {

    static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat timeFormatStr = new SimpleDateFormat("yyyyMMdd_HHmmss");

    /**
     * 获取当前年月日时分秒字符串
     * @return
     */
    public static String getNowTimeStr(){
        Date now = new Date();
        return timeFormatStr.format(now);
    }

    /**
     * 带格式的时间字符串
     * @return
     */
    public static String getFormatNowStr(){
        Date now = new Date();
        return timeFormat.format(now);
    }

}
