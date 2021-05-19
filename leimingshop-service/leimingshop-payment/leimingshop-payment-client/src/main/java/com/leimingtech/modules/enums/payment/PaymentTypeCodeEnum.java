/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.payment;

/**
 * 支付方式client_type（客户端类型）字段（WEB、WAP、APP、MINI）
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-07-09 16:47
 **/
public enum PaymentTypeCodeEnum {

    /**
     * pc端
     */
    WEB("WEB"),

    /**
     * h5端
     */
    WAP("WAP"),

    /**
     * 小程序端
     */
    MINI("MINI"),

    /**
     * 充值
     */
    RECHARGE("雷铭商城充值"),

    /**
     * APP端
     */
    APP("APP");

    private String value;


    PaymentTypeCodeEnum(String value) {
        this.value = value;
    }


    public String value() {
        return this.value;
    }
}
