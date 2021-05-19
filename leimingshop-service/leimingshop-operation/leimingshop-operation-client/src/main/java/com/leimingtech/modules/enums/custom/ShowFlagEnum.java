/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.custom;

/**
 * <广告展示类目展示状态枚举类>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/7/16
 */
public enum ShowFlagEnum {

    /**
     * 0未展示
     */
    NO(0),
    /**
     * 1展示中
     */
    YES(1);

    private int value;

    ShowFlagEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
