/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName MobileUtil
 * @Description 手机号验证工具类
 * @Author DY
 * @Date 2019/5/15 19:22
 * @Version 1.0
 **/
public class MobileUtil {
    private static Pattern MOBILE_PATTERN = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");

    private MobileUtil() {
    }

    /**
     * 手机号格式验证
     *
     * @param phone
     * @return
     */
    public static boolean isMobile(String phone) {
        Matcher m = null;
        boolean b = false;
        m = MOBILE_PATTERN.matcher(phone);
        b = m.matches();
        return b;
    }
}
