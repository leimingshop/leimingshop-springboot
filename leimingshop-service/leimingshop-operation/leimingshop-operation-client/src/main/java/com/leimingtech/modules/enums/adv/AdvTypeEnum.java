/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.adv;

/**
 * 广告分类类型枚举
 *
 * @Description 广告分类类型枚举
 * @Author 刘远杰
 * @Date 2019/5/16 11:16
 * Version 7.0
 **/
public enum AdvTypeEnum {
    /**
     * 普通广告
     */
    NORMAL(0),
    /**
     * 楼层广告
     */
    FLOOR(1),
    /**
     * 分类广告
     */
    CLASS(2);

    private int value;

    AdvTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
