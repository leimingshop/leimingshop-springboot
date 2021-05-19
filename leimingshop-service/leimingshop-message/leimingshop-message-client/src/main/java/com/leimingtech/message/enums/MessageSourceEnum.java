/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.enums;

/**
 * 消息来源枚举类
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/22 20:58
 **/
public enum MessageSourceEnum {

    /**
     * 消息来源 0 雷铭
     */
    LEIMING_SOURCE(0);

    private int value;

    MessageSourceEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
