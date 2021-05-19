/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.store;

/**
 * @Description 角色枚举
 * @Data: 2019/7/2 9:56
 * @Author: chengqian
 * @Version: 1.0
 */
public enum RoleEnum {


    /**
     * 普通角色
     */
    ORDINARY_ROLE(0),
    /**
     * 超级管理员
     */
    ADMIN_ROLE(1),
    /**
     * 注册角色
     */
    REGISTER_ROLE(2);

    private int value;

    RoleEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
