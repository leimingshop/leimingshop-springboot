/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.log.enums;

/**
 * 日志类型枚举
 *
 * @since 1.0.0
 */
public enum LogTypeEnum {
    /**
     * 登录日志
     */
    LOGIN(0),
    /**
     * 操作日志
     */
    OPERATION(1),
    /**
     * 异常日志
     */
    ERROR(2);

    private int value;

    LogTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}