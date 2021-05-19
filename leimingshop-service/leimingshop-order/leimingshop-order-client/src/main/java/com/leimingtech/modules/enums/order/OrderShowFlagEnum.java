/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 订单用户删除状态枚举
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-08-16
 */
public enum OrderShowFlagEnum {

    /**
     * 未删除
     */
    NO(0),

    /**
     * 已删除
     */
    YES(1);

    private int value;

    OrderShowFlagEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
