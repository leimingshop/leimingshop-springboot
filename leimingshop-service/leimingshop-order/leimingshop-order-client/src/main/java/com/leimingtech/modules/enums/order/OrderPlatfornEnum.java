/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;


/**
 * 订单来源平台枚举
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
public enum OrderPlatfornEnum {

    /**
     * pc
     */
    PC(0, "pc"),

    /**
     * h5
     */
    H5(1, "h5"),

    /**
     * android
     */
    ANDROID(2, "android"),

    /**
     * ios
     */
    IOS(3, "ios"),

    /**
     * 微信
     */
    WECHAT(4, "wechat"),

    /**
     * 小程序
     */
    APPLET(5, "applet");

    private int code;

    private String value;

    OrderPlatfornEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int code() {
        return this.code;
    }

    public String value() {
        return this.value;
    }

}
