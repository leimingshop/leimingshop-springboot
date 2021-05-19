/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.attribute;

/**
 * 属性样式枚举
 *
 * @author 刘远杰
 * @Description 属性样式枚举
 * @Date 2019/5/20 11：29
 * Version 7.0
 **/
public enum AttrFormatEnum {
    /**
     * 文本
     */
    TEXT(0),

    /**
     * 图片
     */
    IMAGE(1);

    private int value;

    AttrFormatEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
