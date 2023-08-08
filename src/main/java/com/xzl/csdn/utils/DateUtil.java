package com.xzl.csdn.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 时间处理工具类
 */
@Slf4j
public class DateUtil {

    private static final String ymdhms = "yyyy-MM-dd HH:mm:ss";
    private static final String ymd = "yyyy-MM-dd";

    /**
     * @author：Lul
     * @description： Date转化字符串
     * @date：10:44 2018/06/08
     */
    public static String getStringByDate(Date date, String timeType) {
        //将Date转化成LocalDateTime
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        //LocalDateTime转化String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeType);
        String str = localDateTime.format(formatter);
        return str;
    }

    /**
     * @author：Lul
     * @description： 字符串转化Data
     * @date：10:44 2018/06/08
     */
    public static Date getDateToString(String str, String dateType) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateType);
        Date date = new Date();
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return date;
    }

    /**
     * @author：Lul
     * @description： Long转化Data
     * @date：10:44 2018/06/08
     */
    public static Date getDateToLong(Long date) {
        //前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        Date dt = new Date(date);
        return dt;
    }

    /**
     * @author：Lul
     * @description： Date转字符串
     * @date：10:44 2018/06/08
     */
    public static String getStringToDate(Date date, String dateType) {
        if (!StringUtils.isEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateType);
            try {
                return sdf.format(date);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return "";
    }

    /**
     * 描述：将String转化成指定的date类型
     *
     * @author ljf
     * @date 2018/12/6 15:16
     */
    public static Date string2Date(String dateStr, String formatType) {
        if (!StringUtils.isEmpty(dateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat(formatType);
            try {
                return sdf.parse(dateStr);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }


    /**
     * 将时间差封装成String字符串
     */
    public static String timeToString(Map<Integer, Integer> map) {
        // 定义时间差
        StringBuilder difTime = new StringBuilder();
        for (Integer str : map.keySet()) {
            if (str == 0) {
                difTime.append(map.get(str)).append("年");
            } else if (str == 1) {
                difTime.append(map.get(str)).append("月");
            } else if (str == 2) {
                difTime.append(map.get(str)).append("天");
            } else if (str == 3) {
                difTime.append(map.get(str)).append("小时");
            } else if (str == 4) {
                difTime.append(map.get(str)).append("分");
            } else if (str == 5) {
                difTime.append(map.get(str)).append("秒");
            }
        }
        if (StringUtils.isEmpty(difTime.toString())) {
            difTime = new StringBuilder("0");
        }
        return difTime.toString();
    }

    public static final DecimalFormat decimalFormat = new DecimalFormat("000000");

    public static String now() {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }

    public static String formatString(int number) {
        return decimalFormat.format(number);
    }

    /**
     * @author：Lul
     * @description： 对时间加减几年、几月、几天  Calendar.DAY_OF_YEAR
     * @date：10:44 2018/06/08
     */
    public static Date dateOperation(Date date, Integer type, Integer num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, num);//日期加num天
        Date dt = calendar.getTime();
        return dt;
    }

    /**
     * @author：Lul
     * @description： 比较两个时间大小
     * @date：10:44 2018/06/08
     */
    public static Boolean compareDate(Date date1, Date date2) {
        boolean b = false;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(getStringByDate(date1, "yyyy-MM-dd"));
            Date dt2 = df.parse(getStringByDate(date2, "yyyy-MM-dd"));
            if (dt1.getTime() >= dt2.getTime()) {
                b = true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return b;
    }

    /**
     * @author：Lul
     * @description： 比较两个时间大小
     * @date：10:44 2018/06/08
     */
    public static Boolean compareLong(Long date1, Long date2) {
        boolean b = false;
        try {
            if (date1 >= date2) {
                b = true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return b;
    }

    /**
     * @author：Lul
     * @description： 获取一天的各个小时
     * @date：10:44 2018/06/08
     */
    public static List<Integer> getHourByToday() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 24; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 5);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.HOUR_OF_DAY, i);
            list.add(calendar.get(Calendar.HOUR_OF_DAY));
        }
        return list;
    }

    /**
     * @author：Lul
     * @description： 获取月份  -- 季度
     * @date：10:44 2018/06/08
     */
    @SuppressWarnings("static-access")
    public static List<Integer> getMonthByQuarter(Integer quarter) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 3 * (quarter - 1); i < 3 * (quarter - 1) + 3; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, i); //设置为前num月
            list.add(calendar.get(Calendar.MONTH) + 1);
        }
        return list;
    }

    /**
     * @author：Lul
     * @description： 获取日期---月份
     * @date：10:44 2018/06/08
     */
    @SuppressWarnings("static-access")
    public static List<Integer> getDayByMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        //先求出后一个月的第一天
        calendar.set(calendar.get(Calendar.YEAR), month, 1);
        calendar.add(Calendar.DATE, -1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.clear();
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < day; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1); //设置为前num月
            list.add(calendar.get(Calendar.DAY_OF_MONTH));
        }
        return list;
    }

    /**
     * @author：Lul
     * @description： 获取季度 --- 年
     * @date：10:44 2018/06/08
     */
    public static List<Integer> getQuarterByYear() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            list.add(i + 1);
        }
        return list;
    }

    /**
     * @author：Lul
     * @description： 获取date之前见的N月月数
     * @date：10:44 2018/06/08
     */
    public static List<Integer> getMonthBeforeToday(int num, Date date) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = num; i > 0; i--) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, -i);
            list.add(calendar.get(Calendar.MONTH) + 1);
        }
        return list;
    }


    /**
     * @author：Lul
     * @description： 获取date之前见的N月开始时间
     * @date：10:44 2018/06/08
     */
    public static Date getDateBeforeToday(int num, Date date) {
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.MONTH, -num); //设置为前num月
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @author：Lul
     * @description： 获取date之前见的N月结束时间
     * @date：10:44 2018/06/08
     */
    public static Date getDateAfterToday(int num, Date date) {
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.MONTH, -num); //设置为前num月
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取当前时间之前几天的 UTC 时间戳.
     */
    public static Long getUTCTimestampBeforeToday(int beforeDays) {
        return Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")).minusDays(beforeDays).toLocalDate().atStartOfDay()).getTime();
    }

    /**
     * @author：Lul
     * @description： 获取一天开始时间
     * @date：10:44 2018/06/08
     */
    public static Date getStartTimeByToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        return date;
    }


    /**
     * @author：Lul
     * @description： 获取一天的结束时间
     * @date：10:44 2018/06/08
     */
    public static Date getEndTimeByToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        date = calendar.getTime();
        return date;
    }


    /**
     * @author：Lul
     * @description： 获取当前时间前某秒断的时间
     * @date：10:44 2018/06/08
     */
    public static Date getDateBySeconds(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * @author：Lul
     * @description： 比较多个时间大小，返回最闲的时间
     * @date：10:44 2018/06/08
     */
    public static Date compareMinDate(List<Date> dateList) {
        Date date = new Date();
        for (int i = 0; i < dateList.size(); i++) {
            for (int j = 0; j < dateList.size() - i - 1; j++) {
                if (dateList.get(j + 1).before(dateList.get(j))) {
                    date = dateList.get(j);
                    dateList.set(j, dateList.get(j + 1));
                    dateList.set(j + i, date);
                }
            }
        }
        return dateList.get(0);
    }

    /**
     * 描述：将秒数转化成 00:00:00 的时分秒格式
     *
     * @author ljf
     * @date 2018/9/6 10:25
     */
    public static String secToTime(int time) {
        String timeStr;
        int hour;
        int minute;
        int second;
        if (time <= 0) {
            return "0:00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "0:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    /**
     * 描述：获取两个时间的天数差
     *
     * @author ljf
     * @date 2018/10/8 15:29
     */
    public static Integer differDays(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 描述：获取当前月份名称
     *
     * @author ljf
     * @date 2018/10/9 17:59
     */
    public static String getMonthName(Integer mon) {
        if (mon != null) {
            switch (mon) {
                case 0:
                    return "一月";
                case 1:
                    return "二月";
                case 2:
                    return "三月";
                case 3:
                    return "四月";
                case 4:
                    return "五月";
                case 5:
                    return "六月";
                case 6:
                    return "七月";
                case 7:
                    return "八月";
                case 8:
                    return "九月";
                case 9:
                    return "十月";
                case 10:
                    return "十一月";
                case 11:
                    return "十二月";
            }
        }
        return null;
    }

    /**
     * 调取方法   获取开始时间
     *
     * @param year  年
     * @param month 月
     * @return 返回第几周的开始与结束日期
     */
    public static Date getStartScope(Integer year, Integer quarter, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (!StringUtils.isEmpty(year) && !StringUtils.isEmpty(quarter) && !StringUtils.isEmpty(month)) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } else if (!StringUtils.isEmpty(year) && !StringUtils.isEmpty(quarter) && StringUtils.isEmpty(month)) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, ((quarter - 1) * 3));
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } else if (!StringUtils.isEmpty(year) && StringUtils.isEmpty(quarter) && StringUtils.isEmpty(month)) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 调取方法   获取开始时间
     *
     * @param year  年
     * @param month 月
     * @return 返回第几周的开始与结束日期
     */
    public static Date getEndScope(Integer year, Integer quarter, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (!StringUtils.isEmpty(year)) {
            calendar.set(Calendar.YEAR, year);
        }
        //先求出后一个月的第一天
        if (!StringUtils.isEmpty(month)) {
            calendar.set(calendar.get(Calendar.YEAR), month, 1);
        }
        if (!StringUtils.isEmpty(quarter)) {
            calendar.set(calendar.get(Calendar.YEAR), ((quarter - 1) * 3) + 3, 1);
        }
        calendar.add(Calendar.DATE, -1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(calendar.get(Calendar.MONTH));
        calendar = Calendar.getInstance();
        if (!StringUtils.isEmpty(year) && !StringUtils.isEmpty(quarter) && !StringUtils.isEmpty(month)) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, day);

        } else if (!StringUtils.isEmpty(year) && !StringUtils.isEmpty(quarter) && StringUtils.isEmpty(month)) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, ((quarter - 1) * 3) + 2);
            calendar.set(Calendar.DAY_OF_MONTH, day);
        } else if (!StringUtils.isEmpty(year) && StringUtils.isEmpty(quarter) && StringUtils.isEmpty(month)) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, 11);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date longStrToDate(String strTimeLong) {

        if (strTimeLong == null) {
            return null;
        }

        Date date;
        if (!StringUtils.isEmpty(strTimeLong)) {
            date = new Date(Long.valueOf(strTimeLong));
            return date;
        }
        return null;
    }

    /**
     * @author：lianp
     * @description： 加月数
     * @date：10:44 2019/09/04
     */
    public static Date addMonths(Date date, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * @author：lianp
     * @description： 加天数
     * @date：10:44 2019/09/04
     */
    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }

    /**
     * @author：Lul
     * @description： 比较两个时间是否是同一天
     * @date：10:44 2018/06/08
     */
    public static Boolean equalDate(Date date1, Date date2) {
        boolean b = false;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(getStringByDate(date1, "yyyy-MM-dd"));
            Date dt2 = df.parse(getStringByDate(date2, "yyyy-MM-dd"));
            if (dt1.getTime() == dt2.getTime()) {
                b = true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return b;
    }

    /**
     * @author：Lul
     * @description： 比较两个时间是否是同一时间
     * @date：10:44 2018/06/08
     */
    public static Boolean equalLong(Long date1, Long date2) {
        boolean b = false;
        try {
            if (date1.longValue() == date2.longValue()) {
                b = true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return b;
    }

    public static int getMonthSpace(Date date1, Date date2) {

        int result = 0;


        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(date1);
        c2.setTime(date2);

        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

        return Math.abs(result);
    }

    public static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
        return monthsDiff;
    }

    /**
     * 获取月的最后一天
     *
     * @param date
     */
    public static long getLastDayOfMonth(long date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date(date));
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return ca.getTime().getTime();
    }


    public static String getRFCTimeNow() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(calendar.getTime());
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    public static String getDateByLong(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        return sdf.format(time);
    }

    /**
     * 获取月的第一天 00:00:00
     *
     * @param date
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMinimum(Calendar.DAY_OF_MONTH));
        return getStartTimeByToday(ca.getTime());
    }

    /**
     * 获取月的最后一天 23:59:59
     *
     * @param date
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getEndTimeByToday(ca.getTime());
    }

    /**
     * 获取年的第一天 xxxx-01-01 00:00:00
     *
     * @param date
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_YEAR, ca.getActualMinimum(Calendar.DAY_OF_YEAR));
        return getStartTimeByToday(ca.getTime());
    }

    /**
     * 获取年的最后一天 xxxx-12-31 23:59:59
     *
     * @param date
     */
    public static Date getLastDayOfYear(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getEndTimeByToday(ca.getTime());
    }

    /**
     * date转string
     */
    public static String getTimeByDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(ymdhms);
        return sdf.format(date);
    }

    /**
     * 获取指定月的天数
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获取时间的年份
     */
    public static Integer getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取时间的月份
     */
    public static Integer getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取时间的当前天
     */
    public static Integer getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * LONG转时分秒
     *
     * @return 100 -> 1分40秒
     */
    public static String buildTime(long dateTime) {
        String stopDuration = "";
        long dayFormat = 60 * 60 * 24;
        int day = (int) (dateTime / dayFormat);
        long timeFormat = 60 * 60;
        int time = (int) ((dateTime - day * dayFormat) / timeFormat);
        long minuteFormat = 60;
        long reMinute = dateTime - day * dayFormat - time * timeFormat;
        int minute = (int) (reMinute / minuteFormat);
        int second = (int) ((reMinute - minute * minuteFormat));

        if (day != 0) {
            stopDuration = day + "天";
        }
        if (time != 0) {
            stopDuration += time + "时";
        }
        if (minute != 0) {
            stopDuration += minute + "分";
        }
        if (second != 0) {
            stopDuration += second + "秒";
        }
        return "".equals(stopDuration) ? "0秒" : stopDuration;
    }


}
