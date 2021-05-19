/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.enums;

/**
 * @description 邮件模板枚举
 * @date: 2019/12/25 14:50
 * @author: chengqian
 */
public enum EmailEnum {
    /**
     * 忘记密码邮件模板
     */
    FORGET_PWD_EMAIL_TEMPLATE("1209727068905922561"),
    /**
     * 登录验证码邮件模板
     */
    LOGIN_EMAIL_TEMPLATE("1210024087360876546"),
    /**
     * 电子发票邮件模板
     */
    INVOICE_EMAIL_TEMPLATE("1261584246640803841"),
    /**
     * 绑定邮箱邮件模板
     */
    ACCOUNT_BIND_EMAIL_TEMPLATE("1262984476460322818");

    private String value;

    EmailEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
