/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums;

/**
 * 菜单资源标识
 *
 * @since 1.0.0
 */
public enum MenuFlagEnum {
    /**
     * 菜单资源
     */
    YES(1),
    /**
     * 非菜单资源
     */
    NO(0);

    private int value;

    MenuFlagEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}