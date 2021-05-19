/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.adv;

/**
 * 广告类别类型枚举
 *
 * @Description 广告类别类型枚举
 * @Author 刘远杰
 * @Date 2019/5/15 09:16
 * Version 7.0
 **/
public enum SysFlagEnum {
    /**
     * 分类广告
     */
    CLASS(2),
    /**
     * 楼层广告
     */
    WEBFLOOR(1),
    /**
     * 店铺普通广告
     */
    STORE_NOMARL(3),
    /**
     * 普通广告
     */
    NOMARL(0);


    private int value;

    SysFlagEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
