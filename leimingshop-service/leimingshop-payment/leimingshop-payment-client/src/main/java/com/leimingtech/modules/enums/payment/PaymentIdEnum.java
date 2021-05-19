/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.payment;

/**
 * 对于数据库中lmshop_payment表中的id
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-08-07 17:10
 **/
public enum PaymentIdEnum {

    /**
     * 微信WAP支付
     */
    WECHAT_WAP_PAY(1L),

    /**
     * 支付宝WAP支付
     */
    ALI_WAP_PAY(5L),

    /**
     * 微信APP支付
     */
    WECHAT_APP_PAY(2L),

    /**
     * 支付宝APP支付
     */
    ALI_APP_PAY(6L),

    /**
     * 余额支付
     */
    BALANCE_PAY(8L);

    private Long value;

    PaymentIdEnum(Long value) {
        this.value = value;
    }

    public Long value() {
        return this.value;
    }
}
