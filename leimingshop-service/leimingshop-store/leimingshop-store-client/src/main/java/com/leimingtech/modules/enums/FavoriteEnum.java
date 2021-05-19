/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums;

/**
 * 收藏枚举
 *
 * @author chengqian
 */
public enum FavoriteEnum {

    /**
     * 未收藏
     */
    NOT_FAVORITE(0),

    /**
     * 已收藏
     */
    ALREADY_FAVORITE(1);
    private int value;

    FavoriteEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
