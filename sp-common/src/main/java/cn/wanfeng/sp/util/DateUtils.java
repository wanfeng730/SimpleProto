package cn.wanfeng.sp.util;


import cn.hutool.core.date.DateUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @date: 2024-12-19 23:28
 * @author: luozh.wanfeng
 * @description: 时间工具类，继承了伟大的hutool的日期工具类，在此类上增加更多方便实用的功能
 * @since: 1.0
 */
public class DateUtils extends DateUtil {

    private static final String F_DATE_TIME_DIGITS = "yyyyMMddHHmmss";
    private static final String F_DATE_TIME_MILLIS_DIGITS = "yyyyMMddHHmmssSSS";

    private static final String F_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    private static final String F_DATE_TIME_MILLIS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 用于当前时间显示的格式，不使用空格避免不兼容
     */
    private static final String F_DATE_TIME_NO_SPACE = "yyyy-MM-dd_HH:mm:ss";
    private static final String F_DATE_TIME_MILLIS_NO_SPACE = "yyyy-MM-dd_HH:mm:ss.SSS";

    /**
     * 获取当前时间 yyyyMMddHHmmss
     */
    public static String currentDateTimeDigits(){
        return format(new Date(), F_DATE_TIME_DIGITS);
    }

    /**
     * 获取当前时间 yyyyMMddHHmmssSSS
     */
    public static String currentDateTimeMillisDigits(){
        return format(new Date(), F_DATE_TIME_MILLIS_DIGITS);
    }

    /**
     * 获取当前时间 yyyy-MM-dd_HH:mm:ss
     * @return 当前时间
     */
    public static String currentDateTimeNoSpace() {
        return format(new Date(), F_DATE_TIME_NO_SPACE);
    }

    public static String currentDateTimeMillisNoSpace() {
        return format(new Date(), F_DATE_TIME_MILLIS_NO_SPACE);
    }

    public static String currentDateTime() {
        return format(new Date(), F_DATE_TIME);
    }

    public static String currentDateTimeMillis() {
        return format(new Date(), F_DATE_TIME_MILLIS);
    }

    /**
     * LocalDateTime 转 Date
     * @param localDateTime LocalDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime localDateTime){
        // 获取系统默认时区
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static String formatWithMillis(Date date){
        return format(date, F_DATE_TIME_MILLIS);
    }
}
