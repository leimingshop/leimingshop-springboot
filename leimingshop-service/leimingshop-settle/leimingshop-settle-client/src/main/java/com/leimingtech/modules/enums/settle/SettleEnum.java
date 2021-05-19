/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.settle;

/**
 * 结算枚举类
 *
 * @author chengqian
 */
public enum SettleEnum {

    /**
     * 对账周期类型(1 天数，2 周，3 月)
     */
    TYPE_DAY(1),
    TYPE_WEEK(2),
    TYPE_MONTH(3);


    private int value;


    SettleEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
