/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.enums;

/**
 * 消息发送方式
 *
 * @author xuzhch
 * @date 2020年9月16日
 */
public enum MessageSendTypeEnum {

    //消息发送方式（0：站内信，1：短信，2：微信，3：邮件，4：全部）
    /**
     * 站内信
     */
    INNER_MESSAGE("0"),

    /**
     * 短信
     */
    SMS_MESSAGE("1"),

    /**
     * 微信
     */
    WECHAT_MESSAGE("2"),

    /**
     * 邮件
     */
    EMAIL_MESSAGE("3"),

    /**
     * 全部
     */
    ALL_MESSAGE("4");

    private String value;

    MessageSendTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
