/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.util;

import com.leimingtech.commons.tools.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 功能描述：
 * <TenpayUtil>
 *
 * @author 刘远杰
 * @Date 2019/5/23 10:59
 * Version 7.0
 **/
public class TenpayUtil {

    private TenpayUtil() {
    }

    /**
     * 功能描述:
     * 〈把对象转换成字符串〉
     *
     * @param obj obj
     * @author : 刘远杰
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }


        return obj.toString();
    }

    /**
     * 功能描述:
     * 〈把对象转换为int数值〉
     *
     * @param obj 包含数字的对象
     * @return : int 转换后的数值,对不能转换的对象返回0
     * @author : 刘远杰
     */
    public static int toInt(Object obj) {
        int a = 0;
        try {
            if (obj != null) {
                a = Integer.parseInt(obj.toString());
            }

        } catch (Exception e) {

        }
        return a;
    }

    /**
     * 功能描述:
     * 〈获取当前时间 yyyyMMddHHmmss〉
     *
     * @author : 刘远杰
     */
    public static String getCurrTime() {
        return DateUtils.format(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * 功能描述:
     * 〈获取当前日期 yyyyMMdd〉
     *
     * @param date 日期
     * @author : 刘远杰
     */
    public static String formatDate(Date date) {
        return DateUtils.format(date, DateUtils.DATE_TIME_PATTERN);
    }

    /**
     * 功能描述:
     * 〈取出一个指定长度大小的随机正整数〉
     *
     * @param length 设定所取出随机数的长度。length小于11
     * @return : int 返回生成的随机数
     * @author : 刘远杰
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 功能描述:
     * 〈获取编码字符集〉
     *
     * @param request  request
     * @param response response
     * @author : 刘远杰
     */
    public static String getCharacterEncoding(HttpServletRequest request,
                                              HttpServletResponse response) {

        if (null == request || null == response) {
            return "gbk";
        }

        String enc = request.getCharacterEncoding();
        if (null == enc || "".equals(enc)) {
            enc = response.getCharacterEncoding();
        }

        if (null == enc || "".equals(enc)) {
            enc = "gbk";
        }

        return enc;
    }

    /**
     * 功能描述:
     * 〈获取unix时间，从1970-01-01 00:00:00开始的秒数〉
     *
     * @param date date
     * @return : long
     * @author : 刘远杰
     */
    public static long getUnixTime(Date date) {
        if (null == date) {
            return 0;
        }

        return date.getTime() / 1000;
    }

    /**
     * 功能描述:
     * 〈时间转换成字符串〉
     *
     * @param date       时间
     * @param formatType 格式化类型
     * @author : 刘远杰
     */
    public static String date2String(Date date, String formatType) {
        return DateUtils.format(new Date(), formatType);
    }
}












