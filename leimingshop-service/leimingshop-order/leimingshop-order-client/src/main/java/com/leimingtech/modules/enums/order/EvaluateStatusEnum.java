/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 功能描述：
 * <评价状态枚举>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/19 9:46
 **/
public enum EvaluateStatusEnum {

    /**
     * 未评价
     */
    NO(0),

    /**
     * 已评价
     */
    YES(1);

    private int value;

    EvaluateStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
