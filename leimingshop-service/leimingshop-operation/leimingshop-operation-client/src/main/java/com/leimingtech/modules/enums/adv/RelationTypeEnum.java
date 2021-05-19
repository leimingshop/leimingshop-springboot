/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.adv;

/**
 * 广告关联类型枚举
 *
 * @Description 广告关联类型枚举
 * @Author 刘远杰
 * @Date 2019/5/15 09:16
 * Version 7.0
 **/
public enum RelationTypeEnum {
    /**
     * 不关联
     */
    NO("no"),
    /**
     * url
     */
    URL("url"),
    /**
     * 单页
     */
    SPECIAL("special"),
    /**
     * 可视化楼层
     */
    VISUALIZATION("visualization");


    private String value;

    RelationTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
