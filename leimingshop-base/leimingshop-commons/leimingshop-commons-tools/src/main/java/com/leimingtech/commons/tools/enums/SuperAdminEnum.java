/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.enums;

/**
 * 超级管理员枚举
 *
 * @since 1.0.0
 */
public enum SuperAdminEnum {
    YES(1),
    NO(0);

    private int value;

    SuperAdminEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}