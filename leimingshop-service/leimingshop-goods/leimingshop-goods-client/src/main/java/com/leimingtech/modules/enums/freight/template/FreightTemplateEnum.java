/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.freight.template;

/**
 * <>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/4/21
 */
public enum FreightTemplateEnum {

    /**
     * 模板类型 按件计算
     */
    TEMPLATE_TYPE_PIECE(0),

    /**
     * 模板类型 按重量计算
     */
    TEMPLATE_TYPE_WEIGHT(1),

    /**
     * 是否默认运费模板 否
     */
    DEFAULT_FLAG_NO(0),

    /**
     * 是否默认运费模板 是
     */
    DEFAULT_FLAG_YES(1);

    private int value;

    FreightTemplateEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
