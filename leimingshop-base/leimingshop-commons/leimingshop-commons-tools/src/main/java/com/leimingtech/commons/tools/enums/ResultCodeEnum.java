/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.enums;

/**
 * 返回状态码枚举
 *
 * @author xuzhch
 * @date 2021年1月15日
 */
public enum ResultCodeEnum {

    /**
     * Error result code enum.
     */
    ERROR(500),
    /**
     * Warn result code enum.
     */
    WARN(400),
    /**
     * Retry result code enum. 需要发起重试
     */
    RETRY(401),
    /**
     * Success result code enum.
     */
    SUCCESS(200);


    private int value;

    ResultCodeEnum(int value) {
        this.value = value;
    }

    /**
     * Value int.
     *
     * @return the int
     */
    public int value() {
        return this.value;
    }

}
