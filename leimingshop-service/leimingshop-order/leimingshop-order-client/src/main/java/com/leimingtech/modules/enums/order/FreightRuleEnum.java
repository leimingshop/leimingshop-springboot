/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

public enum FreightRuleEnum {

    /**
     * 未设置
     */
    RULE_TYPE_NO(-1),

    /**
     * 叠加运费计算
     */
    RULE_TYPE_ADD(0),

    /**
     * 最高运费计算
     */
    RULE_TYPE_CEIL(1),

    /**
     * 最低运费计算
     */
    RULE_TYPE_FLOOR(2),

    /**
     * 智能运费计算
     */
    RULE_TYPE_MIND(3);


    private int value;

    FreightRuleEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
