/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.attribute;

/**
 * 属性分组状态枚举
 *
 * @author 刘远杰
 * @Description 属性分组状态枚举
 * @Date 2019/5/20 11：28
 * Version 7.0
 **/
public enum GroupStatusEnum {
    /**
     * 启用
     */
    ENABLED(1),
    /**
     * 停用
     */
    NOTENABLED(2);

    private int value;

    GroupStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
