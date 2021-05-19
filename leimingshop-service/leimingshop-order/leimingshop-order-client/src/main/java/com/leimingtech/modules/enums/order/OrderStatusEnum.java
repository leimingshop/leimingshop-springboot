/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 订单状态枚举
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
public enum OrderStatusEnum {

    /**
     * 已取消
     */
    CANCELED(0),

    /**
     * 待付款
     */
    WAITTING_PAYMENT(10),

    /**
     * 待发货
     */
    WAITTING_SHIPPED(20),

    /**
     * 待收货
     */
    WAITTING_RECEIVED(30),

    /**
     * 已完成
     */
    COMPLETE(40);

    private int value;

    OrderStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
