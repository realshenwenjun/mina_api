package com.mina.core.util;

/*
 * <p>Title: EAP企业应用开发平台</p>
 *
 * <p>Description: 旨在为各位同仁提供统一的基础开发平台，提高开发效率，改进工作质量！</p>
 *
 * <p>Copyright: Copyright (C) Surekam 2008</p>
 *
 * <p>Company: www.surekam.com</p>
 */

import com.mina.core.enu.DateStyle;
import org.apache.commons.lang.time.DateUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p> 描述: 日期组件</p>
 * <p>
 * <p> Create Date: 2008-04-15 <p>
 *
 * @author zzq
 * @version 1.0
 */
public class DateUtil {

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

    private static final Object object = new Object();

    /**
     * 时间毫秒单位.
     */
    private static final int TIMEMSUNIT = 1000;

    /**
     * 时间单位.
     */
    private static final int TIMEUNIT = 60;

    private static final String CHS_YYYY_MM_DD = "yyyy年MM月dd日";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String HH_MM_SS = "HH:mm:ss";
    private static final String HH_MM = "HH:mm";
    private static final String CHS_HH_MM = "HH点mm";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String YYYY_MM_DD_HH_MM_SS_zcg = "yyyyMMddHHmmss";
    private static final String YYYY_MM_DD_HH_MM_SS_MS = "yyyyMMddHHmmssms";
    private static final String YYYY_MM_DD_HH_MM_SS_ID = "yyMMddHHmmss";
    private static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    private static final String YYYY_MMDD = "yyyyMMdd";
    private static final String MM_DD = "MMdd";
    private static final String yyyy_MM = "yyyy/MM";
    private static final String yyyyMM = "yyyyMM";

    private final static SimpleDateFormat dateformater1 = new SimpleDateFormat("yyyy-MM-dd");
    public static boolean defaultTimeFlag = true; // 查询条件默认时间显示标志

    /* 开始时间点 */
    public final static String startTimePoint = " 00:00:00";
    /* 结束时间点 */
    public final static String endTimePoint = " 23:59:59";

    /**
     * 得到本月的第一天
     *
     * @return
     */
    public static String getMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMinimum(Calendar.DAY_OF_MONTH));

        return dateformater1.format(calendar.getTime());
    }

    /**
     * 得到本月的最后一天
     *
     * @return
     */
    public static String getMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateformater1.format(calendar.getTime());
    }

    private static SimpleDateFormat chshhmmFotmator, mmddFotmator, chsDateformater, dateformater, timeformater, dateTimeformater, dateTimeformaterzcg, dateIDformater, dateTimeformaterAsSeqNo, dateTimeformaterByIDGenerator,
            timeformaterzcg;


    private static DateFormat getMmddFormater() {
        if (mmddFotmator == null)
            mmddFotmator = new SyncSimpleDateFormat(MM_DD);
        return mmddFotmator;
    }

    private static DateFormat getChsHhmmFormater() {
        if (chshhmmFotmator == null)
            chshhmmFotmator = new SyncSimpleDateFormat(CHS_HH_MM);
        return chshhmmFotmator;
    }


    /**
     * 获取日期格式的DateFormater
     *
     * @return DateFormat
     */
    private static DateFormat getDateFormater() {
        if (dateformater == null)
            dateformater = new SyncSimpleDateFormat(YYYY_MM_DD);
        return dateformater;
    }

    public static SimpleDateFormat getChsDateformater() {
        if (chsDateformater == null)
            chsDateformater = new SyncSimpleDateFormat(CHS_YYYY_MM_DD);
        return chsDateformater;
    }

    public static void setChsDateformater(SimpleDateFormat chsDateformater) {
        DateUtil.chsDateformater = chsDateformater;
    }

    /**
     * 获取时间格式的DateFormater
     *
     * @return DateFormat
     */
    private static DateFormat getTimeFormater() {
        if (timeformater == null)
            timeformater = new SyncSimpleDateFormat(HH_MM_SS);
        return timeformater;
    }

    /**
     * 获取日期时间格式的DateFormater
     *
     * @return DateFormat
     */
    private static DateFormat getDateTimeFormater() {
        if (dateTimeformater == null)
            dateTimeformater = new SyncSimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return dateTimeformater;
    }

    private static DateFormat getDateTimeFormaterZcg() {
        if (dateTimeformaterzcg == null)
            dateTimeformaterzcg = new SyncSimpleDateFormat(YYYY_MM_DD_HH_MM_SS_zcg);
        return dateTimeformaterzcg;
    }

    private static DateFormat getTimeFormaterZcg() {
        if (timeformaterzcg == null) {
            timeformaterzcg = new SyncSimpleDateFormat(HH_MM);
        }
        return timeformaterzcg;
    }

    private static DateFormat getDateTimeFormaterByIDGenerator() {
        if (dateTimeformaterByIDGenerator == null)
            dateTimeformaterByIDGenerator = new SyncSimpleDateFormat(YYYY_MM_DD_HH_MM_SS_ID);
        return dateTimeformaterByIDGenerator;
    }


    /**
     * 获取日期时间格式的DateFormater
     *
     * @return DateFormat
     */
    private static DateFormat getDateTimeFormaterAsSeqNo() {
        if (dateTimeformaterAsSeqNo == null)
            dateTimeformaterAsSeqNo = new SyncSimpleDateFormat(YYYY_MM_DD_HH_MM_SS_MS);
        return dateTimeformaterAsSeqNo;
    }

    /**
     * 获得没有秒的时间字符串
     *
     * @return
     */
    public static String getDateTimeFormaterExceptSeconds(Date date) {
        dateTimeformater = new SyncSimpleDateFormat(YYYY_MM_DD_HH_MM);
        return dateTimeformater.format(date);
    }


    /**
     * 获取日期时间格式的DateFormater
     *
     * @return DateFormat
     */
    private static DateFormat getDateIDFormater() {
        if (dateIDformater == null)
            dateIDformater = new SyncSimpleDateFormat(YYYY_MMDD);
        return dateIDformater;
    }

    /**
     * 每天的毫秒数
     */
    private final static long DAY_IN_MILLISECOND = 1000 * 3600 * 24;

    /**
     * 每天的秒数
     */
    public final static long DAY_IN_SECOND = 3600 * 24;


    private DateUtil() {
    }

    /**
     * 获取现在的时间加日期
     *
     * @return String
     */
    public static String getNow() {
        return getDateTimeFormater().format(new Date());
    }

    /**
     * 获取现在的日期
     *
     * @return String
     */
    public static String getDate() {
        return getDateFormater().format(new Date());
    }

    /**
     * 获取现在的时间
     *
     * @return String
     */
    public static String getTime() {
        return getTimeFormater().format(new Date());
    }

    /**
     * 获取日期
     *
     * @param checkTime Timestamp
     * @return String
     */
    public static String getDate(Timestamp checkTime) {
        return formatDate(checkTime);
    }

    /**
     * 获取时间
     *
     * @param checkTime Timestamp
     * @return String
     */
    public static String getTime(Timestamp checkTime) {
        return getTimeFormater().format(checkTime);
    }

    /**
     * 获取java.util.Date类型的日期
     *
     * @param startDate Date : 起始时间
     * @param days      int : 指定天数
     * @return Date java.util.Date
     */
    public static Date getDate(Date startDate, int days) {
        Date returnDay = dateDayAdd(startDate, days);
        return returnDay;
    }

    /**
     * 获取java.util.Date类型的日期
     *
     * @param startDate String : 起始日期
     * @param days      int : 指定天数
     * @return Date java.util.Date
     */
    public static Date getDate(String startDate, int days) {
        Date start = parseDateTime(startDate);
        Date date = dateDayAdd(start, days);
        return date;
    }

    /**
     * 获取java.util.Date类型的日期
     *
     * @param startDate Date : 起始日期
     * @param months    int : 指定月数
     * @return Date java.util.Date
     */
    public static Date getMonthDate(Date startDate,
                                              int months) {
        Date returnDay = dateMonthAdd(startDate, months);
        return returnDay;
    }

    /**
     * 获取java.util.Date类型的日期
     *
     * @param startDate String : 起始日期
     * @param months    int : 指定月数
     * @return Date java.util.Date
     */
    public static Date getMonthDate(String startDate, int months) {
        Date start = parseDate(startDate);
        Date date = dateMonthAdd(start, months);
        return date;
    }

    /**
     * 获取java.sql.Timestamp类型的日期时间
     *
     * @param startTimestamp : 起始日期时间
     * @param minutes        : 指定分钟数
     */
    public static Timestamp getDateTime(String startTimestamp,
                                                 int minutes) {
        Timestamp start = parseTimestamp(startTimestamp);
        return dateDateTimeAdd(start, minutes);
    }

    /**
     * 获取当前月份
     *
     * @return String 格式:yyyy-mm
     */
    public static String getCurMonth() {
        return new SimpleDateFormat("yyyy-MM").format(new Date());
    }

    /**
     * 根据日期获得当前月的开始日期
     *
     * @param month 日期
     * @return 所属月份的开始日期(java.util.Date)
     */
    public static Date getMonthBegin(Date month) {
        GregorianCalendar ca = new GregorianCalendar();
        ca.setTime(month);
        int year = ca.get(GregorianCalendar.YEAR);
        int mon = ca.get(GregorianCalendar.MONTH);
        GregorianCalendar gCal = new GregorianCalendar(year, mon, 1);
        return gCal.getTime();
    }

    /**
     * 根据年份和月份获得当前月的开始日期
     *
     * @param year  年份
     * @param month 月份
     * @return 所属月份的开始日期(java.util.Date)
     */
    public static Date getMonthBegin(int year, int month) {
        if (month <= 0 || month > 12)
            throw new IllegalArgumentException("month must from 1 to 12");
        GregorianCalendar gCal = new GregorianCalendar(year, month - 1, 1);
        return gCal.getTime();
    }

    /**
     * 根据日期获得当前月的结束日期
     *
     * @param month 日期
     * @return 所属月份的结束日期(java.util.Date)
     */
    public static Date getMonthEnd(Date month) {
        GregorianCalendar ca = new GregorianCalendar();
        ca.setTime(month);
        int year = ca.get(GregorianCalendar.YEAR);
        int mon = ca.get(GregorianCalendar.MONTH);
        int lastDay = ca.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        GregorianCalendar gCal = new GregorianCalendar(year, mon, lastDay);
        return gCal.getTime();
    }

    /**
     * 根据年份和月份获得当前月的结束日期
     *
     * @param year  年份
     * @param month 月份
     * @return 所属月份的结束日期(java.util.Date)
     */
    public static Date getMonthEnd(int year, int month) {
        Date start = getMonthBegin(year, month);
        return getMonthEnd(start);
    }

    /**
     * 根据日期参数获取当前周的起始日期(周日)
     *
     * @param date 日期
     * @return 所属星期的起始日期(java.util.Date)
     */
    public static Date getWeekBegin(Date date) {
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(date);
        int startDay = gCal.getActualMinimum(GregorianCalendar.DAY_OF_WEEK);
        gCal.set(GregorianCalendar.DAY_OF_WEEK, startDay);
        return gCal.getTime();
    }

    /**
     * 根据日期参数获取当前周的结束日期(周六)
     *
     * @param date 日期
     * @return 所属星期的结束日期(java.util.Date)
     */
    public static Date getWeekEnd(Date date) {
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(date);
        int lastDay = gCal.getActualMaximum(GregorianCalendar.DAY_OF_WEEK);
        gCal.set(GregorianCalendar.DAY_OF_WEEK, lastDay);
        return gCal.getTime();
    }

    /**
     * 获取指定时间段内的天数
     *
     * @param beginDate Date
     * @param endDate   Date
     * @return long
     */
    public static long getDays(Date beginDate, Date endDate) {
        return (beginDate.getTime() - endDate.getTime()) / (24 * 60 * 60 * 1000);

    }


    /**
     * 获取指定时间段内的天数
     *
     * @param beginTime Timestamp
     * @param endTime   Timestamp
     * @return int
     */
    public static int getDays(Timestamp beginTime,
                              Timestamp endTime) {
        int days = (int) ((endTime.getTime() - beginTime.getTime()) / DAY_IN_MILLISECOND);
        return days;
    }

    /**
     * 生成日期ID ex: 传入参数:2004-5-1 返回:20040501
     *
     * @param date Date
     * @return String
     */
    public static String getDateID(Date date) {
        return getDateIDFormater().format(date);
    }

    /**
     * 生成日期ID ex: 传入参数:2004-5-1 12:32:33 返回:20040501
     *
     * @param timestamp Timestamp
     * @return String
     */
    public static String getDateID(Timestamp timestamp) {
        return getDateIDFormater().format(timestamp);
    }

    /**
     * 将java.util.Date格式转换成日期字符串
     *
     * @return String
     */
    public static String formatDate(Date date) {
        return getDateFormater().format(date);
    }

    public static String formatMmdd(Date date) {
        return getMmddFormater().format(date);
    }

    //取日期时分  返回XX点XX分
    public static String formatChsHhmm(Date date) {
        return getChsHhmmFormater().format(date);
    }

    //将百度08:10:10格式的时分秒转换为XX点XX分
    public static String formatBdhhmmssToChsHhmm(String hhmmBdStr) {
        String[] strArr = hhmmBdStr.split(":");
        return strArr[0] + "点" + strArr[1] + "分";
    }


    //将百度2015-04-21格式转换为04月21日
    public static String formatBdDateToChsmmdd(String bdDateStr) {
        String[] strArr = bdDateStr.split("-");
        return strArr[1] + "月" + strArr[2] + "日";
    }


    public static String formatChsDate(Date date) {
        return getChsDateformater().format(date);
    }

    /**
     * 将java.util.Date格式转换成时间字符串
     *
     * @return String
     */
    public static String formatTime(Date date) {
        return getTimeFormater().format(date);
    }

    /**
     * 将java.util.Date格式转换成日期时间字符串
     *
     * @return String
     */
    public static String formatDateTime(Date date) {
        return getDateTimeFormater().format(date);
    }

    public static String formatDateTimeZcg(Date date) {
        return getDateTimeFormaterZcg().format(date);
    }

    public static String formatTimeZcg(Date time) {
        return getTimeFormaterZcg().format(time);
    }

    public static String formatDateTimeByIDGenerator(Date date) {
        return getDateTimeFormaterByIDGenerator().format(date);
    }

    /**
     * 将Timestamp格式转换成日期时间字符串
     *
     * @return String
     */
    public static String formatDateTime(Timestamp timestamp) {
        return getDateTimeFormater().format(timestamp);
    }

    /**
     * 将Timestamp格式转换成日期时间字符串作为唯一编号
     *
     * @return String
     */
    public static String formatDateTimeAsSeqNo(Timestamp timestamp) {
        return getDateTimeFormaterAsSeqNo().format(timestamp);
    }

    /**
     * 根据字符串转换成日期格式
     *
     * @param strDate 日期格式字符串
     * @return java.util.Date
     */
    public static Date parseDate(String strDate) {
        Date date = null;
        try {
            date = getDateFormater().parse(strDate);
        } catch (Exception ex) {
            //System.err.println(ex.getMessage());
        }
        return date;
    }

    public static Date parseTime(String strTime) {
        Date time = null;
        try {
            time = getTimeFormater().parse(strTime);
        } catch (Exception e) {

        }
        return time;
    }

    public static Date parseChsDate(String strDate) {
        Date date = null;
        try {
            date = getChsDateformater().parse(strDate);
        } catch (Exception ex) {
            //System.err.println(ex.getMessage());
        }
        return date;
    }


    /**
     * 将喜马拉雅时间字符串转换成日期格式 "2015-02-04T14:47:00+08:00"
     *
     * @param strDate 日期格式字符串
     * @return java.util.Date
     */
    public static Date parseXmlyDateTime(String strDate) {
        Date date = null;

        try {
            String str = strDate.replace("T", " ");
            str = str.replace("+08:00", "");
            date = getDateTimeFormater().parse(str);
        } catch (Exception ex) {
            //System.err.println(ex.getMessage());
        }
        return date;
    }


    /**
     * 根据字符串转换成日期格式
     *
     * @param strDate 日期格式字符串
     * @return java.sql.Date
     */
    public static java.sql.Date parseSqlDate(String strDate) {
        return java.sql.Date.valueOf(strDate);
    }

    /**
     * 将Timestamp转换成java.sql.Date格式
     *
     * @param checkTime Timestamp
     * @return java.sql.Date
     */
    public static java.sql.Date parseSqlDate(Timestamp checkTime) {
        String sDate = formatDate(checkTime);
        return java.sql.Date.valueOf(sDate);
    }

    /**
     * 根据字符串转换成日期格式
     *
     * @param strDateTime 日期时间格式字符串
     * @return java.util.Date
     */
    public static Date parseDateTime(String strDateTime) {
        Date date = null;
        if (strDateTime.length() < 19) {
            strDateTime += ":00";
        }
        try {
            date = getDateTimeFormater().parse(strDateTime);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return date;
    }


    public static Date parseDateTimeZcg(String strDateTime) {
        Date date = null;
        try {
            date = getDateTimeFormaterZcg().parse(strDateTime);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return date;
    }


    /**
     * 获取当前时间的前一个月时间
     *
     * @return java.util.Date
     */
    public static Date parseLastMonthDate() {
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(new Date());
        gCal.add(GregorianCalendar.MONTH, -1);
        return gCal.getTime();
    }

    /**
     * 获取当前时间的前一个月时间
     *
     * @return java.sql.Timestamp
     */
    public static Timestamp parseLastMonthTimestamp() {
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(new Date());
        gCal.add(GregorianCalendar.MONTH, -1);
        return new Timestamp(gCal.getTime().getTime());
    }

    /**
     * 获取当前时间的后几天时间
     *
     * @return java.sql.Timestamp
     */
    public static Timestamp getNextTimestamp(Timestamp e, int i) {
        if (e == null) return new Timestamp(new Date().getTime());
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(new Date(e.getTime() + i * 1000 * 60 * 60 * 24));
        return new Timestamp(gCal.getTime().getTime());
    }

    /**
     * 获取当前时间的后几天时间的字符串
     *
     * @param e
     * @param i
     * @return
     */
    public static String getNextTimeStr(Timestamp e, int i) {
        if (e == null) {
            e = new Timestamp(new Date().getTime());
        }
        GregorianCalendar gCal = new GregorianCalendar();
        gCal.setTime(new Date(e.getTime() + i * 1000 * 60 * 60 * 24));
        return formatDateTime(gCal.getTime());
    }

    /**
     * 将字符串转换成Timestamp格式
     *
     * @return Timestamp
     */
    public static Timestamp parseTimestamp(String strTimestamp) {
        if (strTimestamp == null) return new Timestamp(new Date().getTime());
        String datetime = getDateTimeStr(strTimestamp);
        return new Timestamp(parseDateTime(datetime).getTime());
    }

    /**
     * 获取指定时间段内的日期List
     *
     * @param beginTime Timestamp
     * @param endTime   Timestamp
     * @return List
     */
    public static List getDateList(Timestamp beginTime,
                                             Timestamp endTime) {
        List list = new ArrayList();
        String sBeginDate = formatDate(beginTime);
        Date beginDate = dateDayAdd(parseDate(sBeginDate), -1);

        java.sql.Date date = java.sql.Date.valueOf(formatDate(beginDate));
        int days = getDays(beginTime, endTime);
        for (int i = 0; i <= days; i++) {
            date = java.sql.Date.valueOf(formatDate(dateDayAdd(date, 1)));
            list.add(i, date);
        }
        return list;
    }

    /**
     * 获取日期
     *
     * @param date 指定日期
     * @param days 经过的天数
     * @return Date
     */
    private static Date dateDayAdd(Date date, int days) {
        long now = date.getTime() + days * DAY_IN_MILLISECOND;
        return (new Date(now));
    }

    /**
     * 获取时间
     *
     * @param timestamp 指定日期时间
     * @param minutes   经过的分钟数
     * @return Timestamp
     */
    private static Timestamp dateDateTimeAdd(
            Timestamp timestamp, int minutes) {
        long now = timestamp.getTime() + minutes * 1000 * 60;
        return (new Timestamp(now));
    }

    /**
     * 根据当前时间以及传入的时间计算相差的分钟数。
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return long
     */
    public static long getDiffMinu(Timestamp startDate, Timestamp endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        System.out.println(startTime);
        System.out.println(endTime);

        long minu = (long) ((endTime - startTime) / (1000 * 60));
        return minu;
    }

    /**
     * 获取日期
     *
     * @param date   指定日期
     * @param months 经过的月数
     * @return Date
     */
    private static Date dateMonthAdd(Date date, int months) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int m = cal.get(GregorianCalendar.MONTH) + months;
        if (m < 0)
            m += -12;
        cal.roll(GregorianCalendar.YEAR, m / 12);
        cal.roll(GregorianCalendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 根据字符串生成日期时间格式的字符串
     *
     * @param str 日期型字符串,不含时间
     * @return String
     */
    private static String getDateTimeStr(String str) {
        if (str.indexOf(" ") == -1)
            str += "  00:00:00";
        else if (str.lastIndexOf(":") == 13)
            str += ":00";
        return str;
    }

    /**
     * 本月有多少天
     *
     * @return 天
     * @author zhangjing
     */
    public static int GetDaysOfMonth(int iYear, int Month) {
        int days = 0;
        switch (Month) {
            case 1:
                days = 31;
                break;
            case 2:
                if (IsRuYear(iYear)) {
                    //闰年多 1 天 即：2 月为 29 天
                    days = 29;
                } else {
                    //--非闰年少1天 即：2 月为 28 天
                    days = 28;
                }

                break;
            case 3:
                days = 31;
                break;
            case 4:
                days = 30;
                break;
            case 5:
                days = 31;
                break;
            case 6:
                days = 30;
                break;
            case 7:
                days = 31;
                break;
            case 8:
                days = 31;
                break;
            case 9:
                days = 30;
                break;
            case 10:
                days = 31;
                break;
            case 11:
                days = 30;
                break;
            case 12:
                days = 31;
                break;
        }

        return days;


    }

    /**
     * 判断当前日期所属的年份是否是闰年，私有函数
     * 是闰年：True ，不是闰年：False
     *
     * @author zhangjing
     */
    private static boolean IsRuYear(int year) {

        if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到星期
     *
     * @author zhangjing
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();

        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekDays[w];

    }

    /**
     * 按格式转换时间
     *
     * @param format，常用格式："yyMMdd","yyyyMMddhhmmss","yyyy-MM-dd HH:mm:ss",
     *                                                          "yyyy-MM-dd HH:mm:ss.SS"
     * @author dush
     */
    public static String convertDateFormat(Date date, String format) throws Exception {
        String time = null;
        try {
            time = (new SimpleDateFormat(format)).format(date);
        } catch (RuntimeException e) {
            throw e;
        }

        return time;
    }

    /**
     * 根据输入的日期字符串返回相应的秒数
     */
    public static long getSecondsOfDate(String dateStr) {
        try {
            Date date = getDateFormater().parse(dateStr);
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            return ca.getTimeInMillis() / 1000;
        } catch (ParseException e) {
            throw new IllegalArgumentException("输入的日期字符串：'" + dateStr + "'不合法！");
        }
    }

    /**
     * 根据传入的日期时间字符串，返回相应的秒数
     */
    public static long getSecondsOfDateTime(String dateStr) {
        try {
            Date date = getDateTimeFormater().parse(dateStr);
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            return ca.getTimeInMillis() / 1000;
        } catch (ParseException e) {
            throw new IllegalArgumentException("输入的日期时间字符串：'" + dateStr + "'不合法！");
        }
    }

    /**
     * 根据传入的描述返回日期时间
     */
    public static Date getDateFromSeconds(long seconds) {
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(seconds * 1000);
        return ca.getTime();
    }

    /**
     * 根据传入的描述返回持续时间秒数
     *
     * @param duration 00:20:00
     * @return long 秒数
     */
    public static long getSecondsFromDurTime(String duration) {
        long seconds = 0;
        String[] dur = duration.split(":");
        seconds = new Long(dur[0]).longValue() * 3600 + new Long(dur[1]).longValue() * 60 + new Long(dur[2]).longValue();
        return seconds;
    }

    public static String formatDurTime(long durLong, String formatStr) {
        String durTime = "";
        if (formatStr == null || "".equals(formatStr))
            formatStr = "hh:mm:ss";
        String[] fmtArray = formatStr.split(":");
        for (int i = 0; i < fmtArray.length; i++) {
            if (fmtArray[i].equals("hh") || fmtArray[i].endsWith("小时")) {
                long hl = durLong / 3600;
                if (hl == 0)
                    continue;
                durLong = durLong % 3600;
                if (fmtArray[i].endsWith("小时"))
                    durTime += hl + fmtArray[i];
                else
                    durTime += hl + ":";
            } else if (fmtArray[i].equals("mm") || fmtArray[i].startsWith("分")) {
                long ml = durLong / 60;
                durLong = durLong % 60;
                if (fmtArray[i].startsWith("分"))
                    durTime += ml + fmtArray[i];
                else
                    durTime += ml + ":";
            } else if (fmtArray[i].equals("ss") || fmtArray[i].equals("秒")) {
                long sl = durLong;
                if (fmtArray[i].equals("秒"))
                    durTime += sl + "秒";
                else
                    durTime += sl;
            }
        }


        return durTime;
    }

    /**
     * 得到指定日期的一天的的最后时刻23:59:59
     *
     * @param date Date对象
     * @return java.util.Date 返回指定日期的最后时间 比如：2010-8-11 23:59:59
     */
    public static Date getFinallyDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        String fm = format.format(date);
        fm += " 23:59:59";
        try {
            return DateUtils.parseDate(fm, new String[]{"yyyyMMdd HH:mm:ss"});
        } catch (ParseException e1) {
            return null;
        }
    }

    /**
     * 得到指定日期的一天的开始时刻00:00:00
     *
     * @param date Date对象
     * @return java.util.Date 返回指定日期的最开始时间 比如：2010-8-11 00:00:00
     */
    public static Date getStartDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        String fm = format.format(date);
        fm += " 00:00:00";
        try {
            return DateUtils.parseDate(fm, new String[]{"yyyyMMdd HH:mm:ss"});
        } catch (ParseException e1) {
            return null;
        }
    }

    public static void main(String[] args) {
        //Date ss = parseDateTime("2010-12-20 23:00");
        //ss.toString();
        System.out.println(parseDateTime("2012-01-12 12:00"));
// 		System.out.println(getSecondsOfDate("2008-4-23"));
// 		System.out.println(getDateFromSeconds(getSecondsOfDate("2008-4-11")));

        //System.out.println(getCurMonth());
        System.out.println(getYearMonth());
        System.out.println(getMonthLastDay());
    }

    public static String getYearMonthPath() {
        SimpleDateFormat format = new SyncSimpleDateFormat(yyyy_MM);
        Date date = new Date();
        return format.format(date);
    }

    public static String getYearMonth() {
        SimpleDateFormat format = new SyncSimpleDateFormat(yyyyMM);
        Date date = new Date();
        return format.format(date);
    }

    /**
     * 根据出生日期获得年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    //do nothing
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        } else {
            //monthNow<monthBirth
            //donothing
        }

        return age;
    }

    /**
     * 根据出生日期获得年龄(yyyy年MM月dd日)
     *
     * @param str
     * @return
     */
    public static int getAge(String str) {
        int age = 0;
        SimpleDateFormat sd = new SimpleDateFormat("yyyy年MM月dd日");

        //然后进行转化
        Date date;
        try {
            date = sd.parse(str);
            age = DateUtil.getAge(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return age;
    }

    /**
     * 得到当前年份
     *
     * @return 如：2013
     */
    public static String getYearCurrent() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date());
    }

    /**
     * 返回当前月所在季度的季度末的年，月 键值对
     *
     * @param month
     * @return
     */
    public static Map<String, Integer> getSessionMonth(Integer month) {
        int[] months = {1, 4, 7, 10};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Integer year = null;
        Integer sessionMonth = null;
        if (null == month) {
            month = calendar.get(Calendar.MONTH) + 1;
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        year = calendar.get(Calendar.YEAR);
        if (month >= 2 && month <= 4) {
            sessionMonth = months[1];
        } else if (month >= 5 && month <= 7) {
            sessionMonth = months[2];
        } else if (month >= 8 && month <= 10) {
            sessionMonth = months[3];
        } else if (month >= 11 && month <= 12) {
            year = calendar.get(Calendar.YEAR) + 1;//跨年
            sessionMonth = months[0];
        } else if (month == 1) {
            sessionMonth = months[0];
        }
        map.put("year", year);
        map.put("sessionMonth", sessionMonth);
        return map;
    }

    /**
     * 返回当前月所在半年的的年，月 键值对
     *
     * @param month
     * @return
     */
    public static Map<String, Integer> getHalfYearMonth(Integer month) {
        int[] months = {7, 12};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Integer year = null;
        Integer sessionMonth = null;
        if (null == month) {
            month = calendar.get(Calendar.MONTH) + 1;
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        year = calendar.get(Calendar.YEAR);
        if (month >= 1 && month <= 6) {
            sessionMonth = months[0];
        } else if (month >= 7) {
            sessionMonth = months[1];
        }
        map.put("year", year);
        map.put("sessionMonth", sessionMonth);
        return map;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date      日期
     * @param dateStyle 日期风格
     * @return 日期字符串
     */
    public static String DateToString(Date date, DateStyle dateStyle) {
        String dateString = null;
        if (dateStyle != null) {
            dateString = DateToString(date, dateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return 日期字符串
     */
    public static String DateToString(Date date, String pattern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(pattern).format(date);
            } catch (Exception e) {
            }
        }
        return dateString;
    }

    /**
     * 获取SimpleDateFormat
     *
     * @param pattern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String pattern)
            throws RuntimeException {
        SimpleDateFormat dateFormat = threadLocal.get();
        if (dateFormat == null) {
            synchronized (object) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern, Locale.US);
                    dateFormat.setLenient(false);
                    threadLocal.set(dateFormat);
                }
            }
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    /**
     * 转化时间单位.用于FTP
     *
     * @param time 转化前大小(MS)
     * @return 转化后大小
     */
    public static String getFormatTime(long time) {
        double second = (double) time / TIMEMSUNIT;
        if (second < 1) {
            return time + " MS";
        }

        double minute = second / TIMEUNIT;
        if (minute < 1) {
            BigDecimal result = new BigDecimal(Double.toString(second));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " SEC";
        }

        double hour = minute / TIMEUNIT;
        if (hour < 1) {
            BigDecimal result = new BigDecimal(Double.toString(minute));
            return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " MIN";
        }

        BigDecimal result = new BigDecimal(Double.toString(hour));
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " H";
    }

    public static String FmtSppecgDate(String datestr) {
        StringBuffer sb = new StringBuffer();
        sb.append(datestr.substring(0, 4));
        sb.append("-");
        sb.append(datestr.substring(4, 6));
        sb.append("-");
        sb.append(datestr.substring(6, 8));
        return sb.toString();
    }

    //20160123 转换为2016年1月23日
    public static String FmtSpeechSolarDate(String datestr) {
        String tmp = null;
        StringBuffer sb = new StringBuffer("今天是");
        sb.append(datestr.substring(0, 4));
        sb.append("年");
        sb.append(datestr.substring(4, 6).replace("0", ""));
        sb.append("月");
        sb.append(datestr.substring(6, 8).replace("0", ""));
        sb.append("日");
        return sb.toString();
    }


}
