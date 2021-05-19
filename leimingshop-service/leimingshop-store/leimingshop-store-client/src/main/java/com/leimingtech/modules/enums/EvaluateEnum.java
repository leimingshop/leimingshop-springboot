/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums;

/**
 * 评价枚举类
 *
 * @author chengqian
 */
public enum EvaluateEnum {

    /**
     * 店铺评价平均值
     */
    EVALUATE_NUM(3.0),
    /**
     * 店铺暂无评分
     */
    NOT_EVALUATE_NUM(0.0),

    /**
     * 已评价
     */
    EVALUATE_YES(1),
    /**
     * 5星好评
     */
    EVALUATE_SCORES(5);

    private double value;
    private int intValue;

    EvaluateEnum(double value) {
        this.value = value;
    }

    EvaluateEnum(int value) {
        this.intValue = value;
    }

    public double value() {
        return this.value;
    }

    public int intValue() {
        return this.intValue;
    }
}
