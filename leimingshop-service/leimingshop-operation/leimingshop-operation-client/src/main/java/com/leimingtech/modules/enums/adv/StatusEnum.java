/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.adv;

/**
 * 广告类别状态枚举
 *
 * @Description 广告类别状态枚举
 * @Author 刘远杰
 * @Date 2019/5/15 09:16
 * Version 7.0
 **/
public enum StatusEnum {
    /**
     * 启用
     */
    ENABLED(1),
    /**
     * 未启用
     */
    NO_ENABLED(2);

    private int value;

    StatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
