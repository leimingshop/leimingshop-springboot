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
public enum PayStateEnum {

    /**
     * 未支付
     */
    NO(0),

    /**
     * 已支付
     */
    Yes(1),

    /**
     * 已取消
     */
    CANCEL(2);

    private int value;

    PayStateEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
