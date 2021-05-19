/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 功能描述：
 * <>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/19 10:04
 **/
public enum PaymentStatusEnum {

    /**
     * 未付款
     */
    NO(0),

    /**
     * 已付款
     */
    YES(1);

    private int value;

    PaymentStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
