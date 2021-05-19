/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 功能描述：
 * <用户类型枚举>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/25 17:22
 **/
public enum OperatorTypeEnum {

    ADMIN("平台"),

    SELLER("商家"),

    BUYER("用户");

    private String value;

    OperatorTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
