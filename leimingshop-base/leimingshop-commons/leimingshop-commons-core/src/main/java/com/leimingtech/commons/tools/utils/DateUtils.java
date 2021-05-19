/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期处理工具类
 *
 * @since 1.0.0
 */
public class DateUtils {

    /**
     * 时间格式(yyyy-MM-dd)
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH)
     */
    public static final String DATE_HOUR_PATTERN = "yyyy-MM-dd HH";
    /**
     * 时间格式(yyyy-MM)
     */
    public static final String DATE_MONTH_PATTERN = "yyyy-MM";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    private DateUtils() {
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date 日期
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 日期解析
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回Date
     */
    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            logger.error("错误信息为" + e);
        }
        return null;
    }

    /**
     * 字符串转换成日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)) {
            return null;
        }

        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(strDate).toDate();
    }

    /**
     * 根据周数，获取开始日期、结束日期
     *
     * @param week 周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return 返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 对日期的【秒】进行加/减
     *
     * @param date    日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date    日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date  日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date  日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date   日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date  日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    /**
     * 字符串转为long型
     *
     * @param dateStr 必须带时、分、秒
     * @return
     */
    public static long strToLong(String dateStr) {
        Date date = toDate(dateStr, DATE_TIME_PATTERN);
        long time = 0;
        if (date != null) {
            time = date.getTime();
        }
        return time;
    }

    /**
     * 将一个字符串转换成日期格式, 字符串类型必须于格式化对应
     * 例如：2015-09-01对应yyyy-MM-dd
     * 例如：2015-09-01 00:00:00对应yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date toDate(String date, String pattern) {
        if (("").equals("" + date)) {
            return null;
        }
        if (pattern == null) {
            pattern = DATE_TIME_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date newDate = new Date();
        try {
            newDate = sdf.parse(date);
        } catch (Exception ex) {
            logger.error("错误信息为" + ex);
        }
        return newDate;
    }

    /**
     * 将当前时间转化为指定格式字符串
     *
     * @param pattern 转化格式
     * @return
     */
    public static String getDateStr(String pattern) {
        return DateTime.now().toString(pattern);
    }

    /**
     * 时间戳转化为指定格式时间
     *
     * @param time:   时间戳
     * @param format: 格式
     * @return 格式化的时间
     * @date 2019/12/26 9:49
     * @author lixiangx@leimingtech.com
     **/
    public static Date longToDate(long time, String format) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return parse(sf.format(d), format);
    }

    /**
     * 指定七日近七天的日期
     *
     * @param date
     * @return List<Map> map的key是日期</>
     */
    public static List<String> sevenDayList(Date date) {
        List<String> dateList = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            Date day = addDateDays(date, -i);
            dateList.add(format(day));
        }
        return dateList;
    }

    /**
     * 获取某个时间区间的小时、或天 、或月
     *
     * @param start   开始时间
     * @param end     结束时间
     * @param pattern 时间格式[yyyy-MM-dd HH]或[yyyy-MM-dd]或[yyyy-MM]
     * @return
     */
    public static List<String> getSpecifyDate(Date start, Date end, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        List<String> dateList = new ArrayList<>();
        if (pattern.equals(DATE_HOUR_PATTERN)) {
            long between = DateUtil.between(start, end, DateUnit.HOUR, true);
            int hourCount = (int) between;
            for (int i = 0; i <= hourCount; i++) {
                Date date = addDateHours(start, i);
                dateList.add(dateFormat.format(date));
            }
        }
        if (pattern.equals(DATE_PATTERN)) {
            long between = DateUtil.between(start, end, DateUnit.DAY, true);
            int dayCount = (int) between;
            for (int i = 0; i <= dayCount; i++) {
                Date date = addDateDays(start, i);
                dateList.add(dateFormat.format(date));
            }
        }
        if (pattern.equals(DATE_MONTH_PATTERN)) {
            long between = DateUtil.betweenMonth(start, end, true);
            int monthCount = (int) between;
            for (int i = 0; i <= monthCount; i++) {
                Date date = addDateMonths(start, i);
                dateList.add(dateFormat.format(date));
            }
        }
        return dateList;
    }


    /**
     * * 计算2个日期之间相差的  月
     * * 比如：2011-02-02 到  2017-03-02
     * * 以月为单位相差为：73个月
     *
     * @param fromDate: 开始时间
     * @param toDate:   结束时间
     * @return 相差月数
     * @date 2019/12/25 16:11
     * @author lixiangx@leimingtech.com
     **/
    public static int monthCompare(Date fromDate, Date toDate) {
        Calendar from = Calendar.getInstance();
        from.setTime(fromDate);
        Calendar to = Calendar.getInstance();
        to.setTime(toDate);
        //只要年月
        int fromYear = from.get(Calendar.YEAR);
        int fromMonth = from.get(Calendar.MONTH);

        int toYear = to.get(Calendar.YEAR);
        int toMonth = to.get(Calendar.MONTH);

        int month = toYear * 12 + toMonth - (fromYear * 12 + fromMonth);
        return month;
    }

    public static void main(String[] args) {
        int monthCompare = monthCompare(DateUtils.getCurrYearFirst(), new Date());
        System.out.println(monthCompare);
    }

    /**
     * 获取当年的第一天
     *
     * @return 当年的第一天
     * @date 2019/12/25 16:14
     * @author lixiangx@leimingtech.com
     **/
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取当年最后一天
     *
     * @return 当年最后一天
     * @date 2019/12/25 16:16
     * @author lixiangx@leimingtech.com
     **/
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }

    /**
     * 获取当月一号时间
     *
     * @return
     */
    public static Date initDateByMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当天时间23:59:59
     *
     * @return
     */
    public static Date getToday() {
        Calendar calendar2 = Calendar.getInstance();

        calendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        return calendar2.getTime();
    }

    /**
     * 根据类型获取固定时间
     *
     * @param date 被减时间 type[1：减一天，2：减7天，3：减15天，4：减30天]
     * @return
     * @date 2020/3/17/017 14:49
     * @author xuzhch
     **/
    public static Date getFixedDate(Date date, Integer type) {
        Date date1 = null;

        if (type == 1) {
            date1 = DateUtils.addDateDays(date, -1);
        }
        if (type == 2) {
            date1 = DateUtils.addDateDays(date, -7);
        }
        if (type == 3) {
            date1 = DateUtils.addDateDays(date, -15);
        }
        if (type == 4) {
            date1 = DateUtils.addDateDays(date, -30);
        }
        return date1;
    }
}
