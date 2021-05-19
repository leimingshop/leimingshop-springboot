/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.payment;

/**
 * @Author: weixianchun
 * @Description: 支付方式提示信息枚举类
 * @Date :2019/5/20 14:48
 * @Version V1.0
 **/
public enum PaymentCodeEnum {

    /**
     * 收支标识（0:收入）
     */
    TRANSACTION_STATUS_IN(0),
    /**
     * 收支标识（1:支出）
     */
    TRANSACTION_STATUS_OUT(1);

    private int value;

    PaymentCodeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
