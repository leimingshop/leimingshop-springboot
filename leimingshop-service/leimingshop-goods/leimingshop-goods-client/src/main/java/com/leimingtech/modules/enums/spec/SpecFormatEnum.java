/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.spec;

/**
 * 规格样式枚举
 *
 * @author 刘远杰
 * @Description 规格样式枚举
 * @Date 2019/5/15 09:16
 * Version 7.0
 **/
public enum SpecFormatEnum {
    /**
     * 文本
     */
    TEXT(0),
    /**
     * 图片
     */
    IMAGE(1);

    private int value;

    SpecFormatEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
