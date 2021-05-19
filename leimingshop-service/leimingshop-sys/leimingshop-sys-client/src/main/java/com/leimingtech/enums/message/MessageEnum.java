/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums.message;

import java.io.Serializable;

/**
 * 站内信枚举
 * <p>
 * dy 2019/5/17
 */
public enum MessageEnum implements Serializable {
    /**
     * 信息类型:私信
     */
    MESSAGE_TYPE_PRVT(0),
    /**
     * 信息类型:系统信息
     */
    MESSAGE_TYPE_SYSINFO(1),
    /**
     * 发送方式:站内信
     */
    SEND_MODE_MESSAGE(0),
    /**
     * 发送方式:短信
     */
    SEND_MODE_SMS(1),
    /**
     * 发送方式，短信，站内信，微信公众号消息推送
     */
    SEND_MODE_SMS_INNER(4),
    /**
     * 发送方式:微信
     */
    SEND_MODE_VX(2),
    /**
     * 发送方式:邮件
     */
    SEND_MODE_EMAIL(3),
    /**
     * 邮件查看状态:已读
     */
    MESSAGE_IS_READ(1),
    /**
     * 用户是否删除:未删除
     */
    MESSAGE_SHOW_YES(0),
    /**
     * 用户是否删除:已删除
     */
    MESSAGE_SHOW_NO(1),

    /**
     * 收件人类型:全员
     */
    RECEIVER_TYPE_ALL(0),
    /**
     * 发送方式：友盟
     */
    SEND_MODE_UMENG(5),
    /**
     * 收件人类型：会员
     */
    RECEIVER_TYPE_VIP(1),
    YES(0),
    NO(1),
    /**
     * 收件人类型：商户
     */
    RECEIVER_TYPE_SELLER(2),
    /**
     * 邮件查看状态:未读
     */
    MESSAGE_IS_UNREAD(0),

    /**
     * 是否发送站内信/短信/微信模板   0发送 1不发送
     */
    SEND_MESSAGE(0),
    NO_SEND_MESSAGE(1),


    /**
     * 短信退订标记（0：订阅，1：退订）
     */
    SUBSCRIPTION(0),
    UNSUBSCRIBE(1),

    /**
     * 消息是否发送
     */
    MESSAGE_SEND_SUCCESS(1),
    MESSAGE_UN_SEND(0),;
    private int value;


    MessageEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}

