/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums;

/**
 * 微信枚举类
 *
 * @author DY 2019.5.25
 * @since 1.0.0
 */
public enum WechatEnum {

    /**
     * 微信token名称
     */
    WECHAT_ACCESS_TOKEN("access_token"),

    /**
     * 微信获取openid名称
     */
    WECHAT_OPENID("openid"),

    /**
     * 微信获取unionid名称
     */
    WECHAT_UNIONID("unionid"),

    /**
     * 微信支付交易类型：JSAPI
     */
    TRADE_TYPE_JSAPI("JSAPI"),

    /**
     * 微信支付交易类型：NATIVE
     */
    TRADE_TYPE_NATIVE("NATIVE"),

    /**
     * 微信支付交易类型：APP
     */
    TRADE_TYPE_APP("APP"),

    /**
     * 微信支付：total_fee
     */
    TOTAL_FEE("total_fee"),

    /**
     * 微信支付：out_trade_no
     */
    OUT_TRADE_NO("out_trade_no"),

    WX_MINI_PROGRAM("miniwx"),

    WX_APP_PROGRAM("app"),

    /**
     * 微信支付：transaction_id
     */
    TRANSACTION_ID("transaction_id"),;


    private String value;

    WechatEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
