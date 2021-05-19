/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums;

/**
 * 用户状态
 *
 * @since 1.0.0
 */
public enum UserStatusEnum {
    /**
     * 禁用
     */
    DISABLE(0),
    /**
     * 启用
     */
    ENABLED(1);

    private int value;

    UserStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
