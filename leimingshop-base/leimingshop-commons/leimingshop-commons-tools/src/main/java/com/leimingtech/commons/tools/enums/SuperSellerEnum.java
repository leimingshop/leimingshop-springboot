/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.enums;

/**
 * 超级商家管理员枚举
 *
 * @since 1.0.0
 */
public enum SuperSellerEnum {

    /**
     * 1 是超级管理员   0 否
     */
    YES(1),
    NO(0);

    private int value;

    SuperSellerEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}