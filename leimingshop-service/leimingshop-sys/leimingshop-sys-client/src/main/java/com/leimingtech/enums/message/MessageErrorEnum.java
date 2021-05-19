/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums.message;

/**
 * 站内信错误枚举
 *
 * @author dy
 * @since 1.0.0
 */
public enum MessageErrorEnum {

    /**
     * 站内信为必选
     */
    E_MESSAGETEXT_NULL(5001, "站内信为必选"),
    /**
     * 用户类型非法
     */
    E_MEMBER_ROLE(5003, "用户类型非法"),
    /**
     * 请选择正确类型
     */
    E_MESSAGE_TYPE_NULL(5004, "请选择正确类型"),
    /**
     * 发送系统消息已关闭
     */
    E_MESSAGE_TYPE_SYSINFO(5005, "发送系统消息已关闭"),
    /**
     * 发送私信已关闭
     */
    E_MESSAGE_TYPE_PRVT(5006, "发送私信已关闭");

    private String msg;
    private int code;

    MessageErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    MessageErrorEnum(int code) {
        this.code = code;
    }

    public String msg() {
        return this.msg;
    }

    public int code() {
        return this.code;
    }

}
