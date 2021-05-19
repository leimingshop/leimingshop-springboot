/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.util;

import java.util.Date;

/**
 * 功能描述：
 * <UUID工具类>
 *
 * @author 刘远杰
 * @Date 2019/5/23 10:59
 * Version 7.0
 **/
public class UUID {

    private static final int ROTATION = 99999;
    private static Date date = new Date();
    private static StringBuilder buf = new StringBuilder();
    private static int seq = 0;

    private UUID() {

    }

    public static synchronized long next() {
        if (seq > ROTATION) {
            seq = 0;
        }
        buf.delete(0, buf.length());
        date.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", date, seq++);
        return Long.parseLong(str);
    }

}
