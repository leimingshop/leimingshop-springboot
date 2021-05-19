/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.member;


/**
 * @Author: weixianchun
 * @Description: 登录日志枚举
 * @Date :2019/7/4 17:00
 * @Version V1.0
 **/
public enum LoginLogEnum {

    /**
     * 手机端
     */
    LOGIN_SOURCE_PHONE(1),
    /**
     * PC登录
     */
    LOGIN_SOURCE_PC(0),
    /**
     * 其他
     */
    LOGIN_SOURCE_OTHER(2);

    private int value;

    LoginLogEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
