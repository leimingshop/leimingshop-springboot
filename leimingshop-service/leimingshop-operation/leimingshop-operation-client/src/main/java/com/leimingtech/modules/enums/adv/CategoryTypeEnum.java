/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.adv;

/**
 * 广告分类展示类型枚举
 *
 * @Description 广告分类展示类型枚举
 * @Author 刘远杰
 * @Date 2019/5/15 09:16
 * Version 7.0
 **/
public enum CategoryTypeEnum {

    /**
     * 单图
     */
    ONE(0),
    /**
     * 多图
     */
    MANY(1);

    private int value;

    CategoryTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
