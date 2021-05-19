/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.adv;

/**
 * 广告状态枚举
 *
 * @Description 广告状态枚举
 * @Author 刘远杰
 * @Date 2019/5/15 09:16
 * Version 7.0
 **/
public enum AdvStateEnum {
    /**
     * 未启用
     */
    NO_ENABLED(0),
    /**
     * 启用
     */
    ENABLED(1),
    /**
     * 停用
     */
    DEACTIVATED(2),
    /**
     * h5查询广告
     */
    TYPE(3);

    private int value;

    AdvStateEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
