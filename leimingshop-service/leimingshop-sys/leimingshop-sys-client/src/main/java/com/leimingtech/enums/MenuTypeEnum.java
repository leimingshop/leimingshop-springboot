/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums;

/**
 * 菜单类型枚举
 *
 * @since 1.0.0
 */
public enum MenuTypeEnum {
    /**
     * 菜单
     */
    MENU(0),
    /**
     * 按钮
     */
    BUTTON(1);

    private int value;

    MenuTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
