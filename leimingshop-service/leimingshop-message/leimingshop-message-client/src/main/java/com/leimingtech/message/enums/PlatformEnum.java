/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.enums;

/**
 * 平台枚举
 *
 * @since 1.1.0
 */
public enum PlatformEnum {
    /**
     * 阿里云
     */
    ALIYUN(1),
    /**
     * 腾讯云
     */
    QCLOUD(2);

    private int value;

    PlatformEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}