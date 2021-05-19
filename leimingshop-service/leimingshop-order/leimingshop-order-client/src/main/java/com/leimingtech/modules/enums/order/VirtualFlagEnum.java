/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 虚拟订单标识（0:否，1是）
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-05-06 10:48
 **/
public enum VirtualFlagEnum {

    /**
     * 是虚拟订单
     */
    YES(1),

    /**
     * 不是虚拟订单
     */
    NO(0);

    private int value;

    VirtualFlagEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
