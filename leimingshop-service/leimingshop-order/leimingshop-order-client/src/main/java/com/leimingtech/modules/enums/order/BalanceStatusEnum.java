/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 功能描述：
 * <结算状态枚举>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/19 9:49
 **/
public enum BalanceStatusEnum {

    /**
     * 未结算
     */
    NO(0),

    /**
     * 已结算
     */
    YES(1);

    private int value;

    BalanceStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
