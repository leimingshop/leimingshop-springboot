/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;

import com.leimingtech.commons.tools.constant.RegexConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * 邮箱工具类
 *
 * @author xuzhch
 * @date 2020年9月29日
 */
public class MailUtils {


    /**
     * 验证是否邮箱
     *
     * @param mail 邮箱
     * @return boolean 验证结果
     * @author xuzhch
     * @date 2020年09月29日
     */
    public static Boolean isMail(String mail) {
        if (StringUtils.isBlank(mail)) {
            return false;
        }
        return mail.matches(RegexConstant.EMAIL_REGEX);
    }
}
