/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 功能描述：
 * <订单是否拆弹枚举>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/19 9:52
 **/
public enum OrderSplitEnum {

    /**
     * 未拆单
     */
    NO(0),

    /**
     * 已拆单
     */
    YES(1);

    private int value;

    OrderSplitEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
