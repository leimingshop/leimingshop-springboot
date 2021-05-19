/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.custom;

/**
 * 展示分类枚举
 *
 * @author xuzhch
 * @date 2020年10月14日
 */
public enum ShowClassEnum {
    /**
     * H5展示分类
     */
    SHOW_CLASS_TYPE_H5(0),
    /**
     * PC展示分类
     */
    SHOW_CLASS_TYPE_PC(1),;
    private int value;

    ShowClassEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
