/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.utils;

import java.math.BigDecimal;

/**
 * 统计模块工具类
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
public class NumberUtils {
    public static final int ZERO = 0;
    public static final Double DOUBLE_ZERO = 0.00;

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;

    /**
     * 整数相除 四舍五入保留两位小数
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return
     * @author xuzhch 2019年12月13日14:00
     */
    public static Double divisionTwoDecimal(Integer dividend, Integer divisor) {
        return new BigDecimal((double) dividend / divisor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
