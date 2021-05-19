/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.security.enums;

/**
 * 资源认证枚举
 *
 * @since 1.0.0
 */
public enum ResourceAuthEnum {
    /**
     * 权限认证
     */
    PERMISSIONS_AUTH(0),
    /**
     * 登录认证
     */
    LOGIN_AUTH(1),
    /**
     * 无需认证
     */
    NO_AUTH(2);


    private int value;

    ResourceAuthEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}