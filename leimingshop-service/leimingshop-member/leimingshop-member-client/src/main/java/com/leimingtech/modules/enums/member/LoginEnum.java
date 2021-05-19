/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.member;


/**
 * 注册类型枚举
 *
 * @author DY 2019.5.12
 * @since 1.0.0
 */
public enum LoginEnum {

    /**
     * 手机注册
     */
    LOGIN_MOBILE(2),
    /**
     * 邮箱注册
     */
    LOGIN_EMAIL(1),

    /**
     * 登陆失败 3次
     */
    THREE(3),
    /**
     * 登陆失败 6次
     */
    SIX(6),
    /**
     * 登陆失败 9次
     */
    NINE(9),

    /**
     * 用户名注册
     */
    LOGIN_MEMBERNAME(0);
    private int value;

    LoginEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
