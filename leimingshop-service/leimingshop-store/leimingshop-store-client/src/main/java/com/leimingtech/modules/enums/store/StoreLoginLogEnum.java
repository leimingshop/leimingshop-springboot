/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.store;

/**
 * @Author: weixianchun
 * @Description: 店铺登录日志枚举
 * @Date :2019/7/2 16:50
 * @Version V1.0
 **/
public enum StoreLoginLogEnum {

    /**
     * 状态  0：失败
     */
    STORE_LOGIN_LOG_LOGIN_FAIL(0),

    /**
     * 状态   1：成功
     */
    STORE_LOGIN_LOG_LOGIN_SUCCESS(1),

    /**
     * 状态  2：账号已锁定
     */
    STORE_LOGIN_LOG_LOGIN_LOCK(2),


    /**
     * 用户操作   0：用户登录
     */
    STORE_LOGIN_LOG_ENUM_LOGIN(0),

    /**
     * 1：用户退出
     */
    STORE_LOGIN_LOG_ENUM_OUT(1);

    private int value;

    StoreLoginLogEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
