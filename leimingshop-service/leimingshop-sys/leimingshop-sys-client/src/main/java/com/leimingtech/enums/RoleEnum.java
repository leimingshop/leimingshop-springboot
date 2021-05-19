/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums;

/**
 * 用户状态
 *
 * @since 1.0.0
 */
public enum RoleEnum {

    /**
     * 普通用户
     */
    NORMAL_ROLE(0),

    /**
     * 超级管理员
     */
    SUPER_ADMIN(1);

    private int value;

    RoleEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
