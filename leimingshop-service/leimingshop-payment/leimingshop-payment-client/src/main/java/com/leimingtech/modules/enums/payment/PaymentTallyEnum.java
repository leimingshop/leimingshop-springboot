/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.payment;

/**
 * @Author: weixianchun
 * @Description: 支付流水枚举类
 * @Date :2019/5/20 14:48
 * @Version V1.0
 **/
public enum PaymentTallyEnum {

    /**
     * 交易状态：成功
     */
    PAYMENTTALLY_STATE_SUCCESS(1),

    /**
     * 交易状态：失败
     */
    PAYMENTTALLY_STATE_FAIL(2),

    /**
     * 交易类型：订单支出
     */
    TRADE_TYPE_ORDER(10),
    /**
     * 交易类型：充值
     */
    TRADE_TYPE_RECHANGE(20),
    /**
     * 交易类型：退款
     */
    TRADE_TYPE_REFUND(30);

    private int value;

    PaymentTallyEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
