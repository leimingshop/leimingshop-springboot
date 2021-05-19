/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 订单前台状态枚举
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
public enum OrderAppStatusEnum {

    /**
     * 已取消
     */
    APP_CANCELED(0),

    /**
     * 待付款
     */
    APP_WAITTING_PAYMENT(10),

    /**
     * 待发货
     */
    APP_WAITTING_SHIPPED(20),

    /**
     * 待收货
     */
    APP_WAITTING_RECEIVED(30),

    /**
     * 已完成
     */
    APP_COMPLETE(40);

    private int value;

    OrderAppStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
