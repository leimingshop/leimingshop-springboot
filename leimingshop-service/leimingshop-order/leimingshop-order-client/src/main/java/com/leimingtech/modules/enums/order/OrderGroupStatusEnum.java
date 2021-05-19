/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 订单拼团状态枚举
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-03-18 11:23
 **/
public enum OrderGroupStatusEnum {

    /**
     * 拼团进行中
     */
    GROUP_ONGOING(0),

    /**
     * 拼团成功
     */
    GROUP_SUCCESS(1),

    /**
     * 拼团失败
     */
    GROUP_FAIL(2);

    private int value;

    OrderGroupStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
